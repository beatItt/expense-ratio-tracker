package com.learn.expensetracker.services;

import com.learn.expensetracker.domain.User;
import com.learn.expensetracker.exceptions.EtAuthException;
import com.learn.expensetracker.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.regex.Pattern;

@Service
@Transactional
//each invoked method creates a new db transaction which gets committed only when method gets successfully completed,
// else if any runtime exception say, rollbk
public class UserServiceImpl implements  UserService{

    @Autowired
    UserRepository userRepository;

    @Override
    public User validateUser(String email, String password) throws EtAuthException {
        if (email!=null){
            email=email.toLowerCase();
        }
        User user=userRepository.findByEmailAndPassword(email, password);

        return user;
    }

    @Override
    public User registerUser(String firstName, String lastName, String email, String password) throws EtAuthException {
        if (email!=null){
            email=email.toLowerCase();
        }
        Pattern pattern= Pattern.compile("^(.+)@(.+)$");
        if(!pattern.matcher(email).matches()){
            throw new EtAuthException("Invalid email format");
        }
        //check for email not already existing
        Integer count=userRepository.getCountByEmail(email);
        if(count > 0){
            throw new EtAuthException("email already in use");
        }//else create user
        Integer userId=userRepository.create(firstName, lastName, email, password);
        User createdUser=userRepository.findById(userId);
        return  createdUser;
    }
}
