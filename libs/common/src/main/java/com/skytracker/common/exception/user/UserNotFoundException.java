package com.skytracker.common.exception.user;

import com.skytracker.common.exception.BusinessException;
import com.skytracker.common.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(Long id) {
        super(ErrorCode.USER_NOT_FOUND, "id=" + id);
    }

}

