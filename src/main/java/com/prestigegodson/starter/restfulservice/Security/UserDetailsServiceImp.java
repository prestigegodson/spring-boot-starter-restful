package com.prestigegodson.starter.restfulservice.Security;

import com.prestigegodson.starter.restfulservice.model.User;
import com.prestigegodson.starter.restfulservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by prestige on 8/28/2018.
 */
@Component
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserDetailsServiceImp.class);

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        logger.info("Username : " + s);
        User user = this.userService.findByEmail(s);

        if(user == null){
            throw new UsernameNotFoundException("User cannot be found");
        }

        return new UserDetailsImp(user);

    }

    public UserDetails loadUserById(long id) throws UsernameNotFoundException {
        Optional<User> userOptional = this.userService.findById(id);

        User user = userOptional.isPresent() ? userOptional.get() : null;

        if(user == null){
            throw new UsernameNotFoundException("User cannot be found");
        }

        return new UserDetailsImp(user);

    }
}
