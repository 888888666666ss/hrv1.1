package com.hrms.service;

import com.hrms.entity.ThirdPartyPlatform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ThirdPartyPlatformService {
    
    /**
     * 创建第三方平台配置
     */
    ThirdPartyPlatform createPlatform(ThirdPartyPlatform platform);
    
    /**
     * 更新第三方平台配置
     */
    ThirdPartyPlatform updatePlatform(Long platformId, ThirdPartyPlatform platformData);
    
    /**
     * 删除第三方平台配置
     */
    void deletePlatform(Long platformId);
    
    /**
     * 根据ID获取平台配置
     */
    ThirdPartyPlatform getPlatformById(Long platformId);
    
    /**
     * 根据平台代码获取平台配置
     */
    ThirdPartyPlatform getPlatformByCode(String platformCode);
    
    /**
     * 获取所有平台配置（分页）
     */
    Page<ThirdPartyPlatform> getAllPlatforms(Pageable pageable);
    
    /**
     * 根据条件查询平台配置
     */
    Page<ThirdPartyPlatform> searchPlatforms(String platformName, String platformCode, 
                                           ThirdPartyPlatform.PlatformStatus status,
                                           ThirdPartyPlatform.SyncStatus syncStatus,
                                           Boolean syncEnabled, Pageable pageable);
    
    /**
     * 获取激活状态的平台
     */
    List<ThirdPartyPlatform> getActivePlatforms();
    
    /**
     * 获取启用同步的平台
     */
    List<ThirdPartyPlatform> getSyncEnabledPlatforms();
    
    /**
     * 更新平台状态
     */
    ThirdPartyPlatform updatePlatformStatus(Long platformId, ThirdPartyPlatform.PlatformStatus status);
    
    /**
     * 更新同步状态
     */
    ThirdPartyPlatform updateSyncStatus(Long platformId, ThirdPartyPlatform.SyncStatus syncStatus);
    
    /**
     * 启用/禁用同步
     */
    ThirdPartyPlatform toggleSync(Long platformId, Boolean enabled);
    
    /**
     * 测试平台连接
     */
    Map<String, Object> testConnection(Long platformId);
    
    /**
     * 刷新访问令牌
     */
    ThirdPartyPlatform refreshAccessToken(Long platformId);
    
    /**
     * 验证令牌是否有效
     */
    boolean validateToken(Long platformId);
    
    /**
     * 获取需要同步的平台
     */
    List<ThirdPartyPlatform> getPlatformsNeedingSync();
    
    /**
     * 获取令牌即将过期的平台
     */
    List<ThirdPartyPlatform> getPlatformsWithExpiringTokens(int hoursBeforeExpiry);
    
    /**
     * 同步平台数据
     */
    Map<String, Object> syncPlatformData(Long platformId);
    
    /**
     * 批量同步所有启用的平台
     */
    Map<String, Object> syncAllEnabledPlatforms();
    
    /**
     * 设置平台错误信息
     */
    void setPlatformError(Long platformId, String errorMessage);
    
    /**
     * 清除平台错误信息
     */
    void clearPlatformError(Long platformId);
    
    /**
     * 更新最后同步时间
     */
    void updateLastSyncTime(Long platformId, LocalDateTime syncTime);
    
    /**
     * 获取平台统计信息
     */
    Map<String, Object> getPlatformStatistics();
    
    /**
     * 获取同步历史记录
     */
    Map<String, Object> getSyncHistory(Long platformId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * 验证平台配置完整性
     */
    Map<String, Object> validatePlatformConfig(Long platformId);
    
    /**
     * 导入平台配置
     */
    List<ThirdPartyPlatform> importPlatformConfigs(List<Map<String, Object>> configs);
    
    /**
     * 导出平台配置
     */
    List<Map<String, Object>> exportPlatformConfigs(List<Long> platformIds);
    
    /**
     * 重置平台配置
     */
    ThirdPartyPlatform resetPlatform(Long platformId);
    
    /**
     * 获取平台支持的功能列表
     */
    Map<String, Object> getPlatformCapabilities(String platformCode);
    
    /**
     * 同步特定类型的数据
     */
    Map<String, Object> syncSpecificData(Long platformId, String dataType);
    
    /**
     * 获取平台同步日志
     */
    List<Map<String, Object>> getSyncLogs(Long platformId, int limit);
}