package com.hrms.service;

import com.hrms.entity.Resume;
import com.hrms.entity.Job;
import com.hrms.entity.AIEvaluationCriteria;
import com.hrms.entity.AIEvaluationHistory;

import java.util.List;
import java.util.Map;

public interface EnhancedAIService {
    
    /**
     * 增强版简历质量分析
     */
    Map<String, Object> enhancedAnalyzeResumeQuality(String resumeContent, String industry, String jobLevel);
    
    /**
     * 多维度匹配度计算
     */
    Map<String, Object> enhancedCalculateMatchScore(Resume resume, Job job);
    
    /**
     * 软技能评估
     */
    Map<String, Object> analyzeSoftSkills(String resumeContent);
    
    /**
     * 领导力评估
     */
    Map<String, Object> analyzeLeadership(String resumeContent, String jobLevel);
    
    /**
     * 文化匹配度评估
     */
    Map<String, Object> analyzeCulturalFit(String resumeContent, String companyValues);
    
    /**
     * 语言能力评估
     */
    Map<String, Object> analyzeLanguageSkills(String resumeContent);
    
    /**
     * 项目管理能力评估
     */
    Map<String, Object> analyzeProjectManagement(String resumeContent);
    
    /**
     * 创新能力评估
     */
    Map<String, Object> analyzeInnovation(String resumeContent);
    
    /**
     * 问题解决能力评估
     */
    Map<String, Object> analyzeProblemSolving(String resumeContent);
    
    /**
     * 行业专业知识评估
     */
    Map<String, Object> analyzeDomainKnowledge(String resumeContent, String industry);
    
    /**
     * 批量候选人评估和排序
     */
    List<Map<String, Object>> batchEvaluateAndRank(List<Resume> resumes, Job job);
    
    /**
     * 候选人对比分析
     */
    Map<String, Object> compareResumes(List<Resume> resumes, Job job);
    
    /**
     * 生成评估报告
     */
    Map<String, Object> generateEvaluationReport(Long resumeId, Long jobId);
    
    /**
     * 获取改进建议
     */
    List<String> getImprovementSuggestions(Resume resume, Job job);
    
    /**
     * 预测候选人成功概率
     */
    Map<String, Object> predictSuccessProbability(Resume resume, Job job);
    
    /**
     * 评估标准管理
     */
    AIEvaluationCriteria createEvaluationCriteria(AIEvaluationCriteria criteria);
    AIEvaluationCriteria updateEvaluationCriteria(Long id, AIEvaluationCriteria criteria);
    void deleteEvaluationCriteria(Long id);
    List<AIEvaluationCriteria> getEvaluationCriteriaByCategory(AIEvaluationCriteria.CriteriaCategory category);
    List<AIEvaluationCriteria> getEvaluationCriteriaByIndustry(String industry);
    
    /**
     * 评估历史管理
     */
    AIEvaluationHistory saveEvaluationHistory(AIEvaluationHistory history);
    List<AIEvaluationHistory> getEvaluationHistoryByResumeId(Long resumeId);
    List<AIEvaluationHistory> getEvaluationHistoryByJobId(Long jobId);
    Map<String, Object> getEvaluationStatistics(String period);
    
    /**
     * 自适应权重调整
     */
    Map<String, Double> calculateAdaptiveWeights(Job job, String industry);
    
    /**
     * 基于机器学习的评分校准
     */
    Map<String, Object> calibrateScoring(List<AIEvaluationHistory> historicalData);
    
    /**
     * 生成候选人画像
     */
    Map<String, Object> generateCandidateProfile(Resume resume);
    
    /**
     * 技能差距分析
     */
    Map<String, Object> analyzeSkillGaps(Resume resume, Job job);
    
    /**
     * 职业发展路径建议
     */
    Map<String, Object> suggestCareerPath(Resume resume);
}