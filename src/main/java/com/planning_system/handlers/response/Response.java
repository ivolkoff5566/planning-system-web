package com.planning_system.handlers.response;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Response<T> {
    String message;
    T data;
}