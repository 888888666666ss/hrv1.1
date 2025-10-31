package com.hrms.controller;

import com.hrms.dto.AuthResponse;
import com.hrms.dto.LoginRequest;
import com.hrms.dto.RegisterRequest;
import com.hrms.entity.User;
import com.hrms.repository.UserRepository;
import com.hrms.service.AuthService;
import com.hrms.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtService jwtService;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(createSuccessResponse("注册成功", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("注册失败", e.getMessage()));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(createSuccessResponse("登录成功", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("登录失败", e.getMessage()));
        }
    }
    
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String authHeader) {
        try {
            // 提取JWT令牌
            String token = authHeader.substring(7); // 移除 "Bearer " 前缀
            String username = jwtService.extractUsername(token);
            
            // 获取用户信息
            Optional<User> userOpt = userRepository.findByUsername(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(401)
                        .body(createErrorResponse("获取失败", "用户不存在"));
            }
            
            User user = userOpt.get();
            AuthResponse.UserInfo userInfo = new AuthResponse.UserInfo(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRealName(),
                user.getRole().name()
            );
            
            return ResponseEntity.ok(createSuccessResponse("获取成功", userInfo));
        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(createErrorResponse("获取失败", "无效的令牌"));
        }
    }
    
    private Map<String, Object> createSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    private Map<String, Object> createErrorResponse(String message, String error) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 400);
        response.put("message", message);
        response.put("error", error);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}