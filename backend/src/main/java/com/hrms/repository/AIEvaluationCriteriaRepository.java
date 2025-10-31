package com.hrms.repository;

import com.hrms.entity.AIEvaluationCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIEvaluationCriteriaRepository extends JpaRepository<AIEvaluationCriteria, Long> {
    
    /**
     * 根据类别查找评估标准
     */
    List<AIEvaluationCriteria> findByCategory(AIEvaluationCriteria.CriteriaCategory category);
    
    /**
     * 根据行业查找评估标准
     */
    List<AIEvaluationCriteria> findByIndustry(String industry);
    
    /**
     * 根据职级查找评估标准
     */
    List<AIEvaluationCriteria> findByJobLevel(String jobLevel);
    
    /**
     * 根据启用状态查找评估标准
     */
    List<AIEvaluationCriteria> findByIsEnabled(Boolean isEnabled);
    
    /**
     * 根据类别和启用状态查找评估标准
     */
    List<AIEvaluationCriteria> findByCategoryAndIsEnabled(AIEvaluationCriteria.CriteriaCategory category, Boolean isEnabled);
    
    /**
     * 根据行业和职级查找评估标准
     */
    List<AIEvaluationCriteria> findByIndustryAndJobLevel(String industry, String jobLevel);
    
    /**
     * 根据行业、职级和启用状态查找评估标准
     */
    List<AIEvaluationCriteria> findByIndustryAndJobLevelAndIsEnabled(String industry, String jobLevel, Boolean isEnabled);
    
    /**
     * 根据行业和启用状态查找评估标准
     */
    List<AIEvaluationCriteria> findByIndustryAndIsEnabled(String industry, Boolean isEnabled);
    
    /**
     * 查找所有启用的评估标准，按权重排序
     */
    @Query("SELECT c FROM AIEvaluationCriteria c WHERE c.isEnabled = true ORDER BY c.weight DESC")
    List<AIEvaluationCriteria> findEnabledCriteriaOrderByWeight();
    
    /**
     * 根据名称模糊查找评估标准
     */
    List<AIEvaluationCriteria> findByNameContainingIgnoreCase(String name);
    
    /**
     * 根据类别和行业查找评估标准
     */
    List<AIEvaluationCriteria> findByCategoryAndIndustry(AIEvaluationCriteria.CriteriaCategory category, String industry);
    
    /**
     * 查找特定权重范围的评估标准
     */
    @Query("SELECT c FROM AIEvaluationCriteria c WHERE c.weight BETWEEN :minWeight AND :maxWeight AND c.isEnabled = true")
    List<AIEvaluationCriteria> findByWeightRange(@Param("minWeight") Double minWeight, @Param("maxWeight") Double maxWeight);
    
    /**
     * 统计各类别的评估标准数量
     */
    @Query("SELECT c.category, COUNT(c) FROM AIEvaluationCriteria c WHERE c.isEnabled = true GROUP BY c.category")
    List<Object[]> countByCategory();
    
    /**
     * 统计各行业的评估标准数量
     */
    @Query("SELECT c.industry, COUNT(c) FROM AIEvaluationCriteria c WHERE c.isEnabled = true GROUP BY c.industry")
    List<Object[]> countByIndustry();
    
    /**
     * 查找最常用的评估标准（按使用频率）
     */
    @Query("SELECT c FROM AIEvaluationCriteria c WHERE c.isEnabled = true ORDER BY c.updatedAt DESC")
    List<AIEvaluationCriteria> findMostRecentlyUpdated();
    
    /**
     * 查找默认评估标准（无特定行业限制）
     */
    @Query("SELECT c FROM AIEvaluationCriteria c WHERE (c.industry IS NULL OR c.industry = '') AND c.isEnabled = true")
    List<AIEvaluationCriteria> findDefaultCriteria();
    
    /**
     * 批量更新启用状态
     */
    @Query("UPDATE AIEvaluationCriteria c SET c.isEnabled = :enabled WHERE c.id IN :ids")
    void batchUpdateEnabled(@Param("ids") List<Long> ids, @Param("enabled") Boolean enabled);
}