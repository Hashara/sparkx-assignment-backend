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

@WebFilter(filterName = "patientFilter")
public class PatientFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        Person person = (Person) req.getAttribute("user");
        if (person.getRole() == RoleType.Patient || person.getRole() == RoleType.Doctor
                || person.getRole() == RoleType.Director
                || person.getRole() == RoleType.HospitalStaff) {
            filterChain.doFilter(req, res);
        } else {
            new Controller().sendMessageResponse(Message.FORBIDDEN, res, HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    public void destroy() {

    }
}
