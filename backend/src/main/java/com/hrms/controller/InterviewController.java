package com.hrms.controller;

import com.hrms.entity.Interview;
import com.hrms.service.InterviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interviews")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@Tag(name = "Interview Management", description = "面试管理API")
@Validated
public class InterviewController {
    
    @Autowired
    private InterviewService interviewService;
    
    // ================== 面试安排管理 ==================
    
    @PostMapping
    @Operation(summary = "安排面试", description = "创建新的面试安排")
    public ResponseEntity<Interview> scheduleInterview(
            @Parameter(description = "面试信息") @RequestBody @Valid Interview interview) {
        
        Interview scheduled = interviewService.scheduleInterview(interview);
        return ResponseEntity.ok(scheduled);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "更新面试信息", description = "更新现有面试的详细信息")
    public ResponseEntity<Interview> updateInterview(
            @Parameter(description = "面试ID") @PathVariable @NotNull Long id,
            @Parameter(description = "更新的面试信息") @RequestBody @Valid Interview interview) {
        
        Interview updated = interviewService.updateInterview(id, interview);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "取消面试", description = "取消指定的面试安排")
    public ResponseEntity<Void> cancelInterview(
            @Parameter(description = "面试ID") @PathVariable @NotNull Long id,
            @Parameter(description = "取消原因") @RequestParam String reason) {
        
        interviewService.cancelInterview(id, reason);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/{id}/reschedule")
    @Operation(summary = "改期面试", description = "重新安排面试时间")
    public ResponseEntity<Interview> rescheduleInterview(
            @Parameter(description = "面试ID") @PathVariable @NotNull Long id,
            @Parameter(description = "新的面试时间") @RequestParam 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime newTime) {
        
        Interview rescheduled = interviewService.rescheduleInterview(id, newTime);
        return ResponseEntity.ok(rescheduled);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "获取面试详情", description = "根据ID获取面试的详细信息")
    public ResponseEntity<Interview> getInterviewById(
            @Parameter(description = "面试ID") @PathVariable @NotNull Long id) {
        
        Interview interview = interviewService.getInterviewById(id);
        return ResponseEntity.ok(interview);
    }
    
    // ================== 面试状态管理 ==================
    
    @PutMapping("/{id}/start")
    @Operation(summary = "开始面试", description = "标记面试为进行中状态")
    public ResponseEntity<Interview> startInterview(
            @Parameter(description = "面试ID") @PathVariable @NotNull Long id) {
        
        Interview started = interviewService.startInterview(id);
        return ResponseEntity.ok(started);
    }
    
    @PutMapping("/{id}/complete")
    @Operation(summary = "完成面试", description = "标记面试为已完成状态")
    public ResponseEntity<Interview> completeInterview(
            @Parameter(description = "面试ID") @PathVariable @NotNull Long id) {
        
        Interview completed = interviewService.completeInterview(id);
        return ResponseEntity.ok(completed);
    }
    
    @PutMapping("/{id}/confirm")
    @Operation(summary = "确认面试", description = "确认面试参与者的出席")
    public ResponseEntity<Interview> confirmInterview(
            @Parameter(description = "面试ID") @PathVariable @NotNull Long id,
            @Parameter(description = "候选人是否确认") @RequestParam(defaultValue = "false") boolean candidateConfirmed,
            @Parameter(description = "面试官是否确认") @RequestParam(defaultValue = "false") boolean interviewerConfirmed) {
        
        Interview confirmed = interviewService.confirmInterview(id, candidateConfirmed, interviewerConfirmed);
        return ResponseEntity.ok(confirmed);
    }
    
    @PutMapping("/{id}/no-show")
    @Operation(summary = "标记未出席", description = "标记候选人未出席面试")
    public ResponseEntity<Interview> markNoShow(
            @Parameter(description = "面试ID") @PathVariable @NotNull Long id) {
        
        Interview noShow = interviewService.markNoShow(id);
        return ResponseEntity.ok(noShow);
    }
    
    // ================== 面试查询 ==================
    
    @GetMapping("/job/{jobId}")
    @Operation(summary = "获取职位面试列表", description = "获取指定职位的所有面试记录")
    public ResponseEntity<List<Interview>> getInterviewsByJobId(
            @Parameter(description = "职位ID") @PathVariable @NotNull Long jobId) {
        
        List<Interview> interviews = interviewService.getInterviewsByJobId(jobId);
        return ResponseEntity.ok(interviews);
    }
    
    @GetMapping("/candidate/{candidateId}")
    @Operation(summary = "获取候选人面试列表", description = "获取指定候选人的所有面试记录")
    public ResponseEntity<List<Interview>> getInterviewsByCandidateId(
            @Parameter(description = "候选人ID") @PathVariable @NotNull Long candidateId) {
        
        List<Interview> interviews = interviewService.getInterviewsByCandidateId(candidateId);
        return ResponseEntity.ok(interviews);
    }
    
    @GetMapping("/interviewer/{interviewerId}")
    @Operation(summary = "获取面试官面试列表", description = "获取指定面试官的所有面试记录")
    public ResponseEntity<List<Interview>> getInterviewsByInterviewerId(
            @Parameter(description = "面试官ID") @PathVariable @NotNull Long interviewerId) {
        
        List<Interview> interviews = interviewService.getInterviewsByInterviewerId(interviewerId);
        return ResponseEntity.ok(interviews);
    }
    
    @GetMapping("/today")
    @Operation(summary = "获取今日面试", description = "获取今天安排的所有面试")
    public ResponseEntity<List<Interview>> getTodayInterviews() {
        List<Interview> interviews = interviewService.getTodayInterviews();
        return ResponseEntity.ok(interviews);
    }
    
    @GetMapping("/tomorrow")
    @Operation(summary = "获取明日面试", description = "获取明天安排的所有面试")
    public ResponseEntity<List<Interview>> getTomorrowInterviews() {
        List<Interview> interviews = interviewService.getTomorrowInterviews();
        return ResponseEntity.ok(interviews);
    }
    
    @GetMapping("/this-week")
    @Operation(summary = "获取本周面试", description = "获取本周安排的所有面试")
    public ResponseEntity<List<Interview>> getThisWeekInterviews() {
        List<Interview> interviews = interviewService.getThisWeekInterviews();
        return ResponseEntity.ok(interviews);
    }
    
    @GetMapping("/date-range")
    @Operation(summary = "按时间范围查询", description = "获取指定时间范围内的面试记录")
    public ResponseEntity<List<Interview>> getInterviewsByDateRange(
            @Parameter(description = "开始时间") @RequestParam 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        List<Interview> interviews = interviewService.getInterviewsByDateRange(startTime, endTime);
        return ResponseEntity.ok(interviews);
    }
    
    // ================== 调度冲突检测 ==================
    
    @GetMapping("/conflicts")
    @Operation(summary = "检查调度冲突", description = "检查指定时间段的面试冲突")
    public ResponseEntity<List<Interview>> checkScheduleConflicts(
            @Parameter(description = "面试官ID") @RequestParam(required = false) Long interviewerId,
            @Parameter(description = "候选人ID") @RequestParam(required = false) Long candidateId,
            @Parameter(description = "开始时间") @RequestParam 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        List<Interview> conflicts = interviewService.checkScheduleConflicts(interviewerId, candidateId, startTime, endTime);
        return ResponseEntity.ok(conflicts);
    }
    
    @GetMapping("/has-conflict")
    @Operation(summary = "是否存在冲突", description = "检查指定时间段是否存在调度冲突")
    public ResponseEntity<Map<String, Boolean>> hasScheduleConflict(
            @Parameter(description = "面试官ID") @RequestParam(required = false) Long interviewerId,
            @Parameter(description = "候选人ID") @RequestParam(required = false) Long candidateId,
            @Parameter(description = "开始时间") @RequestParam 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "结束时间") @RequestParam 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        boolean hasConflict = interviewService.hasScheduleConflict(interviewerId, candidateId, startTime, endTime);
        return ResponseEntity.ok(Map.of("hasConflict", hasConflict));
    }
    
    @GetMapping("/suggest-times")
    @Operation(summary = "建议替代时间", description = "为冲突的面试建议替代时间段")
    public ResponseEntity<Map<String, Object>> suggestAlternativeTimeSlots(
            @Parameter(description = "面试官ID") @RequestParam @NotNull Long interviewerId,
            @Parameter(description = "候选人ID") @RequestParam @NotNull Long candidateId,
            @Parameter(description = "首选时间") @RequestParam 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime preferredTime) {
        
        Map<String, Object> suggestions = interviewService.suggestAlternativeTimeSlots(interviewerId, candidateId, preferredTime);
        return ResponseEntity.ok(suggestions);
    }
    
    // ================== 面试官日程管理 ==================
    
    @GetMapping("/interviewer/{interviewerId}/schedule")
    @Operation(summary = "获取面试官日程", description = "获取面试官在指定时间范围内的日程安排")
    public ResponseEntity<Map<String, Object>> getInterviewerSchedule(
            @Parameter(description = "面试官ID") @PathVariable @NotNull Long interviewerId,
            @Parameter(description = "开始日期") @RequestParam 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "结束日期") @RequestParam 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        Map<String, Object> schedule = interviewService.getInterviewerSchedule(interviewerId, startDate, endDate);
        return ResponseEntity.ok(schedule);
    }
    
    // ================== 面试评估管理 ==================
    
    @PutMapping("/{id}/evaluate")
    @Operation(summary = "评估面试", description = "提交面试评估结果")
    public ResponseEntity<Interview> evaluateInterview(
            @Parameter(description = "面试ID") @PathVariable @NotNull Long id,
            @Parameter(description = "评估信息") @RequestBody Map<String, Object> evaluation) {
        
        Interview evaluated = interviewService.evaluateInterview(id, evaluation);
        return ResponseEntity.ok(evaluated);
    }
    
    @PutMapping("/{id}/feedback")
    @Operation(summary = "添加面试反馈", description = "添加面试反馈和推荐意见")
    public ResponseEntity<Interview> addInterviewFeedback(
            @Parameter(description = "面试ID") @PathVariable @NotNull Long id,
            @Parameter(description = "反馈内容") @RequestParam String feedback,
            @Parameter(description = "推荐决定") @RequestParam String recommendation) {
        
        Interview updated = interviewService.addInterviewFeedback(id, feedback, recommendation);
        return ResponseEntity.ok(updated);
    }
    
    @PutMapping("/{id}/scores")
    @Operation(summary = "更新评分", description = "更新面试各项评分")
    public ResponseEntity<Interview> updateInterviewScores(
            @Parameter(description = "面试ID") @PathVariable @NotNull Long id,
            @Parameter(description = "评分信息") @RequestBody Map<String, Integer> scores) {
        
        Interview updated = interviewService.updateInterviewScores(id, scores);
        return ResponseEntity.ok(updated);
    }
    
    @GetMapping("/{id}/evaluation")
    @Operation(summary = "获取评估结果", description = "获取面试的评估详情")
    public ResponseEntity<Map<String, Object>> getInterviewEvaluation(
            @Parameter(description = "面试ID") @PathVariable @NotNull Long id) {
        
        Map<String, Object> evaluation = interviewService.getInterviewEvaluation(id);
        return ResponseEntity.ok(evaluation);
    }
    
    // ================== 面试提醒和通知 ==================
    
    @GetMapping("/reminders")
    @Operation(summary = "获取需要提醒的面试", description = "获取需要发送提醒的面试列表")
    public ResponseEntity<List<Interview>> getInterviewsNeedingReminder(
            @Parameter(description = "提前小时数") @RequestParam(defaultValue = "24") int hoursBeforeInterview) {
        
        List<Interview> interviews = interviewService.getInterviewsNeedingReminder(hoursBeforeInterview);
        return ResponseEntity.ok(interviews);
    }
    
    @PostMapping("/send-reminders")
    @Operation(summary = "发送面试提醒", description = "批量发送面试提醒通知")
    public ResponseEntity<Void> sendInterviewReminders() {
        interviewService.sendInterviewReminders();
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{id}/send-invitation")
    @Operation(summary = "发送面试邀请", description = "发送面试邀请给候选人")
    public ResponseEntity<Void> sendInterviewInvitation(
            @Parameter(description = "面试ID") @PathVariable @NotNull Long id) {
        
        interviewService.sendInterviewInvitation(id);
        return ResponseEntity.ok().build();
    }
    
    // ================== 统计和分析 ==================
    
    @GetMapping("/statistics")
    @Operation(summary = "获取面试统计", description = "获取指定时间段的面试统计数据")
    public ResponseEntity<Map<String, Object>> getInterviewStatistics(
            @Parameter(description = "开始日期") @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        // 如果没有指定日期，默认查询最近30天
        if (startDate == null) {
            startDate = LocalDateTime.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }
        
        Map<String, Object> statistics = interviewService.getInterviewStatistics(startDate, endDate);
        return ResponseEntity.ok(statistics);
    }
    
    @GetMapping("/interviewer/{interviewerId}/performance")
    @Operation(summary = "面试官绩效统计", description = "获取面试官的绩效统计数据")
    public ResponseEntity<Map<String, Object>> getInterviewerPerformanceStats(
            @Parameter(description = "面试官ID") @PathVariable @NotNull Long interviewerId) {
        
        Map<String, Object> stats = interviewService.getInterviewerPerformanceStats(interviewerId);
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/candidate/{candidateId}/history")
    @Operation(summary = "候选人面试历史", description = "获取候选人的面试历史记录")
    public ResponseEntity<Map<String, Object>> getCandidateInterviewHistory(
            @Parameter(description = "候选人ID") @PathVariable @NotNull Long candidateId) {
        
        Map<String, Object> history = interviewService.getCandidateInterviewHistory(candidateId);
        return ResponseEntity.ok(history);
    }
    
    @GetMapping("/analytics")
    @Operation(summary = "面试分析报告", description = "获取综合面试分析数据")
    public ResponseEntity<Map<String, Object>> getInterviewAnalytics() {
        Map<String, Object> analytics = interviewService.getInterviewAnalytics();
        return ResponseEntity.ok(analytics);
    }
    
    @GetMapping("/pass-rate")
    @Operation(summary = "面试通过率", description = "获取指定时间段的面试通过率")
    public ResponseEntity<Map<String, Double>> getInterviewPassRate(
            @Parameter(description = "开始日期") @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        if (startDate == null) {
            startDate = LocalDateTime.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }
        
        Double passRate = interviewService.getInterviewPassRate(startDate, endDate);
        return ResponseEntity.ok(Map.of("passRate", passRate));
    }
    
    // ================== 批量操作 ==================
    
    @PostMapping("/batch-schedule")
    @Operation(summary = "批量安排面试", description = "批量创建面试安排")
    public ResponseEntity<List<Interview>> batchScheduleInterviews(
            @Parameter(description = "面试列表") @RequestBody List<Interview> interviews) {
        
        List<Interview> scheduled = interviewService.batchScheduleInterviews(interviews);
        return ResponseEntity.ok(scheduled);
    }
    
    @PutMapping("/batch-update-status")
    @Operation(summary = "批量更新状态", description = "批量更新面试状态")
    public ResponseEntity<Void> batchUpdateInterviewStatus(
            @Parameter(description = "面试ID列表") @RequestBody List<Long> interviewIds,
            @Parameter(description = "新状态") @RequestParam Interview.InterviewStatus status) {
        
        interviewService.batchUpdateInterviewStatus(interviewIds, status);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/batch-cancel")
    @Operation(summary = "批量取消面试", description = "批量取消面试安排")
    public ResponseEntity<Void> batchCancelInterviews(
            @Parameter(description = "面试ID列表") @RequestBody List<Long> interviewIds,
            @Parameter(description = "取消原因") @RequestParam String reason) {
        
        interviewService.batchCancelInterviews(interviewIds, reason);
        return ResponseEntity.ok().build();
    }
    
    // ================== 面试流程管理 ==================
    
    @PostMapping("/{id}/next-round")
    @Operation(summary = "安排下一轮面试", description = "为候选人安排下一轮面试")
    public ResponseEntity<Interview> scheduleNextRound(
            @Parameter(description = "当前面试ID") @PathVariable @NotNull Long id,
            @Parameter(description = "下一轮面试类型") @RequestParam Interview.InterviewType nextType) {
        
        Interview nextRound = interviewService.scheduleNextRound(id, nextType);
        return ResponseEntity.ok(nextRound);
    }
    
    @GetMapping("/flow")
    @Operation(summary = "获取面试流程", description = "获取候选人在特定职位的面试流程")
    public ResponseEntity<List<Interview>> getInterviewFlow(
            @Parameter(description = "候选人ID") @RequestParam @NotNull Long candidateId,
            @Parameter(description = "职位ID") @RequestParam @NotNull Long jobId) {
        
        List<Interview> flow = interviewService.getInterviewFlow(candidateId, jobId);
        return ResponseEntity.ok(flow);
    }
    
    @GetMapping("/{id}/can-proceed")
    @Operation(summary = "是否可进入下一轮", description = "检查是否可以安排下一轮面试")
    public ResponseEntity<Map<String, Boolean>> canProceedToNextRound(
            @Parameter(description = "面试ID") @PathVariable @NotNull Long id) {
        
        boolean canProceed = interviewService.canProceedToNextRound(id);
        return ResponseEntity.ok(Map.of("canProceed", canProceed));
    }
    
    @GetMapping("/progress")
    @Operation(summary = "获取面试进度", description = "获取候选人在特定职位的面试进度")
    public ResponseEntity<Map<String, Object>> getInterviewProgress(
            @Parameter(description = "候选人ID") @RequestParam @NotNull Long candidateId,
            @Parameter(description = "职位ID") @RequestParam @NotNull Long jobId) {
        
        Map<String, Object> progress = interviewService.getInterviewProgress(candidateId, jobId);
        return ResponseEntity.ok(progress);
    }
}