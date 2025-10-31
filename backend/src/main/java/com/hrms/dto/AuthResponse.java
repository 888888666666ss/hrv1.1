package com.hrms.dto;

public class AuthResponse {
    
    private String token;
    private String type = "Bearer";
    private UserInfo user;
    
    public AuthResponse(String token, UserInfo user) {
        this.token = token;
        this.user = user;
    }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public UserInfo getUser() { return user; }
    public void setUser(UserInfo user) { this.user = user; }
    
    public static class UserInfo {
        private Long id;
        private String username;
        private String email;
        private String realName;
        private String role;
        
        public UserInfo(Long id, String username, String email, String realName, String role) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.realName = realName;
            this.role = role;
        }
        
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getRealName() { return realName; }
        public void setRealName(String realName) { this.realName = realName; }
        
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
    }
}