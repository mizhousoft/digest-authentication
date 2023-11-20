package com.mizhousoft.digest.authentication.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.mizhousoft.digest.authentication.DigestAuthenticationService;
import com.mizhousoft.digest.authentication.www.DigestAuthenticationEntryPoint;
import com.mizhousoft.digest.authentication.www.DigestAuthenticationFilter;
import com.mizhousoft.digest.authentication.www.SecurityContextPersistenceFilter;

import jakarta.servlet.Filter;

@ComponentScan("com.mizhousoft")
@SpringBootApplication
public class DemoApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public DigestAuthenticationEntryPoint getAuthenticationEntryPoint()
	{
		DigestAuthenticationEntryPoint entryPoint = new DigestAuthenticationEntryPoint();
		entryPoint.setKey("test");
		entryPoint.setRealmName("Spring boot");

		return entryPoint;
	}

	@Bean
	public FilterRegistrationBean<Filter> getDigestAuthenticationFilter(DigestAuthenticationService digestAuthenticationService,
	        DigestAuthenticationEntryPoint authenticationEntryPoint)
	{
		DigestAuthenticationFilter filter = new DigestAuthenticationFilter();
		filter.setAuthenticationEntryPoint(authenticationEntryPoint);
		filter.setDigestAuthenticationService(digestAuthenticationService);

		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
		registration.setFilter(filter);
		registration.addUrlPatterns("/*");
		registration.setName("digestFilter");
		registration.setOrder(-1100);
		return registration;
	}

	@Bean
	public FilterRegistrationBean<Filter> getSecurityContextPersistenceFilter(DigestAuthenticationService digestAuthenticationService,
	        DigestAuthenticationEntryPoint authenticationEntryPoint)
	{
		SecurityContextPersistenceFilter filter = new SecurityContextPersistenceFilter();
		filter.setAuthenticationEntryPoint(authenticationEntryPoint);
		filter.setDigestAuthenticationService(digestAuthenticationService);

		FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>();
		registration.setFilter(filter);
		registration.addUrlPatterns("/*");
		registration.setName("persistenceFilter");
		registration.setOrder(-1000);
		return registration;
	}
}
