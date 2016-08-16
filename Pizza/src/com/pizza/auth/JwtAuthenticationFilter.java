package com.pizza.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

// This class is the entry point of our JWT authentication process.
// The filter extracts the JWT token from the request headers
// and delegates authentication to the injected AuthenticationManager.
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

		private static final String BEARER_PREFIX = "Bearer ";
	
		public JwtAuthenticationFilter() {
			super("/**");
		} // TODO needed?
	   
	    @Override
	    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
	    		throws AuthenticationException, IOException, ServletException {	    		    	
	    	
	    	// TODO need to work with SecurityContextHolder??
	    	
	    	String authHeader = request.getHeader("authorization");

	        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
	            throw new InsufficientAuthenticationException("No JWT token found in request headers");
	        }

	        String authToken = authHeader.substring(BEARER_PREFIX.length());

	        JwtAuthenticationToken authRequest = new JwtAuthenticationToken(authToken);

	        Authentication auth = getAuthenticationManager().authenticate(authRequest);
	        SecurityContextHolder.getContext().setAuthentication(auth); 
	        // important so that later can access the details from the code using the context
	        return auth;
	   }

	    @Override
	    //We also need an override for successful authentication because the default Spring flow would stop the filter chain
	    //and proceed with a redirect. Keep in mind we need the chain to execute fully, including generating the response.	    
	    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
	    		throws IOException, ServletException {
	        //super.successfulAuthentication(request, response, chain, authResult);
	    	// !! Don't do this! This will cause a redirect and will get an error on the browser:
	    	// "XMLHttpRequest cannot load .../Pizza/api/order/get. 
	    	// The request was redirected to '../Pizza/', which is disallowed for cross-origin requests that require preflight."
	    	

	        // As this authentication is in HTTP header, after success we need to continue the request normally
	        // and return the response as if the resource was not secured at all
	        chain.doFilter(request, response);
	    }
}
