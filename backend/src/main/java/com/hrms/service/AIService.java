package com.hrms.service;

import com.hrms.entity.Resume;
import com.hrms.entity.Job;

import java.util.Map;

public interface AIService {
    
    /**
     * 解析简历内容
     * @param filePath 简历文件路径
     * @param fileType 文件类型 (PDF, DOC, DOCX)
     * @return 解析后的结构化数据
     */
    Map<String, Object> parseResume(String filePath, String fileType);
    
    /**
     * 分析简历质量
     * @param resumeContent 简历内容
     * @return 质量评分和分析结果
     */
    Map<String, Object> analyzeResumeQuality(String resumeContent);
    
    /**
     * 计算简历与职位匹配度
     * @param resume 简历对象
     * @param job 职位对象
     * @return 匹配度评分和详细分析
     */
    Map<String, Object> calculateMatchScore(Resume resume, Job job);
    
    /**
     * 提取关键技能
     * @param content 内容文本
     * @return 技能列表和权重
     */
    Map<String, Object> extractSkills(String content);
    
    /**
     * 分析工作经验
     * @param experienceText 工作经验文本
     * @return 结构化的工作经验数据
     */
    Map<String, Object> analyzeWorkExperience(String experienceText);
    
    /**
     * 生成候选人画像
     * @param resume 简历对象
     * @return 候选人综合画像
     */
    Map<String, Object> generateCandidateProfile(Resume resume);
    
    /**
     * 智能推荐职位
     * @param candidateId 候选人ID
     * @return 推荐职位列表
     */
    Map<String, Object> recommendJobs(Long candidateId);
}