package com.homewrk.backpack.common.filter;


import lombok.extern.java.Log;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

@Component("loggingFilter")
@Log
public class LoggingFilter extends AbstractRequestLoggingFilter {


    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        logger.info("Request access.................. " + message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        logger.info("Request exite.................. " + message);
    }
}
