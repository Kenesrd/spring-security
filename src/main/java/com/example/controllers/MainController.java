package com.example.controllers;

import com.example.entities.User;
import com.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class MainController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String homePage(Principal principal){
        if (principal != null){
            System.out.println(((Authentication)principal).getAuthorities());
        }
        return "home";
    }

    @GetMapping("/authenticated")
    public String pageForAuthenticatedUsers(Principal principal){
//       Authentication a = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.findByUserName(principal.getName());

        return "secured part of web service. Principal: " + user.getUsername() + ", Email: " + user.getEmail();
    }

    @GetMapping("/read-profile")
    public String pageForReadProfile(){
        return "READ PROFILE PAGE!";
    }

    @GetMapping("/only_for_admins")
    public String pageOnlyForAdmins(){
        return "Admins PAGE!";
    }
}
