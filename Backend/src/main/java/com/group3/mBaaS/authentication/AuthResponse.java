package com.group3.mBaaS.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Contains the return values which are returned after a successful authentication request
 */
public class AuthResponse {
    private String token;
    private String role;
    private String userName;
}
