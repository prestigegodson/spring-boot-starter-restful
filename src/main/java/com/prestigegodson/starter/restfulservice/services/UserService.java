package com.prestigegodson.starter.restfulservice.services;

import com.prestigegodson.starter.restfulservice.model.User;
import com.prestigegodson.starter.restfulservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by prestige on 8/15/2018.
 */
@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user){

        return this.userRepository.save(user);
    }

    public Optional<User> findById(long id){

        return this.userRepository.findById(id);
    }

    public User findByEmail(String email){

        return this.userRepository.findUsersByEmail(email);
    }
}
