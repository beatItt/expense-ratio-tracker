package com.learn.expensetracker.repositories;

import com.learn.expensetracker.domain.User;
import com.learn.expensetracker.exceptions.EtAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;

//spring identifies annotated classes and configures them in application context @Service, @Component, @Repository
//automatic bean detection
//diff annotation for new updates , and for our distinction betw tiers(data access tier, service bu logic tier etc)
@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String SQL_CREATE = "INSERT INTO ET_USERS(USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD) VALUES(NEXTVAL('ET_USERS_SEQ'), ?, ?, ?, ?)";
    private static final String SQL_COUNT_BY_EMAIL = "SELECT COUNT(*) FROM ET_USERS WHERE EMAIL = ?";
    private static final String SQL_FIND_BY_ID = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD " +
            "FROM ET_USERS WHERE USER_ID = ?";
    private static final String SQL_FIND_BY_EMAIL = "SELECT USER_ID, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD " +
            "FROM ET_USERS WHERE EMAIL = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Integer create(String firstName, String lastName, String email, String password) throws EtAuthException {

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();//jdbcTemplate update method does not return obj, use keyHolder
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, email);
                ps.setString(4, password);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("USER_ID");//get key for user_id specifically
        } catch (Exception e) {
            throw new EtAuthException("invalid details. Account not created");
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password) throws EtAuthException {
        return null;
    }

    @Override
    public Integer getCountByEmail(String email) {
        //passing array of obj with params, classType we expect method to return
        //query for single row
        return jdbcTemplate.queryForObject(SQL_COUNT_BY_EMAIL,Integer.class,email);

    }

    @Override
    public User findById(Integer userId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID,userRowMapper,userId);
        //resultset will contain a row of user which we will map to Use java obj with UserMapper
        //Mapper converts resultset object to proper java object, like User to be returned by fn
    }

    private RowMapper<User> userRowMapper=((resultSet,rowNum)->{
        return new User(
                resultSet.getInt("USER_ID"),
                resultSet.getString("FIRST_NAME"),
                resultSet.getString("LAST_NAME"),
                resultSet.getString("EMAIL"),
                resultSet.getString("PASSWORD") );


    });
}
