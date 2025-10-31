package com.hrms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "interview_questions")
public class InterviewQuestion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(name = "question", nullable = false, columnDefinition = "TEXT")
    private String question; // 面试问题
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 30)
    private QuestionCategory category;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", length = 20)
    private DifficultyLevel difficulty;
    
    @Column(name = "department", length = 100)
    private String department; // 适用部门
    
    @Column(name = "position_level", length = 30)
    private String positionLevel; // 适用职级
    
    @Column(name = "skills", columnDefinition = "TEXT")
    private String skills; // 考察技能（JSON格式）
    
    @Column(name = "expected_answer", columnDefinition = "TEXT")
    private String expectedAnswer; // 期望答案/参考答案
    
    @Column(name = "scoring_criteria", columnDefinition = "TEXT")
    private String scoringCriteria; // 评分标准（JSON格式）
    
    @Column(name = "time_limit_minutes")
    private Integer timeLimitMinutes; // 答题时间限制（分钟）
    
    @Column(name = "tags", columnDefinition = "TEXT")
    private String tags; // 标签（JSON格式）
    
    @Column(name = "usage_count")
    private Integer usageCount = 0; // 使用次数
    
    @Column(name = "average_score")
    private Double averageScore; // 平均得分
    
    @Column(name = "is_active")
    private Boolean isActive = true; // 是否启用
    
    @Column(name = "created_by")
    private Long createdBy; // 创建者ID
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 问题描述/背景
    
    @Column(name = "weight")
    private Double weight = 1.0; // 权重
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 枚举类型定义
    public enum QuestionCategory {
        TECHNICAL,         // 技术问题
        BEHAVIORAL,        // 行为问题
        SITUATIONAL,       // 情景问题
        CASE_STUDY,        // 案例分析
        CODING,           // 编程题
        SYSTEM_DESIGN,    // 系统设计
        ALGORITHM,        // 算法题
        DATABASE,         // 数据库
        NETWORKING,       // 网络
        SECURITY,         // 安全
        PROJECT_MANAGEMENT, // 项目管理
        LEADERSHIP,       // 领导力
        COMMUNICATION,    // 沟通能力
        PROBLEM_SOLVING,  // 问题解决
        CREATIVITY,       // 创造力
        GENERAL          // 通用问题
    }
    
    public enum DifficultyLevel {
        EASY,      // 简单
        MEDIUM,    // 中等
        HARD,      // 困难
        EXPERT     // 专家级
    }
    
    // 构造函数
    public InterviewQuestion() {}
    
    public InterviewQuestion(String question, QuestionCategory category, DifficultyLevel difficulty) {
        this.question = question;
        this.category = category;
        this.difficulty = difficulty;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getQuestion() {
        return question;
    }
    
    public void setQuestion(String question) {
        this.question = question;
    }
    
    public QuestionCategory getCategory() {
        return category;
    }
    
    public void setCategory(QuestionCategory category) {
        this.category = category;
    }
    
    public DifficultyLevel getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(DifficultyLevel difficulty) {
        this.difficulty = difficulty;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getPositionLevel() {
        return positionLevel;
    }
    
    public void setPositionLevel(String positionLevel) {
        this.positionLevel = positionLevel;
    }
    
    public String getSkills() {
        return skills;
    }
    
    public void setSkills(String skills) {
        this.skills = skills;
    }
    
    public String getExpectedAnswer() {
        return expectedAnswer;
    }
    
    public void setExpectedAnswer(String expectedAnswer) {
        this.expectedAnswer = expectedAnswer;
    }
    
    public String getScoringCriteria() {
        return scoringCriteria;
    }
    
    public void setScoringCriteria(String scoringCriteria) {
        this.scoringCriteria = scoringCriteria;
    }
    
    public Integer getTimeLimitMinutes() {
        return timeLimitMinutes;
    }
    
    public void setTimeLimitMinutes(Integer timeLimitMinutes) {
        this.timeLimitMinutes = timeLimitMinutes;
    }
    
    public String getTags() {
        return tags;
    }
    
    public void setTags(String tags) {
        this.tags = tags;
    }
    
    public Integer getUsageCount() {
        return usageCount;
    }
    
    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }
    
    public Double getAverageScore() {
        return averageScore;
    }
    
    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Long getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Double getWeight() {
        return weight;
    }
    
    public void setWeight(Double weight) {
        this.weight = weight;
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