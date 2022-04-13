package com.learn.expensetracker.controller;

import com.learn.expensetracker.domain.Category;
import com.learn.expensetracker.exceptions.EtBadRequestException;
import com.learn.expensetracker.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("")
    public ResponseEntity<List<Category> > getAllCategories(HttpServletRequest request){//request forwarded by AuthFilter which has attrb userId attached
        Integer userId= (Integer) request.getAttribute("userId");
        List<Category>  categories=categoryService.fetchAllCategories(userId);
        return  new ResponseEntity<>(categories, HttpStatus.OK);

    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategoryByUserId( HttpServletRequest request,@PathVariable("categoryId") Integer categoryId){//request forwarded by AuthFilter which has attrb userId attached
        Integer userId= (Integer) request.getAttribute("userId");
        Category category=categoryService.fetchCategoryById(userId,categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);

    }

    @PostMapping("")
    public ResponseEntity<Category> addCategory(HttpServletRequest request, @RequestBody Map<String,String> categoryMap){
        Integer userId= (Integer) request.getAttribute("userId");
        String title=categoryMap.get("title");
        String description=categoryMap.get("description");
       Category category= categoryService.addCategory(userId,title,description);
        return  new ResponseEntity<>(category,HttpStatus.CREATED);
        //can use Map<String,Object> and typecast title to (String)
    }

    @PutMapping("/{categoryId}") //PARTIAL UPDATES not happening , ex. only title update not happening
    public ResponseEntity<Map<String,Boolean>> updateCategory(HttpServletRequest request,@PathVariable("categoryId") Integer categoryId, @RequestBody Category category){
        Integer userId= (Integer) request.getAttribute("userId");
        categoryService.updateCategory(userId,categoryId,category);
        Map<String,Boolean> map=new HashMap<>();
        map.put("success",true);
        return  new ResponseEntity<>(map,HttpStatus.OK);
    }

    @PatchMapping("/{categoryId}") //PARTIAL UPDATES happening , ex. only title update happening
    public ResponseEntity<Map<String,Boolean>> updatePartialCategory(HttpServletRequest request,@PathVariable("categoryId") Integer categoryId, @RequestBody Map<String,String> categoryMap){
        Integer userId= (Integer) request.getAttribute("userId");
        String description=categoryMap.get("description");
        String title=categoryMap.get("title");

        categoryService.updatePartialCategory(userId, categoryId,title,description);
        Map<String,Boolean> map=new HashMap<>();
        map.put("success",true);
        return  new ResponseEntity<>(map,HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Map<String, Boolean>> deleteCategory(HttpServletRequest request,
                                                               @PathVariable("categoryId") Integer categoryId) {
        int userId = (Integer) request.getAttribute("userId");
        categoryService.removeCategoryWithAllTransactions(userId, categoryId);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }



}
