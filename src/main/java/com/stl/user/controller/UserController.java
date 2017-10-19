package com.stl.user.controller;


import com.stl.user.dto.ResponseDTO;
import com.stl.user.model.User;
import com.stl.user.service.UserService;
import com.stl.user.util.STLValidator;
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
@RequestMapping("/api/stl/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * Creating a new user
     * @param user
     * @return
     */
    @RequestMapping(path="/signup" ,method = RequestMethod.POST)
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


    /**
     * Cahnge password
     * @param userToUpdate
     * @return
     */
    @RequestMapping(path="/change/password" ,method = RequestMethod.POST)
    public ResponseEntity<Object> changePassword(@RequestBody User userToUpdate) {

        if(userToUpdate == null){
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }

        if(StringUtils.isEmpty(userToUpdate.getPassword()) || StringUtils.isEmpty(userToUpdate.getNewPassword())){
            return new ResponseEntity<Object>(new ResponseDTO(true,"Passwords cannot be blank"),HttpStatus.BAD_REQUEST);
        }

        if(StringUtils.isEmpty(userToUpdate.getUserName())){
            return new ResponseEntity<Object>(new ResponseDTO(true,"Username cannot be blank"),HttpStatus.BAD_REQUEST);
        }

        String userName = userToUpdate.getUserName();
        String oldPwd = userToUpdate.getPassword();
        String newPwd = userToUpdate.getNewPassword();

        if(!STLValidator.validatePassword(oldPwd) || !STLValidator.validatePassword(newPwd)){
            return new ResponseEntity<Object>(new ResponseDTO(true,"Passwords are weak"),HttpStatus.BAD_REQUEST);
        }

        String hashedOldPwd = STLValidator.hashPassword(oldPwd);
        String hashedNewPwd = STLValidator.hashPassword(newPwd);

        Optional<User> userInDbOpt = userService.getUserByUserName(userName);
        if(!userInDbOpt.isPresent()){
            return new ResponseEntity<Object>(new ResponseDTO(true,"No user found"),HttpStatus.NOT_FOUND);
        }else{
            User userInDb = userInDbOpt.get();
            if(!userService.arePasswordsEqual(userInDb.getPassword(), hashedOldPwd)){
                //The user given old password does not match with the password in the db
                return new ResponseEntity<Object>(new ResponseDTO(false,"Invalid current password"),HttpStatus.UNAUTHORIZED);

            }

            //Validations passed. Update the new password
            userService.changePassword(userInDb, hashedNewPwd);
            return new ResponseEntity<Object>(new ResponseDTO(false,"Passwords changed successfully"),HttpStatus.OK);
        }



    }


    /**
     * Sending password reset link to the user email adderess
     * @param emailAddress
     * @param userName
     * @return
     */
    @RequestMapping(path="/recover/password" ,method = RequestMethod.GET)
    public ResponseEntity<Object> recoverPassword(String emailAddress, String userName) {
        if(StringUtils.isEmpty(emailAddress) || StringUtils.isEmpty(userName)){
            return new ResponseEntity<Object>( HttpStatus.BAD_REQUEST);
        }

        Optional<User> userInDbOpt = userService.getUserByUserName(userName);

        if(userInDbOpt.isPresent()){
            User userToRecover = userInDbOpt.get();

            if(emailAddress.equals(userToRecover.getEmailAddress())){
                /**
                 * @TODO Sending the password reset link to the address
                 */
                logger.debug("Password reset link sent to the "+userToRecover.getEmailAddress());
                return new ResponseEntity<Object>(new ResponseDTO(false, "Password reset link sent successfully to "+userToRecover.getEmailAddress()), HttpStatus.OK);
            }



        }

        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);

    }



}
