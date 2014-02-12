package com.github.vdurmont.doit;

import com.github.vdurmont.doit.config.WebConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class Doit {
	public static void main(String[] args) throws Exception {
		AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
		applicationContext.register(WebConfig.class);
		applicationContext.setDisplayName("Doit");

		ServletHolder servletHolder = new ServletHolder("doit", new DispatcherServlet(applicationContext));

		ServletContextHandler servletContext = new ServletContextHandler(null, "/", true, false);
		servletContext.addEventListener(new ContextLoaderListener(applicationContext));
		servletContext.addServlet(servletHolder, "/*");

		// Spring Security
		FilterHolder filterHolder = new FilterHolder(DelegatingFilterProxy.class);
		filterHolder.setName("springSecurityFilterChain");
		servletContext.addFilter(filterHolder, "/*", EnumSet.allOf(DispatcherType.class));

		// Server
		int port = 9093;
		Server server = new Server(port);
		server.setHandler(servletContext);
		server.start();
		server.join();
	}
}
