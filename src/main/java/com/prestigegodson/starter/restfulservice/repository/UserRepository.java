package com.prestigegodson.starter.restfulservice.repository;

import com.prestigegodson.starter.restfulservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by prestige on 8/15/2018.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUsersByEmail(String email);
}
