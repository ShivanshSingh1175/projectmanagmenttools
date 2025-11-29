package com.pmt.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * AuthorizationFilter - Ensures user has proper role for resource access
 */
@WebFilter(urlPatterns = {"/admin-dashboard", "/pm-dashboard", "/team-dashboard"})
public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
        // Initialization
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        HttpSession session = httpRequest.getSession(false);
        
        if (session == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        String userRole = (String) session.getAttribute("userRole");
        boolean hasAccess = checkAccess(requestURI, userRole);

        if (!hasAccess) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, 
                    "You do not have permission to access this resource");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup
    }

    /**
     * Check if user has access to resource
     */
    private boolean checkAccess(String requestURI, String userRole) {
        if (requestURI.contains("admin-dashboard")) {
            return "ADMIN".equals(userRole);
        } else if (requestURI.contains("pm-dashboard")) {
            return "PROJECT_MANAGER".equals(userRole) || "ADMIN".equals(userRole);
        } else if (requestURI.contains("team-dashboard")) {
            return "TEAM_MEMBER".equals(userRole) || "PROJECT_MANAGER".equals(userRole) || "ADMIN".equals(userRole);
        }
        return false;
    }
}
