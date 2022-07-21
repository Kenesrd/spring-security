package com.example.config;


import com.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;


@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/authenticated/**").authenticated()
                .antMatchers("/only_for_admins/**").hasRole("ADMIN")
                .antMatchers("/read-profile/**").hasAuthority("READ_PROFILE")
                .and()
                .formLogin()
                .and()
                .logout().logoutSuccessUrl("/");
    }

//    DAO AUTHENTICATION
    //=======================================================================================

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }


    //=======================================================================================
//    JDBC Authentication
//    @Bean
//    public JdbcUserDetailsManager users(DataSource dataSource){
////        UserDetails user = User.builder()
////                .username("user")
////                .password("{bcrypt}$2a$12$bwXayb0..SkZtOTvz48VuOJQJralGLVMZrRXT6jiCWRFEJP4UrOWi") // Пароль: 100
////                .roles("USER")
////                .build();
////        UserDetails admin = User.builder()
////                .username("admin")
////                .password("{bcrypt}$2a$12$bwXayb0..SkZtOTvz48VuOJQJralGLVMZrRXT6jiCWRFEJP4UrOWi") // Пароль: 100
////                .roles("ADMIN","USER")
////                .build();
////
//        JdbcUserDetailsManager jdbcUserDetails = new JdbcUserDetailsManager(dataSource);
////        if (jdbcUserDetails.userExists(user.getUsername())){
////            jdbcUserDetails.deleteUser(user.getUsername());
////        }
////        if (jdbcUserDetails.userExists(admin.getUsername())){
////            jdbcUserDetails.deleteUser(admin.getUsername());
////        }
////        jdbcUserDetails.createUser(user);
////        jdbcUserDetails.createUser(admin);
//
//        return jdbcUserDetails;
//    }


//=======================================================================================
//    Для Создание Пользователя в памяти Spring In Memory
//    @Bean
//    public UserDetailsService user(){
//        UserDetails user = User.builder()
//                .username("user")
//                .password("{bcrypt}$2a$12$bwXayb0..SkZtOTvz48VuOJQJralGLVMZrRXT6jiCWRFEJP4UrOWi") // Пароль: 100
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{bcrypt}$2a$12$bwXayb0..SkZtOTvz48VuOJQJralGLVMZrRXT6jiCWRFEJP4UrOWi") // Пароль: 100
//                .roles("ADMIN","USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user, admin);
//    }

}
