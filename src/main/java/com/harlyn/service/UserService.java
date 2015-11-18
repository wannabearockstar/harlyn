package com.harlyn.service;

import com.harlyn.domain.User;
import com.harlyn.exception.NonUniqueUserDataException;
import com.harlyn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import java.util.Set;

/**
 * Created by wannabe on 15.11.15.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User createUser(User user) {
        Set<ConstraintViolation<User>> errors = Validation.buildDefaultValidatorFactory().getValidator().validate(user);
        if (!errors.isEmpty()) {
            throw new ConstraintViolationException(errors);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    public void validateUniqueData(final User user) {
        if (userRepository.userExistByEmail(user.getEmail())) {
            throw new NonUniqueUserDataException("Email already taken", user);
        }
        if (userRepository.userExistByUsername(user.getUsername())) {
            throw new NonUniqueUserDataException("Username already taken", user);
        }

    }

    public UserService setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
        return this;
    }

    public UserService setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        return this;
    }
}
