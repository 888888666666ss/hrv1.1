package com.hrms.service.thirdparty.impl;

import com.hrms.entity.ThirdPartyPlatform;
import com.hrms.entity.Job;
import com.hrms.service.thirdparty.ThirdPartyPlatformApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service("bossApiService")
public class BossApiServiceImpl implements ThirdPartyPlatformApiService {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private static final String PLATFORM_CODE = "BOSS";
    
    @Override
    public boolean testConnection(ThirdPartyPlatform platform) {
        try {
            String url = platform.getApiBaseUrl() + "/api/v1/ping";
            HttpHeaders headers = createHeaders(platform);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public Map<String, String> refreshAccessToken(ThirdPartyPlatform platform) {
        try {
            String url = platform.getApiBaseUrl() + "/oauth2/token";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            String body = String.format("grant_type=refresh_token&refresh_token=%s&client_id=%s&client_secret=%s",
                    platform.getRefreshToken(), platform.getAppKey(), platform.getAppSecret());
            
            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                Map<String, String> tokenInfo = new HashMap<>();
                tokenInfo.put("accessToken", (String) responseBody.get("access_token"));
                tokenInfo.put("refreshToken", (String) responseBody.get("refresh_token"));
                tokenInfo.put("expiresIn", String.valueOf(responseBody.get("expires_in")));
                return tokenInfo;
            }
        } catch (Exception e) {
            throw new RuntimeException("Boss直聘令牌刷新失败: " + e.getMessage());
        }
        
        throw new RuntimeException("Boss直聘令牌刷新失败");
    }
    
    @Override
    public boolean validateToken(ThirdPartyPlatform platform) {
        try {
            String url = platform.getApiBaseUrl() + "/api/v1/user/info";
            HttpHeaders headers = createHeaders(platform);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public Map<String, Object> publishJob(ThirdPartyPlatform platform, Job job) {
        try {
            String url = platform.getApiBaseUrl() + "/api/v1/jobs";
            HttpHeaders headers = createHeaders(platform);
            
            Map<String, Object> jobData = new HashMap<>();
            jobData.put("title", job.getTitle());
            jobData.put("description", job.getDescription());
            jobData.put("requirements", job.getRequirements());
            jobData.put("department", job.getDepartment());
            jobData.put("location", job.getLocation());
            jobData.put("salary_min", job.getSalaryMin());
            jobData.put("salary_max", job.getSalaryMax());
            jobData.put("employment_type", job.getEmploymentType().toString());
            jobData.put("benefits", job.getBenefits());
            jobData.put("application_deadline", job.getApplicationDeadline() != null ? 
                    job.getApplicationDeadline().toEpochSecond(ZoneOffset.UTC) : null);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(jobData, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            
            Map<String, Object> result = new HashMap<>();
            if (response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.CREATED) {
                Map<String, Object> responseBody = response.getBody();
                result.put("success", true);
                result.put("externalJobId", responseBody.get("job_id"));
                result.put("publishUrl", responseBody.get("job_url"));
                result.put("message", "职位发布成功");
            } else {
                result.put("success", false);
                result.put("message", "职位发布失败");
            }
            
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "职位发布失败: " + e.getMessage());
            return result;
        }
    }
    
    @Override
    public Map<String, Object> updateJob(ThirdPartyPlatform platform, Job job, String externalJobId) {
        try {
            String url = platform.getApiBaseUrl() + "/api/v1/jobs/" + externalJobId;
            HttpHeaders headers = createHeaders(platform);
            
            Map<String, Object> jobData = new HashMap<>();
            jobData.put("title", job.getTitle());
            jobData.put("description", job.getDescription());
            jobData.put("requirements", job.getRequirements());
            jobData.put("department", job.getDepartment());
            jobData.put("location", job.getLocation());
            jobData.put("salary_min", job.getSalaryMin());
            jobData.put("salary_max", job.getSalaryMax());
            jobData.put("employment_type", job.getEmploymentType().toString());
            jobData.put("benefits", job.getBenefits());
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(jobData, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Map.class);
            
            Map<String, Object> result = new HashMap<>();
            if (response.getStatusCode() == HttpStatus.OK) {
                result.put("success", true);
                result.put("message", "职位更新成功");
            } else {
                result.put("success", false);
                result.put("message", "职位更新失败");
            }
            
            return result;
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "职位更新失败: " + e.getMessage());
            return result;
        }
    }
    
    @Override
    public boolean closeJob(ThirdPartyPlatform platform, String externalJobId) {
        try {
            String url = platform.getApiBaseUrl() + "/api/v1/jobs/" + externalJobId + "/close";
            HttpHeaders headers = createHeaders(platform);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public Map<String, Object> getJobStatus(ThirdPartyPlatform platform, String externalJobId) {
        try {
            String url = platform.getApiBaseUrl() + "/api/v1/jobs/" + externalJobId + "/status";
            HttpHeaders headers = createHeaders(platform);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (Exception e) {
            // 记录日志
        }
        
        return new HashMap<>();
    }
    
    @Override
    public List<Map<String, Object>> syncResumes(ThirdPartyPlatform platform, String jobId, int limit) {
        try {
            String url = platform.getApiBaseUrl() + "/api/v1/jobs/" + jobId + "/resumes" +
                    "?limit=" + limit + "&order=desc";
            HttpHeaders headers = createHeaders(platform);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                List<Map<String, Object>> resumes = (List<Map<String, Object>>) responseBody.get("data");
                
                // 处理简历数据格式
                return resumes.stream().map(this::processResumeData).toList();
            }
        } catch (Exception e) {
            // 记录日志
        }
        
        return new ArrayList<>();
    }
    
    @Override
    public Map<String, Object> getCandidateDetails(ThirdPartyPlatform platform, String candidateId) {
        try {
            String url = platform.getApiBaseUrl() + "/api/v1/candidates/" + candidateId;
            HttpHeaders headers = createHeaders(platform);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (Exception e) {
            // 记录日志
        }
        
        return new HashMap<>();
    }
    
    @Override
    public byte[] downloadResume(ThirdPartyPlatform platform, String resumeId) {
        try {
            String url = platform.getApiBaseUrl() + "/api/v1/resumes/" + resumeId + "/download";
            HttpHeaders headers = createHeaders(platform);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (Exception e) {
            // 记录日志
        }
        
        return new byte[0];
    }
    
    @Override
    public List<Map<String, Object>> syncMessages(ThirdPartyPlatform platform, String jobId, Long lastSyncTime) {
        try {
            String url = platform.getApiBaseUrl() + "/api/v1/jobs/" + jobId + "/messages" +
                    "?since=" + (lastSyncTime != null ? lastSyncTime : 0);
            HttpHeaders headers = createHeaders(platform);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                return (List<Map<String, Object>>) responseBody.get("data");
            }
        } catch (Exception e) {
            // 记录日志
        }
        
        return new ArrayList<>();
    }
    
    @Override
    public boolean sendMessage(ThirdPartyPlatform platform, String candidateId, String message) {
        try {
            String url = platform.getApiBaseUrl() + "/api/v1/messages";
            HttpHeaders headers = createHeaders(platform);
            
            Map<String, Object> messageData = new HashMap<>();
            messageData.put("candidate_id", candidateId);
            messageData.put("content", message);
            messageData.put("type", "text");
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(messageData, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            
            return response.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public List<Map<String, Object>> searchCandidates(ThirdPartyPlatform platform, Map<String, Object> searchCriteria) {
        try {
            String url = platform.getApiBaseUrl() + "/api/v1/candidates/search";
            HttpHeaders headers = createHeaders(platform);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(searchCriteria, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                return (List<Map<String, Object>>) responseBody.get("data");
            }
        } catch (Exception e) {
            // 记录日志
        }
        
        return new ArrayList<>();
    }
    
    @Override
    public Map<String, Object> getPlatformStatistics(ThirdPartyPlatform platform) {
        try {
            String url = platform.getApiBaseUrl() + "/api/v1/statistics/dashboard";
            HttpHeaders headers = createHeaders(platform);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (Exception e) {
            // 记录日志
        }
        
        return new HashMap<>();
    }
    
    @Override
    public Map<String, Object> syncAllData(ThirdPartyPlatform platform) {
        Map<String, Object> result = new HashMap<>();
        result.put("platform", PLATFORM_CODE);
        result.put("startTime", LocalDateTime.now());
        
        try {
            // 同步职位数据
            Map<String, Object> jobsResult = syncSpecificDataType(platform, "jobs", new HashMap<>());
            result.put("jobs", jobsResult);
            
            // 同步简历数据
            Map<String, Object> resumesResult = syncSpecificDataType(platform, "resumes", new HashMap<>());
            result.put("resumes", resumesResult);
            
            // 同步消息数据
            Map<String, Object> messagesResult = syncSpecificDataType(platform, "messages", new HashMap<>());
            result.put("messages", messagesResult);
            
            result.put("success", true);
            result.put("endTime", LocalDateTime.now());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("endTime", LocalDateTime.now());
        }
        
        return result;
    }
    
    @Override
    public List<String> getSupportedDataTypes() {
        return Arrays.asList("jobs", "resumes", "messages", "candidates", "statistics");
    }
    
    @Override
    public Map<String, Object> syncSpecificDataType(ThirdPartyPlatform platform, String dataType, Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("dataType", dataType);
        result.put("platform", PLATFORM_CODE);
        
        try {
            switch (dataType.toLowerCase()) {
                case "jobs":
                    List<Map<String, Object>> jobs = getJobs(platform, params);
                    result.put("count", jobs.size());
                    result.put("data", jobs);
                    break;
                case "resumes":
                    String jobId = (String) params.get("jobId");
                    int limit = (Integer) params.getOrDefault("limit", 100);
                    List<Map<String, Object>> resumes = syncResumes(platform, jobId, limit);
                    result.put("count", resumes.size());
                    result.put("data", resumes);
                    break;
                case "messages":
                    String messageJobId = (String) params.get("jobId");
                    Long lastSyncTime = (Long) params.get("lastSyncTime");
                    List<Map<String, Object>> messages = syncMessages(platform, messageJobId, lastSyncTime);
                    result.put("count", messages.size());
                    result.put("data", messages);
                    break;
                case "candidates":
                    List<Map<String, Object>> candidates = searchCandidates(platform, params);
                    result.put("count", candidates.size());
                    result.put("data", candidates);
                    break;
                case "statistics":
                    Map<String, Object> stats = getPlatformStatistics(platform);
                    result.put("data", stats);
                    break;
                default:
                    throw new IllegalArgumentException("不支持的数据类型: " + dataType);
            }
            
            result.put("success", true);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        result.put("timestamp", LocalDateTime.now());
        return result;
    }
    
    private HttpHeaders createHeaders(ThirdPartyPlatform platform) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(platform.getAccessToken());
        headers.set("User-Agent", "HRMS-System/1.0");
        return headers;
    }
    
    private Map<String, Object> processResumeData(Map<String, Object> rawData) {
        Map<String, Object> processedData = new HashMap<>();
        
        // 标准化字段名称
        processedData.put("externalId", rawData.get("id"));
        processedData.put("candidateName", rawData.get("name"));
        processedData.put("candidateEmail", rawData.get("email"));
        processedData.put("candidatePhone", rawData.get("phone"));
        processedData.put("position", rawData.get("current_position"));
        processedData.put("company", rawData.get("current_company"));
        processedData.put("education", rawData.get("education"));
        processedData.put("experience", rawData.get("work_experience"));
        processedData.put("skills", rawData.get("skills"));
        processedData.put("expectedSalary", rawData.get("expected_salary"));
        processedData.put("resumeUrl", rawData.get("resume_url"));
        processedData.put("appliedAt", rawData.get("applied_at"));
        processedData.put("source", PLATFORM_CODE);
        
        return processedData;
    }
    
    private List<Map<String, Object>> getJobs(ThirdPartyPlatform platform, Map<String, Object> params) {
        try {
            String url = platform.getApiBaseUrl() + "/api/v1/jobs";
            HttpHeaders headers = createHeaders(platform);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                return (List<Map<String, Object>>) responseBody.get("data");
            }
        } catch (Exception e) {
            // 记录日志
        }
        
        return new ArrayList<>();
    }
}