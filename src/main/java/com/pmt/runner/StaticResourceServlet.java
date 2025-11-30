package com.pmt.runner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StaticResourceServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contextPath = req.getContextPath();
        String uri = req.getRequestURI();
        String rel = uri.substring(contextPath.length()); // e.g., /css/style.css

        if (rel == null || rel.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Try to load resource from the webapp root
        InputStream in = getServletContext().getResourceAsStream(rel);
        if (in == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mime = getServletContext().getMimeType(rel);
        if (mime != null) resp.setContentType(mime);

        try (InputStream is = in; OutputStream os = resp.getOutputStream()) {
            byte[] buf = new byte[8192];
            int r;
            while ((r = is.read(buf)) != -1) os.write(buf, 0, r);
        }
    }
}
