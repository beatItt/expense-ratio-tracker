package com.learn.expensetracker.repositories;

import com.learn.expensetracker.domain.Category;
import com.learn.expensetracker.domain.User;
import com.learn.expensetracker.exceptions.EtBadRequestException;
import com.learn.expensetracker.exceptions.EtResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.rowset.JdbcRowSet;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
@Repository
public class CategoryRepositoryImpl implements CategoryRepository{


    private static final String SQL_FIND_ALL = "SELECT C.CATEGORY_ID, C.USER_ID, C.TITLE, C.DESCRIPTION, " +
            "COALESCE(SUM(T.AMOUNT), 0) TOTAL_EXPENSE " +
            "FROM ET_TRANSACTIONS T RIGHT OUTER JOIN ET_CATEGORIES C ON C.CATEGORY_ID = T.CATEGORY_ID " +
            "WHERE C.USER_ID = ? GROUP BY C.CATEGORY_ID";
    private static final String SQL_FIND_BY_ID = "SELECT C.CATEGORY_ID, C.USER_ID, C.TITLE, C.DESCRIPTION, " +
            "COALESCE(SUM(T.AMOUNT), 0) TOTAL_EXPENSE " +
            "FROM ET_TRANSACTIONS T RIGHT OUTER JOIN ET_CATEGORIES C ON C.CATEGORY_ID = T.CATEGORY_ID " +
            "WHERE C.USER_ID = ? AND C.CATEGORY_ID = ? GROUP BY C.CATEGORY_ID";
    private static final String SQL_CREATE = "INSERT INTO ET_CATEGORIES (CATEGORY_ID, USER_ID, TITLE, DESCRIPTION) VALUES(NEXTVAL('ET_CATEGORIES_SEQ'), ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE ET_CATEGORIES SET TITLE = ?, DESCRIPTION = ? " +
            "WHERE USER_ID = ? AND CATEGORY_ID = ?";
    private static final String SQL_DELETE_CATEGORY = "DELETE FROM ET_CATEGORIES WHERE USER_ID = ? AND CATEGORY_ID = ?";
    private static final String SQL_DELETE_ALL_TRANSACTIONS = "DELETE FROM ET_TRANSACTIONS WHERE CATEGORY_ID = ?";

    //partial updates
    private static final String SQL_UPDATE_PARTIAL_TITLE = "UPDATE ET_CATEGORIES SET TITLE = ? " +
            "WHERE USER_ID = ? AND CATEGORY_ID = ?";
    private static final String SQL_UPDATE_PARTIAL_DESCRIPTION = "UPDATE ET_CATEGORIES SET DESCRIPTION = ? " +
            "WHERE USER_ID = ? AND CATEGORY_ID = ?";

    //right outer join cuz we wanted categ table wholly and transactions if commonly present, else default to 0.
    //group by for agg fn sum by category

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public List<Category> fetchAll(Integer userId) {
        //query for multiple records, queryForObject for single record
        try {
            return jdbcTemplate.query(SQL_FIND_ALL, categoryRowMapper, userId);
        }catch(Exception e){
            throw new EtResourceNotFoundException("categories not found");
        }
    }

    @Override
    public Category fetchById(Integer userId, Integer categoryId) {
        try{
            Category category=jdbcTemplate.queryForObject(SQL_FIND_BY_ID,categoryRowMapper,userId,categoryId);
            return  category;
        }catch(Exception e){
            throw new EtResourceNotFoundException("category not present for given id");
        }
    }

    @Override
    public Integer create(Integer userId, String title, String description) {

        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setString(2, title);
                ps.setString(3, description);
                return ps;

            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("CATEGORY_ID");
        }catch(Exception e){
            throw new EtBadRequestException("invalid request");
        }
    }

    @Override
    public void update(Integer userId, Integer categoryId, Category category) throws EtBadRequestException {
        try {
            jdbcTemplate.update(SQL_UPDATE, new Object[]{category.getTitle(), category.getDescription(), userId, categoryId});
        }catch(Exception e){
            throw new EtResourceNotFoundException("invalid update request");
        }
    }

    @Override
    public void removeById(Integer userId, Integer categoryId) throws EtResourceNotFoundException {
        removeAllTransactions(categoryId);
        jdbcTemplate.update(SQL_DELETE_CATEGORY,new Object[]{userId,categoryId});


    }

    @Override
    public void updatePartialOnlyTitle(Integer userId, Integer categoryId,String title) {
        jdbcTemplate.update(SQL_UPDATE_PARTIAL_TITLE,new Object[]{title,userId,categoryId});

    }

    @Override
    public void updatePartialOnlyDescription(Integer userId, Integer categoryId,String description) {
        jdbcTemplate.update(SQL_UPDATE_PARTIAL_DESCRIPTION,new Object[]{description,userId,categoryId});

    }

    private void removeAllTransactions(Integer categoryId){
        jdbcTemplate.update(SQL_DELETE_ALL_TRANSACTIONS,new Object[]{categoryId});

    }

    private RowMapper<Category> categoryRowMapper=((resultSet, rowNum)->{
        return new Category(

                resultSet.getInt("CATEGORY_ID"),
                resultSet.getInt("USER_ID"),
                resultSet.getString("TITLE"),
                resultSet.getString("DESCRIPTION"),
                resultSet.getBigDecimal("TOTAL_EXPENSE"));


    });
}
