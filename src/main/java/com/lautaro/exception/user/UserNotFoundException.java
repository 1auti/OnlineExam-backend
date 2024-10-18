package com.lautaro.exception.user;

import jakarta.validation.constraints.Email;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(@Email String s) {
    }
}
