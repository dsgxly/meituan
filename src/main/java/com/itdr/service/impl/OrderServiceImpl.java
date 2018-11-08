package com.itdr.service.impl;

import com.google.common.collect.Lists;
import com.itdr.common.ResponseCode;
import com.itdr.common.ServerResponse;
import com.itdr.dao.*;
import com.itdr.pojo.*;
import com.itdr.service.IOrderService;
import com.itdr.utils.BigDecimalUtils;
import com.itdr.utils.DateUtils;
import com.itdr.utils.PropertiesUtils;
import com.itdr.vo.OrderItemVO;
import com.itdr.vo.OrderVO;
import com.itdr.vo.ShoppingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    CartMapper cartMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    OrderItemMapper orderItemMapper;
    @Autowired
    ShoppingMapper shoppingMapper;

    @Override
    public ServerResponse create(Integer userId,Integer shoppingId) {

        //1.参数非空校验
        if(shoppingId==null){
            return ServerResponse.createByError("地址参数不能为空");
        }
        //2.根据userId查询购物车中已选择的商品  --->List<Cart>
        List<Cart> cartList = cartMapper.findCartListByUserIdChecked(userId);

        //3.List<Cart>  ---> List<OrderItem>订单明细
        ServerResponse serverResponse = getCartOrderItem(userId,cartList);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        //4.创建订单order并将其保存到数据库，产生订单编号
        //计算订单的价格
        BigDecimal orderTotalPrice = new BigDecimal("0");
        List<OrderItem> orderItemList = (List<OrderItem>) serverResponse.getData();
        if(orderItemList==null||orderItemList.size()==0){
            return ServerResponse.createByError("购物车为空");
        }
        orderTotalPrice = getOrderTotalPrice(orderItemList);
        Order order = createOrder(userId,shoppingId,orderTotalPrice);
        if(order==null){
            return ServerResponse.createByError("订单创建失败");
        }
        //5.有了订单编号，将List<OrderItem>保存到数据库
        for(OrderItem orderItem:orderItemList){
            orderItem.setOrderNo(order.getOrderNo());

        }
        //得到订单编号就可以保存到数据库中，涉及到Mybatis的批量插入
        orderItemMapper.insertBatch(orderItemList);

        //6.扣库存
        redecuProductStock(orderItemList);

        //7.购物车中清空已下单的商品
        cleanCart(cartList);

        //8.前台返回OrderVO
        OrderVO orderVO = assembleOrderVO(order,orderItemList,shoppingId);
        return ServerResponse.createBySuccess(orderVO);
    }

    private OrderVO assembleOrderVO(Order order,List<OrderItem> orderItemList,Integer shoppingId){
        OrderVO orderVO = new OrderVO();

        List<OrderItemVO> orderItemVOList = Lists.newArrayList();
        for(OrderItem orderItem:orderItemList){
           OrderItemVO orderItemVO = assembleOrderItemVO(orderItem);
           orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        orderVO.setImageHost(PropertiesUtils.readByKey("imageHost"));
        Shopping shopping = shoppingMapper.selectByPrimaryKey(shoppingId);
        if(shopping!=null){
            orderVO.setShoppingId(shoppingId);
            ShoppingVO shoppingVO =assembleShoppingVO(shopping);
            orderVO.setShoppingVO(shoppingVO);
            orderVO.setReceiverName(shopping.getReceiverName());
        }
        orderVO.setStatus(order.getStatus());
        orderVO.setPaymentType(order.getPaymentType());
        ResponseCode.Order order1 = ResponseCode.Order.codeOf(order.getStatus());
        ResponseCode.Order order2 = ResponseCode.Order.codeOf(order.getPaymentType());
        if(order1!=null){
            orderVO.setStatusDesc(order1.getDesc());
        }
        if(order2!=null){
            orderVO.setPaymentTypeDesc(order2.getDesc());
        }

        orderVO.setPostage(0);
        orderVO.setPayment(order.getPayment());
        orderVO.setOrderNo(order.getOrderNo());

        return orderVO;
    }

    private ShoppingVO assembleShoppingVO(Shopping shopping){
        ShoppingVO shoppingVO = new ShoppingVO();
        if(shoppingVO!=null){
            shoppingVO.setReceiverAddress(shopping.getReceiverAddress());
            shoppingVO.setReceiverCity(shopping.getReceiverCity());
            shoppingVO.setReceiverDistrict(shopping.getReceiverDistrict());
            shoppingVO.setReceiverMobile(shopping.getReceiverMobile());
            shoppingVO.setReceiverName(shopping.getReceiverName());
            shoppingVO.setReceiverPhone(shopping.getReceiverPhone());
            shoppingVO.setReceiverProvince(shopping.getReceiverProvince());
            shoppingVO.setReceiverZip(shopping.getReceiverZip());
        }
        return shoppingVO;
    }

    private OrderItemVO assembleOrderItemVO(OrderItem orderItem){
        OrderItemVO orderItemVO = new OrderItemVO();
        if(orderItem!=null){
            orderItemVO.setQuantity(orderItem.getQuantity());
            orderItemVO.setCreateTime(DateUtils.dateToStr(orderItem.getCreateTime()));
            orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
            orderItemVO.setOrderNo(orderItem.getOrderNo());
            orderItemVO.setProductId(orderItem.getProductId());
            orderItemVO.setProductName(orderItem.getProductName());
            orderItemVO.setProductImage(orderItem.getProductImage());
            orderItemVO.setTotalPrice(orderItem.getTotalPrice());
        }
        return orderItemVO;
    }

    //购物车中清空已选择的商品
    private void cleanCart(List<Cart> cartList){
        //批量删除
        if(cartList!=null&&cartList.size()>0){
            cartMapper.deleteBatch(cartList);
        }
    }

    //扣库存
    private void redecuProductStock(List<OrderItem> orderItemList){
        if(orderItemList!=null&&orderItemList.size()>0){
            for(OrderItem orderItem:orderItemList){
                Integer productId = orderItem.getProductId();
                Integer quantity = orderItem.getQuantity();
                Product product = productMapper.selectByPrimaryKey(productId);
                if (product != null) {
                    product.setStock(product.getStock()-quantity);
                }
                productMapper.updateByPrimaryKey(product);
            }
        }
    }

    //计算订单的总价格
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList){
        BigDecimal bigDecimal = new BigDecimal("0");
        for(OrderItem orderItem:orderItemList){
          bigDecimal =  BigDecimalUtils.add(bigDecimal.doubleValue(),orderItem.getTotalPrice().doubleValue());
        }
        return bigDecimal;
    }

    //创建订单的方法
    private Order createOrder(Integer userId, Integer shoppingId, BigDecimal orderTotalPrice){
        Order order = new Order();
        //需要生成一个唯一的订单编号
        order.setOrderNo(generatorOrderNO());
        order.setUserId(userId);
        order.setShippingId(shoppingId);
        order.setStatus(ResponseCode.Order.ORDER_UN_PAY.getCode());
        //订单金额
        order.setPayment(orderTotalPrice);
        order.setPostage(0);
        order.setPaymentType(ResponseCode.Order.ONLINE.getCode());
        //保存订单
        int result = orderMapper.insert(order);
        if(result>0){
            return order;
        }
        return null;
    }

    //生成订单编号的方法
    private Long generatorOrderNO(){
        //适用于少量y用户，并发少的状况
        return System.currentTimeMillis()+new Random().nextInt(100);
    }

    //List<Cart>  ---> List<OrderItem>订单明细  方法
    private ServerResponse getCartOrderItem(Integer userId,List<Cart> cartList){

        if(cartList==null||cartList.size()==0){
            return ServerResponse.createByError("购物车为空");
        }
        List<OrderItem> orderItemList = Lists.newArrayList();
        for(Cart cart : cartList){
            OrderItem orderItem = new OrderItem();
            orderItem.setUserId(userId);
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            if(product==null){
                return ServerResponse.createByError("id为"+cart.getId()+"的商品不存在");
            }
            if(product.getStatus()==ResponseCode.PRODUCT_OFFLINE.getCode()){
                return ServerResponse.createByError("id为"+product.getId()+"的商品已下架");
            }
            if(product.getStock()<cart.getQuantity()){
                return ServerResponse.createByError("id为"+product.getId()+"的商品库存不足");
            }
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setProductId(cart.getProductId());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setProductName(product.getName());
            orderItem.setTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),cart.getQuantity().doubleValue()));
            orderItemList.add(orderItem);
        }

        return ServerResponse.createBySuccess(orderItemList);
    }
}
