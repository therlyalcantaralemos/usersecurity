package com.usersecurity.controllers;

import com.usersecurity.domains.user.UserDTO;
import com.usersecurity.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public void create(@RequestBody UserDTO userDTO){
        userService.create(userDTO);
    }

    @GetMapping("/login")
    public void login(){

    }


}
