package com.hrms.service.thirdparty;

import com.hrms.entity.ThirdPartyPlatform;
import com.hrms.service.thirdparty.impl.BossApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ThirdPartyPlatformApiFactory {
    
    @Autowired
    private ApplicationContext applicationContext;
    
    private final Map<String, String> platformServiceMap = new HashMap<>();
    
    public ThirdPartyPlatformApiFactory() {
        // 注册平台对应的服务实现
        platformServiceMap.put("BOSS", "bossApiService");
        platformServiceMap.put("ZHILIAN", "zhilianApiService");
        platformServiceMap.put("LIEPIN", "liepinApiService");
        platformServiceMap.put("LAGOU", "lagouApiService");
    }
    
    /**
     * 根据平台获取对应的API服务实现
     */
    public ThirdPartyPlatformApiService getApiService(ThirdPartyPlatform platform) {
        String platformCode = platform.getPlatformCode().toUpperCase();
        String serviceName = platformServiceMap.get(platformCode);
        
        if (serviceName == null) {
            throw new RuntimeException("不支持的第三方平台: " + platformCode);
        }
        
        try {
            return (ThirdPartyPlatformApiService) applicationContext.getBean(serviceName);
        } catch (Exception e) {
            throw new RuntimeException("获取平台API服务失败: " + platformCode, e);
        }
    }
    
    /**
     * 根据平台代码获取对应的API服务实现
     */
    public ThirdPartyPlatformApiService getApiService(String platformCode) {
        String serviceName = platformServiceMap.get(platformCode.toUpperCase());
        
        if (serviceName == null) {
            throw new RuntimeException("不支持的第三方平台: " + platformCode);
        }
        
        try {
            return (ThirdPartyPlatformApiService) applicationContext.getBean(serviceName);
        } catch (Exception e) {
            throw new RuntimeException("获取平台API服务失败: " + platformCode, e);
        }
    }
    
    /**
     * 检查平台是否支持
     */
    public boolean isPlatformSupported(String platformCode) {
        return platformServiceMap.containsKey(platformCode.toUpperCase());
    }
    
    /**
     * 获取所有支持的平台代码
     */
    public String[] getSupportedPlatforms() {
        return platformServiceMap.keySet().toArray(new String[0]);
    }
    
    /**
     * 注册新的平台服务
     */
    public void registerPlatformService(String platformCode, String serviceName) {
        platformServiceMap.put(platformCode.toUpperCase(), serviceName);
    }
}