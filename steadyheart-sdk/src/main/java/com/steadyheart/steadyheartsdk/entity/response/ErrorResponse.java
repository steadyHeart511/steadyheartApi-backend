package com.steadyheart.steadyheartsdk.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lts
 * @create 2023-11-02 21:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    Integer code;
    String message;
}
