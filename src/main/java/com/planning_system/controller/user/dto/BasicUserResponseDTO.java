package com.planning_system.controller.user.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class BasicUserResponseDTO {
    int id;
    String userName;
}