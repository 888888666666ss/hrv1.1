package com.hrms.repository;

import com.hrms.entity.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    
    /**
     * 根据职位ID查找面试记录
     */
    List<Interview> findByJobId(Long jobId);
    
    /**
     * 根据候选人ID查找面试记录
     */
    List<Interview> findByCandidateId(Long candidateId);
    
    /**
     * 根据面试官ID查找面试记录
     */
    List<Interview> findByInterviewerId(Long interviewerId);
    
    /**
     * 根据面试状态查找记录
     */
    List<Interview> findByStatus(Interview.InterviewStatus status);
    
    /**
     * 根据面试类型查找记录
     */
    List<Interview> findByType(Interview.InterviewType type);
    
    /**
     * 根据面试轮次查找记录
     */
    List<Interview> findByRound(Interview.InterviewRound round);
    
    /**
     * 根据面试模式查找记录
     */
    List<Interview> findByMode(Interview.InterviewMode mode);
    
    /**
     * 根据职位和候选人查找面试记录
     */
    List<Interview> findByJobIdAndCandidateId(Long jobId, Long candidateId);
    
    /**
     * 根据候选人和状态查找面试记录
     */
    List<Interview> findByCandidateIdAndStatus(Long candidateId, Interview.InterviewStatus status);
    
    /**
     * 根据面试官和状态查找面试记录
     */
    List<Interview> findByInterviewerIdAndStatus(Long interviewerId, Interview.InterviewStatus status);
    
    /**
     * 查找指定时间范围内的面试
     */
    List<Interview> findByScheduledTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 查找今日面试安排
     */
    @Query("SELECT i FROM Interview i WHERE DATE(i.scheduledTime) = CURRENT_DATE ORDER BY i.scheduledTime")
    List<Interview> findTodayInterviews();
    
    /**
     * 查找明日面试安排
     */
    @Query("SELECT i FROM Interview i WHERE DATE(i.scheduledTime) = CURRENT_DATE + 1 ORDER BY i.scheduledTime")
    List<Interview> findTomorrowInterviews();
    
    /**
     * 查找本周面试安排
     */
    @Query("SELECT i FROM Interview i WHERE WEEK(i.scheduledTime) = WEEK(CURRENT_DATE) AND YEAR(i.scheduledTime) = YEAR(CURRENT_DATE) ORDER BY i.scheduledTime")
    List<Interview> findThisWeekInterviews();
    
    /**
     * 查找指定面试官在指定时间的面试冲突
     */
    @Query("SELECT i FROM Interview i WHERE i.interviewerId = :interviewerId " +
           "AND i.status NOT IN ('CANCELLED', 'COMPLETED') " +
           "AND i.scheduledTime BETWEEN :startTime AND :endTime")
    List<Interview> findInterviewerConflicts(@Param("interviewerId") Long interviewerId,
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);
    
    /**
     * 查找指定候选人在指定时间的面试冲突
     */
    @Query("SELECT i FROM Interview i WHERE i.candidateId = :candidateId " +
           "AND i.status NOT IN ('CANCELLED', 'COMPLETED') " +
           "AND i.scheduledTime BETWEEN :startTime AND :endTime")
    List<Interview> findCandidateConflicts(@Param("candidateId") Long candidateId,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);
    
    /**
     * 查找需要发送提醒的面试
     */
    @Query("SELECT i FROM Interview i WHERE i.reminderSent = false " +
           "AND i.status = 'SCHEDULED' " +
           "AND i.scheduledTime BETWEEN CURRENT_TIMESTAMP AND :reminderTime")
    List<Interview> findInterviewsNeedingReminder(@Param("reminderTime") LocalDateTime reminderTime);
    
    /**
     * 查找未确认的面试
     */
    @Query("SELECT i FROM Interview i WHERE i.status = 'SCHEDULED' " +
           "AND (i.candidateConfirmed = false OR i.interviewerConfirmed = false)")
    List<Interview> findUnconfirmedInterviews();
    
    /**
     * 查找超时未完成评估的面试
     */
    @Query("SELECT i FROM Interview i WHERE i.status = 'COMPLETED' " +
           "AND i.evaluationCompleted = false " +
           "AND i.actualEndTime < :deadline")
    List<Interview> findOverdueEvaluations(@Param("deadline") LocalDateTime deadline);
    
    /**
     * 统计面试官的面试次数
     */
    @Query("SELECT COUNT(i) FROM Interview i WHERE i.interviewerId = :interviewerId " +
           "AND i.status = 'COMPLETED'")
    Long countCompletedInterviewsByInterviewer(@Param("interviewerId") Long interviewerId);
    
    /**
     * 统计候选人的面试次数
     */
    @Query("SELECT COUNT(i) FROM Interview i WHERE i.candidateId = :candidateId")
    Long countInterviewsByCandidate(@Param("candidateId") Long candidateId);
    
    /**
     * 统计各状态面试数量
     */
    @Query("SELECT i.status, COUNT(i) FROM Interview i GROUP BY i.status")
    List<Object[]> countByStatus();
    
    /**
     * 统计各类型面试数量
     */
    @Query("SELECT i.type, COUNT(i) FROM Interview i GROUP BY i.type")
    List<Object[]> countByType();
    
    /**
     * 查找最近的面试记录
     */
    @Query("SELECT i FROM Interview i ORDER BY i.scheduledTime DESC")
    List<Interview> findRecentInterviews();
    
    /**
     * 查找候选人的最新面试记录
     */
    Optional<Interview> findTopByCandidateIdOrderByScheduledTimeDesc(Long candidateId);
    
    /**
     * 查找职位的最新面试记录
     */
    Optional<Interview> findTopByJobIdOrderByScheduledTimeDesc(Long jobId);
    
    /**
     * 查找高分面试记录
     */
    @Query("SELECT i FROM Interview i WHERE i.overallScore >= :minScore ORDER BY i.overallScore DESC")
    List<Interview> findHighScoreInterviews(@Param("minScore") Integer minScore);
    
    /**
     * 查找平均面试时长
     */
    @Query("SELECT AVG(TIMESTAMPDIFF(MINUTE, i.actualStartTime, i.actualEndTime)) FROM Interview i " +
           "WHERE i.actualStartTime IS NOT NULL AND i.actualEndTime IS NOT NULL")
    Double calculateAverageInterviewDuration();
    
    /**
     * 查找面试通过率
     */
    @Query("SELECT " +
           "COUNT(CASE WHEN i.recommendation = 'HIRE' THEN 1 END) * 100.0 / COUNT(i) " +
           "FROM Interview i WHERE i.recommendation IS NOT NULL")
    Double calculateInterviewPassRate();
    
    /**
     * 查找指定时间段的面试统计
     */
    @Query("SELECT " +
           "COUNT(i), " +
           "COUNT(CASE WHEN i.status = 'COMPLETED' THEN 1 END), " +
           "COUNT(CASE WHEN i.recommendation = 'HIRE' THEN 1 END), " +
           "AVG(i.overallScore) " +
           "FROM Interview i WHERE i.scheduledTime BETWEEN :startTime AND :endTime")
    Object[] getInterviewStatistics(@Param("startTime") LocalDateTime startTime,
                                   @Param("endTime") LocalDateTime endTime);
    
    /**
     * 查找改期次数过多的面试
     */
    @Query("SELECT i FROM Interview i WHERE i.rescheduleCount > :maxReschedules")
    List<Interview> findFrequentlyRescheduledInterviews(@Param("maxReschedules") Integer maxReschedules);
    
    /**
     * 查找面试官工作负荷
     */
    @Query("SELECT i.interviewerId, COUNT(i) FROM Interview i " +
           "WHERE i.scheduledTime BETWEEN :startTime AND :endTime " +
           "AND i.status NOT IN ('CANCELLED') " +
           "GROUP BY i.interviewerId")
    List<Object[]> getInterviewerWorkload(@Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime);
    
    /**
     * 查找面试评分分布
     */
    @Query("SELECT " +
           "SUM(CASE WHEN i.overallScore >= 90 THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN i.overallScore >= 80 AND i.overallScore < 90 THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN i.overallScore >= 70 AND i.overallScore < 80 THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN i.overallScore >= 60 AND i.overallScore < 70 THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN i.overallScore < 60 THEN 1 ELSE 0 END) " +
           "FROM Interview i WHERE i.overallScore IS NOT NULL")
    Object[] getScoreDistribution();
    
    /**
     * 删除指定时间之前的已取消面试记录
     */
    @Query("DELETE FROM Interview i WHERE i.status = 'CANCELLED' AND i.updatedAt < :threshold")
    void deleteOldCancelledInterviews(@Param("threshold") LocalDateTime threshold);
    
    /**
     * 批量更新面试状态
     */
    @Query("UPDATE Interview i SET i.status = :status WHERE i.id IN :ids")
    void batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Interview.InterviewStatus status);
}