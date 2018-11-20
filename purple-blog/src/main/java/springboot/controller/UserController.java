package springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springboot.modal.vo.UserVo;
import springboot.modal.vo.UserVoExample;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import springboot.service.UserServiceTest;

import java.util.List;

@Controller
@EnableAutoConfiguration
@RequestMapping("/test")
public class UserController {


    @Autowired
    UserServiceTest userServiceTest;

    @RequestMapping("/getUser")
    @ResponseBody
    public UserVo getUserInfo(){

        UserVo userVo= userServiceTest.getUserById(1);



        return userVo;
    }

    @RequestMapping(value="/getUserList",method = RequestMethod.GET)
    public String getUserList(ModelMap map){

        UserVoExample userVoExample=new UserVoExample();



         List<UserVo> list = userServiceTest.getUser(userVoExample);
          map.addAttribute("users",list);


        return "getUserList";
    }






}
