package com.hrms.controller;

import com.hrms.entity.Resume;
import com.hrms.entity.Job;
import com.hrms.service.AIService;
import com.hrms.service.ResumeService;
import com.hrms.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "http://localhost:5173")
public class AIController {
    
    @Autowired
    private AIService aiService;
    
    @Autowired
    private ResumeService resumeService;
    
    @Autowired
    private JobService jobService;
    
    @PostMapping("/parse-resume")
    public ResponseEntity<?> parseResume(@RequestParam("file") MultipartFile file) {
        try {
            // 保存文件并获取路径
            String filePath = saveUploadedFile(file);
            String fileType = getFileExtension(file.getOriginalFilename());
            
            // 解析简历
            Map<String, Object> result = aiService.parseResume(filePath, fileType);
            
            return ResponseEntity.ok(createSuccessResponse("简历解析成功", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("简历解析失败", e.getMessage()));
        }
    }
    
    @PostMapping("/analyze-quality")
    public ResponseEntity<?> analyzeResumeQuality(@RequestBody Map<String, String> request) {
        try {
            String content = request.get("content");
            Map<String, Object> result = aiService.analyzeResumeQuality(content);
            
            return ResponseEntity.ok(createSuccessResponse("简历质量分析完成", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("简历质量分析失败", e.getMessage()));
        }
    }
    
    @PostMapping("/calculate-match")
    public ResponseEntity<?> calculateMatch(@RequestBody Map<String, Long> request) {
        try {
            Long resumeId = request.get("resumeId");
            Long jobId = request.get("jobId");
            
            Resume resume = resumeService.getResumeById(resumeId);
            Job job = jobService.getJobEntityById(jobId);
            
            Map<String, Object> result = aiService.calculateMatchScore(resume, job);
            
            return ResponseEntity.ok(createSuccessResponse("匹配度计算完成", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("匹配度计算失败", e.getMessage()));
        }
    }
    
    @PostMapping("/extract-skills")
    public ResponseEntity<?> extractSkills(@RequestBody Map<String, String> request) {
        try {
            String content = request.get("content");
            Map<String, Object> result = aiService.extractSkills(content);
            
            return ResponseEntity.ok(createSuccessResponse("技能提取完成", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("技能提取失败", e.getMessage()));
        }
    }
    
    @PostMapping("/analyze-experience")
    public ResponseEntity<?> analyzeExperience(@RequestBody Map<String, String> request) {
        try {
            String experienceText = request.get("experienceText");
            Map<String, Object> result = aiService.analyzeWorkExperience(experienceText);
            
            return ResponseEntity.ok(createSuccessResponse("工作经验分析完成", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("工作经验分析失败", e.getMessage()));
        }
    }
    
    @GetMapping("/candidate-profile/{resumeId}")
    public ResponseEntity<?> getCandidateProfile(@PathVariable Long resumeId) {
        try {
            Resume resume = resumeService.getResumeById(resumeId);
            Map<String, Object> result = aiService.generateCandidateProfile(resume);
            
            return ResponseEntity.ok(createSuccessResponse("候选人画像生成完成", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("候选人画像生成失败", e.getMessage()));
        }
    }
    
    @GetMapping("/recommend-jobs/{candidateId}")
    public ResponseEntity<?> recommendJobs(@PathVariable Long candidateId) {
        try {
            Map<String, Object> result = aiService.recommendJobs(candidateId);
            
            return ResponseEntity.ok(createSuccessResponse("职位推荐完成", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("职位推荐失败", e.getMessage()));
        }
    }
    
    @PostMapping("/batch-analyze")
    public ResponseEntity<?> batchAnalyze(@RequestBody Map<String, Object> request) {
        try {
            java.util.List<Long> resumeIds = (java.util.List<Long>) request.get("resumeIds");
            String analysisType = (String) request.get("analysisType");
            
            Map<String, Object> result = new HashMap<>();
            java.util.List<Map<String, Object>> analyses = new java.util.ArrayList<>();
            
            for (Long resumeId : resumeIds) {
                try {
                    Resume resume = resumeService.getResumeById(resumeId);
                    Map<String, Object> analysis = new HashMap<>();
                    analysis.put("resumeId", resumeId);
                    
                    if ("quality".equals(analysisType)) {
                        analysis.put("result", aiService.analyzeResumeQuality(resume.getParsedContent()));
                    } else if ("profile".equals(analysisType)) {
                        analysis.put("result", aiService.generateCandidateProfile(resume));
                    }
                    
                    analyses.add(analysis);
                } catch (Exception e) {
                    Map<String, Object> errorAnalysis = new HashMap<>();
                    errorAnalysis.put("resumeId", resumeId);
                    errorAnalysis.put("error", e.getMessage());
                    analyses.add(errorAnalysis);
                }
            }
            
            result.put("analyses", analyses);
            result.put("totalCount", resumeIds.size());
            result.put("successCount", analyses.stream().mapToInt(a -> a.containsKey("result") ? 1 : 0).sum());
            
            return ResponseEntity.ok(createSuccessResponse("批量分析完成", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("批量分析失败", e.getMessage()));
        }
    }
    
    @GetMapping("/analytics/summary")
    public ResponseEntity<?> getAnalyticsSummary() {
        try {
            Map<String, Object> summary = new HashMap<>();
            
            // 这里应该实现真实的统计逻辑
            summary.put("totalResumes", 156);
            summary.put("analyzedResumes", 142);
            summary.put("averageQualityScore", 75.6);
            summary.put("topSkills", java.util.Arrays.asList("Java", "Python", "React", "Spring", "MySQL"));
            summary.put("qualityDistribution", Map.of(
                "excellent", 45,
                "good", 67,
                "average", 30,
                "poor", 14
            ));
            
            return ResponseEntity.ok(createSuccessResponse("统计数据获取成功", summary));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("统计数据获取失败", e.getMessage()));
        }
    }
    
    private String saveUploadedFile(MultipartFile file) {
        // 这里应该实现实际的文件保存逻辑
        // 返回保存后的文件路径
        return "/uploads/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
    }
    
    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastDot = filename.lastIndexOf('.');
        return lastDot == -1 ? "" : filename.substring(lastDot + 1).toLowerCase();
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