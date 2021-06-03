package com.sparkx.Filter;

import com.sparkx.model.Person;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "authFilter")
public class AuthServletFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        Person person = AuthFilterHelper.authUser(req, res);

        if (person == null) {
            res.setHeader("WWW-Authenticate", "Basic realm=\"Insert credentials\"");
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            req.setAttribute("user", person);
            filterChain.doFilter(req, res);
        }

    }

    public void destroy() {
    }
}