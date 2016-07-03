package com.pizza.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pizza.security.JwtAuthenticationFilter;
import com.pizza.security.JwtAuthenticationProvider;
import com.pizza.security.RestAuthenticationEntryPoint;


//This configuration creates a Servlet Filter known as the springSecurityFilterChain 
//which is responsible for all the security (protecting the application URLs, 
//validating submitted username and passwords, redirecting to the log in form, etc) within our application.

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
 
    @Autowired
    private UserDetailsService userDetailsService;
    
	@Autowired
	PasswordEncoder passwordEncoder;
	
    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder);
    }
    
    @Autowired
    private RestAuthenticationEntryPoint unauthorizedHandler;
    
    @Bean
    @Override
    // This is a must!
    // otherwise the authentication manager is not known (even that configured in configureAuthentication())
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new PizzaPasswordEncoder(); 
//    }
	
    @Bean
    public AuthenticationProvider authenticator() {
    	return new JwtAuthenticationProvider();
    }
	
    @Bean
    public AbstractAuthenticationProcessingFilter authenticationTokenFilterBean() throws Exception {
    	JwtAuthenticationFilter authenticationTokenFilter = new JwtAuthenticationFilter();
        authenticationTokenFilter.setAuthenticationManager(authenticationManagerBean());
        
        
		List<RequestMatcher> matchersList = new ArrayList<RequestMatcher>();
		// restrict matcher to specific http methods - to allow OPTIONS not to go through the filter!
		matchersList.add(new AntPathRequestMatcher("/api/**", RequestMethod.POST.toString(), false));
		matchersList.add(new AntPathRequestMatcher("/api/**", RequestMethod.GET.toString(), false));		
		RequestMatcher requestMatcher = new OrRequestMatcher(matchersList);
		authenticationTokenFilter.setRequiresAuthenticationRequestMatcher(requestMatcher );
		
        return authenticationTokenFilter;
    }
	
    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
    	auth.authenticationProvider(authenticator());
    	super.configure(auth);
    }
     
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
//    	TODO
//    	httpSecurity.authorizeRequests()
//		.antMatchers(
//                HttpMethod.GET,"/favicon.ico").permitAll()
//		.antMatchers(HttpMethod.POST,"/login1").permitAll();
    	
    	httpSecurity
    		.csrf().disable() // TODO correct?? don't need CSRF because token is invulnerable
    		
    	// don't create session
    		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    		.and().exceptionHandling().authenticationEntryPoint(unauthorizedHandler);    	    	    
    		;	
    		
    	httpSecurity
    		//.addFilterAfter(authenticationTokenFilterBean(), BasicAuthenticationFilter.class)
	        .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
	        // addFilterBefore(the new before filter to add, the known filter)
	        //.formLogin()
	        ;
    	
        // disable page caching
        httpSecurity.headers().cacheControl();
        
        
       //super.configure(httpSecurity);
    }
}
