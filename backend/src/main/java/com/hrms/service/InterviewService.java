package com.hrms.service;

import com.hrms.entity.Interview;
import com.hrms.entity.InterviewQuestion;
import com.hrms.entity.InterviewAnswer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface InterviewService {
    
    /**
     * 面试安排管理
     */
    Interview scheduleInterview(Interview interview);
    Interview updateInterview(Long id, Interview interview);
    void cancelInterview(Long id, String reason);
    Interview rescheduleInterview(Long id, LocalDateTime newTime);
    Interview getInterviewById(Long id);
    List<Interview> getInterviewsByJobId(Long jobId);
    List<Interview> getInterviewsByCandidateId(Long candidateId);
    List<Interview> getInterviewsByInterviewerId(Long interviewerId);
    
    /**
     * 面试状态管理
     */
    Interview startInterview(Long id);
    Interview completeInterview(Long id);
    Interview confirmInterview(Long id, boolean candidateConfirmed, boolean interviewerConfirmed);
    Interview markNoShow(Long id);
    
    /**
     * 面试调度和冲突检测
     */
    List<Interview> checkScheduleConflicts(Long interviewerId, Long candidateId, LocalDateTime startTime, LocalDateTime endTime);
    boolean hasScheduleConflict(Long interviewerId, Long candidateId, LocalDateTime startTime, LocalDateTime endTime);
    List<Interview> getAvailableTimeSlots(Long interviewerId, LocalDateTime date);
    Map<String, Object> suggestAlternativeTimeSlots(Long interviewerId, Long candidateId, LocalDateTime preferredTime);
    
    /**
     * 面试日程管理
     */
    List<Interview> getTodayInterviews();
    List<Interview> getTomorrowInterviews();
    List<Interview> getThisWeekInterviews();
    List<Interview> getInterviewsByDateRange(LocalDateTime startTime, LocalDateTime endTime);
    Map<String, Object> getInterviewerSchedule(Long interviewerId, LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * 面试评估管理
     */
    Interview evaluateInterview(Long id, Map<String, Object> evaluation);
    Interview addInterviewFeedback(Long id, String feedback, String recommendation);
    Interview updateInterviewScores(Long id, Map<String, Integer> scores);
    Map<String, Object> getInterviewEvaluation(Long id);
    
    /**
     * 面试提醒和通知
     */
    List<Interview> getInterviewsNeedingReminder(int hoursBeforeInterview);
    void sendInterviewReminders();
    void sendInterviewInvitation(Long interviewId);
    void sendRescheduleNotification(Long interviewId);
    void sendCancellationNotification(Long interviewId);
    
    /**
     * 面试统计和分析
     */
    Map<String, Object> getInterviewStatistics(LocalDateTime startDate, LocalDateTime endDate);
    Map<String, Object> getInterviewerPerformanceStats(Long interviewerId);
    Map<String, Object> getCandidateInterviewHistory(Long candidateId);
    Double getInterviewPassRate(LocalDateTime startDate, LocalDateTime endDate);
    Map<String, Object> getInterviewAnalytics();
    
    /**
     * 批量操作
     */
    List<Interview> batchScheduleInterviews(List<Interview> interviews);
    void batchUpdateInterviewStatus(List<Long> interviewIds, Interview.InterviewStatus status);
    void batchCancelInterviews(List<Long> interviewIds, String reason);
    
    /**
     * 面试流程管理
     */
    Interview scheduleNextRound(Long currentInterviewId, Interview.InterviewType nextType);
    List<Interview> getInterviewFlow(Long candidateId, Long jobId);
    boolean canProceedToNextRound(Long interviewId);
    Map<String, Object> getInterviewProgress(Long candidateId, Long jobId);
}