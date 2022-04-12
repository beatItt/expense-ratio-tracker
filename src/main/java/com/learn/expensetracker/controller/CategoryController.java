package com.learn.expensetracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @GetMapping
    public String getCategories(HttpServletRequest request){
        Integer userId= (Integer) request.getAttribute("userId");
        return "Authenticated" + userId;
    }


}
