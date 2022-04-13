package com.learn.expensetracker.filters;

import com.learn.expensetracker.Constants;
import com.sun.deploy.net.HttpResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//for protected resources
public class AuthFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        //extract token and claims
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null) {


            String[] bearerToken = authHeader.split("Bearer");

            if (bearerToken.length > 1 && bearerToken[1] != null) {
                String token = bearerToken[1];

                ///get claims

                try {
                    Claims claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY).parseClaimsJws(token).getBody();
                    httpRequest.setAttribute("userId", Integer.parseInt(claims.get("userId").toString()));
                    //can access userId anywhere from request


                } catch (Exception e) {
                    System.out.println(e);
                    httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "invalid/expired token");
                    return; //end processing of request
                }

            } else {
                httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "empty bearer token");
                return;
            }
        } else {
            httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "Authorization header missing");
            return;
        }
        // everything is validated,continue processing with req resp objects
        filterChain.doFilter(httpRequest,httpResponse);
    }
}
