package com.stl.user.controller;


import com.stl.user.dto.ResponseDTO;
import com.stl.user.model.User;
import com.stl.user.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
            //Checking username is already taken
            if(StringUtils.isNotEmpty(user.getUserName())){
                if(userService.isAlreadyTakenUserName(user.getUserName())){
                    ResponseDTO alreadyTaken = new ResponseDTO(true, "Username is already taken");
                    return  new ResponseEntity<Object>(alreadyTaken, HttpStatus.BAD_REQUEST);
                }

            }

            boolean status = userService.saveUser(user);
            ResponseDTO response = status ? new ResponseDTO(false, "User Created Successfully") : new ResponseDTO(true, "Error in creating the user");
            return new ResponseEntity<Object>(response, HttpStatus.CREATED);
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }


    /**
     * Login user
     * @param userToValidate
     * @return
     */
    @RequestMapping(path="/login" ,method = RequestMethod.POST)
    public ResponseEntity<Object> loginUser(@RequestBody User userToValidate) {

        if(userToValidate != null){
            ResponseDTO badRequest = new ResponseDTO(true, "Required data is not present in the request");
            return new ResponseEntity<Object>(badRequest, HttpStatus.BAD_REQUEST);

        }

        if(userService.login(userToValidate)){
            Optional<User> authenticatedUser = userService.getUserByUserName(userToValidate.getUserName());
            return new ResponseEntity<Object>(authenticatedUser.get(), HttpStatus.OK);
        }

        ResponseDTO notFound = new ResponseDTO(true, "Invalid login credentials");
        return new ResponseEntity<Object>(notFound, HttpStatus.NOT_FOUND);

    }
}
