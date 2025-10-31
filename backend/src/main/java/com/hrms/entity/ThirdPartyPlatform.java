package com.hrms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "third_party_platforms")
public class ThirdPartyPlatform {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "平台名称不能为空")
    @Column(name = "platform_name", nullable = false, length = 50)
    private String platformName;
    
    @NotBlank(message = "平台代码不能为空")
    @Column(name = "platform_code", nullable = false, unique = true, length = 20)
    private String platformCode; // BOSS, ZHILIAN, LIEPIN, etc.
    
    @Column(name = "api_base_url", length = 200)
    private String apiBaseUrl;
    
    @Column(name = "app_key", length = 100)
    private String appKey;
    
    @Column(name = "app_secret", length = 255)
    private String appSecret;
    
    @Column(name = "access_token", length = 500)
    private String accessToken;
    
    @Column(name = "refresh_token", length = 500)
    private String refreshToken;
    
    @Column(name = "token_expires_at")
    private LocalDateTime tokenExpiresAt;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private PlatformStatus status = PlatformStatus.INACTIVE;
    
    @Column(name = "webhook_url", length = 300)
    private String webhookUrl;
    
    @Column(name = "sync_enabled")
    private Boolean syncEnabled = false;
    
    @Column(name = "auto_sync_interval_minutes")
    private Integer autoSyncIntervalMinutes = 60; // 默认60分钟同步一次
    
    @Column(name = "last_sync_time")
    private LocalDateTime lastSyncTime;
    
    @Column(name = "sync_status", length = 20)
    @Enumerated(EnumType.STRING)
    private SyncStatus syncStatus = SyncStatus.IDLE;
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(name = "config_json", columnDefinition = "TEXT")
    private String configJson; // 存储平台特定的配置
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 枚举类型
    public enum PlatformStatus {
        ACTIVE,     // 已激活
        INACTIVE,   // 未激活  
        SUSPENDED,  // 已暂停
        ERROR       // 错误状态
    }
    
    public enum SyncStatus {
        IDLE,       // 空闲
        SYNCING,    // 同步中
        SUCCESS,    // 同步成功
        FAILED      // 同步失败
    }
    
    // 构造函数
    public ThirdPartyPlatform() {}
    
    public ThirdPartyPlatform(String platformName, String platformCode) {
        this.platformName = platformName;
        this.platformCode = platformCode;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPlatformName() {
        return platformName;
    }
    
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
    
    public String getPlatformCode() {
        return platformCode;
    }
    
    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }
    
    public String getApiBaseUrl() {
        return apiBaseUrl;
    }
    
    public void setApiBaseUrl(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
    }
    
    public String getAppKey() {
        return appKey;
    }
    
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
    
    public String getAppSecret() {
        return appSecret;
    }
    
    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }
    
    public String getAccessToken() {
        return accessToken;
    }
    
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public LocalDateTime getTokenExpiresAt() {
        return tokenExpiresAt;
    }
    
    public void setTokenExpiresAt(LocalDateTime tokenExpiresAt) {
        this.tokenExpiresAt = tokenExpiresAt;
    }
    
    public PlatformStatus getStatus() {
        return status;
    }
    
    public void setStatus(PlatformStatus status) {
        this.status = status;
    }
    
    public String getWebhookUrl() {
        return webhookUrl;
    }
    
    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }
    
    public Boolean getSyncEnabled() {
        return syncEnabled;
    }
    
    public void setSyncEnabled(Boolean syncEnabled) {
        this.syncEnabled = syncEnabled;
    }
    
    public Integer getAutoSyncIntervalMinutes() {
        return autoSyncIntervalMinutes;
    }
    
    public void setAutoSyncIntervalMinutes(Integer autoSyncIntervalMinutes) {
        this.autoSyncIntervalMinutes = autoSyncIntervalMinutes;
    }
    
    public LocalDateTime getLastSyncTime() {
        return lastSyncTime;
    }
    
    public void setLastSyncTime(LocalDateTime lastSyncTime) {
        this.lastSyncTime = lastSyncTime;
    }
    
    public SyncStatus getSyncStatus() {
        return syncStatus;
    }
    
    public void setSyncStatus(SyncStatus syncStatus) {
        this.syncStatus = syncStatus;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public String getConfigJson() {
        return configJson;
    }
    
    public void setConfigJson(String configJson) {
        this.configJson = configJson;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // 辅助方法
    public boolean isTokenExpired() {
        return tokenExpiresAt != null && LocalDateTime.now().isAfter(tokenExpiresAt);
    }
    
    public boolean isActive() {
        return status == PlatformStatus.ACTIVE;
    }
    
    public boolean canSync() {
        return isActive() && syncEnabled && syncStatus != SyncStatus.SYNCING;
    }
}