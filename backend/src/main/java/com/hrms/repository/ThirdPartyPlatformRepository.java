package com.hrms.repository;

import com.hrms.entity.ThirdPartyPlatform;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ThirdPartyPlatformRepository extends JpaRepository<ThirdPartyPlatform, Long> {
    
    /**
     * 根据平台代码查找平台
     */
    Optional<ThirdPartyPlatform> findByPlatformCode(String platformCode);
    
    /**
     * 根据平台状态查找平台
     */
    List<ThirdPartyPlatform> findByStatus(ThirdPartyPlatform.PlatformStatus status);
    
    /**
     * 查找激活状态的平台
     */
    List<ThirdPartyPlatform> findByStatusAndSyncEnabled(ThirdPartyPlatform.PlatformStatus status, Boolean syncEnabled);
    
    /**
     * 根据同步状态查找平台
     */
    List<ThirdPartyPlatform> findBySyncStatus(ThirdPartyPlatform.SyncStatus syncStatus);
    
    /**
     * 查找启用同步的平台
     */
    @Query("SELECT p FROM ThirdPartyPlatform p WHERE p.syncEnabled = true AND p.status = 'ACTIVE'")
    List<ThirdPartyPlatform> findActiveSyncEnabledPlatforms();
    
    /**
     * 查找需要同步的平台（根据同步间隔）
     */
    @Query("SELECT p FROM ThirdPartyPlatform p WHERE p.syncEnabled = true AND p.status = 'ACTIVE' " +
           "AND p.syncStatus != 'SYNCING' AND " +
           "(p.lastSyncTime IS NULL OR p.lastSyncTime <= :cutoffTime)")
    List<ThirdPartyPlatform> findPlatformsNeedingSync(@Param("cutoffTime") LocalDateTime cutoffTime);
    
    /**
     * 查找Token即将过期的平台
     */
    @Query("SELECT p FROM ThirdPartyPlatform p WHERE p.tokenExpiresAt IS NOT NULL " +
           "AND p.tokenExpiresAt <= :expiryTime AND p.status = 'ACTIVE'")
    List<ThirdPartyPlatform> findPlatformsWithExpiringTokens(@Param("expiryTime") LocalDateTime expiryTime);
    
    /**
     * 查找Token已过期的平台
     */
    @Query("SELECT p FROM ThirdPartyPlatform p WHERE p.tokenExpiresAt IS NOT NULL " +
           "AND p.tokenExpiresAt <= :currentTime")
    List<ThirdPartyPlatform> findPlatformsWithExpiredTokens(@Param("currentTime") LocalDateTime currentTime);
    
    /**
     * 根据平台名称模糊查找
     */
    List<ThirdPartyPlatform> findByPlatformNameContainingIgnoreCase(String platformName);
    
    /**
     * 查找有错误的平台
     */
    @Query("SELECT p FROM ThirdPartyPlatform p WHERE p.errorMessage IS NOT NULL AND p.errorMessage != ''")
    List<ThirdPartyPlatform> findPlatformsWithErrors();
    
    /**
     * 查找最近同步的平台
     */
    @Query("SELECT p FROM ThirdPartyPlatform p WHERE p.lastSyncTime >= :startTime ORDER BY p.lastSyncTime DESC")
    List<ThirdPartyPlatform> findRecentlySyncedPlatforms(@Param("startTime") LocalDateTime startTime);
    
    /**
     * 多条件查询平台
     */
    @Query("SELECT p FROM ThirdPartyPlatform p WHERE " +
           "(:platformName IS NULL OR p.platformName LIKE %:platformName%) AND " +
           "(:platformCode IS NULL OR p.platformCode = :platformCode) AND " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:syncStatus IS NULL OR p.syncStatus = :syncStatus) AND " +
           "(:syncEnabled IS NULL OR p.syncEnabled = :syncEnabled)")
    Page<ThirdPartyPlatform> findPlatformsWithCriteria(
            @Param("platformName") String platformName,
            @Param("platformCode") String platformCode,
            @Param("status") ThirdPartyPlatform.PlatformStatus status,
            @Param("syncStatus") ThirdPartyPlatform.SyncStatus syncStatus,
            @Param("syncEnabled") Boolean syncEnabled,
            Pageable pageable
    );
    
    /**
     * 统计各状态的平台数量
     */
    @Query("SELECT p.status, COUNT(p) FROM ThirdPartyPlatform p GROUP BY p.status")
    List<Object[]> countPlatformsByStatus();
    
    /**
     * 统计各同步状态的平台数量
     */
    @Query("SELECT p.syncStatus, COUNT(p) FROM ThirdPartyPlatform p GROUP BY p.syncStatus")
    List<Object[]> countPlatformsBySyncStatus();
    
    /**
     * 统计启用同步的平台数量
     */
    @Query("SELECT COUNT(p) FROM ThirdPartyPlatform p WHERE p.syncEnabled = true")
    long countSyncEnabledPlatforms();
    
    /**
     * 统计激活状态的平台数量
     */
    long countByStatus(ThirdPartyPlatform.PlatformStatus status);
    
    /**
     * 查找配置不完整的平台
     */
    @Query("SELECT p FROM ThirdPartyPlatform p WHERE " +
           "p.appKey IS NULL OR p.appKey = '' OR " +
           "p.appSecret IS NULL OR p.appSecret = '' OR " +
           "p.apiBaseUrl IS NULL OR p.apiBaseUrl = ''")
    List<ThirdPartyPlatform> findIncompleteConfigPlatforms();
    
    /**
     * 批量更新平台状态
     */
    @Query("UPDATE ThirdPartyPlatform p SET p.status = :status WHERE p.id IN :ids")
    void batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") ThirdPartyPlatform.PlatformStatus status);
    
    /**
     * 批量更新同步状态
     */
    @Query("UPDATE ThirdPartyPlatform p SET p.syncStatus = :syncStatus WHERE p.id IN :ids")
    void batchUpdateSyncStatus(@Param("ids") List<Long> ids, @Param("syncStatus") ThirdPartyPlatform.SyncStatus syncStatus);
    
    /**
     * 检查平台代码是否存在
     */
    boolean existsByPlatformCode(String platformCode);
    
    /**
     * 检查平台名称是否存在
     */
    boolean existsByPlatformName(String platformName);
    
    /**
     * 查找指定时间范围内更新的平台
     */
    @Query("SELECT p FROM ThirdPartyPlatform p WHERE p.updatedAt BETWEEN :startDate AND :endDate")
    List<ThirdPartyPlatform> findByUpdatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * 查找同步频率最高的平台
     */
    @Query("SELECT p FROM ThirdPartyPlatform p WHERE p.autoSyncIntervalMinutes = " +
           "(SELECT MIN(p2.autoSyncIntervalMinutes) FROM ThirdPartyPlatform p2 WHERE p2.syncEnabled = true)")
    List<ThirdPartyPlatform> findHighestFrequencySyncPlatforms();
    
    /**
     * 根据Webhook URL查找平台
     */
    Optional<ThirdPartyPlatform> findByWebhookUrl(String webhookUrl);
    
    /**
     * 查找有Webhook配置的平台
     */
    @Query("SELECT p FROM ThirdPartyPlatform p WHERE p.webhookUrl IS NOT NULL AND p.webhookUrl != ''")
    List<ThirdPartyPlatform> findPlatformsWithWebhook();
}