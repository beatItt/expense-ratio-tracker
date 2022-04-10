package com.learn.expensetracker.services;

import com.learn.expensetracker.domain.User;
import com.learn.expensetracker.exceptions.EtAuthException;

public interface UserService {

     User validateUser(String email, String password) throws EtAuthException;

     User registerUser(String firstName, String lastName, String email, String password) throws EtAuthException;


}
