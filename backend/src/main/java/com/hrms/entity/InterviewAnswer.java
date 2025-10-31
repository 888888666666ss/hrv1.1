package com.hrms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "interview_answers")
public class InterviewAnswer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(name = "interview_id", nullable = false)
    private Long interviewId;
    
    @NotNull
    @Column(name = "question_id", nullable = false)
    private Long questionId;
    
    @Column(name = "answer", columnDefinition = "TEXT")
    private String answer; // 候选人回答
    
    @Column(name = "score")
    private Integer score; // 得分 (0-100)
    
    @Column(name = "time_taken_minutes")
    private Integer timeTakenMinutes; // 实际用时（分钟）
    
    @Column(name = "evaluator_notes", columnDefinition = "TEXT")
    private String evaluatorNotes; // 评估者备注
    
    @Column(name = "strengths", columnDefinition = "TEXT")
    private String strengths; // 回答亮点
    
    @Column(name = "weaknesses", columnDefinition = "TEXT")
    private String weaknesses; // 回答不足
    
    @Enumerated(EnumType.STRING)
    @Column(name = "rating", length = 20)
    private AnswerRating rating; // 评级
    
    @Column(name = "follow_up_questions", columnDefinition = "TEXT")
    private String followUpQuestions; // 追问问题
    
    @Column(name = "keywords_mentioned", columnDefinition = "TEXT")
    private String keywordsMentioned; // 提到的关键词（JSON格式）
    
    @Column(name = "confidence_level")
    private Integer confidenceLevel; // 回答自信度 (1-10)
    
    @Column(name = "communication_quality")
    private Integer communicationQuality; // 表达质量 (1-10)
    
    @Column(name = "depth_of_knowledge")
    private Integer depthOfKnowledge; // 知识深度 (1-10)
    
    @Column(name = "practical_experience")
    private Integer practicalExperience; // 实践经验体现 (1-10)
    
    @Column(name = "ai_analysis", columnDefinition = "TEXT")
    private String aiAnalysis; // AI分析结果（JSON格式）
    
    @Column(name = "recorded_audio_path", length = 500)
    private String recordedAudioPath; // 录音文件路径
    
    @Column(name = "transcription", columnDefinition = "TEXT")
    private String transcription; // 语音转文字结果
    
    @Column(name = "evaluated_by")
    private Long evaluatedBy; // 评估者ID
    
    @Column(name = "evaluation_completed")
    private Boolean evaluationCompleted = false; // 评估是否完成
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 枚举类型定义
    public enum AnswerRating {
        EXCELLENT,    // 优秀
        GOOD,         // 良好
        SATISFACTORY, // 满意
        NEEDS_IMPROVEMENT, // 需要改进
        POOR          // 较差
    }
    
    // 构造函数
    public InterviewAnswer() {}
    
    public InterviewAnswer(Long interviewId, Long questionId) {
        this.interviewId = interviewId;
        this.questionId = questionId;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getInterviewId() {
        return interviewId;
    }
    
    public void setInterviewId(Long interviewId) {
        this.interviewId = interviewId;
    }
    
    public Long getQuestionId() {
        return questionId;
    }
    
    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
    
    public String getAnswer() {
        return answer;
    }
    
    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
    public Integer getScore() {
        return score;
    }
    
    public void setScore(Integer score) {
        this.score = score;
    }
    
    public Integer getTimeTakenMinutes() {
        return timeTakenMinutes;
    }
    
    public void setTimeTakenMinutes(Integer timeTakenMinutes) {
        this.timeTakenMinutes = timeTakenMinutes;
    }
    
    public String getEvaluatorNotes() {
        return evaluatorNotes;
    }
    
    public void setEvaluatorNotes(String evaluatorNotes) {
        this.evaluatorNotes = evaluatorNotes;
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
    
    public AnswerRating getRating() {
        return rating;
    }
    
    public void setRating(AnswerRating rating) {
        this.rating = rating;
    }
    
    public String getFollowUpQuestions() {
        return followUpQuestions;
    }
    
    public void setFollowUpQuestions(String followUpQuestions) {
        this.followUpQuestions = followUpQuestions;
    }
    
    public String getKeywordsMentioned() {
        return keywordsMentioned;
    }
    
    public void setKeywordsMentioned(String keywordsMentioned) {
        this.keywordsMentioned = keywordsMentioned;
    }
    
    public Integer getConfidenceLevel() {
        return confidenceLevel;
    }
    
    public void setConfidenceLevel(Integer confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }
    
    public Integer getCommunicationQuality() {
        return communicationQuality;
    }
    
    public void setCommunicationQuality(Integer communicationQuality) {
        this.communicationQuality = communicationQuality;
    }
    
    public Integer getDepthOfKnowledge() {
        return depthOfKnowledge;
    }
    
    public void setDepthOfKnowledge(Integer depthOfKnowledge) {
        this.depthOfKnowledge = depthOfKnowledge;
    }
    
    public Integer getPracticalExperience() {
        return practicalExperience;
    }
    
    public void setPracticalExperience(Integer practicalExperience) {
        this.practicalExperience = practicalExperience;
    }
    
    public String getAiAnalysis() {
        return aiAnalysis;
    }
    
    public void setAiAnalysis(String aiAnalysis) {
        this.aiAnalysis = aiAnalysis;
    }
    
    public String getRecordedAudioPath() {
        return recordedAudioPath;
    }
    
    public void setRecordedAudioPath(String recordedAudioPath) {
        this.recordedAudioPath = recordedAudioPath;
    }
    
    public String getTranscription() {
        return transcription;
    }
    
    public void setTranscription(String transcription) {
        this.transcription = transcription;
    }
    
    public Long getEvaluatedBy() {
        return evaluatedBy;
    }
    
    public void setEvaluatedBy(Long evaluatedBy) {
        this.evaluatedBy = evaluatedBy;
    }
    
    public Boolean getEvaluationCompleted() {
        return evaluationCompleted;
    }
    
    public void setEvaluationCompleted(Boolean evaluationCompleted) {
        this.evaluationCompleted = evaluationCompleted;
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