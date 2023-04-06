package com.weatherwhere.airservice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class ResultDTO<T> {
    private final int resultCode;
    private final String message;
    private final T data;
}
