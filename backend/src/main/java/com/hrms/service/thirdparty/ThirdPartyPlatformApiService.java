package com.hrms.service.thirdparty;

import com.hrms.entity.ThirdPartyPlatform;
import com.hrms.entity.Job;
import com.hrms.entity.Candidate;
import com.hrms.entity.Resume;

import java.util.List;
import java.util.Map;

public interface ThirdPartyPlatformApiService {
    
    /**
     * 测试平台连接
     */
    boolean testConnection(ThirdPartyPlatform platform);
    
    /**
     * 刷新访问令牌
     */
    Map<String, String> refreshAccessToken(ThirdPartyPlatform platform);
    
    /**
     * 验证令牌有效性
     */
    boolean validateToken(ThirdPartyPlatform platform);
    
    /**
     * 发布职位到第三方平台
     */
    Map<String, Object> publishJob(ThirdPartyPlatform platform, Job job);
    
    /**
     * 更新第三方平台上的职位
     */
    Map<String, Object> updateJob(ThirdPartyPlatform platform, Job job, String externalJobId);
    
    /**
     * 从第三方平台下架职位
     */
    boolean closeJob(ThirdPartyPlatform platform, String externalJobId);
    
    /**
     * 获取第三方平台上的职位状态
     */
    Map<String, Object> getJobStatus(ThirdPartyPlatform platform, String externalJobId);
    
    /**
     * 同步简历数据
     */
    List<Map<String, Object>> syncResumes(ThirdPartyPlatform platform, String jobId, int limit);
    
    /**
     * 获取候选人详细信息
     */
    Map<String, Object> getCandidateDetails(ThirdPartyPlatform platform, String candidateId);
    
    /**
     * 下载简历文件
     */
    byte[] downloadResume(ThirdPartyPlatform platform, String resumeId);
    
    /**
     * 同步消息数据
     */
    List<Map<String, Object>> syncMessages(ThirdPartyPlatform platform, String jobId, Long lastSyncTime);
    
    /**
     * 发送消息给候选人
     */
    boolean sendMessage(ThirdPartyPlatform platform, String candidateId, String message);
    
    /**
     * 搜索候选人
     */
    List<Map<String, Object>> searchCandidates(ThirdPartyPlatform platform, Map<String, Object> searchCriteria);
    
    /**
     * 获取平台统计数据
     */
    Map<String, Object> getPlatformStatistics(ThirdPartyPlatform platform);
    
    /**
     * 同步全部数据
     */
    Map<String, Object> syncAllData(ThirdPartyPlatform platform);
    
    /**
     * 获取支持的数据类型
     */
    List<String> getSupportedDataTypes();
    
    /**
     * 根据数据类型同步特定数据
     */
    Map<String, Object> syncSpecificDataType(ThirdPartyPlatform platform, String dataType, Map<String, Object> params);
}