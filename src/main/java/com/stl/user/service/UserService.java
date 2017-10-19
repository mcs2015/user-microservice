package com.stl.user.service;

import com.stl.user.model.User;
import com.stl.user.repository.user.UserRepository;
import com.stl.user.util.STLValidator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;


    /**
     * Saving a new user
     */
    public boolean saveUser(User user){
        //Validate and hash user password
        String userPwd = user.getPassword();
        if(STLValidator.validatePassword(userPwd)){
            user.setPassword(STLValidator.hashPassword(userPwd));
            userRepository.save(user);
            return true;
        }

        return false;

    }

    /**
     * Usernames are unique and getting the user by userName
     * @param userName
     * @return
     */
    public Optional<User> getUserByUserName(String userName){
        return userRepository.findByUserName(userName);

    }

    public boolean login(User userToValidate){
        if(StringUtils.isNotEmpty(userToValidate.getUserName()) && StringUtils.isNotEmpty(userToValidate.getPassword())){
            Optional<User> userInDB = getUserByUserName(userToValidate.getUserName());
            if(userInDB.isPresent()){
                String passwordToCheck = STLValidator.hashPassword(userToValidate.getPassword());
                if(passwordToCheck.equals(userInDB.get().getPassword())){
                    //Passwords match. Valid user
                    return true;
                }

            }

        }

        return false;

    }

    public boolean isAlreadyTakenUserName(String usernameToCheck){

        Optional<User> alreadyRegisteredUser = getUserByUserName(usernameToCheck);
        if(alreadyRegisteredUser.isPresent()){
            return true;
        }else{
            return false;
        }

    }

    public void changePassword(User userToChange, String newPassword){
        userToChange.setPassword(newPassword);
        userRepository.save(userToChange);

    }

    public boolean arePasswordsEqual(String userGivenPwd, String actualPwd){
        return userGivenPwd.equals(actualPwd);

    }

}
