package com.itdr.service.impl;

import com.itdr.common.ResponseCode;
import com.itdr.common.ServerResponse;
import com.itdr.dao.UserInfoMapper;
import com.itdr.pojo.UserInfo;
import com.itdr.service.IUserService;
import com.itdr.utils.MD5Utils;
import com.itdr.utils.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public ServerResponse login(String username, String password) {

        //1.参数的非空校验
        if (username==null||username.equals("")){
            return ServerResponse.createByError("用户名不能为空");
        }
        if (password==null||password.equals("")){
            return ServerResponse.createByError("密码不能为空");
        }

        //2.检查用户名是否存在
        int result = userInfoMapper.checkUsername(username);
        if(result==0){
            return ServerResponse.createByError("用户名不存在");
        }

        //3.根据用户名和密码查找用户信息
        UserInfo userInfo;
        userInfo = userInfoMapper.selectUserInfoByUsernameAndPassword(username,MD5Utils.getMD5Code(password));

        //4.判断是否存在
        if(userInfo==null){
            return ServerResponse.createByError("密码错误");
        }

        //5.返回结果
        userInfo.setPassword("");
        return ServerResponse.createBySuccess("登录成功",userInfo);
    }

    @Transactional
    @Override
    public ServerResponse register(UserInfo userInfo) {
        //1.参数的非空校验
        if(userInfo==null){
            return ServerResponse.createByError("参数必须");
        }
        //2.校验用户名
        int result_username = userInfoMapper.checkUsername(userInfo.getUsername());
        if(result_username>0){
            return ServerResponse.createByError("用户名已经存在");
        }
        //3.校验邮箱
        int result_emial = userInfoMapper.checkEmail(userInfo.getEmail());
        if(result_emial>0){
            return ServerResponse.createByError("邮箱已经存在");
        }

        //4.注册
        userInfo.setRole(ResponseCode.ROLE_CUSTOMER.getCode());        //设置用户角色
        userInfo.setPassword(MD5Utils.getMD5Code(userInfo.getPassword()));      //为用户密码进行MD5加密
        int count =  userInfoMapper.insert(userInfo);
        if(count>0){
            return ServerResponse.createBySuccess("注册成功");
        }
        //5.返回结果
        return ServerResponse.createByError("注册失败");
    }

    @Override
    public ServerResponse forget_get_question(String username) {
        //1.参数校验
        if(username==null||username.equals("")){
            return ServerResponse.createByError("用户名不能为空");
        }
        //2.校验username
        int result = userInfoMapper.checkUsername(username);
        if(result==0){
            return ServerResponse.createByError("用户名不存在，请重新输入");
        }

        //3.查找密保问题
        String question = userInfoMapper.selectQuestionByUsername(username);
        if(question==null||question.equals("")){
            return ServerResponse.createByError("密保问题为空");
        }

        return ServerResponse.createBySuccess(question);
    }

    @Override
    public ServerResponse forget_check_answer(String username, String question, String answer) {

        //1.参数校验
        if (username==null||username.equals("")){
            return ServerResponse.createByError("用户名不能为空");
        }
        if (question==null||question.equals("")){
            return ServerResponse.createByError("问题不能为空");
        }
        if (answer==null||answer.equals("")){
            return ServerResponse.createByError("答案不能为空");
        }

        //2.根据username问题和答案 进行查询
        int result = userInfoMapper.SelectByUsernameAndQuestionAndAnswer(username,question,answer);
        if(result==0){
            return ServerResponse.createByError("答案错误");
        }

        //3.服务端要生成一个token保存，并将token返回给客户端
        String forgetToken = UUID.randomUUID().toString();      //防止用户横向越权修改别人密码
        //guava cache
        TokenCache.set(username,forgetToken);
        return ServerResponse.createBySuccess(forgetToken);
    }

    @Transactional
    @Override
    public ServerResponse forget_reset_password(String username, String newpassword, String forgetToken) {

        //1.参数校验
        if (username==null||username.equals("")){
            return ServerResponse.createByError("用户名不能为空");
        }
        if (newpassword==null||newpassword.equals("")){
            return ServerResponse.createByError("密码不能为空");
        }
        if (forgetToken==null||forgetToken.equals("")){
            return ServerResponse.createByError("token不能为空");
        }

        //2.校验token
        String token = TokenCache.get(username);
        if(token==null){
            return ServerResponse.createByError("token已经过期");
        }
        if(!token.equals(forgetToken)){
            return ServerResponse.createByError("无效的token");
        }

        //3.修改密码
        int result =  userInfoMapper.updateUserPassword(username,MD5Utils.getMD5Code(newpassword));
        if(result>0){
            return ServerResponse.createBySuccess("修改成功");
        }
        return ServerResponse.createByError("密码修改失败");
    }

    @Override
    public ServerResponse check_valid(String str, String type) {

        //1.参数非空校验
        if (str==null||str.equals("")){
            return ServerResponse.createByError("用户名或者邮箱不能为空");
        }
        if (type==null||type.equals("")){
            return ServerResponse.createByError("校验的类型参数不能为空");
        }
        //2.type:username -->校验用户名str
        //       email    -->校验邮箱str
        if(type.equals("username")){
            int result = userInfoMapper.checkUsername(str);
            if(result>0){
                //用户已存在
                return ServerResponse.createByError("用户已存在");
            }else {
                return ServerResponse.createBySuccess();
            }
        }else if(type.equals("email")){
            int result_email = userInfoMapper.checkEmail(str);
            if(result_email>0){
                //邮箱已存在
                return ServerResponse.createByError("邮箱已存在");
            }else {
                return ServerResponse.createBySuccess();
            }
        }else{
            return ServerResponse.createByError("参数类型错误");
        }

    }

    @Transactional
    @Override
    public ServerResponse reset_password(String username,String passwordOld, String passwordNew) {

        //1.参数的非空校验
        if (passwordOld==null||passwordOld.equals("")){
            return ServerResponse.createByError("用户名旧密码不能为空");
        }
        if (passwordNew==null||passwordNew.equals("")){
            return ServerResponse.createByError("用户新密码不能为空");
        }
        //2.根据usernamehe passwordOld

        UserInfo userInfo = userInfoMapper.selectUserInfoByUsernameAndPassword(username,MD5Utils.getMD5Code(passwordOld));
        if(userInfo==null){
            return ServerResponse.createByError("旧密码错误");
        }
        //3.修改密码
        userInfo.setPassword(MD5Utils.getMD5Code(passwordNew));
        int result = userInfoMapper.updateByPrimaryKey(userInfo);
        if(result>0){
            return ServerResponse.createBySuccess("修改成功");
        }else {
            return ServerResponse.createByError("修改失败");
        }

    }


    @Transactional
    @Override
    public ServerResponse update_information(UserInfo user) {

        //1.参数非空校验
        if(user==null){
            return ServerResponse.createByError("参数不能为空");
        }
        //2.更新个人信息
        int result = userInfoMapper.updateUserBySelectActive(user);
        if(result>0){
            return  ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError("更新个人信息失败");
    }

    @Override
    public UserInfo findUserInfoByUserid(Integer id) {

        return userInfoMapper.selectByPrimaryKey(id);

    }

    @Transactional
    @Override
    public int updateTokenByUserId(Integer id, String token) {
        return userInfoMapper.updateTokenByUserId(id,token);
    }

    @Override
    public UserInfo findUserInfoByToken(String autoLoginToken) {
        if(autoLoginToken==null||autoLoginToken.equals("")){
            return  null;
        }
        return userInfoMapper.findUserInfoByToken(autoLoginToken);
    }


}