package com.learn.expensetracker.repositories;

import com.learn.expensetracker.domain.User;
import com.learn.expensetracker.exceptions.EtAuthException;

public interface UserRepository {

    //register
     Integer create(String firstName, String lastName, String email, String password) throws EtAuthException;

     //login
     User findByEmailAndPassword(String email, String password) throws EtAuthException;

     //if already registered user tries to register, check email id
     Integer getCountByEmail(String email);

     User findById(Integer userId);
}
