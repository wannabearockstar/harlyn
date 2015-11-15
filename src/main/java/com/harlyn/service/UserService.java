package com.harlyn.service;

import com.harlyn.domain.User;
import com.harlyn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wannabe on 15.11.15.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
