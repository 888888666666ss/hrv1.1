package com.hrms.service.impl;

import com.hrms.entity.ThirdPartyPlatform;
import com.hrms.repository.ThirdPartyPlatformRepository;
import com.hrms.service.ThirdPartyPlatformService;
import com.hrms.service.thirdparty.ThirdPartyPlatformApiFactory;
import com.hrms.service.thirdparty.ThirdPartyPlatformApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ThirdPartyPlatformServiceImpl implements ThirdPartyPlatformService {
    
    @Autowired
    private ThirdPartyPlatformRepository platformRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private ThirdPartyPlatformApiFactory apiFactory;
    
    @Override
    public ThirdPartyPlatform createPlatform(ThirdPartyPlatform platform) {
        // 验证平台代码唯一性
        if (platformRepository.existsByPlatformCode(platform.getPlatformCode())) {
            throw new RuntimeException("平台代码已存在: " + platform.getPlatformCode());
        }
        
        // 验证平台名称唯一性
        if (platformRepository.existsByPlatformName(platform.getPlatformName())) {
            throw new RuntimeException("平台名称已存在: " + platform.getPlatformName());
        }
        
        // 设置默认值
        if (platform.getStatus() == null) {
            platform.setStatus(ThirdPartyPlatform.PlatformStatus.INACTIVE);
        }
        if (platform.getSyncEnabled() == null) {
            platform.setSyncEnabled(false);
        }
        if (platform.getSyncStatus() == null) {
            platform.setSyncStatus(ThirdPartyPlatform.SyncStatus.IDLE);
        }
        if (platform.getAutoSyncIntervalMinutes() == null) {
            platform.setAutoSyncIntervalMinutes(60);
        }
        
        return platformRepository.save(platform);
    }
    
    @Override
    public ThirdPartyPlatform updatePlatform(Long platformId, ThirdPartyPlatform platformData) {
        ThirdPartyPlatform platform = getPlatformById(platformId);
        
        // 检查平台代码唯一性（如果有变更）
        if (!platform.getPlatformCode().equals(platformData.getPlatformCode()) &&
            platformRepository.existsByPlatformCode(platformData.getPlatformCode())) {
            throw new RuntimeException("平台代码已存在: " + platformData.getPlatformCode());
        }
        
        // 检查平台名称唯一性（如果有变更）
        if (!platform.getPlatformName().equals(platformData.getPlatformName()) &&
            platformRepository.existsByPlatformName(platformData.getPlatformName())) {
            throw new RuntimeException("平台名称已存在: " + platformData.getPlatformName());
        }
        
        // 更新字段
        platform.setPlatformName(platformData.getPlatformName());
        platform.setPlatformCode(platformData.getPlatformCode());
        platform.setApiBaseUrl(platformData.getApiBaseUrl());
        platform.setAppKey(platformData.getAppKey());
        platform.setAppSecret(platformData.getAppSecret());
        platform.setWebhookUrl(platformData.getWebhookUrl());
        platform.setAutoSyncIntervalMinutes(platformData.getAutoSyncIntervalMinutes());
        platform.setConfigJson(platformData.getConfigJson());
        
        return platformRepository.save(platform);
    }
    
    @Override
    public void deletePlatform(Long platformId) {
        ThirdPartyPlatform platform = getPlatformById(platformId);
        
        // 检查是否正在同步
        if (platform.getSyncStatus() == ThirdPartyPlatform.SyncStatus.SYNCING) {
            throw new RuntimeException("平台正在同步中，无法删除");
        }
        
        platformRepository.delete(platform);
    }
    
    @Override
    @Transactional(readOnly = true)
    public ThirdPartyPlatform getPlatformById(Long platformId) {
        return platformRepository.findById(platformId)
                .orElseThrow(() -> new RuntimeException("平台不存在: " + platformId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public ThirdPartyPlatform getPlatformByCode(String platformCode) {
        return platformRepository.findByPlatformCode(platformCode)
                .orElseThrow(() -> new RuntimeException("平台不存在: " + platformCode));
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ThirdPartyPlatform> getAllPlatforms(Pageable pageable) {
        return platformRepository.findAll(pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<ThirdPartyPlatform> searchPlatforms(String platformName, String platformCode,
                                                   ThirdPartyPlatform.PlatformStatus status,
                                                   ThirdPartyPlatform.SyncStatus syncStatus,
                                                   Boolean syncEnabled, Pageable pageable) {
        return platformRepository.findPlatformsWithCriteria(
                platformName, platformCode, status, syncStatus, syncEnabled, pageable);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ThirdPartyPlatform> getActivePlatforms() {
        return platformRepository.findByStatus(ThirdPartyPlatform.PlatformStatus.ACTIVE);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ThirdPartyPlatform> getSyncEnabledPlatforms() {
        return platformRepository.findActiveSyncEnabledPlatforms();
    }
    
    @Override
    public ThirdPartyPlatform updatePlatformStatus(Long platformId, ThirdPartyPlatform.PlatformStatus status) {
        ThirdPartyPlatform platform = getPlatformById(platformId);
        platform.setStatus(status);
        
        // 如果设置为非激活状态，则禁用同步
        if (status != ThirdPartyPlatform.PlatformStatus.ACTIVE) {
            platform.setSyncEnabled(false);
            platform.setSyncStatus(ThirdPartyPlatform.SyncStatus.IDLE);
        }
        
        return platformRepository.save(platform);
    }
    
    @Override
    public ThirdPartyPlatform updateSyncStatus(Long platformId, ThirdPartyPlatform.SyncStatus syncStatus) {
        ThirdPartyPlatform platform = getPlatformById(platformId);
        platform.setSyncStatus(syncStatus);
        
        // 如果同步完成，更新最后同步时间
        if (syncStatus == ThirdPartyPlatform.SyncStatus.SUCCESS) {
            platform.setLastSyncTime(LocalDateTime.now());
            platform.setErrorMessage(null); // 清除错误信息
        }
        
        return platformRepository.save(platform);
    }
    
    @Override
    public ThirdPartyPlatform toggleSync(Long platformId, Boolean enabled) {
        ThirdPartyPlatform platform = getPlatformById(platformId);
        
        // 只有激活状态的平台才能启用同步
        if (enabled && platform.getStatus() != ThirdPartyPlatform.PlatformStatus.ACTIVE) {
            throw new RuntimeException("只有激活状态的平台才能启用同步");
        }
        
        platform.setSyncEnabled(enabled);
        
        // 如果禁用同步，重置同步状态
        if (!enabled) {
            platform.setSyncStatus(ThirdPartyPlatform.SyncStatus.IDLE);
        }
        
        return platformRepository.save(platform);
    }
    
    @Override
    public Map<String, Object> testConnection(Long platformId) {
        ThirdPartyPlatform platform = getPlatformById(platformId);
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 这里应该实现实际的连接测试逻辑
            // 根据不同平台调用相应的API测试接口
            boolean connected = performConnectionTest(platform);
            
            result.put("success", connected);
            result.put("message", connected ? "连接成功" : "连接失败");
            result.put("timestamp", LocalDateTime.now());
            
            if (connected) {
                // 连接成功时清除错误信息
                clearPlatformError(platformId);
            }
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "连接测试失败: " + e.getMessage());
            result.put("timestamp", LocalDateTime.now());
            
            // 设置错误信息
            setPlatformError(platformId, e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public ThirdPartyPlatform refreshAccessToken(Long platformId) {
        ThirdPartyPlatform platform = getPlatformById(platformId);
        
        try {
            // 这里应该实现实际的令牌刷新逻辑
            Map<String, String> tokenInfo = performTokenRefresh(platform);
            
            platform.setAccessToken(tokenInfo.get("accessToken"));
            platform.setRefreshToken(tokenInfo.get("refreshToken"));
            platform.setTokenExpiresAt(LocalDateTime.now().plusHours(
                Integer.parseInt(tokenInfo.getOrDefault("expiresIn", "3600")) / 3600));
            
            clearPlatformError(platformId);
            return platformRepository.save(platform);
            
        } catch (Exception e) {
            setPlatformError(platformId, "令牌刷新失败: " + e.getMessage());
            throw new RuntimeException("令牌刷新失败", e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateToken(Long platformId) {
        ThirdPartyPlatform platform = getPlatformById(platformId);
        
        // 检查令牌是否存在
        if (platform.getAccessToken() == null || platform.getAccessToken().isEmpty()) {
            return false;
        }
        
        // 检查令牌是否过期
        if (platform.isTokenExpired()) {
            return false;
        }
        
        // 这里可以调用平台API验证令牌有效性
        return performTokenValidation(platform);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ThirdPartyPlatform> getPlatformsNeedingSync() {
        LocalDateTime cutoffTime = LocalDateTime.now();
        return platformRepository.findPlatformsNeedingSync(cutoffTime).stream()
                .filter(p -> {
                    if (p.getLastSyncTime() == null) return true;
                    return p.getLastSyncTime().plusMinutes(p.getAutoSyncIntervalMinutes()).isBefore(cutoffTime);
                })
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<ThirdPartyPlatform> getPlatformsWithExpiringTokens(int hoursBeforeExpiry) {
        LocalDateTime expiryTime = LocalDateTime.now().plusHours(hoursBeforeExpiry);
        return platformRepository.findPlatformsWithExpiringTokens(expiryTime);
    }
    
    @Override
    public Map<String, Object> syncPlatformData(Long platformId) {
        ThirdPartyPlatform platform = getPlatformById(platformId);
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 检查是否可以同步
            if (!platform.canSync()) {
                throw new RuntimeException("平台当前不能执行同步操作");
            }
            
            // 设置同步状态
            updateSyncStatus(platformId, ThirdPartyPlatform.SyncStatus.SYNCING);
            
            // 执行同步逻辑
            Map<String, Object> syncResult = performDataSync(platform);
            
            // 更新同步状态为成功
            updateSyncStatus(platformId, ThirdPartyPlatform.SyncStatus.SUCCESS);
            
            result.put("success", true);
            result.put("message", "同步完成");
            result.put("data", syncResult);
            result.put("timestamp", LocalDateTime.now());
            
        } catch (Exception e) {
            // 更新同步状态为失败
            updateSyncStatus(platformId, ThirdPartyPlatform.SyncStatus.FAILED);
            setPlatformError(platformId, e.getMessage());
            
            result.put("success", false);
            result.put("message", "同步失败: " + e.getMessage());
            result.put("timestamp", LocalDateTime.now());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> syncAllEnabledPlatforms() {
        List<ThirdPartyPlatform> platforms = getSyncEnabledPlatforms();
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> syncResults = new ArrayList<>();
        
        int successCount = 0;
        int failureCount = 0;
        
        for (ThirdPartyPlatform platform : platforms) {
            try {
                Map<String, Object> syncResult = syncPlatformData(platform.getId());
                syncResults.add(Map.of(
                    "platformId", platform.getId(),
                    "platformName", platform.getPlatformName(),
                    "result", syncResult
                ));
                
                if ((Boolean) syncResult.get("success")) {
                    successCount++;
                } else {
                    failureCount++;
                }
                
            } catch (Exception e) {
                syncResults.add(Map.of(
                    "platformId", platform.getId(),
                    "platformName", platform.getPlatformName(),
                    "result", Map.of("success", false, "message", e.getMessage())
                ));
                failureCount++;
            }
        }
        
        result.put("totalCount", platforms.size());
        result.put("successCount", successCount);
        result.put("failureCount", failureCount);
        result.put("results", syncResults);
        result.put("timestamp", LocalDateTime.now());
        
        return result;
    }
    
    @Override
    public void setPlatformError(Long platformId, String errorMessage) {
        ThirdPartyPlatform platform = getPlatformById(platformId);
        platform.setErrorMessage(errorMessage);
        platform.setStatus(ThirdPartyPlatform.PlatformStatus.ERROR);
        platformRepository.save(platform);
    }
    
    @Override
    public void clearPlatformError(Long platformId) {
        ThirdPartyPlatform platform = getPlatformById(platformId);
        platform.setErrorMessage(null);
        if (platform.getStatus() == ThirdPartyPlatform.PlatformStatus.ERROR) {
            platform.setStatus(ThirdPartyPlatform.PlatformStatus.ACTIVE);
        }
        platformRepository.save(platform);
    }
    
    @Override
    public void updateLastSyncTime(Long platformId, LocalDateTime syncTime) {
        ThirdPartyPlatform platform = getPlatformById(platformId);
        platform.setLastSyncTime(syncTime);
        platformRepository.save(platform);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getPlatformStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 状态统计
        List<Object[]> statusCounts = platformRepository.countPlatformsByStatus();
        Map<String, Long> statusStats = statusCounts.stream()
                .collect(Collectors.toMap(
                    arr -> arr[0].toString(),
                    arr -> (Long) arr[1]
                ));
        
        // 同步状态统计
        List<Object[]> syncStatusCounts = platformRepository.countPlatformsBySyncStatus();
        Map<String, Long> syncStats = syncStatusCounts.stream()
                .collect(Collectors.toMap(
                    arr -> arr[0].toString(),
                    arr -> (Long) arr[1]
                ));
        
        // 基本统计
        long totalPlatforms = platformRepository.count();
        long activePlatforms = platformRepository.countByStatus(ThirdPartyPlatform.PlatformStatus.ACTIVE);
        long syncEnabledCount = platformRepository.countSyncEnabledPlatforms();
        
        stats.put("totalPlatforms", totalPlatforms);
        stats.put("activePlatforms", activePlatforms);
        stats.put("syncEnabledPlatforms", syncEnabledCount);
        stats.put("statusDistribution", statusStats);
        stats.put("syncStatusDistribution", syncStats);
        stats.put("lastUpdated", LocalDateTime.now());
        
        return stats;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getSyncHistory(Long platformId, LocalDateTime startDate, LocalDateTime endDate) {
        // 这里应该从同步日志表中查询历史记录
        // 目前返回模拟数据
        Map<String, Object> history = new HashMap<>();
        history.put("platformId", platformId);
        history.put("syncHistory", new ArrayList<>());
        history.put("totalSyncs", 0);
        history.put("successfulSyncs", 0);
        history.put("failedSyncs", 0);
        return history;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> validatePlatformConfig(Long platformId) {
        ThirdPartyPlatform platform = getPlatformById(platformId);
        Map<String, Object> validation = new HashMap<>();
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        // 检查必需配置
        if (platform.getApiBaseUrl() == null || platform.getApiBaseUrl().isEmpty()) {
            errors.add("API基础URL未配置");
        }
        if (platform.getAppKey() == null || platform.getAppKey().isEmpty()) {
            errors.add("AppKey未配置");
        }
        if (platform.getAppSecret() == null || platform.getAppSecret().isEmpty()) {
            errors.add("AppSecret未配置");
        }
        
        // 检查令牌状态
        if (platform.getAccessToken() == null || platform.getAccessToken().isEmpty()) {
            warnings.add("访问令牌未配置");
        } else if (platform.isTokenExpired()) {
            warnings.add("访问令牌已过期");
        }
        
        // 检查同步配置
        if (platform.getSyncEnabled() && platform.getAutoSyncIntervalMinutes() < 5) {
            warnings.add("同步间隔过短，建议至少5分钟");
        }
        
        validation.put("isValid", errors.isEmpty());
        validation.put("errors", errors);
        validation.put("warnings", warnings);
        validation.put("timestamp", LocalDateTime.now());
        
        return validation;
    }
    
    @Override
    public List<ThirdPartyPlatform> importPlatformConfigs(List<Map<String, Object>> configs) {
        List<ThirdPartyPlatform> platforms = new ArrayList<>();
        
        for (Map<String, Object> config : configs) {
            try {
                ThirdPartyPlatform platform = objectMapper.convertValue(config, ThirdPartyPlatform.class);
                platforms.add(createPlatform(platform));
            } catch (Exception e) {
                // 记录导入失败的配置
                System.err.println("导入平台配置失败: " + e.getMessage());
            }
        }
        
        return platforms;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> exportPlatformConfigs(List<Long> platformIds) {
        List<ThirdPartyPlatform> platforms = platformRepository.findAllById(platformIds);
        return platforms.stream()
                .map(platform -> {
                    Map<String, Object> config = objectMapper.convertValue(platform, Map.class);
                    // 移除敏感信息
                    config.remove("appSecret");
                    config.remove("accessToken");
                    config.remove("refreshToken");
                    return config;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public ThirdPartyPlatform resetPlatform(Long platformId) {
        ThirdPartyPlatform platform = getPlatformById(platformId);
        
        // 重置状态和令牌信息
        platform.setStatus(ThirdPartyPlatform.PlatformStatus.INACTIVE);
        platform.setSyncEnabled(false);
        platform.setSyncStatus(ThirdPartyPlatform.SyncStatus.IDLE);
        platform.setAccessToken(null);
        platform.setRefreshToken(null);
        platform.setTokenExpiresAt(null);
        platform.setLastSyncTime(null);
        platform.setErrorMessage(null);
        
        return platformRepository.save(platform);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getPlatformCapabilities(String platformCode) {
        // 根据平台代码返回支持的功能
        Map<String, Object> capabilities = new HashMap<>();
        
        switch (platformCode.toUpperCase()) {
            case "BOSS":
                capabilities.put("jobPosting", true);
                capabilities.put("resumeDownload", true);
                capabilities.put("messageSync", true);
                capabilities.put("candidateSearch", false);
                break;
            case "ZHILIAN":
                capabilities.put("jobPosting", true);
                capabilities.put("resumeDownload", true);
                capabilities.put("messageSync", false);
                capabilities.put("candidateSearch", true);
                break;
            case "LIEPIN":
                capabilities.put("jobPosting", true);
                capabilities.put("resumeDownload", true);
                capabilities.put("messageSync", true);
                capabilities.put("candidateSearch", true);
                break;
            default:
                capabilities.put("jobPosting", false);
                capabilities.put("resumeDownload", false);
                capabilities.put("messageSync", false);
                capabilities.put("candidateSearch", false);
        }
        
        return capabilities;
    }
    
    @Override
    public Map<String, Object> syncSpecificData(Long platformId, String dataType) {
        ThirdPartyPlatform platform = getPlatformById(platformId);
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 根据数据类型执行特定同步
            Object syncData = performSpecificDataSync(platform, dataType);
            
            result.put("success", true);
            result.put("dataType", dataType);
            result.put("data", syncData);
            result.put("timestamp", LocalDateTime.now());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "同步失败: " + e.getMessage());
            result.put("timestamp", LocalDateTime.now());
        }
        
        return result;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getSyncLogs(Long platformId, int limit) {
        // 这里应该从同步日志表中查询
        // 目前返回空列表
        return new ArrayList<>();
    }
    
    // 私有辅助方法
    private boolean performConnectionTest(ThirdPartyPlatform platform) {
        try {
            ThirdPartyPlatformApiService apiService = apiFactory.getApiService(platform);
            return apiService.testConnection(platform);
        } catch (Exception e) {
            return false;
        }
    }
    
    private Map<String, String> performTokenRefresh(ThirdPartyPlatform platform) {
        try {
            ThirdPartyPlatformApiService apiService = apiFactory.getApiService(platform);
            return apiService.refreshAccessToken(platform);
        } catch (Exception e) {
            throw new RuntimeException("令牌刷新失败: " + e.getMessage(), e);
        }
    }
    
    private boolean performTokenValidation(ThirdPartyPlatform platform) {
        try {
            ThirdPartyPlatformApiService apiService = apiFactory.getApiService(platform);
            return apiService.validateToken(platform);
        } catch (Exception e) {
            return false;
        }
    }
    
    private Map<String, Object> performDataSync(ThirdPartyPlatform platform) {
        try {
            ThirdPartyPlatformApiService apiService = apiFactory.getApiService(platform);
            return apiService.syncAllData(platform);
        } catch (Exception e) {
            throw new RuntimeException("数据同步失败: " + e.getMessage(), e);
        }
    }
    
    private Object performSpecificDataSync(ThirdPartyPlatform platform, String dataType) {
        try {
            ThirdPartyPlatformApiService apiService = apiFactory.getApiService(platform);
            Map<String, Object> params = new HashMap<>();
            return apiService.syncSpecificDataType(platform, dataType, params);
        } catch (Exception e) {
            throw new RuntimeException("特定数据同步失败: " + e.getMessage(), e);
        }
    }
}