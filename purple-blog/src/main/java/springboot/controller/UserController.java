package springboot.controller;

import springboot.modal.vo.UserVo;
import springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@RequestMapping("/test")
public class UserController {


    @Autowired
    UserService userService;

    @RequestMapping("getUser")
    public UserVo getUserInfo(){

        UserVo userVo= userService.getUserById(1);



        return userVo;
    }



}
