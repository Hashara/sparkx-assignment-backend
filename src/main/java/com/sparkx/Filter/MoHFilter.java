package com.sparkx.Filter;

import com.sparkx.controller.Controller;
import com.sparkx.model.Person;
import com.sparkx.model.Types.RoleType;
import com.sparkx.util.Message;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "mohFilter")
public class MoHFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        if (req.getAttribute("role").equals(RoleType.MoH.toString())) {
            filterChain.doFilter(req, res);
        } else {
            new Controller().sendMessageResponse(Message.FORBIDDEN, res, HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    public void destroy() {

    }
}
