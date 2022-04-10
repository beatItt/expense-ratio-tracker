package com.learn.expensetracker.controller;

import com.learn.expensetracker.domain.User;
import com.learn.expensetracker.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public  ResponseEntity<Map<String,String>> loginUser(@RequestBody Map<String,String> userMap){
        String email=userMap.get("email");
        String password=userMap.get("password");
        User user=userService.validateUser(email,password);
        Map<String, String> map=new HashMap<>();
        map.put("message","logged in successfully");
        return  new ResponseEntity<>(map, HttpStatus.OK);

    }

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> registerUser(@RequestBody Map<String,String> userMap){ //the json reqBody data auto conv to java Map obj by @RequestBody,
        // but format of json from client side(postman) and the java obj mentioned with @RequestBody ie Map<String,Object>
        // should be compatible. The required resource presentation class ex User pojo should be present alongwith resource controller

        String firstName=userMap.get("firstName");
        String lastName=userMap.get("lastName");
        String email=userMap.get("email");
        String password=userMap.get("password");
        //return firstName+", "+lastName; //jackson returning conv java obj to json(auto included in spring web starter)
        User user=userService.registerUser(firstName,lastName,email,password);
        Map<String,String> map=new HashMap<>();//map is dummy for now, later will have jwt key value
        map.put("message","successfully registered");
        return new ResponseEntity<>(map, HttpStatus.OK);//can set programmatically response headers with ResponseEntity

    }
}
