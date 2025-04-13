package org.thevoids.oncologic.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thevoids.oncologic.entity.User;
import org.thevoids.oncologic.service.UserService;

@Controller
@RequestMapping("/web/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String signupCreate(@ModelAttribute User user) {
        userService.createUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
