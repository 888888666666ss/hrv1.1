package com.hrms.controller;

import com.hrms.entity.AIEvaluationCriteria;
import com.hrms.entity.AIEvaluationHistory;
import com.hrms.entity.Resume;
import com.hrms.entity.Job;
import com.hrms.service.EnhancedAIService;
import com.hrms.service.ResumeService;
import com.hrms.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/enhanced-ai")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@Tag(name = "Enhanced AI Service", description = "增强AI评估服务API")
@Validated
public class EnhancedAIController {
    
    @Autowired
    private EnhancedAIService enhancedAIService;
    
    @Autowired
    private ResumeService resumeService;
    
    @Autowired
    private JobService jobService;
    
    // ================== 简历分析相关接口 ==================
    
    @PostMapping("/analyze-resume-quality")
    @Operation(summary = "增强版简历质量分析", description = "基于AI评估标准进行多维度简历质量分析")
    public ResponseEntity<Map<String, Object>> analyzeResumeQuality(
            @Parameter(description = "简历内容") @RequestBody String resumeContent,
            @Parameter(description = "行业类型") @RequestParam(required = false) String industry,
            @Parameter(description = "职级要求") @RequestParam(required = false) String jobLevel) {
        
        Map<String, Object> result = enhancedAIService.enhancedAnalyzeResumeQuality(resumeContent, industry, jobLevel);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/calculate-match-score")
    @Operation(summary = "多维度匹配度计算", description = "计算简历与职位的多维度匹配分数")
    public ResponseEntity<Map<String, Object>> calculateMatchScore(
            @Parameter(description = "简历ID") @RequestParam @NotNull Long resumeId,
            @Parameter(description = "职位ID") @RequestParam @NotNull Long jobId) {
        
        Resume resume = resumeService.getResumeById(resumeId);
        Job job = jobService.getJobById(jobId);
        
        if (resume == null || job == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Map<String, Object> result = enhancedAIService.enhancedCalculateMatchScore(resume, job);
        return ResponseEntity.ok(result);
    }
    
    // ================== 技能分析相关接口 ==================
    
    @PostMapping("/analyze-soft-skills")
    @Operation(summary = "软技能评估", description = "分析候选人的软技能水平")
    public ResponseEntity<Map<String, Object>> analyzeSoftSkills(
            @Parameter(description = "简历内容") @RequestBody String resumeContent) {
        
        Map<String, Object> result = enhancedAIService.analyzeSoftSkills(resumeContent);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/analyze-leadership")
    @Operation(summary = "领导力评估", description = "分析候选人的领导力水平")
    public ResponseEntity<Map<String, Object>> analyzeLeadership(
            @Parameter(description = "简历内容") @RequestBody String resumeContent,
            @Parameter(description = "职级要求") @RequestParam(required = false) String jobLevel) {
        
        Map<String, Object> result = enhancedAIService.analyzeLeadership(resumeContent, jobLevel);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/analyze-cultural-fit")
    @Operation(summary = "文化匹配度评估", description = "分析候选人与公司文化的匹配度")
    public ResponseEntity<Map<String, Object>> analyzeCulturalFit(
            @Parameter(description = "简历内容") @RequestBody String resumeContent,
            @Parameter(description = "公司价值观") @RequestParam(required = false) String companyValues) {
        
        Map<String, Object> result = enhancedAIService.analyzeCulturalFit(resumeContent, companyValues);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/analyze-language-skills")
    @Operation(summary = "语言能力评估", description = "分析候选人的语言技能水平")
    public ResponseEntity<Map<String, Object>> analyzeLanguageSkills(
            @Parameter(description = "简历内容") @RequestBody String resumeContent) {
        
        Map<String, Object> result = enhancedAIService.analyzeLanguageSkills(resumeContent);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/analyze-project-management")
    @Operation(summary = "项目管理能力评估", description = "分析候选人的项目管理能力")
    public ResponseEntity<Map<String, Object>> analyzeProjectManagement(
            @Parameter(description = "简历内容") @RequestBody String resumeContent) {
        
        Map<String, Object> result = enhancedAIService.analyzeProjectManagement(resumeContent);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/analyze-innovation")
    @Operation(summary = "创新能力评估", description = "分析候选人的创新能力")
    public ResponseEntity<Map<String, Object>> analyzeInnovation(
            @Parameter(description = "简历内容") @RequestBody String resumeContent) {
        
        Map<String, Object> result = enhancedAIService.analyzeInnovation(resumeContent);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/analyze-problem-solving")
    @Operation(summary = "问题解决能力评估", description = "分析候选人的问题解决能力")
    public ResponseEntity<Map<String, Object>> analyzeProblemSolving(
            @Parameter(description = "简历内容") @RequestBody String resumeContent) {
        
        Map<String, Object> result = enhancedAIService.analyzeProblemSolving(resumeContent);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/analyze-domain-knowledge")
    @Operation(summary = "行业专业知识评估", description = "分析候选人的行业专业知识水平")
    public ResponseEntity<Map<String, Object>> analyzeDomainKnowledge(
            @Parameter(description = "简历内容") @RequestBody String resumeContent,
            @Parameter(description = "行业类型") @RequestParam String industry) {
        
        Map<String, Object> result = enhancedAIService.analyzeDomainKnowledge(resumeContent, industry);
        return ResponseEntity.ok(result);
    }
    
    // ================== 批量处理相关接口 ==================
    
    @PostMapping("/batch-evaluate-and-rank")
    @Operation(summary = "批量候选人评估和排序", description = "批量评估多个候选人并进行排序")
    public ResponseEntity<List<Map<String, Object>>> batchEvaluateAndRank(
            @Parameter(description = "简历ID列表") @RequestBody List<Long> resumeIds,
            @Parameter(description = "职位ID") @RequestParam @NotNull Long jobId) {
        
        Job job = jobService.getJobById(jobId);
        if (job == null) {
            return ResponseEntity.badRequest().build();
        }
        
        List<Resume> resumes = resumeIds.stream()
                .map(resumeService::getResumeById)
                .filter(resume -> resume != null)
                .toList();
        
        List<Map<String, Object>> result = enhancedAIService.batchEvaluateAndRank(resumes, job);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/compare-resumes")
    @Operation(summary = "候选人对比分析", description = "对比分析多个候选人的优劣势")
    public ResponseEntity<Map<String, Object>> compareResumes(
            @Parameter(description = "简历ID列表") @RequestBody List<Long> resumeIds,
            @Parameter(description = "职位ID") @RequestParam @NotNull Long jobId) {
        
        Job job = jobService.getJobById(jobId);
        if (job == null) {
            return ResponseEntity.badRequest().build();
        }
        
        List<Resume> resumes = resumeIds.stream()
                .map(resumeService::getResumeById)
                .filter(resume -> resume != null)
                .toList();
        
        Map<String, Object> result = enhancedAIService.compareResumes(resumes, job);
        return ResponseEntity.ok(result);
    }
    
    // ================== 报告生成相关接口 ==================
    
    @GetMapping("/evaluation-report")
    @Operation(summary = "生成评估报告", description = "生成详细的候选人评估报告")
    public ResponseEntity<Map<String, Object>> generateEvaluationReport(
            @Parameter(description = "简历ID") @RequestParam @NotNull Long resumeId,
            @Parameter(description = "职位ID") @RequestParam(required = false) Long jobId) {
        
        Map<String, Object> result = enhancedAIService.generateEvaluationReport(resumeId, jobId);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/improvement-suggestions")
    @Operation(summary = "获取改进建议", description = "获取候选人技能改进建议")
    public ResponseEntity<List<String>> getImprovementSuggestions(
            @Parameter(description = "简历ID") @RequestParam @NotNull Long resumeId,
            @Parameter(description = "职位ID") @RequestParam @NotNull Long jobId) {
        
        Resume resume = resumeService.getResumeById(resumeId);
        Job job = jobService.getJobById(jobId);
        
        if (resume == null || job == null) {
            return ResponseEntity.badRequest().build();
        }
        
        List<String> result = enhancedAIService.getImprovementSuggestions(resume, job);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/predict-success-probability")
    @Operation(summary = "预测成功概率", description = "预测候选人在该职位的成功概率")
    public ResponseEntity<Map<String, Object>> predictSuccessProbability(
            @Parameter(description = "简历ID") @RequestParam @NotNull Long resumeId,
            @Parameter(description = "职位ID") @RequestParam @NotNull Long jobId) {
        
        Resume resume = resumeService.getResumeById(resumeId);
        Job job = jobService.getJobById(jobId);
        
        if (resume == null || job == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Map<String, Object> result = enhancedAIService.predictSuccessProbability(resume, job);
        return ResponseEntity.ok(result);
    }
    
    // ================== 评估标准管理接口 ==================
    
    @PostMapping("/evaluation-criteria")
    @Operation(summary = "创建评估标准", description = "创建新的AI评估标准")
    public ResponseEntity<AIEvaluationCriteria> createEvaluationCriteria(
            @Parameter(description = "评估标准信息") @RequestBody @Valid AIEvaluationCriteria criteria) {
        
        AIEvaluationCriteria result = enhancedAIService.createEvaluationCriteria(criteria);
        return ResponseEntity.ok(result);
    }
    
    @PutMapping("/evaluation-criteria/{id}")
    @Operation(summary = "更新评估标准", description = "更新现有的AI评估标准")
    public ResponseEntity<AIEvaluationCriteria> updateEvaluationCriteria(
            @Parameter(description = "评估标准ID") @PathVariable @NotNull Long id,
            @Parameter(description = "更新的评估标准信息") @RequestBody @Valid AIEvaluationCriteria criteria) {
        
        AIEvaluationCriteria result = enhancedAIService.updateEvaluationCriteria(id, criteria);
        return ResponseEntity.ok(result);
    }
    
    @DeleteMapping("/evaluation-criteria/{id}")
    @Operation(summary = "删除评估标准", description = "删除指定的AI评估标准")
    public ResponseEntity<Void> deleteEvaluationCriteria(
            @Parameter(description = "评估标准ID") @PathVariable @NotNull Long id) {
        
        enhancedAIService.deleteEvaluationCriteria(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/evaluation-criteria/category/{category}")
    @Operation(summary = "按类别获取评估标准", description = "根据类别获取评估标准列表")
    public ResponseEntity<List<AIEvaluationCriteria>> getEvaluationCriteriaByCategory(
            @Parameter(description = "评估类别") @PathVariable AIEvaluationCriteria.CriteriaCategory category) {
        
        List<AIEvaluationCriteria> result = enhancedAIService.getEvaluationCriteriaByCategory(category);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/evaluation-criteria/industry/{industry}")
    @Operation(summary = "按行业获取评估标准", description = "根据行业获取评估标准列表")
    public ResponseEntity<List<AIEvaluationCriteria>> getEvaluationCriteriaByIndustry(
            @Parameter(description = "行业类型") @PathVariable String industry) {
        
        List<AIEvaluationCriteria> result = enhancedAIService.getEvaluationCriteriaByIndustry(industry);
        return ResponseEntity.ok(result);
    }
    
    // ================== 评估历史管理接口 ==================
    
    @PostMapping("/evaluation-history")
    @Operation(summary = "保存评估历史", description = "保存AI评估历史记录")
    public ResponseEntity<AIEvaluationHistory> saveEvaluationHistory(
            @Parameter(description = "评估历史记录") @RequestBody @Valid AIEvaluationHistory history) {
        
        AIEvaluationHistory result = enhancedAIService.saveEvaluationHistory(history);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/evaluation-history/resume/{resumeId}")
    @Operation(summary = "获取简历评估历史", description = "获取指定简历的评估历史记录")
    public ResponseEntity<List<AIEvaluationHistory>> getEvaluationHistoryByResumeId(
            @Parameter(description = "简历ID") @PathVariable @NotNull Long resumeId) {
        
        List<AIEvaluationHistory> result = enhancedAIService.getEvaluationHistoryByResumeId(resumeId);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/evaluation-history/job/{jobId}")
    @Operation(summary = "获取职位评估历史", description = "获取指定职位的评估历史记录")
    public ResponseEntity<List<AIEvaluationHistory>> getEvaluationHistoryByJobId(
            @Parameter(description = "职位ID") @PathVariable @NotNull Long jobId) {
        
        List<AIEvaluationHistory> result = enhancedAIService.getEvaluationHistoryByJobId(jobId);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/evaluation-statistics")
    @Operation(summary = "获取评估统计数据", description = "获取指定时间段的评估统计数据")
    public ResponseEntity<Map<String, Object>> getEvaluationStatistics(
            @Parameter(description = "统计周期") @RequestParam(defaultValue = "month") String period) {
        
        Map<String, Object> result = enhancedAIService.getEvaluationStatistics(period);
        return ResponseEntity.ok(result);
    }
    
    // ================== 高级分析功能接口 ==================
    
    @PostMapping("/calculate-adaptive-weights")
    @Operation(summary = "计算自适应权重", description = "根据职位和行业特点计算动态权重")
    public ResponseEntity<Map<String, Double>> calculateAdaptiveWeights(
            @Parameter(description = "职位ID") @RequestParam @NotNull Long jobId,
            @Parameter(description = "行业类型") @RequestParam String industry) {
        
        Job job = jobService.getJobById(jobId);
        if (job == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Map<String, Double> result = enhancedAIService.calculateAdaptiveWeights(job, industry);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/calibrate-scoring")
    @Operation(summary = "评分校准", description = "基于历史数据进行机器学习评分校准")
    public ResponseEntity<Map<String, Object>> calibrateScoring(
            @Parameter(description = "简历ID列表") @RequestBody List<Long> resumeIds) {
        
        List<AIEvaluationHistory> historicalData = resumeIds.stream()
                .flatMap(resumeId -> enhancedAIService.getEvaluationHistoryByResumeId(resumeId).stream())
                .toList();
        
        Map<String, Object> result = enhancedAIService.calibrateScoring(historicalData);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/candidate-profile/{resumeId}")
    @Operation(summary = "生成候选人画像", description = "生成候选人的综合能力画像")
    public ResponseEntity<Map<String, Object>> generateCandidateProfile(
            @Parameter(description = "简历ID") @PathVariable @NotNull Long resumeId) {
        
        Resume resume = resumeService.getResumeById(resumeId);
        if (resume == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Map<String, Object> result = enhancedAIService.generateCandidateProfile(resume);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/analyze-skill-gaps")
    @Operation(summary = "技能差距分析", description = "分析候选人技能与职位要求的差距")
    public ResponseEntity<Map<String, Object>> analyzeSkillGaps(
            @Parameter(description = "简历ID") @RequestParam @NotNull Long resumeId,
            @Parameter(description = "职位ID") @RequestParam @NotNull Long jobId) {
        
        Resume resume = resumeService.getResumeById(resumeId);
        Job job = jobService.getJobById(jobId);
        
        if (resume == null || job == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Map<String, Object> result = enhancedAIService.analyzeSkillGaps(resume, job);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/career-path-suggestion/{resumeId}")
    @Operation(summary = "职业发展路径建议", description = "为候选人提供职业发展路径建议")
    public ResponseEntity<Map<String, Object>> suggestCareerPath(
            @Parameter(description = "简历ID") @PathVariable @NotNull Long resumeId) {
        
        Resume resume = resumeService.getResumeById(resumeId);
        if (resume == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Map<String, Object> result = enhancedAIService.suggestCareerPath(resume);
        return ResponseEntity.ok(result);
    }
}