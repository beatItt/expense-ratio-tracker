package com.learn.expensetracker.services;

import com.learn.expensetracker.domain.Category;
import com.learn.expensetracker.exceptions.EtBadRequestException;
import com.learn.expensetracker.exceptions.EtResourceNotFoundException;
import com.learn.expensetracker.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Category> fetchAllCategories(Integer userId) throws EtResourceNotFoundException {
        if(userId!=null){
            List<Category> categoriesList=categoryRepository.fetchAll(userId);
            return categoriesList;
        }else{
            throw new EtResourceNotFoundException("user id is N/A");
        }
    }

    @Override
    public Category fetchCategoryById(Integer userId, Integer categoryId) throws EtResourceNotFoundException {
        return categoryRepository.fetchById(userId,categoryId);
    }

    @Override
    public Category addCategory(Integer userId, String title, String description) throws EtBadRequestException {
        Integer categoryId=categoryRepository.create(userId, title, description);
        Category category=categoryRepository.fetchById(userId,categoryId);
        return  category;

    }

    @Override
    public void updateCategory(Integer userId, Integer categoryId,Category category) throws EtBadRequestException {
        categoryRepository.update(userId,categoryId, category);

    }

    @Override
    public void removeCategoryWithAllTransactions(Integer userId, Integer categoryId) throws EtResourceNotFoundException {
        categoryRepository.removeById(userId, categoryId);
    }


    @Override
    public void updatePartialCategory(Integer userId, Integer categoryId,String title, String description) {

        //System.out.println(" userId: "+userId+" categoryId: "+categoryId+" title: "+title+" desc: "+description);

        if(title!=null && description==null)
            categoryRepository.updatePartialOnlyTitle(userId,categoryId,title);
        else if(description!=null && title==null)
            categoryRepository.updatePartialOnlyDescription(userId,categoryId,description);
        else if(title!=null && description!=null) {
            Category category = new Category();
            category.setDescription(description);
            category.setTitle(title);
            this.updateCategory(userId, categoryId, category);//delegate to put request service method in case by mistake client
            //tries to update entire resource in PATCH request
        }
        else{
            throw new EtBadRequestException("invalid request for patch");
        }


    }


}
