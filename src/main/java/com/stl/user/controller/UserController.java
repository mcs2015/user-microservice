package com.stl.user.controller;


import com.stl.user.dto.ResponseDTO;
import com.stl.user.model.User;
import com.stl.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stl/customer")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * Creating a new user
     * @param user
     * @return
     */
    @RequestMapping(path="/account" ,method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(@RequestBody User user) {
        if(user != null){
            boolean status = userService.saveCustomer(user);
            ResponseDTO response = status ? new ResponseDTO(false, "User Created Successfully") : new ResponseDTO(true, "Error in creating the user");
            return new ResponseEntity<Object>(response, HttpStatus.CREATED);
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
