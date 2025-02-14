package io.github.iamkrishna73.Ecomm.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LoginResponse {
    private String jwtToken;
    private String username;
    private List<String> roles;

    public LoginResponse(String jwtToken, String username, List<String> roles) {
        this.jwtToken = jwtToken;
        this.username = username;
        this.roles = roles;
    }
}
