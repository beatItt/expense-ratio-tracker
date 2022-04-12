package com.learn.expensetracker.controller;

import com.learn.expensetracker.Constants;
import com.learn.expensetracker.domain.User;
import com.learn.expensetracker.services.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Date;
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
        return  new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);

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
        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);//can set programmatically response headers with ResponseEntity

    }//adding jwt, for protected resources, need to create filter
    private Map<String,String> generateJWTToken(User user){
        //validity for 2 hrs
        long currentTimeInMilliSec=System.currentTimeMillis();
        String token= Jwts.builder().signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
                .setIssuedAt(new Date(currentTimeInMilliSec))
                .setExpiration(new Date(currentTimeInMilliSec+Constants.TOKEN_VALIDITY))
                .claim("userId",user.getUserId())
                .claim("firstName",user.getFirstName())//dont put confidential info like pwd in claims cuz accessible on internet
                .claim("lastName",user.getLastName())
                .claim("email",user.getEmail())
                .compact();
        Map<String,String> map=new HashMap<>();
        map.put("token",token);

        //https://github.com/utPLSQL/utPLSQL-cli/issues/82

    return map;
    }
}
