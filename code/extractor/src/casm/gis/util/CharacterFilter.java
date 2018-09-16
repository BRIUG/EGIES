package casm.gis.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

public class CharacterFilter implements Filter {

	private Logger logger = Logger.getLogger(this.getClass());
	String encoding = null;
	@Override
	public void destroy() {
		logger.info("***************the CharacterFilter class' destroy() is invoking .***************");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		request.setCharacterEncoding(encoding);
		response.setCharacterEncoding(encoding);
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("***************the CharacterFilter class' init() is invoking .***************");
		encoding = filterConfig.getInitParameter("encoding");
	}
}