package com.learn.expensetracker;

import com.learn.expensetracker.filters.AuthFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ExpenseTrackerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExpenseTrackerApiApplication.class, args);}

//register custom filter using spring'sFilterRegistrationBean
// login and regsiter open to anyone, manipulate/view categoy/transaction are protected resources needing token filter validation

@Bean
public FilterRegistrationBean<AuthFilter> filterRegistrationBean(){

		FilterRegistrationBean<AuthFilter> registrationBean=new FilterRegistrationBean<>();
		AuthFilter authFilter=new AuthFilter();
		registrationBean.setFilter(authFilter);
		registrationBean.addUrlPatterns("/api/categories/*"); //protected resource
		return  registrationBean;

	}

}

