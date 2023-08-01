package com.group3.mBaaS.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.converter.json.GsonBuilderUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Contains the values which are needed for an authentication request
 */
public class AuthRequest {
    private String username;
    private String password;
}
