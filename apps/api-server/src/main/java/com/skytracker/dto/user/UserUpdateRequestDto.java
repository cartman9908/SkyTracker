package com.skytracker.dto.user;

import com.skytracker.validation.ValidationGroups;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {

    @NotBlank(message = "이메일을 입력해주세요.", groups = ValidationGroups.NotBlankGroups.class)
    private String email;

    @NotBlank(message = "닉네임을 입력해주세요.", groups = ValidationGroups.NotBlankGroups.class)
    private String username;
}