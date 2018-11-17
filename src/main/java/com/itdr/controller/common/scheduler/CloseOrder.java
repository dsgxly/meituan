package com.itdr.controller.common.scheduler;


import com.itdr.service.IOrderService;
import com.itdr.utils.PropertiesUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

//定时关闭订单
@Component
public class CloseOrder {

    @Autowired
    IOrderService orderService;

    @Scheduled(cron ="0 0/5 * * * * ")  //每隔30s执行一次
    public void closerOrder(){
        System.out.println("关闭订单");
        Integer hour = Integer.parseInt(PropertiesUtils.readByKey("close.order.time"));
        String date = com.itdr.utils.DateUtils.dateToStr(DateUtils.addHours(new Date(),-hour));
        orderService.closeOrder(date);
    }
}
