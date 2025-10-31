package com.hrms.repository;

import com.hrms.entity.AIEvaluationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AIEvaluationHistoryRepository extends JpaRepository<AIEvaluationHistory, Long> {
    
    /**
     * 根据简历ID查找评估历史
     */
    List<AIEvaluationHistory> findByResumeId(Long resumeId);
    
    /**
     * 根据职位ID查找评估历史
     */
    List<AIEvaluationHistory> findByJobId(Long jobId);
    
    /**
     * 根据评估类型查找历史记录
     */
    List<AIEvaluationHistory> findByEvaluationType(AIEvaluationHistory.EvaluationType evaluationType);
    
    /**
     * 根据简历ID和职位ID查找评估历史
     */
    List<AIEvaluationHistory> findByResumeIdAndJobId(Long resumeId, Long jobId);
    
    /**
     * 查找指定简历的最新评估记录
     */
    Optional<AIEvaluationHistory> findTopByResumeIdOrderByCreatedAtDesc(Long resumeId);
    
    /**
     * 查找指定简历和职位的最新评估记录
     */
    Optional<AIEvaluationHistory> findTopByResumeIdAndJobIdOrderByCreatedAtDesc(Long resumeId, Long jobId);
    
    /**
     * 根据评分范围查找评估历史
     */
    List<AIEvaluationHistory> findByOverallScoreBetween(Integer minScore, Integer maxScore);
    
    /**
     * 根据技术评分范围查找评估历史
     */
    List<AIEvaluationHistory> findByTechnicalScoreBetween(Integer minScore, Integer maxScore);
    
    /**
     * 根据软技能评分范围查找评估历史
     */
    List<AIEvaluationHistory> findBySoftSkillsScoreBetween(Integer minScore, Integer maxScore);
    
    /**
     * 根据时间范围查找评估历史
     */
    List<AIEvaluationHistory> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * 根据AI模型版本查找评估历史
     */
    List<AIEvaluationHistory> findByAiModelVersion(String aiModelVersion);
    
    /**
     * 查找处理时间超过指定阈值的评估记录
     */
    @Query("SELECT h FROM AIEvaluationHistory h WHERE h.processingTimeMs > :threshold")
    List<AIEvaluationHistory> findByProcessingTimeGreaterThan(@Param("threshold") Long threshold);
    
    /**
     * 统计指定时间范围内的评估次数
     */
    @Query("SELECT COUNT(h) FROM AIEvaluationHistory h WHERE h.createdAt BETWEEN :startDate AND :endDate")
    Long countEvaluationsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * 统计各评估类型的数量
     */
    @Query("SELECT h.evaluationType, COUNT(h) FROM AIEvaluationHistory h GROUP BY h.evaluationType")
    List<Object[]> countByEvaluationType();
    
    /**
     * 计算指定时间范围内的平均评分
     */
    @Query("SELECT AVG(h.overallScore) FROM AIEvaluationHistory h WHERE h.createdAt BETWEEN :startDate AND :endDate")
    Double calculateAverageScoreByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * 查找高分候选人（评分大于等于指定分数）
     */
    @Query("SELECT h FROM AIEvaluationHistory h WHERE h.overallScore >= :minScore ORDER BY h.overallScore DESC")
    List<AIEvaluationHistory> findHighScoreCandidates(@Param("minScore") Integer minScore);
    
    /**
     * 根据简历ID列表批量查找最新评估记录
     */
    @Query("SELECT h FROM AIEvaluationHistory h WHERE h.resumeId IN :resumeIds AND h.id IN " +
           "(SELECT MAX(h2.id) FROM AIEvaluationHistory h2 WHERE h2.resumeId = h.resumeId)")
    List<AIEvaluationHistory> findLatestEvaluationsByResumeIds(@Param("resumeIds") List<Long> resumeIds);
    
    /**
     * 查找指定职位的所有候选人最新评估记录
     */
    @Query("SELECT h FROM AIEvaluationHistory h WHERE h.jobId = :jobId AND h.id IN " +
           "(SELECT MAX(h2.id) FROM AIEvaluationHistory h2 WHERE h2.jobId = :jobId GROUP BY h2.resumeId)")
    List<AIEvaluationHistory> findLatestEvaluationsByJobId(@Param("jobId") Long jobId);
    
    /**
     * 根据技术技能评分排序查找候选人
     */
    List<AIEvaluationHistory> findByJobIdOrderByTechnicalScoreDesc(Long jobId);
    
    /**
     * 根据软技能评分排序查找候选人
     */
    List<AIEvaluationHistory> findByJobIdOrderBySoftSkillsScoreDesc(Long jobId);
    
    /**
     * 根据经验评分排序查找候选人
     */
    List<AIEvaluationHistory> findByJobIdOrderByExperienceScoreDesc(Long jobId);
    
    /**
     * 根据文化匹配度排序查找候选人
     */
    List<AIEvaluationHistory> findByJobIdOrderByCulturalFitScoreDesc(Long jobId);
    
    /**
     * 查找评估时间最近的记录
     */
    @Query("SELECT h FROM AIEvaluationHistory h ORDER BY h.createdAt DESC")
    List<AIEvaluationHistory> findRecentEvaluations();
    
    /**
     * 根据综合评分排序查找指定职位的候选人
     */
    List<AIEvaluationHistory> findByJobIdOrderByOverallScoreDesc(Long jobId);
    
    /**
     * 查找需要重新评估的记录（评估时间过久）
     */
    @Query("SELECT h FROM AIEvaluationHistory h WHERE h.createdAt < :threshold")
    List<AIEvaluationHistory> findOutdatedEvaluations(@Param("threshold") LocalDateTime threshold);
    
    /**
     * 根据AI模型版本统计评估数量
     */
    @Query("SELECT h.aiModelVersion, COUNT(h) FROM AIEvaluationHistory h GROUP BY h.aiModelVersion")
    List<Object[]> countByAiModelVersion();
    
    /**
     * 查找特定简历的评估趋势（按时间排序）
     */
    List<AIEvaluationHistory> findByResumeIdOrderByCreatedAtAsc(Long resumeId);
    
    /**
     * 删除指定时间之前的评估历史记录
     */
    @Query("DELETE FROM AIEvaluationHistory h WHERE h.createdAt < :threshold")
    void deleteOldEvaluations(@Param("threshold") LocalDateTime threshold);
    
    /**
     * 查找处理时间异常的评估记录
     */
    @Query("SELECT h FROM AIEvaluationHistory h WHERE h.processingTimeMs IS NULL OR h.processingTimeMs < 0")
    List<AIEvaluationHistory> findAnomalousProcessingTimes();
    
    /**
     * 计算指定职位候选人的评分分布
     */
    @Query("SELECT " +
           "SUM(CASE WHEN h.overallScore >= 90 THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN h.overallScore >= 80 AND h.overallScore < 90 THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN h.overallScore >= 70 AND h.overallScore < 80 THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN h.overallScore >= 60 AND h.overallScore < 70 THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN h.overallScore < 60 THEN 1 ELSE 0 END) " +
           "FROM AIEvaluationHistory h WHERE h.jobId = :jobId")
    Object[] getScoreDistributionByJobId(@Param("jobId") Long jobId);
    
    /**
     * 查找评分变化趋势（同一简历的评分变化）
     */
    @Query("SELECT h FROM AIEvaluationHistory h WHERE h.resumeId = :resumeId ORDER BY h.createdAt ASC")
    List<AIEvaluationHistory> findScoreTrendByResumeId(@Param("resumeId") Long resumeId);
    
    /**
     * 查找平均处理时间
     */
    @Query("SELECT AVG(h.processingTimeMs) FROM AIEvaluationHistory h WHERE h.processingTimeMs IS NOT NULL")
    Double calculateAverageProcessingTime();
}