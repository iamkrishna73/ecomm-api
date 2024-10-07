package io.github.iamkrishna73.Ecomm.controller;


import io.github.iamkrishna73.Ecomm.constant.LoggingConstant;
import io.github.iamkrishna73.Ecomm.request.LoginRequest;
import io.github.iamkrishna73.Ecomm.request.SignUpRequest;
import io.github.iamkrishna73.Ecomm.response.LoginResponse;
import io.github.iamkrishna73.Ecomm.service.IAuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {
    private final IAuthService authService;

    @ResponseBody
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest request) {
        var methodName = "AuthController:registerUser";
        log.info(LoggingConstant.START_METHOD_LOG, methodName, request.getRoles());
        authService.registerUser(request);
        log.info(LoggingConstant.END_METHOD_LOG, methodName);
        return new ResponseEntity<>("user successfully register", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        var methodName = "AuthController:loginUser";
        log.info(LoggingConstant.START_METHOD_LOG, methodName, loginRequest.getUsername());

        LoginResponse response = authService.loginUser(loginRequest);

        log.info(LoggingConstant.END_METHOD_LOG, methodName);
        return ResponseEntity.ok(response);
    }

}
