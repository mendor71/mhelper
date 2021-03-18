//package ru.pack.csps.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
//import ru.pack.csps.security.app.CustomAuthenticationProvider;
//import ru.pack.csps.security.app.CustomSuccessAuthHandler;
//import ru.pack.csps.security.app.RestAuthEntryPoint;
//import ru.pack.csps.service.UserDetailsServiceImpl;
//
///**
// * Created by Gushchin-AA1 on 08.09.2017.
// */
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//    private UserDetailsServiceImpl userDetailsService;
//
//    @Autowired
//    private CustomAuthenticationProvider authenticationProvider;
//
//    @Autowired
//    private RestAuthEntryPoint entryPoint;
//
//    @Autowired
//    private void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService)
//                .passwordEncoder(getPasswordEncoder())
//                .and()
//                .authenticationProvider(authenticationProvider);
//    }
//
//    @Override
//    protected void configure(HttpSecurity yand) throws Exception {
//        yand.csrf()
//                .disable()
//                .exceptionHandling().authenticationEntryPoint(entryPoint)
//                .and()
//                .authorizeRequests()
//                .antMatchers("/resources/**", "/**").permitAll()
//                .anyRequest().permitAll()
//                .and().exceptionHandling().accessDeniedPage("/403");
//        yand.formLogin()
//                .loginPage("/login")
//                .loginProcessingUrl("/j_spring_security_check")
//                .failureUrl("/login?error")
//                .usernameParameter("j_username")
//                .passwordParameter("j_password")
//                .successHandler(new CustomSuccessAuthHandler())
//                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
//                .permitAll();
//        yand.logout()
//                .permitAll()
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login?logout")
//                .deleteCookies("JSESSIONID")
//                .invalidateHttpSession(true);
//    }
//
//    @Bean
//    public PasswordEncoder getPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
