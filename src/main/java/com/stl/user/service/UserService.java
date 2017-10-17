package com.stl.user.service;

import com.stl.user.model.User;
import com.stl.user.repository.user.UserRepository;
import com.stl.user.util.STLValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository customerRepository;


    /**
     * Saving a new user
     */
    public boolean saveCustomer(User user){
        //Validate and hash user password
        String customerPwd = user.getPassword();
        if(STLValidator.validatePassword(customerPwd)){
            user.setPassword(STLValidator.hashPassword(customerPwd));
            customerRepository.save(user);
            return true;
        }

        return false;

    }

}
