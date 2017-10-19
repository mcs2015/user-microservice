package com.stl.user.repository.user;


import com.stl.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User,String>{

    public Optional<User> findByUserName(String userName);
}
