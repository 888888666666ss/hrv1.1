package com.hrms.repository;

import com.hrms.entity.InterviewAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewAnswerRepository extends JpaRepository<InterviewAnswer, Long> {
    
    /**
     * 根据面试ID查找所有答案
     */
    List<InterviewAnswer> findByInterviewId(Long interviewId);
    
    /**
     * 根据问题ID查找所有答案
     */
    List<InterviewAnswer> findByQuestionId(Long questionId);
    
    /**
     * 根据面试ID和问题ID查找特定答案
     */
    Optional<InterviewAnswer> findByInterviewIdAndQuestionId(Long interviewId, Long questionId);
    
    /**
     * 根据评估者查找答案
     */
    List<InterviewAnswer> findByEvaluatedBy(Long evaluatedBy);
    
    /**
     * 根据评级查找答案
     */
    List<InterviewAnswer> findByRating(InterviewAnswer.AnswerRating rating);
    
    /**
     * 查找已完成评估的答案
     */
    List<InterviewAnswer> findByEvaluationCompletedTrue();
    
    /**
     * 查找未完成评估的答案
     */
    List<InterviewAnswer> findByEvaluationCompletedFalse();
    
    /**
     * 根据分数范围查找答案
     */
    List<InterviewAnswer> findByScoreBetween(Integer minScore, Integer maxScore);
    
    /**
     * 根据面试ID查找已评估的答案
     */
    List<InterviewAnswer> findByInterviewIdAndEvaluationCompletedTrue(Long interviewId);
    
    /**
     * 根据面试ID查找未评估的答案
     */
    List<InterviewAnswer> findByInterviewIdAndEvaluationCompletedFalse(Long interviewId);
    
    /**
     * 查找高分答案
     */
    @Query("SELECT a FROM InterviewAnswer a WHERE a.score >= :minScore ORDER BY a.score DESC")
    List<InterviewAnswer> findHighScoreAnswers(@Param("minScore") Integer minScore);
    
    /**
     * 统计面试的答案完成情况
     */
    @Query("SELECT COUNT(a), SUM(CASE WHEN a.evaluationCompleted = true THEN 1 ELSE 0 END) " +
           "FROM InterviewAnswer a WHERE a.interviewId = :interviewId")
    Object[] getAnswerCompletionStats(@Param("interviewId") Long interviewId);
    
    /**
     * 计算面试的平均得分
     */
    @Query("SELECT AVG(a.score) FROM InterviewAnswer a WHERE a.interviewId = :interviewId AND a.score IS NOT NULL")
    Double calculateAverageScoreByInterview(@Param("interviewId") Long interviewId);
    
    /**
     * 计算问题的平均得分
     */
    @Query("SELECT AVG(a.score) FROM InterviewAnswer a WHERE a.questionId = :questionId AND a.score IS NOT NULL")
    Double calculateAverageScoreByQuestion(@Param("questionId") Long questionId);
    
    /**
     * 统计各评级的答案数量
     */
    @Query("SELECT a.rating, COUNT(a) FROM InterviewAnswer a GROUP BY a.rating")
    List<Object[]> countByRating();
    
    /**
     * 获取问题答案统计
     */
    @Query("SELECT a.questionId, COUNT(a), AVG(a.score), MAX(a.score), MIN(a.score) " +
           "FROM InterviewAnswer a WHERE a.score IS NOT NULL GROUP BY a.questionId")
    List<Object[]> getQuestionAnswerStats();
    
    /**
     * 查找特定面试中得分最高的答案
     */
    @Query("SELECT a FROM InterviewAnswer a WHERE a.interviewId = :interviewId " +
           "AND a.score = (SELECT MAX(a2.score) FROM InterviewAnswer a2 WHERE a2.interviewId = :interviewId)")
    List<InterviewAnswer> findTopAnswersByInterview(@Param("interviewId") Long interviewId);
    
    /**
     * 查找特定问题的最佳答案
     */
    @Query("SELECT a FROM InterviewAnswer a WHERE a.questionId = :questionId " +
           "ORDER BY a.score DESC, a.communicationQuality DESC")
    List<InterviewAnswer> findBestAnswersByQuestion(@Param("questionId") Long questionId);
    
    /**
     * 统计答案的详细评分分布
     */
    @Query("SELECT " +
           "AVG(a.score), AVG(a.confidenceLevel), AVG(a.communicationQuality), " +
           "AVG(a.depthOfKnowledge), AVG(a.practicalExperience) " +
           "FROM InterviewAnswer a WHERE a.interviewId = :interviewId")
    Object[] getDetailedScoreStats(@Param("interviewId") Long interviewId);
    
    /**
     * 查找需要AI分析的答案
     */
    @Query("SELECT a FROM InterviewAnswer a WHERE a.answer IS NOT NULL AND a.aiAnalysis IS NULL")
    List<InterviewAnswer> findAnswersNeedingAIAnalysis();
    
    /**
     * 查找有录音的答案
     */
    List<InterviewAnswer> findByRecordedAudioPathIsNotNull();
    
    /**
     * 查找有转录的答案
     */
    List<InterviewAnswer> findByTranscriptionIsNotNull();
    
    /**
     * 统计评估者的评估工作量
     */
    @Query("SELECT a.evaluatedBy, COUNT(a) FROM InterviewAnswer a " +
           "WHERE a.evaluationCompleted = true GROUP BY a.evaluatedBy")
    List<Object[]> getEvaluatorWorkload();
    
    /**
     * 查找评估时间超长的答案
     */
    @Query("SELECT a FROM InterviewAnswer a WHERE a.timeTakenMinutes > :threshold")
    List<InterviewAnswer> findLongTimeAnswers(@Param("threshold") Integer threshold);
    
    /**
     * 查找快速回答的答案（可能需要检查质量）
     */
    @Query("SELECT a FROM InterviewAnswer a WHERE a.timeTakenMinutes < :threshold AND a.timeTakenMinutes > 0")
    List<InterviewAnswer> findQuickAnswers(@Param("threshold") Integer threshold);
    
    /**
     * 批量更新评估状态
     */
    @Query("UPDATE InterviewAnswer a SET a.evaluationCompleted = :completed WHERE a.id IN :ids")
    void batchUpdateEvaluationStatus(@Param("ids") List<Long> ids, @Param("completed") Boolean completed);
    
    /**
     * 删除指定面试的所有答案
     */
    void deleteByInterviewId(Long interviewId);
    
    /**
     * 统计候选人在不同问题上的表现
     */
    @Query("SELECT q.category, AVG(a.score) FROM InterviewAnswer a " +
           "JOIN Interview i ON a.interviewId = i.id " +
           "JOIN InterviewQuestion q ON a.questionId = q.id " +
           "WHERE i.candidateId = :candidateId AND a.score IS NOT NULL " +
           "GROUP BY q.category")
    List<Object[]> getCandidatePerformanceByCategory(@Param("candidateId") Long candidateId);
    
    /**
     * 查找答题时间分布
     */
    @Query("SELECT " +
           "SUM(CASE WHEN a.timeTakenMinutes <= 5 THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN a.timeTakenMinutes > 5 AND a.timeTakenMinutes <= 10 THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN a.timeTakenMinutes > 10 AND a.timeTakenMinutes <= 15 THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN a.timeTakenMinutes > 15 THEN 1 ELSE 0 END) " +
           "FROM InterviewAnswer a WHERE a.timeTakenMinutes IS NOT NULL")
    Object[] getTimeDistribution();
}