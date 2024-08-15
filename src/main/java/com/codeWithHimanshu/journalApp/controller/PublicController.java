package com.codeWithHimanshu.journalApp.controller;

import com.codeWithHimanshu.journalApp.entity.User;
import com.codeWithHimanshu.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    private UserService userService;

    @PostMapping("/createUser")
    public boolean createUser(@RequestBody User newUser){
        userService.saveNewUser(newUser);
        return true;
    }

}
