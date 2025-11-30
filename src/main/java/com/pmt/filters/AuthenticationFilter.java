package com.pmt.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * AuthenticationFilter - Ensures user is authenticated
 * Demonstrates role-based access control and filter pattern
 */
@WebFilter(urlPatterns = {"/*"})
public class AuthenticationFilter implements Filter {

    private static final String[] PUBLIC_PATHS = {"/login", "/jsp/login.jsp", "/css/", "/js/", "/pmt-health"};

    @Override
    public void init(FilterConfig config) throws ServletException {
        // Initialization code if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        // Debug logging for requests
        try {
            System.out.println("[DEBUG][AuthFilter] Incoming request: " + httpRequest.getMethod() + " " + requestURI);
        } catch (Exception ignored) {}

        // Check if path is public
        if (isPublicPath(requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        // Check session
        HttpSession session = httpRequest.getSession(false);
        try {
            System.out.println("[DEBUG][AuthFilter] Session present: " + (session != null));
        } catch (Exception ignored) {}
        
        if (session == null || session.getAttribute("userId") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup code if needed
    }

    /**
     * Check if path is public
     */
    private boolean isPublicPath(String path) {
        for (String publicPath : PUBLIC_PATHS) {
            if (path.contains(publicPath) || path.contains("login")) {
                return true;
            }
        }
        return false;
    }
}
