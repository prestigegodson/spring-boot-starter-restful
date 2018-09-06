package com.prestigegodson.starter.restfulservice.controller;

import com.prestigegodson.starter.restfulservice.Security.JwtTokenProvider;
import com.prestigegodson.starter.restfulservice.Security.UserDetailsImp;
import com.prestigegodson.starter.restfulservice.Security.UserDetailsServiceImp;
import com.prestigegodson.starter.restfulservice.dto.CreateUserDto;
import com.prestigegodson.starter.restfulservice.dto.LoginDto;
import com.prestigegodson.starter.restfulservice.dto.UserDto;
import com.prestigegodson.starter.restfulservice.error.ErrorResponse;
import com.prestigegodson.starter.restfulservice.model.User;
import com.prestigegodson.starter.restfulservice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by prestige on 8/15/2018.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;
    private UserDetailsServiceImp userDetailsServiceImp;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService, PasswordEncoder passwordEncoder, UserDetailsServiceImp userDetailsServiceImp,
                          JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.userDetailsServiceImp = userDetailsServiceImp;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, Errors errors){

        if(errors.hasErrors()){

            return new ResponseEntity<>("Bad Credentials", HttpStatus.BAD_REQUEST);
        }


        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));



        User user = this.userService.findByEmail(loginDto.getUsername());

        String token = this.jwtTokenProvider.generateToken(new UserDetailsImp(user));

        Map responseBody = new HashMap();
        responseBody.put("token",token);
        responseBody.put("email",user.getEmail());
        responseBody.put("firstName",user.getFirstName());
        responseBody.put("lastName",user.getLastName());

        return new ResponseEntity<>( responseBody, HttpStatus.OK);

    }

    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody CreateUserDto userDto){

        String email = userDto.getEmail();
        String lastName = userDto.getLastName() == null ? "" : userDto.getLastName();
        String firstName = userDto.getFirstName() == null ? "" : userDto.getFirstName();
        String password = userDto.getPassword() == null ? "" : userDto.getPassword();

        Map errorMessage = new HashMap();

        //Validation

        User user = this.userService.findByEmail(email);
        if(user != null){
            errorMessage.put("email","Email already exists");
        }

        if(lastName.length() == 0){
            errorMessage.put("lastName", "Last name is required");
        }

        if(firstName.length() == 0){
            errorMessage.put("firstName", "First name is required");
        }

        if(password.length() == 0){
            errorMessage.put("password", "Password is required");
        }

        if(!errorMessage.isEmpty()){

            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        user = new User();
        user.setEmail(email);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setPassword(this.passwordEncoder.encode(password));

        this.userService.save(user);

        return new ResponseEntity<>("User created successfully", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto, @PathVariable long id){

        Optional<User> userOptional = this.userService.findById(id);

        if(!userOptional.isPresent()){

            return new ResponseEntity<>("User cannot be found", HttpStatus.NOT_FOUND);
        }

        String email = userDto.getEmail();
        String lastName = userDto.getLastName() == null ? "" : userDto.getLastName();
        String firstName = userDto.getFirstName() == null ? "" : userDto.getFirstName();

        Map errorMessage = new HashMap();

        //Validation
        if(!userOptional.get().getEmail().equalsIgnoreCase(email)){

            if(this.userService.findByEmail(email) != null){
                errorMessage.put("email","Email already exists");
            }
        }

        if(lastName.length() == 0){
            errorMessage.put("lastName", "Last name is required");
        }

        if(firstName.length() == 0){
            errorMessage.put("firstName", "First name is required");
        }

        if(!errorMessage.isEmpty()){

            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        this.userService.save(user);

        return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<?> authenticationException(AuthenticationException e){

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("UnAuthorized");
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setMessage(e.getMessage());
        errorResponse.setTimestamp(new Date());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}
