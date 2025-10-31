package com.hrms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ai_evaluation_criteria")
public class AIEvaluationCriteria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(name = "name", nullable = false, length = 100)
    private String name; // 评估标准名称
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 50)
    private CriteriaCategory category; // 评估类别
    
    @Column(name = "industry", length = 50)
    private String industry; // 适用行业
    
    @Column(name = "job_level", length = 30)
    private String jobLevel; // 适用职级
    
    @Column(name = "weight", nullable = false)
    private Double weight = 1.0; // 权重
    
    @Column(name = "keywords", columnDefinition = "TEXT")
    private String keywords; // 关键词（JSON格式）
    
    @Column(name = "scoring_rules", columnDefinition = "TEXT")
    private String scoringRules; // 评分规则（JSON格式）
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 说明
    
    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled = true;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 枚举类型
    public enum CriteriaCategory {
        TECHNICAL_SKILLS,    // 技术技能
        SOFT_SKILLS,         // 软技能
        EXPERIENCE,          // 工作经验
        EDUCATION,           // 教育背景
        LEADERSHIP,          // 领导力
        COMMUNICATION,       // 沟通能力
        PROBLEM_SOLVING,     // 问题解决能力
        TEAMWORK,           // 团队协作
        INNOVATION,         // 创新能力
        CULTURAL_FIT,       // 文化匹配度
        CERTIFICATION,      // 资质认证
        LANGUAGE,           // 语言能力
        DOMAIN_KNOWLEDGE,   // 领域知识
        PROJECT_MANAGEMENT, // 项目管理
        CUSTOMER_SERVICE    // 客户服务
    }
    
    // 构造函数
    public AIEvaluationCriteria() {}
    
    public AIEvaluationCriteria(String name, CriteriaCategory category, String industry, Double weight) {
        this.name = name;
        this.category = category;
        this.industry = industry;
        this.weight = weight;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public CriteriaCategory getCategory() {
        return category;
    }
    
    public void setCategory(CriteriaCategory category) {
        this.category = category;
    }
    
    public String getIndustry() {
        return industry;
    }
    
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    
    public String getJobLevel() {
        return jobLevel;
    }
    
    public void setJobLevel(String jobLevel) {
        this.jobLevel = jobLevel;
    }
    
    public Double getWeight() {
        return weight;
    }
    
    public void setWeight(Double weight) {
        this.weight = weight;
    }
    
    public String getKeywords() {
        return keywords;
    }
    
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    
    public String getScoringRules() {
        return scoringRules;
    }
    
    public void setScoringRules(String scoringRules) {
        this.scoringRules = scoringRules;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Boolean getIsEnabled() {
        return isEnabled;
    }
    
    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}