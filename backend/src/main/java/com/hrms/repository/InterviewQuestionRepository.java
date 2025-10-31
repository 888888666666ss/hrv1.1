package com.hrms.repository;

import com.hrms.entity.InterviewQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {
    
    /**
     * 根据类别查找面试问题
     */
    List<InterviewQuestion> findByCategory(InterviewQuestion.QuestionCategory category);
    
    /**
     * 根据难度级别查找问题
     */
    List<InterviewQuestion> findByDifficulty(InterviewQuestion.DifficultyLevel difficulty);
    
    /**
     * 根据部门查找问题
     */
    List<InterviewQuestion> findByDepartment(String department);
    
    /**
     * 根据职级查找问题
     */
    List<InterviewQuestion> findByPositionLevel(String positionLevel);
    
    /**
     * 查找启用的问题
     */
    List<InterviewQuestion> findByIsActiveTrue();
    
    /**
     * 根据创建者查找问题
     */
    List<InterviewQuestion> findByCreatedBy(Long createdBy);
    
    /**
     * 根据类别和难度查找问题
     */
    List<InterviewQuestion> findByCategoryAndDifficulty(InterviewQuestion.QuestionCategory category, 
                                                       InterviewQuestion.DifficultyLevel difficulty);
    
    /**
     * 根据部门和职级查找问题
     */
    List<InterviewQuestion> findByDepartmentAndPositionLevel(String department, String positionLevel);
    
    /**
     * 根据类别、部门和启用状态查找问题
     */
    List<InterviewQuestion> findByCategoryAndDepartmentAndIsActive(InterviewQuestion.QuestionCategory category, 
                                                                  String department, Boolean isActive);
    
    /**
     * 查找热门问题（使用次数最多）
     */
    @Query("SELECT q FROM InterviewQuestion q WHERE q.isActive = true ORDER BY q.usageCount DESC")
    List<InterviewQuestion> findPopularQuestions();
    
    /**
     * 查找高质量问题（平均得分高）
     */
    @Query("SELECT q FROM InterviewQuestion q WHERE q.isActive = true AND q.averageScore IS NOT NULL ORDER BY q.averageScore DESC")
    List<InterviewQuestion> findHighQualityQuestions();
    
    /**
     * 根据权重排序查找问题
     */
    @Query("SELECT q FROM InterviewQuestion q WHERE q.isActive = true ORDER BY q.weight DESC")
    List<InterviewQuestion> findQuestionsByWeight();
    
    /**
     * 模糊搜索问题内容
     */
    @Query("SELECT q FROM InterviewQuestion q WHERE q.isActive = true AND " +
           "(LOWER(q.question) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(q.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<InterviewQuestion> searchQuestions(@Param("keyword") String keyword);
    
    /**
     * 根据技能标签查找问题
     */
    @Query("SELECT q FROM InterviewQuestion q WHERE q.isActive = true AND " +
           "q.skills LIKE CONCAT('%', :skill, '%')")
    List<InterviewQuestion> findBySkill(@Param("skill") String skill);
    
    /**
     * 随机获取指定数量的问题
     */
    @Query(value = "SELECT * FROM interview_questions WHERE is_active = true ORDER BY RAND() LIMIT :count", 
           nativeQuery = true)
    List<InterviewQuestion> findRandomQuestions(@Param("count") Integer count);
    
    /**
     * 根据类别随机获取问题
     */
    @Query(value = "SELECT * FROM interview_questions WHERE is_active = true AND category = :category ORDER BY RAND() LIMIT :count", 
           nativeQuery = true)
    List<InterviewQuestion> findRandomQuestionsByCategory(@Param("category") String category, @Param("count") Integer count);
    
    /**
     * 统计各类别问题数量
     */
    @Query("SELECT q.category, COUNT(q) FROM InterviewQuestion q WHERE q.isActive = true GROUP BY q.category")
    List<Object[]> countByCategory();
    
    /**
     * 统计各难度级别问题数量
     */
    @Query("SELECT q.difficulty, COUNT(q) FROM InterviewQuestion q WHERE q.isActive = true GROUP BY q.difficulty")
    List<Object[]> countByDifficulty();
    
    /**
     * 统计各部门问题数量
     */
    @Query("SELECT q.department, COUNT(q) FROM InterviewQuestion q WHERE q.isActive = true GROUP BY q.department")
    List<Object[]> countByDepartment();
    
    /**
     * 获取问题使用统计
     */
    @Query("SELECT q.id, q.question, q.usageCount, q.averageScore FROM InterviewQuestion q " +
           "WHERE q.isActive = true ORDER BY q.usageCount DESC")
    List<Object[]> getQuestionUsageStats();
    
    /**
     * 查找未使用的问题
     */
    @Query("SELECT q FROM InterviewQuestion q WHERE q.isActive = true AND (q.usageCount IS NULL OR q.usageCount = 0)")
    List<InterviewQuestion> findUnusedQuestions();
    
    /**
     * 查找低质量问题（平均得分低）
     */
    @Query("SELECT q FROM InterviewQuestion q WHERE q.isActive = true AND q.averageScore IS NOT NULL AND q.averageScore < :threshold")
    List<InterviewQuestion> findLowQualityQuestions(@Param("threshold") Double threshold);
    
    /**
     * 更新问题使用次数
     */
    @Query("UPDATE InterviewQuestion q SET q.usageCount = q.usageCount + 1 WHERE q.id = :id")
    void incrementUsageCount(@Param("id") Long id);
    
    /**
     * 批量更新问题状态
     */
    @Query("UPDATE InterviewQuestion q SET q.isActive = :isActive WHERE q.id IN :ids")
    void batchUpdateActiveStatus(@Param("ids") List<Long> ids, @Param("isActive") Boolean isActive);
    
    /**
     * 获取问题评分统计
     */
    @Query("SELECT " +
           "AVG(q.averageScore), " +
           "MIN(q.averageScore), " +
           "MAX(q.averageScore), " +
           "COUNT(CASE WHEN q.averageScore >= 80 THEN 1 END), " +
           "COUNT(CASE WHEN q.averageScore < 60 THEN 1 END) " +
           "FROM InterviewQuestion q WHERE q.averageScore IS NOT NULL")
    Object[] getScoreStatistics();
    
    /**
     * 查找需要更新的问题（长时间未使用）
     */
    @Query("SELECT q FROM InterviewQuestion q WHERE q.isActive = true AND " +
           "q.updatedAt < :threshold ORDER BY q.updatedAt ASC")
    List<InterviewQuestion> findQuestionsNeedingUpdate(@Param("threshold") java.time.LocalDateTime threshold);
    
    /**
     * 按权重和使用次数综合排序
     */
    @Query("SELECT q FROM InterviewQuestion q WHERE q.isActive = true " +
           "ORDER BY (q.weight * 0.7 + COALESCE(q.usageCount, 0) * 0.3) DESC")
    List<InterviewQuestion> findQuestionsByScore();
}