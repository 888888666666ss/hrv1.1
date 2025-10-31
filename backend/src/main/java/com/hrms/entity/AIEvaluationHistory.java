package com.hrms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_evaluation_history")
public class AIEvaluationHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(name = "resume_id", nullable = false)
    private Long resumeId;
    
    @Column(name = "job_id")
    private Long jobId; // 可选，如果是针对特定职位的评估
    
    @Enumerated(EnumType.STRING)
    @Column(name = "evaluation_type", length = 50)
    private EvaluationType evaluationType;
    
    @Column(name = "overall_score", nullable = false)
    private Integer overallScore;
    
    @Column(name = "technical_score")
    private Integer technicalScore;
    
    @Column(name = "soft_skills_score")
    private Integer softSkillsScore;
    
    @Column(name = "experience_score")
    private Integer experienceScore;
    
    @Column(name = "education_score")
    private Integer educationScore;
    
    @Column(name = "cultural_fit_score")
    private Integer culturalFitScore;
    
    @Column(name = "detailed_scores", columnDefinition = "TEXT")
    private String detailedScores; // JSON格式详细评分
    
    @Column(name = "strengths", columnDefinition = "TEXT")
    private String strengths; // JSON格式优势分析
    
    @Column(name = "weaknesses", columnDefinition = "TEXT")
    private String weaknesses; // JSON格式劣势分析
    
    @Column(name = "recommendations", columnDefinition = "TEXT")
    private String recommendations; // JSON格式建议
    
    @Column(name = "ai_model_version", length = 50)
    private String aiModelVersion;
    
    @Column(name = "evaluation_criteria_snapshot", columnDefinition = "TEXT")
    private String evaluationCriteriaSnapshot; // 评估时使用的标准快照
    
    @Column(name = "processing_time_ms")
    private Long processingTimeMs;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    // 枚举类型
    public enum EvaluationType {
        GENERAL,           // 通用评估
        JOB_SPECIFIC,      // 针对特定职位的评估
        BATCH_SCREENING,   // 批量筛选
        DETAILED_ANALYSIS, // 详细分析
        COMPARATIVE       // 对比分析
    }
    
    // 构造函数
    public AIEvaluationHistory() {}
    
    public AIEvaluationHistory(Long resumeId, EvaluationType evaluationType, Integer overallScore) {
        this.resumeId = resumeId;
        this.evaluationType = evaluationType;
        this.overallScore = overallScore;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getResumeId() {
        return resumeId;
    }
    
    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }
    
    public Long getJobId() {
        return jobId;
    }
    
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
    
    public EvaluationType getEvaluationType() {
        return evaluationType;
    }
    
    public void setEvaluationType(EvaluationType evaluationType) {
        this.evaluationType = evaluationType;
    }
    
    public Integer getOverallScore() {
        return overallScore;
    }
    
    public void setOverallScore(Integer overallScore) {
        this.overallScore = overallScore;
    }
    
    public Integer getTechnicalScore() {
        return technicalScore;
    }
    
    public void setTechnicalScore(Integer technicalScore) {
        this.technicalScore = technicalScore;
    }
    
    public Integer getSoftSkillsScore() {
        return softSkillsScore;
    }
    
    public void setSoftSkillsScore(Integer softSkillsScore) {
        this.softSkillsScore = softSkillsScore;
    }
    
    public Integer getExperienceScore() {
        return experienceScore;
    }
    
    public void setExperienceScore(Integer experienceScore) {
        this.experienceScore = experienceScore;
    }
    
    public Integer getEducationScore() {
        return educationScore;
    }
    
    public void setEducationScore(Integer educationScore) {
        this.educationScore = educationScore;
    }
    
    public Integer getCulturalFitScore() {
        return culturalFitScore;
    }
    
    public void setCulturalFitScore(Integer culturalFitScore) {
        this.culturalFitScore = culturalFitScore;
    }
    
    public String getDetailedScores() {
        return detailedScores;
    }
    
    public void setDetailedScores(String detailedScores) {
        this.detailedScores = detailedScores;
    }
    
    public String getStrengths() {
        return strengths;
    }
    
    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }
    
    public String getWeaknesses() {
        return weaknesses;
    }
    
    public void setWeaknesses(String weaknesses) {
        this.weaknesses = weaknesses;
    }
    
    public String getRecommendations() {
        return recommendations;
    }
    
    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }
    
    public String getAiModelVersion() {
        return aiModelVersion;
    }
    
    public void setAiModelVersion(String aiModelVersion) {
        this.aiModelVersion = aiModelVersion;
    }
    
    public String getEvaluationCriteriaSnapshot() {
        return evaluationCriteriaSnapshot;
    }
    
    public void setEvaluationCriteriaSnapshot(String evaluationCriteriaSnapshot) {
        this.evaluationCriteriaSnapshot = evaluationCriteriaSnapshot;
    }
    
    public Long getProcessingTimeMs() {
        return processingTimeMs;
    }
    
    public void setProcessingTimeMs(Long processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}