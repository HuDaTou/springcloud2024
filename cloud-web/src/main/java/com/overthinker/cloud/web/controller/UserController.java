package com.overthinker.cloud.web.controller;

import cn.hutool.core.bean.BeanUtil;
import com.overthinker.cloud.resp.ResultData;
import com.overthinker.cloud.web.entity.PO.UserInfo;
import com.overthinker.cloud.web.entity.VO.UserInfoVO;
import com.overthinker.cloud.web.service.UserInfoService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.Getter;
import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserInfoService userInfoService;

    public UserController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }




    //    数据库增删改查测试
//    增加
    @PostMapping("/add")
    public String add(){
        return "add";
    }
//    删除
    @PostMapping("/delete")
    public String delete(){
        return "delete";
    }
//    修改
    @PostMapping("/update")
    public String update(){
        return "update";
    }
//    查询
    @GetMapping("/select")
    public ResultData<UserInfoVO> selectUserInfoByIds(@RequestParam("userIds") Integer userIds){
        UserInfo userInfos = userInfoService.getById(userIds);
        UserInfoVO userInfoVO = BeanUtil.copyProperties(userInfos, UserInfoVO.class);
        return ResultData.success(userInfoVO);
    }





}
