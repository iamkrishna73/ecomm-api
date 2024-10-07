package io.github.iamkrishna73.Ecomm.service;


import io.github.iamkrishna73.Ecomm.request.LoginRequest;
import io.github.iamkrishna73.Ecomm.request.SignUpRequest;
import io.github.iamkrishna73.Ecomm.response.LoginResponse;

public interface IAuthService {
    void registerUser(SignUpRequest request);

    LoginResponse loginUser(LoginRequest loginRequest);
}
