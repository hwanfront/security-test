package com.example.spring_security_test.web;

import com.example.spring_security_test.domain.member.MemberDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping
    public String mainPage(Authentication authentication) {

        if (authentication == null) {
            System.out.println("Authentication is null");
        } else {
            System.out.println("Authenticated: " + authentication.isAuthenticated());
            System.out.println("Username: " + authentication.getName());
        }
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(Model model){
        model.addAttribute("memberDTO", new MemberDTO());
        return "login";
    }

    @GetMapping("/join")
    public String userPage(Model model){
        model.addAttribute("memberDTO", new MemberDTO());
        return "join";
    }

    @GetMapping("/user")
    public String userPage(){
        return "user";
    }

    @GetMapping("/admin")
    public String adminPage(){
        return "admin";
    }

    @GetMapping("/guest")
    public String questPage(){
        return "guest";
    }

    @GetMapping("403")
    public String forbiddenPage(){
        return "403";
    }

    @GetMapping("401")
    public String unauthorizedPage(){
        return "401";
    }

    @GetMapping("*")
    public String notfoundPage(){
        return "404";
    }
}
