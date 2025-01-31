package com.overthinker.cloud.security.controller;


import com.overthinker.cloud.security.entity.User;
import com.overthinker.cloud.security.service.IUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author author
 * @since 2025-01-04
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    public IUserService userService;

    @GetMapping("/list")
    public Object list() {
        return userService.list();
    }

    @PostMapping("/add")
    public void add(@RequestBody User user){
        userService.saveUserDetails(user);
    }

}
