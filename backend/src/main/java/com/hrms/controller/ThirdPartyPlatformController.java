package com.hrms.controller;

import com.hrms.entity.ThirdPartyPlatform;
import com.hrms.service.ThirdPartyPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/third-party-platforms")
@CrossOrigin(origins = "http://localhost:5173")
public class ThirdPartyPlatformController {
    
    @Autowired
    private ThirdPartyPlatformService platformService;
    
    @PostMapping
    public ResponseEntity<?> createPlatform(@RequestBody ThirdPartyPlatform platform) {
        try {
            ThirdPartyPlatform created = platformService.createPlatform(platform);
            return ResponseEntity.ok(createSuccessResponse("平台创建成功", created));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("平台创建失败", e.getMessage()));
        }
    }
    
    @PutMapping("/{platformId}")
    public ResponseEntity<?> updatePlatform(@PathVariable Long platformId, 
                                          @RequestBody ThirdPartyPlatform platform) {
        try {
            ThirdPartyPlatform updated = platformService.updatePlatform(platformId, platform);
            return ResponseEntity.ok(createSuccessResponse("平台更新成功", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("平台更新失败", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{platformId}")
    public ResponseEntity<?> deletePlatform(@PathVariable Long platformId) {
        try {
            platformService.deletePlatform(platformId);
            return ResponseEntity.ok(createSuccessResponse("平台删除成功", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("平台删除失败", e.getMessage()));
        }
    }
    
    @GetMapping("/{platformId}")
    public ResponseEntity<?> getPlatform(@PathVariable Long platformId) {
        try {
            ThirdPartyPlatform platform = platformService.getPlatformById(platformId);
            return ResponseEntity.ok(createSuccessResponse("获取平台信息成功", platform));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("获取平台信息失败", e.getMessage()));
        }
    }
    
    @GetMapping("/code/{platformCode}")
    public ResponseEntity<?> getPlatformByCode(@PathVariable String platformCode) {
        try {
            ThirdPartyPlatform platform = platformService.getPlatformByCode(platformCode);
            return ResponseEntity.ok(createSuccessResponse("获取平台信息成功", platform));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("获取平台信息失败", e.getMessage()));
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getPlatforms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String platformName,
            @RequestParam(required = false) String platformCode,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String syncStatus,
            @RequestParam(required = false) Boolean syncEnabled) {
        try {
            Sort sort = Sort.by(sortDir.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            ThirdPartyPlatform.PlatformStatus platformStatus = null;
            if (status != null) {
                platformStatus = ThirdPartyPlatform.PlatformStatus.valueOf(status.toUpperCase());
            }
            
            ThirdPartyPlatform.SyncStatus platformSyncStatus = null;
            if (syncStatus != null) {
                platformSyncStatus = ThirdPartyPlatform.SyncStatus.valueOf(syncStatus.toUpperCase());
            }
            
            Page<ThirdPartyPlatform> platforms = platformService.searchPlatforms(
                    platformName, platformCode, platformStatus, platformSyncStatus, syncEnabled, pageable);
            
            return ResponseEntity.ok(createSuccessResponse("获取平台列表成功", platforms));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("获取平台列表失败", e.getMessage()));
        }
    }
    
    @GetMapping("/active")
    public ResponseEntity<?> getActivePlatforms() {
        try {
            List<ThirdPartyPlatform> platforms = platformService.getActivePlatforms();
            return ResponseEntity.ok(createSuccessResponse("获取激活平台成功", platforms));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("获取激活平台失败", e.getMessage()));
        }
    }
    
    @GetMapping("/sync-enabled")
    public ResponseEntity<?> getSyncEnabledPlatforms() {
        try {
            List<ThirdPartyPlatform> platforms = platformService.getSyncEnabledPlatforms();
            return ResponseEntity.ok(createSuccessResponse("获取同步启用平台成功", platforms));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("获取同步启用平台失败", e.getMessage()));
        }
    }
    
    @PutMapping("/{platformId}/status")
    public ResponseEntity<?> updatePlatformStatus(@PathVariable Long platformId, 
                                                 @RequestBody Map<String, String> request) {
        try {
            String statusStr = request.get("status");
            ThirdPartyPlatform.PlatformStatus status = ThirdPartyPlatform.PlatformStatus.valueOf(statusStr.toUpperCase());
            
            ThirdPartyPlatform updated = platformService.updatePlatformStatus(platformId, status);
            return ResponseEntity.ok(createSuccessResponse("平台状态更新成功", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("平台状态更新失败", e.getMessage()));
        }
    }
    
    @PutMapping("/{platformId}/sync-status")
    public ResponseEntity<?> updateSyncStatus(@PathVariable Long platformId, 
                                             @RequestBody Map<String, String> request) {
        try {
            String statusStr = request.get("syncStatus");
            ThirdPartyPlatform.SyncStatus syncStatus = ThirdPartyPlatform.SyncStatus.valueOf(statusStr.toUpperCase());
            
            ThirdPartyPlatform updated = platformService.updateSyncStatus(platformId, syncStatus);
            return ResponseEntity.ok(createSuccessResponse("同步状态更新成功", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("同步状态更新失败", e.getMessage()));
        }
    }
    
    @PutMapping("/{platformId}/toggle-sync")
    public ResponseEntity<?> toggleSync(@PathVariable Long platformId, 
                                       @RequestBody Map<String, Boolean> request) {
        try {
            Boolean enabled = request.get("enabled");
            ThirdPartyPlatform updated = platformService.toggleSync(platformId, enabled);
            return ResponseEntity.ok(createSuccessResponse("同步开关切换成功", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("同步开关切换失败", e.getMessage()));
        }
    }
    
    @PostMapping("/{platformId}/test-connection")
    public ResponseEntity<?> testConnection(@PathVariable Long platformId) {
        try {
            Map<String, Object> result = platformService.testConnection(platformId);
            return ResponseEntity.ok(createSuccessResponse("连接测试完成", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("连接测试失败", e.getMessage()));
        }
    }
    
    @PostMapping("/{platformId}/refresh-token")
    public ResponseEntity<?> refreshToken(@PathVariable Long platformId) {
        try {
            ThirdPartyPlatform updated = platformService.refreshAccessToken(platformId);
            return ResponseEntity.ok(createSuccessResponse("令牌刷新成功", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("令牌刷新失败", e.getMessage()));
        }
    }
    
    @GetMapping("/{platformId}/validate-token")
    public ResponseEntity<?> validateToken(@PathVariable Long platformId) {
        try {
            boolean isValid = platformService.validateToken(platformId);
            Map<String, Object> result = new HashMap<>();
            result.put("isValid", isValid);
            result.put("message", isValid ? "令牌有效" : "令牌无效");
            
            return ResponseEntity.ok(createSuccessResponse("令牌验证完成", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("令牌验证失败", e.getMessage()));
        }
    }
    
    @GetMapping("/needing-sync")
    public ResponseEntity<?> getPlatformsNeedingSync() {
        try {
            List<ThirdPartyPlatform> platforms = platformService.getPlatformsNeedingSync();
            return ResponseEntity.ok(createSuccessResponse("获取待同步平台成功", platforms));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("获取待同步平台失败", e.getMessage()));
        }
    }
    
    @GetMapping("/expiring-tokens")
    public ResponseEntity<?> getPlatformsWithExpiringTokens(
            @RequestParam(defaultValue = "24") int hoursBeforeExpiry) {
        try {
            List<ThirdPartyPlatform> platforms = platformService.getPlatformsWithExpiringTokens(hoursBeforeExpiry);
            return ResponseEntity.ok(createSuccessResponse("获取令牌即将过期平台成功", platforms));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("获取令牌即将过期平台失败", e.getMessage()));
        }
    }
    
    @PostMapping("/{platformId}/sync")
    public ResponseEntity<?> syncPlatformData(@PathVariable Long platformId) {
        try {
            Map<String, Object> result = platformService.syncPlatformData(platformId);
            return ResponseEntity.ok(createSuccessResponse("平台数据同步完成", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("平台数据同步失败", e.getMessage()));
        }
    }
    
    @PostMapping("/sync-all")
    public ResponseEntity<?> syncAllEnabledPlatforms() {
        try {
            Map<String, Object> result = platformService.syncAllEnabledPlatforms();
            return ResponseEntity.ok(createSuccessResponse("批量同步完成", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("批量同步失败", e.getMessage()));
        }
    }
    
    @PostMapping("/{platformId}/clear-error")
    public ResponseEntity<?> clearPlatformError(@PathVariable Long platformId) {
        try {
            platformService.clearPlatformError(platformId);
            return ResponseEntity.ok(createSuccessResponse("错误信息清除成功", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("错误信息清除失败", e.getMessage()));
        }
    }
    
    @GetMapping("/statistics")
    public ResponseEntity<?> getPlatformStatistics() {
        try {
            Map<String, Object> stats = platformService.getPlatformStatistics();
            return ResponseEntity.ok(createSuccessResponse("获取平台统计成功", stats));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("获取平台统计失败", e.getMessage()));
        }
    }
    
    @GetMapping("/{platformId}/sync-history")
    public ResponseEntity<?> getSyncHistory(@PathVariable Long platformId,
                                          @RequestParam(required = false) String startDate,
                                          @RequestParam(required = false) String endDate) {
        try {
            LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate) : LocalDateTime.now().minusDays(30);
            LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate) : LocalDateTime.now();
            
            Map<String, Object> history = platformService.getSyncHistory(platformId, start, end);
            return ResponseEntity.ok(createSuccessResponse("获取同步历史成功", history));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("获取同步历史失败", e.getMessage()));
        }
    }
    
    @GetMapping("/{platformId}/validate-config")
    public ResponseEntity<?> validatePlatformConfig(@PathVariable Long platformId) {
        try {
            Map<String, Object> validation = platformService.validatePlatformConfig(platformId);
            return ResponseEntity.ok(createSuccessResponse("配置验证完成", validation));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("配置验证失败", e.getMessage()));
        }
    }
    
    @PostMapping("/import")
    public ResponseEntity<?> importPlatformConfigs(@RequestBody List<Map<String, Object>> configs) {
        try {
            List<ThirdPartyPlatform> platforms = platformService.importPlatformConfigs(configs);
            return ResponseEntity.ok(createSuccessResponse("配置导入成功", platforms));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("配置导入失败", e.getMessage()));
        }
    }
    
    @PostMapping("/export")
    public ResponseEntity<?> exportPlatformConfigs(@RequestBody List<Long> platformIds) {
        try {
            List<Map<String, Object>> configs = platformService.exportPlatformConfigs(platformIds);
            return ResponseEntity.ok(createSuccessResponse("配置导出成功", configs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("配置导出失败", e.getMessage()));
        }
    }
    
    @PostMapping("/{platformId}/reset")
    public ResponseEntity<?> resetPlatform(@PathVariable Long platformId) {
        try {
            ThirdPartyPlatform platform = platformService.resetPlatform(platformId);
            return ResponseEntity.ok(createSuccessResponse("平台重置成功", platform));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("平台重置失败", e.getMessage()));
        }
    }
    
    @GetMapping("/capabilities/{platformCode}")
    public ResponseEntity<?> getPlatformCapabilities(@PathVariable String platformCode) {
        try {
            Map<String, Object> capabilities = platformService.getPlatformCapabilities(platformCode);
            return ResponseEntity.ok(createSuccessResponse("获取平台能力成功", capabilities));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("获取平台能力失败", e.getMessage()));
        }
    }
    
    @PostMapping("/{platformId}/sync-data")
    public ResponseEntity<?> syncSpecificData(@PathVariable Long platformId, 
                                            @RequestBody Map<String, String> request) {
        try {
            String dataType = request.get("dataType");
            Map<String, Object> result = platformService.syncSpecificData(platformId, dataType);
            return ResponseEntity.ok(createSuccessResponse("特定数据同步完成", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("特定数据同步失败", e.getMessage()));
        }
    }
    
    @GetMapping("/{platformId}/sync-logs")
    public ResponseEntity<?> getSyncLogs(@PathVariable Long platformId,
                                       @RequestParam(defaultValue = "50") int limit) {
        try {
            List<Map<String, Object>> logs = platformService.getSyncLogs(platformId, limit);
            return ResponseEntity.ok(createSuccessResponse("获取同步日志成功", logs));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("获取同步日志失败", e.getMessage()));
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