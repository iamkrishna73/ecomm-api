package io.github.iamkrishna73.Ecomm.service;

import io.github.iamkrishna73.Ecomm.Repositories.RoleRepository;
import io.github.iamkrishna73.Ecomm.Repositories.UserRepository;
import io.github.iamkrishna73.Ecomm.constant.LoggingConstant;
import io.github.iamkrishna73.Ecomm.entity.AppRole;
import io.github.iamkrishna73.Ecomm.entity.Role;
import io.github.iamkrishna73.Ecomm.entity.User;
import io.github.iamkrishna73.Ecomm.exception.ResourceNotFoundException;
import io.github.iamkrishna73.Ecomm.request.LoginRequest;
import io.github.iamkrishna73.Ecomm.request.SignUpRequest;
import io.github.iamkrishna73.Ecomm.response.LoginResponse;
import io.github.iamkrishna73.Ecomm.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
public class AuthService implements IAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public void registerUser(SignUpRequest request) {
        var methodName = "AuthService:registerUser";
        log.info(LoggingConstant.START_METHOD_LOG, methodName, request.getRoles());
        if (userRepository.existsByUsername(request.getUsername())) {
            log.error(LoggingConstant.ERROR_METHOD_LOG, methodName, " Username already exist");
            throw new ResourceNotFoundException("user not found");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            log.error(LoggingConstant.ERROR_METHOD_LOG, methodName, " Email already exists");
            throw new ResourceNotFoundException("Email already  found");

        }
        User user = new User(request.getUsername(), request.getEmail() ,passwordEncoder.encode(request.getPassword()));

        Set<String> requestRoles = request.getRoles();
        Role role;
        if (requestRoles == null || requestRoles.isEmpty()) {
            role = roleRepository.findByRoleName(AppRole.ROLE_CUSTOMER).orElseThrow(() -> {
                log.error(LoggingConstant.ERROR_METHOD_LOG, methodName, request.getRoles() + " role is not found");
                return new ResourceNotFoundException("role not found");
            });

        } else {
            String remainingRole = requestRoles.iterator().next();
            if (remainingRole.equals("admin")) {
                role = roleRepository.findByRoleName(AppRole.ROLE_ADMIN).orElseThrow(() -> {
                    log.error(LoggingConstant.ERROR_METHOD_LOG, methodName, request.getRoles()+" role not found");
                    return new ResourceNotFoundException("role not found");
                });

            } else {
                role = roleRepository.findByRoleName(AppRole.ROLE_CUSTOMER).orElseThrow(() -> {

                    log.error(LoggingConstant.ERROR_METHOD_LOG, methodName, request.getRoles()+ "role not found");
                    return new ResourceNotFoundException("role not found");
                });
            }
        }
        user.setRole(role);
        log.info(LoggingConstant.END_METHOD_LOG, methodName);
        userRepository.save(user);
    }

    @Override
    public LoginResponse loginUser(LoginRequest loginRequest) {
        var methodName = "AuthService:loginUser";
        log.info(LoggingConstant.START_METHOD_LOG, methodName, loginRequest.getUsername());
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            log.error(LoggingConstant.ERROR_METHOD_LOG, methodName, loginRequest.getUsername() + " bad Credentials");
            throw new BadCredentialsException("Bad Credentials");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return new LoginResponse(jwtToken, userDetails.getUsername(), roles);
    }
}
