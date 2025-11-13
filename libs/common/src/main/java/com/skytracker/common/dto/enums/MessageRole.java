package com.skytracker.common.dto.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageRole {

    USER("user"),
    AI("gpt");

    private final String value;
}
