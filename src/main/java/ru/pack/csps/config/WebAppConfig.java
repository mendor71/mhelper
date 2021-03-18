//package ru.pack.csps.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//
///**
//* Created by Gushchin-AA1 on 08.09.2017.
//*/
//@Configuration
//@EnableWebMvc
//@ComponentScan("ru.pack.csps")
//public class WebAppConfig extends WebMvcConfigurerAdapter {
//
//    /*@Override
//    public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry) {
//        resourceHandlerRegistry.addResourceHandler("/WEB-INF/pages/**").addResourceLocations("/pages/");
//        resourceHandlerRegistry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
//    }*/
//
//    /*@Bean
//    public UrlBasedViewResolver setupViewResolver() {
//        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
//        resolver.setPrefix("/WEB-INF/pages/");
//        resolver.setSuffix(".jsp");
//        resolver.setViewClass(JstlView.class);
//        return resolver;
//    }*/
//
//    @Bean
//    public UserDetailsService getUserDetailsService() {
//        return new UserDetailsServiceImpl();
//    }
//}
