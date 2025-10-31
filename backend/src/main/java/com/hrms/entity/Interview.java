package com.hrms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "interviews")
public class Interview {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(name = "job_id", nullable = false)
    private Long jobId;
    
    @NotNull
    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;
    
    @NotNull
    @Column(name = "interviewer_id", nullable = false)
    private Long interviewerId; // 主面试官ID
    
    @Column(name = "panel_members")
    private String panelMembers; // JSON格式存储面试官团队
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 30)
    private InterviewType type;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "round", length = 20)
    private InterviewRound round;
    
    @NotNull
    @Column(name = "scheduled_time", nullable = false)
    private LocalDateTime scheduledTime;
    
    @Column(name = "actual_start_time")
    private LocalDateTime actualStartTime;
    
    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;
    
    @Column(name = "duration_minutes")
    private Integer durationMinutes; // 预计面试时长（分钟）
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private InterviewStatus status = InterviewStatus.SCHEDULED;
    
    @Column(name = "location", length = 200)
    private String location; // 面试地点
    
    @Column(name = "meeting_link", length = 500)
    private String meetingLink; // 在线面试链接
    
    @Column(name = "meeting_password", length = 50)
    private String meetingPassword; // 会议密码
    
    @Enumerated(EnumType.STRING)
    @Column(name = "mode", length = 20)
    private InterviewMode mode = InterviewMode.IN_PERSON;
    
    @Column(name = "agenda", columnDefinition = "TEXT")
    private String agenda; // 面试议程
    
    @Column(name = "requirements", columnDefinition = "TEXT")
    private String requirements; // 面试要求
    
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes; // 面试备注
    
    // 评估结果相关字段
    @Column(name = "overall_score")
    private Integer overallScore; // 总评分 (0-100)
    
    @Column(name = "technical_score")
    private Integer technicalScore; // 技术评分
    
    @Column(name = "communication_score")
    private Integer communicationScore; // 沟通评分
    
    @Column(name = "cultural_fit_score")
    private Integer culturalFitScore; // 文化匹配度评分
    
    @Column(name = "problem_solving_score")
    private Integer problemSolvingScore; // 问题解决能力评分
    
    @Column(name = "strengths", columnDefinition = "TEXT")
    private String strengths; // 优势（JSON格式）
    
    @Column(name = "weaknesses", columnDefinition = "TEXT")
    private String weaknesses; // 劣势（JSON格式）
    
    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback; // 面试反馈
    
    @Column(name = "recommendation", length = 50)
    private String recommendation; // 推荐决定：HIRE, REJECT, HOLD, NEXT_ROUND
    
    @Column(name = "next_round_type", length = 30)
    private String nextRoundType; // 下一轮面试类型
    
    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason; // 拒绝原因
    
    // 提醒相关字段
    @Column(name = "reminder_sent")
    private Boolean reminderSent = false; // 是否已发送提醒
    
    @Column(name = "candidate_confirmed")
    private Boolean candidateConfirmed = false; // 候选人是否已确认
    
    @Column(name = "interviewer_confirmed")
    private Boolean interviewerConfirmed = false; // 面试官是否已确认
    
    @Column(name = "reschedule_count")
    private Integer rescheduleCount = 0; // 改期次数
    
    @Column(name = "last_rescheduled_at")
    private LocalDateTime lastRescheduledAt; // 最后改期时间
    
    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason; // 取消原因
    
    @Column(name = "evaluation_completed")
    private Boolean evaluationCompleted = false; // 评估是否完成
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 枚举类型定义
    public enum InterviewType {
        PHONE_SCREENING,    // 电话初筛
        VIDEO_INTERVIEW,    // 视频面试
        TECHNICAL,          // 技术面试
        BEHAVIORAL,         // 行为面试
        CASE_STUDY,         // 案例分析
        PRESENTATION,       // 演示面试
        GROUP_INTERVIEW,    // 群面
        FINAL,             // 终面
        HR_INTERVIEW,      // HR面试
        MANAGER_INTERVIEW  // 管理层面试
    }
    
    public enum InterviewRound {
        FIRST,    // 初试
        SECOND,   // 复试
        THIRD,    // 三试
        FINAL     // 终试
    }
    
    public enum InterviewStatus {
        SCHEDULED,    // 已安排
        CONFIRMED,    // 已确认
        IN_PROGRESS,  // 进行中
        COMPLETED,    // 已完成
        CANCELLED,    // 已取消
        NO_SHOW,      // 未出席
        RESCHEDULED   // 已改期
    }
    
    public enum InterviewMode {
        IN_PERSON,    // 现场面试
        VIDEO_CALL,   // 视频通话
        PHONE_CALL,   // 电话面试
        HYBRID        // 混合模式
    }
    
    // 构造函数
    public Interview() {}
    
    public Interview(Long jobId, Long candidateId, Long interviewerId, InterviewType type, 
                    LocalDateTime scheduledTime, InterviewMode mode) {
        this.jobId = jobId;
        this.candidateId = candidateId;
        this.interviewerId = interviewerId;
        this.type = type;
        this.scheduledTime = scheduledTime;
        this.mode = mode;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getJobId() {
        return jobId;
    }
    
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
    
    public Long getCandidateId() {
        return candidateId;
    }
    
    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }
    
    public Long getInterviewerId() {
        return interviewerId;
    }
    
    public void setInterviewerId(Long interviewerId) {
        this.interviewerId = interviewerId;
    }
    
    public String getPanelMembers() {
        return panelMembers;
    }
    
    public void setPanelMembers(String panelMembers) {
        this.panelMembers = panelMembers;
    }
    
    public InterviewType getType() {
        return type;
    }
    
    public void setType(InterviewType type) {
        this.type = type;
    }
    
    public InterviewRound getRound() {
        return round;
    }
    
    public void setRound(InterviewRound round) {
        this.round = round;
    }
    
    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }
    
    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }
    
    public LocalDateTime getActualStartTime() {
        return actualStartTime;
    }
    
    public void setActualStartTime(LocalDateTime actualStartTime) {
        this.actualStartTime = actualStartTime;
    }
    
    public LocalDateTime getActualEndTime() {
        return actualEndTime;
    }
    
    public void setActualEndTime(LocalDateTime actualEndTime) {
        this.actualEndTime = actualEndTime;
    }
    
    public Integer getDurationMinutes() {
        return durationMinutes;
    }
    
    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }
    
    public InterviewStatus getStatus() {
        return status;
    }
    
    public void setStatus(InterviewStatus status) {
        this.status = status;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getMeetingLink() {
        return meetingLink;
    }
    
    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }
    
    public String getMeetingPassword() {
        return meetingPassword;
    }
    
    public void setMeetingPassword(String meetingPassword) {
        this.meetingPassword = meetingPassword;
    }
    
    public InterviewMode getMode() {
        return mode;
    }
    
    public void setMode(InterviewMode mode) {
        this.mode = mode;
    }
    
    public String getAgenda() {
        return agenda;
    }
    
    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }
    
    public String getRequirements() {
        return requirements;
    }
    
    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
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
    
    public Integer getCommunicationScore() {
        return communicationScore;
    }
    
    public void setCommunicationScore(Integer communicationScore) {
        this.communicationScore = communicationScore;
    }
    
    public Integer getCulturalFitScore() {
        return culturalFitScore;
    }
    
    public void setCulturalFitScore(Integer culturalFitScore) {
        this.culturalFitScore = culturalFitScore;
    }
    
    public Integer getProblemSolvingScore() {
        return problemSolvingScore;
    }
    
    public void setProblemSolvingScore(Integer problemSolvingScore) {
        this.problemSolvingScore = problemSolvingScore;
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
    
    public String getFeedback() {
        return feedback;
    }
    
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    
    public String getRecommendation() {
        return recommendation;
    }
    
    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
    
    public String getNextRoundType() {
        return nextRoundType;
    }
    
    public void setNextRoundType(String nextRoundType) {
        this.nextRoundType = nextRoundType;
    }
    
    public String getRejectionReason() {
        return rejectionReason;
    }
    
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
    
    public Boolean getReminderSent() {
        return reminderSent;
    }
    
    public void setReminderSent(Boolean reminderSent) {
        this.reminderSent = reminderSent;
    }
    
    public Boolean getCandidateConfirmed() {
        return candidateConfirmed;
    }
    
    public void setCandidateConfirmed(Boolean candidateConfirmed) {
        this.candidateConfirmed = candidateConfirmed;
    }
    
    public Boolean getInterviewerConfirmed() {
        return interviewerConfirmed;
    }
    
    public void setInterviewerConfirmed(Boolean interviewerConfirmed) {
        this.interviewerConfirmed = interviewerConfirmed;
    }
    
    public Integer getRescheduleCount() {
        return rescheduleCount;
    }
    
    public void setRescheduleCount(Integer rescheduleCount) {
        this.rescheduleCount = rescheduleCount;
    }
    
    public LocalDateTime getLastRescheduledAt() {
        return lastRescheduledAt;
    }
    
    public void setLastRescheduledAt(LocalDateTime lastRescheduledAt) {
        this.lastRescheduledAt = lastRescheduledAt;
    }
    
    public String getCancellationReason() {
        return cancellationReason;
    }
    
    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
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