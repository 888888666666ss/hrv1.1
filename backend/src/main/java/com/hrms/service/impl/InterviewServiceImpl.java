package com.hrms.service.impl;

import com.hrms.entity.Interview;
import com.hrms.entity.Job;
import com.hrms.entity.Candidate;
import com.hrms.repository.InterviewRepository;
import com.hrms.service.InterviewService;
import com.hrms.service.JobService;
import com.hrms.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
public class InterviewServiceImpl implements InterviewService {
    
    @Autowired
    private InterviewRepository interviewRepository;
    
    @Autowired
    private JobService jobService;
    
    @Autowired
    private CandidateService candidateService;
    
    @Override
    public Interview scheduleInterview(Interview interview) {
        // 验证时间冲突
        if (hasScheduleConflict(interview.getInterviewerId(), interview.getCandidateId(), 
                              interview.getScheduledTime(), 
                              interview.getScheduledTime().plusMinutes(interview.getDurationMinutes() != null ? interview.getDurationMinutes() : 60))) {
            throw new RuntimeException("面试时间冲突，请选择其他时间");
        }
        
        interview.setStatus(Interview.InterviewStatus.SCHEDULED);
        interview.setRescheduleCount(0);
        return interviewRepository.save(interview);
    }
    
    @Override
    public Interview updateInterview(Long id, Interview interview) {
        Interview existingInterview = getInterviewById(id);
        
        // 保留原有状态信息
        interview.setId(id);
        interview.setCreatedAt(existingInterview.getCreatedAt());
        
        // 如果时间改变，检查冲突
        if (!existingInterview.getScheduledTime().equals(interview.getScheduledTime())) {
            if (hasScheduleConflict(interview.getInterviewerId(), interview.getCandidateId(), 
                                  interview.getScheduledTime(), 
                                  interview.getScheduledTime().plusMinutes(interview.getDurationMinutes() != null ? interview.getDurationMinutes() : 60))) {
                throw new RuntimeException("面试时间冲突，请选择其他时间");
            }
        }
        
        return interviewRepository.save(interview);
    }
    
    @Override
    public void cancelInterview(Long id, String reason) {
        Interview interview = getInterviewById(id);
        interview.setStatus(Interview.InterviewStatus.CANCELLED);
        interview.setCancellationReason(reason);
        interviewRepository.save(interview);
    }
    
    @Override
    public Interview rescheduleInterview(Long id, LocalDateTime newTime) {
        Interview interview = getInterviewById(id);
        
        // 检查新时间是否冲突
        LocalDateTime endTime = newTime.plusMinutes(interview.getDurationMinutes() != null ? interview.getDurationMinutes() : 60);
        if (hasScheduleConflict(interview.getInterviewerId(), interview.getCandidateId(), newTime, endTime)) {
            throw new RuntimeException("新的面试时间冲突，请选择其他时间");
        }
        
        interview.setScheduledTime(newTime);
        interview.setStatus(Interview.InterviewStatus.RESCHEDULED);
        interview.setRescheduleCount(interview.getRescheduleCount() + 1);
        interview.setLastRescheduledAt(LocalDateTime.now());
        interview.setCandidateConfirmed(false);
        interview.setInterviewerConfirmed(false);
        interview.setReminderSent(false);
        
        return interviewRepository.save(interview);
    }
    
    @Override
    public Interview getInterviewById(Long id) {
        return interviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("面试记录不存在"));
    }
    
    @Override
    public List<Interview> getInterviewsByJobId(Long jobId) {
        return interviewRepository.findByJobId(jobId);
    }
    
    @Override
    public List<Interview> getInterviewsByCandidateId(Long candidateId) {
        return interviewRepository.findByCandidateId(candidateId);
    }
    
    @Override
    public List<Interview> getInterviewsByInterviewerId(Long interviewerId) {
        return interviewRepository.findByInterviewerId(interviewerId);
    }
    
    @Override
    public Interview startInterview(Long id) {
        Interview interview = getInterviewById(id);
        interview.setStatus(Interview.InterviewStatus.IN_PROGRESS);
        interview.setActualStartTime(LocalDateTime.now());
        return interviewRepository.save(interview);
    }
    
    @Override
    public Interview completeInterview(Long id) {
        Interview interview = getInterviewById(id);
        interview.setStatus(Interview.InterviewStatus.COMPLETED);
        interview.setActualEndTime(LocalDateTime.now());
        return interviewRepository.save(interview);
    }
    
    @Override
    public Interview confirmInterview(Long id, boolean candidateConfirmed, boolean interviewerConfirmed) {
        Interview interview = getInterviewById(id);
        interview.setCandidateConfirmed(candidateConfirmed);
        interview.setInterviewerConfirmed(interviewerConfirmed);
        
        if (candidateConfirmed && interviewerConfirmed) {
            interview.setStatus(Interview.InterviewStatus.CONFIRMED);
        }
        
        return interviewRepository.save(interview);
    }
    
    @Override
    public Interview markNoShow(Long id) {
        Interview interview = getInterviewById(id);
        interview.setStatus(Interview.InterviewStatus.NO_SHOW);
        return interviewRepository.save(interview);
    }
    
    @Override
    public List<Interview> checkScheduleConflicts(Long interviewerId, Long candidateId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Interview> conflicts = new ArrayList<>();
        
        if (interviewerId != null) {
            conflicts.addAll(interviewRepository.findInterviewerConflicts(interviewerId, startTime, endTime));
        }
        
        if (candidateId != null) {
            conflicts.addAll(interviewRepository.findCandidateConflicts(candidateId, startTime, endTime));
        }
        
        return conflicts;
    }
    
    @Override
    public boolean hasScheduleConflict(Long interviewerId, Long candidateId, LocalDateTime startTime, LocalDateTime endTime) {
        return !checkScheduleConflicts(interviewerId, candidateId, startTime, endTime).isEmpty();
    }
    
    @Override
    public List<Interview> getAvailableTimeSlots(Long interviewerId, LocalDateTime date) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        
        return interviewRepository.findByInterviewerIdAndScheduledTimeBetween(interviewerId, startOfDay, endOfDay);
    }
    
    @Override
    public Map<String, Object> suggestAlternativeTimeSlots(Long interviewerId, Long candidateId, LocalDateTime preferredTime) {
        Map<String, Object> result = new HashMap<>();
        List<LocalDateTime> suggestions = new ArrayList<>();
        
        // 检查首选时间前后2小时内的可用时间段
        for (int i = -2; i <= 2; i++) {
            LocalDateTime proposedTime = preferredTime.plusHours(i);
            LocalDateTime endTime = proposedTime.plusHours(1); // 假设面试时长1小时
            
            if (!hasScheduleConflict(interviewerId, candidateId, proposedTime, endTime)) {
                suggestions.add(proposedTime);
            }
        }
        
        result.put("suggestions", suggestions);
        result.put("preferredTime", preferredTime);
        result.put("hasConflict", hasScheduleConflict(interviewerId, candidateId, preferredTime, preferredTime.plusHours(1)));
        
        return result;
    }
    
    @Override
    public List<Interview> getTodayInterviews() {
        return interviewRepository.findTodayInterviews();
    }
    
    @Override
    public List<Interview> getTomorrowInterviews() {
        return interviewRepository.findTomorrowInterviews();
    }
    
    @Override
    public List<Interview> getThisWeekInterviews() {
        return interviewRepository.findThisWeekInterviews();
    }
    
    @Override
    public List<Interview> getInterviewsByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        return interviewRepository.findByScheduledTimeBetween(startTime, endTime);
    }
    
    @Override
    public Map<String, Object> getInterviewerSchedule(Long interviewerId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Interview> interviews = findByInterviewerIdAndScheduledTimeBetween(interviewerId, startDate, endDate);
        
        Map<String, Object> schedule = new HashMap<>();
        schedule.put("interviewerId", interviewerId);
        schedule.put("startDate", startDate);
        schedule.put("endDate", endDate);
        schedule.put("interviews", interviews);
        schedule.put("totalCount", interviews.size());
        
        // 按状态分组统计
        Map<Interview.InterviewStatus, Long> statusCount = interviews.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    Interview::getStatus, 
                    java.util.stream.Collectors.counting()
                ));
        schedule.put("statusBreakdown", statusCount);
        
        return schedule;
    }
    
    // 辅助方法：根据面试官和时间范围查找面试
    private List<Interview> findByInterviewerIdAndScheduledTimeBetween(Long interviewerId, LocalDateTime startDate, LocalDateTime endDate) {
        return interviewRepository.findByScheduledTimeBetween(startDate, endDate).stream()
                .filter(interview -> interview.getInterviewerId().equals(interviewerId))
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public Interview evaluateInterview(Long id, Map<String, Object> evaluation) {
        Interview interview = getInterviewById(id);
        
        if (evaluation.containsKey("overallScore")) {
            interview.setOverallScore((Integer) evaluation.get("overallScore"));
        }
        if (evaluation.containsKey("technicalScore")) {
            interview.setTechnicalScore((Integer) evaluation.get("technicalScore"));
        }
        if (evaluation.containsKey("communicationScore")) {
            interview.setCommunicationScore((Integer) evaluation.get("communicationScore"));
        }
        if (evaluation.containsKey("culturalFitScore")) {
            interview.setCulturalFitScore((Integer) evaluation.get("culturalFitScore"));
        }
        if (evaluation.containsKey("problemSolvingScore")) {
            interview.setProblemSolvingScore((Integer) evaluation.get("problemSolvingScore"));
        }
        if (evaluation.containsKey("feedback")) {
            interview.setFeedback((String) evaluation.get("feedback"));
        }
        if (evaluation.containsKey("recommendation")) {
            interview.setRecommendation((String) evaluation.get("recommendation"));
        }
        
        interview.setEvaluationCompleted(true);
        return interviewRepository.save(interview);
    }
    
    @Override
    public Interview addInterviewFeedback(Long id, String feedback, String recommendation) {
        Interview interview = getInterviewById(id);
        interview.setFeedback(feedback);
        interview.setRecommendation(recommendation);
        return interviewRepository.save(interview);
    }
    
    @Override
    public Interview updateInterviewScores(Long id, Map<String, Integer> scores) {
        Interview interview = getInterviewById(id);
        
        scores.forEach((key, value) -> {
            switch (key) {
                case "overall" -> interview.setOverallScore(value);
                case "technical" -> interview.setTechnicalScore(value);
                case "communication" -> interview.setCommunicationScore(value);
                case "culturalFit" -> interview.setCulturalFitScore(value);
                case "problemSolving" -> interview.setProblemSolvingScore(value);
            }
        });
        
        return interviewRepository.save(interview);
    }
    
    @Override
    public Map<String, Object> getInterviewEvaluation(Long id) {
        Interview interview = getInterviewById(id);
        Map<String, Object> evaluation = new HashMap<>();
        
        evaluation.put("interviewId", id);
        evaluation.put("overallScore", interview.getOverallScore());
        evaluation.put("technicalScore", interview.getTechnicalScore());
        evaluation.put("communicationScore", interview.getCommunicationScore());
        evaluation.put("culturalFitScore", interview.getCulturalFitScore());
        evaluation.put("problemSolvingScore", interview.getProblemSolvingScore());
        evaluation.put("feedback", interview.getFeedback());
        evaluation.put("recommendation", interview.getRecommendation());
        evaluation.put("evaluationCompleted", interview.getEvaluationCompleted());
        
        return evaluation;
    }
    
    @Override
    public List<Interview> getInterviewsNeedingReminder(int hoursBeforeInterview) {
        LocalDateTime reminderTime = LocalDateTime.now().plusHours(hoursBeforeInterview);
        return interviewRepository.findInterviewsNeedingReminder(reminderTime);
    }
    
    @Override
    public void sendInterviewReminders() {
        List<Interview> interviews = getInterviewsNeedingReminder(24); // 24小时前提醒
        
        for (Interview interview : interviews) {
            // 这里应该集成邮件或短信服务
            // emailService.sendInterviewReminder(interview);
            
            interview.setReminderSent(true);
            interviewRepository.save(interview);
        }
    }
    
    @Override
    public void sendInterviewInvitation(Long interviewId) {
        Interview interview = getInterviewById(interviewId);
        // 发送面试邀请逻辑
        // emailService.sendInterviewInvitation(interview);
    }
    
    @Override
    public void sendRescheduleNotification(Long interviewId) {
        Interview interview = getInterviewById(interviewId);
        // 发送改期通知逻辑
        // notificationService.sendRescheduleNotification(interview);
    }
    
    @Override
    public void sendCancellationNotification(Long interviewId) {
        Interview interview = getInterviewById(interviewId);
        // 发送取消通知逻辑
        // notificationService.sendCancellationNotification(interview);
    }
    
    @Override
    public Map<String, Object> getInterviewStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        Object[] stats = interviewRepository.getInterviewStatistics(startDate, endDate);
        
        Map<String, Object> result = new HashMap<>();
        result.put("totalInterviews", stats[0]);
        result.put("completedInterviews", stats[1]);
        result.put("hiredCandidates", stats[2]);
        result.put("averageScore", stats[3]);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        
        // 计算通过率
        Long total = (Long) stats[0];
        Long hired = (Long) stats[2];
        Double passRate = total > 0 ? (double) hired / total * 100 : 0.0;
        result.put("passRate", passRate);
        
        return result;
    }
    
    @Override
    public Map<String, Object> getInterviewerPerformanceStats(Long interviewerId) {
        Long completedCount = interviewRepository.countCompletedInterviewsByInterviewer(interviewerId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("interviewerId", interviewerId);
        stats.put("completedInterviews", completedCount);
        
        // 可以添加更多统计信息
        List<Interview> interviews = interviewRepository.findByInterviewerIdAndStatus(interviewerId, Interview.InterviewStatus.COMPLETED);
        if (!interviews.isEmpty()) {
            double avgScore = interviews.stream()
                    .filter(i -> i.getOverallScore() != null)
                    .mapToInt(Interview::getOverallScore)
                    .average()
                    .orElse(0.0);
            stats.put("averageScore", avgScore);
        }
        
        return stats;
    }
    
    @Override
    public Map<String, Object> getCandidateInterviewHistory(Long candidateId) {
        List<Interview> interviews = interviewRepository.findByCandidateId(candidateId);
        
        Map<String, Object> history = new HashMap<>();
        history.put("candidateId", candidateId);
        history.put("interviews", interviews);
        history.put("totalCount", interviews.size());
        
        // 按状态统计
        Map<Interview.InterviewStatus, Long> statusCount = interviews.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                    Interview::getStatus, 
                    java.util.stream.Collectors.counting()
                ));
        history.put("statusBreakdown", statusCount);
        
        return history;
    }
    
    @Override
    public Double getInterviewPassRate(LocalDateTime startDate, LocalDateTime endDate) {
        return interviewRepository.calculateInterviewPassRate();
    }
    
    @Override
    public Map<String, Object> getInterviewAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        
        // 状态分布
        List<Object[]> statusStats = interviewRepository.countByStatus();
        Map<String, Long> statusDistribution = new HashMap<>();
        for (Object[] stat : statusStats) {
            statusDistribution.put(stat[0].toString(), (Long) stat[1]);
        }
        analytics.put("statusDistribution", statusDistribution);
        
        // 类型分布
        List<Object[]> typeStats = interviewRepository.countByType();
        Map<String, Long> typeDistribution = new HashMap<>();
        for (Object[] stat : typeStats) {
            typeDistribution.put(stat[0].toString(), (Long) stat[1]);
        }
        analytics.put("typeDistribution", typeDistribution);
        
        // 评分分布
        Object[] scoreDistribution = interviewRepository.getScoreDistribution();
        analytics.put("scoreDistribution", Map.of(
            "excellent", scoreDistribution[0],  // 90+
            "good", scoreDistribution[1],       // 80-89
            "average", scoreDistribution[2],    // 70-79
            "below", scoreDistribution[3],      // 60-69
            "poor", scoreDistribution[4]        // <60
        ));
        
        // 平均面试时长
        Double avgDuration = interviewRepository.calculateAverageInterviewDuration();
        analytics.put("averageDuration", avgDuration);
        
        return analytics;
    }
    
    @Override
    public List<Interview> batchScheduleInterviews(List<Interview> interviews) {
        List<Interview> scheduled = new ArrayList<>();
        
        for (Interview interview : interviews) {
            try {
                Interview scheduledInterview = scheduleInterview(interview);
                scheduled.add(scheduledInterview);
            } catch (RuntimeException e) {
                // 记录错误但继续处理其他面试
                System.err.println("Failed to schedule interview: " + e.getMessage());
            }
        }
        
        return scheduled;
    }
    
    @Override
    public void batchUpdateInterviewStatus(List<Long> interviewIds, Interview.InterviewStatus status) {
        interviewRepository.batchUpdateStatus(interviewIds, status);
    }
    
    @Override
    public void batchCancelInterviews(List<Long> interviewIds, String reason) {
        for (Long id : interviewIds) {
            cancelInterview(id, reason);
        }
    }
    
    @Override
    public Interview scheduleNextRound(Long currentInterviewId, Interview.InterviewType nextType) {
        Interview currentInterview = getInterviewById(currentInterviewId);
        
        if (!canProceedToNextRound(currentInterviewId)) {
            throw new RuntimeException("当前面试状态不允许安排下一轮");
        }
        
        Interview nextRound = new Interview();
        nextRound.setJobId(currentInterview.getJobId());
        nextRound.setCandidateId(currentInterview.getCandidateId());
        nextRound.setInterviewerId(currentInterview.getInterviewerId());
        nextRound.setType(nextType);
        
        // 设置下一轮次
        nextRound.setRound(getNextRound(currentInterview.getRound()));
        
        // 建议时间：当前面试后的第二天
        nextRound.setScheduledTime(currentInterview.getScheduledTime().plusDays(1));
        nextRound.setDurationMinutes(60); // 默认1小时
        
        return scheduleInterview(nextRound);
    }
    
    @Override
    public List<Interview> getInterviewFlow(Long candidateId, Long jobId) {
        return interviewRepository.findByJobIdAndCandidateId(jobId, candidateId);
    }
    
    @Override
    public boolean canProceedToNextRound(Long interviewId) {
        Interview interview = getInterviewById(interviewId);
        return interview.getStatus() == Interview.InterviewStatus.COMPLETED 
               && "HIRE".equals(interview.getRecommendation())
               && interview.getOverallScore() != null 
               && interview.getOverallScore() >= 70; // 70分以上可以进入下一轮
    }
    
    @Override
    public Map<String, Object> getInterviewProgress(Long candidateId, Long jobId) {
        List<Interview> interviews = getInterviewFlow(candidateId, jobId);
        
        Map<String, Object> progress = new HashMap<>();
        progress.put("candidateId", candidateId);
        progress.put("jobId", jobId);
        progress.put("interviews", interviews);
        progress.put("currentRound", interviews.size());
        
        if (!interviews.isEmpty()) {
            Interview latest = interviews.get(interviews.size() - 1);
            progress.put("currentStatus", latest.getStatus());
            progress.put("canProceed", canProceedToNextRound(latest.getId()));
        }
        
        return progress;
    }
    
    // 私有辅助方法
    private Interview.InterviewRound getNextRound(Interview.InterviewRound currentRound) {
        return switch (currentRound) {
            case FIRST -> Interview.InterviewRound.SECOND;
            case SECOND -> Interview.InterviewRound.THIRD;
            case THIRD -> Interview.InterviewRound.FINAL;
            case FINAL -> Interview.InterviewRound.FINAL; // 已经是最终轮
        };
    }
}