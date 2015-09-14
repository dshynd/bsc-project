package org.danh.project.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

@WebFilter(urlPatterns = { "*.png" })
public class ImageExpirationFilter implements Filter {
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filter) throws IOException, ServletException {
		HttpServletResponse httpResp = (HttpServletResponse) resp;
		httpResp.setDateHeader("Expires", -1);
		httpResp.setHeader("Cache-Control", "no-cache, no-store");
		filter.doFilter(req, resp);
	}

	public void destroy() {
	}

	public void init(FilterConfig config) throws ServletException {
	}
}
