package com.ohx.filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author zhoule
 * @description: 过滤器，处理request
 */

@WebFilter(filterName = "httpServletRequestWrapperFilter", urlPatterns = {"/*"})
public class ChannelFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            ServletRequest requestWrapper = null;
            if(servletRequest instanceof HttpServletRequest) {
                requestWrapper = new RequestWrapper((HttpServletRequest) servletRequest);
            }
            if(requestWrapper == null) {
                System.out.println("servletRequest");
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                System.out.println("requestWrapper");
                filterChain.doFilter(requestWrapper, servletResponse);
            }
        }

        @Override
        public void destroy() {

        }
    }
