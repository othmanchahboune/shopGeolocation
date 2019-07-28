package com.controllers;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.entities.auth.AuthInfo;
import com.utils.Utils;

import io.micrometer.core.instrument.util.StringUtils;



@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return AuthInfo.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.getErrorCount() != 0) {
            return;
        }
        AuthInfo authInfo = (AuthInfo) target;
        String userName = authInfo.getEmail();
        String password = authInfo.getPassword();
       

        boolean emailValid = EmailValidator.getInstance().isValid(userName);

        String message = null;
        if (!emailValid) {
            message = "user name must be an email";
            errors.reject("1", message);
            Utils.throwBadRequest(message);
        }
        if (StringUtils.isBlank(userName)) {
            message = "user name can't be null or empty.";
            errors.reject("2", message);
            Utils.throwBadRequest(message);
        }
        if (StringUtils.isBlank(password)) {
            message = "Password can't be null or empty.";
            errors.reject("3", message);
            Utils.throwBadRequest(message);
        }
      

        
    }
}

