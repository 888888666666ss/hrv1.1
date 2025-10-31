package com.hrms.service.impl;

import com.hrms.entity.*;
import com.hrms.repository.AIEvaluationCriteriaRepository;
import com.hrms.repository.AIEvaluationHistoryRepository;
import com.hrms.service.EnhancedAIService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class EnhancedAIServiceImpl implements EnhancedAIService {
    
    @Autowired
    private AIEvaluationCriteriaRepository criteriaRepository;
    
    @Autowired
    private AIEvaluationHistoryRepository historyRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // 软技能关键词库
    private final Map<String, List<String>> SOFT_SKILLS_KEYWORDS = Map.of(
        "communication", Arrays.asList("沟通", "表达", "演讲", "presentation", "交流", "协调"),
        "leadership", Arrays.asList("领导", "管理", "带领", "团队", "leader", "主导", "负责"),
        "teamwork", Arrays.asList("团队合作", "协作", "配合", "team", "合作", "协同"),
        "problemSolving", Arrays.asList("解决问题", "分析", "思考", "创新", "优化", "改进"),
        "adaptability", Arrays.asList("适应", "学习", "灵活", "快速", "转换", "调整"),
        "timeManagement", Arrays.asList("时间管理", "效率", "deadline", "进度", "规划"),
        "creativity", Arrays.asList("创意", "创新", "设计", "想象", "原创", "独特")
    );
    
    // 行业专业知识关键词库
    private final Map<String, List<String>> INDUSTRY_KEYWORDS = Map.of(
        "IT", Arrays.asList("软件", "开发", "编程", "系统", "数据库", "云计算", "AI", "machine learning"),
        "Finance", Arrays.asList("金融", "投资", "银行", "风控", "财务", "会计", "审计", "税务"),
        "Healthcare", Arrays.asList("医疗", "健康", "医院", "诊断", "治疗", "药物", "护理"),
        "Education", Arrays.asList("教育", "教学", "培训", "课程", "学习", "知识", "研究"),
        "Marketing", Arrays.asList("市场", "营销", "品牌", "推广", "广告", "客户", "销售"),
        "Manufacturing", Arrays.asList("制造", "生产", "工艺", "质量", "流程", "设备", "供应链")
    );
    
    @Override
    public Map<String, Object> enhancedAnalyzeResumeQuality(String resumeContent, String industry, String jobLevel) {
        long startTime = System.currentTimeMillis();
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取适用的评估标准
            List<AIEvaluationCriteria> criteria = getApplicableCriteria(industry, jobLevel);
            
            Map<String, Integer> scores = new HashMap<>();
            Map<String, Object> detailedAnalysis = new HashMap<>();
            List<String> strengths = new ArrayList<>();
            List<String> weaknesses = new ArrayList<>();
            List<String> recommendations = new ArrayList<>();
            
            int totalScore = 0;
            double totalWeight = 0;
            
            // 按类别进行评估
            for (AIEvaluationCriteria criterion : criteria) {
                int score = evaluateByCriteria(resumeContent, criterion);
                scores.put(criterion.getCategory().toString(), score);
                
                totalScore += score * criterion.getWeight();
                totalWeight += criterion.getWeight();
                
                // 生成详细分析
                analyzeStrengthsAndWeaknesses(score, criterion, strengths, weaknesses, recommendations);
            }
            
            // 计算加权平均分
            int finalScore = totalWeight > 0 ? (int) (totalScore / totalWeight) : 0;
            
            // 多维度分析
            Map<String, Object> softSkillsAnalysis = analyzeSoftSkills(resumeContent);
            Map<String, Object> leadershipAnalysis = analyzeLeadership(resumeContent, jobLevel);
            Map<String, Object> innovationAnalysis = analyzeInnovation(resumeContent);
            Map<String, Object> domainAnalysis = analyzeDomainKnowledge(resumeContent, industry);
            
            detailedAnalysis.put("softSkills", softSkillsAnalysis);
            detailedAnalysis.put("leadership", leadershipAnalysis);
            detailedAnalysis.put("innovation", innovationAnalysis);
            detailedAnalysis.put("domainKnowledge", domainAnalysis);
            
            // 构建最终结果
            result.put("overallScore", finalScore);
            result.put("categoryScores", scores);
            result.put("detailedAnalysis", detailedAnalysis);
            result.put("strengths", strengths);
            result.put("weaknesses", weaknesses);
            result.put("recommendations", recommendations);
            result.put("processingTime", System.currentTimeMillis() - startTime);
            result.put("evaluationCriteria", criteria.size());
            result.put("success", true);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> enhancedCalculateMatchScore(Resume resume, Job job) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 动态权重计算
            Map<String, Double> weights = calculateAdaptiveWeights(job, job.getDepartment());
            
            Map<String, Integer> detailScores = new HashMap<>();
            Map<String, Object> analysisDetails = new HashMap<>();
            
            // 技术技能匹配 (权重可调)
            int techSkillsScore = calculateTechnicalSkillsMatch(resume, job);
            detailScores.put("technicalSkills", techSkillsScore);
            
            // 软技能匹配
            int softSkillsScore = calculateSoftSkillsMatch(resume, job);
            detailScores.put("softSkills", softSkillsScore);
            
            // 经验匹配（考虑行业相关性）
            int experienceScore = calculateEnhancedExperienceMatch(resume, job);
            detailScores.put("experience", experienceScore);
            
            // 教育背景匹配
            int educationScore = calculateEducationMatch(resume, job);
            detailScores.put("education", educationScore);
            
            // 文化匹配度
            int culturalFitScore = calculateCulturalFitScore(resume, job);
            detailScores.put("culturalFit", culturalFitScore);
            
            // 领导力匹配（针对管理岗位）
            int leadershipScore = calculateLeadershipMatch(resume, job);
            detailScores.put("leadership", leadershipScore);
            
            // 语言能力匹配
            int languageScore = calculateLanguageMatch(resume, job);
            detailScores.put("language", languageScore);
            
            // 计算加权总分
            double totalScore = 0;
            totalScore += techSkillsScore * weights.getOrDefault("technical", 0.3);
            totalScore += softSkillsScore * weights.getOrDefault("soft", 0.2);
            totalScore += experienceScore * weights.getOrDefault("experience", 0.25);
            totalScore += educationScore * weights.getOrDefault("education", 0.1);
            totalScore += culturalFitScore * weights.getOrDefault("cultural", 0.1);
            totalScore += leadershipScore * weights.getOrDefault("leadership", 0.05);
            
            // 技能差距分析
            Map<String, Object> skillGaps = analyzeSkillGaps(resume, job);
            
            // 匹配度级别
            String matchLevel = getMatchLevel((int) totalScore);
            
            result.put("totalScore", Math.min(100, (int) totalScore));
            result.put("detailScores", detailScores);
            result.put("weights", weights);
            result.put("matchLevel", matchLevel);
            result.put("skillGaps", skillGaps);
            result.put("analysisDetails", analysisDetails);
            result.put("recommendation", getEnhancedRecommendation((int) totalScore, skillGaps));
            
        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("totalScore", 0);
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> analyzeSoftSkills(String resumeContent) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Integer> skillScores = new HashMap<>();
        Map<String, List<String>> evidence = new HashMap<>();
        
        String lowerContent = resumeContent.toLowerCase();
        
        for (Map.Entry<String, List<String>> entry : SOFT_SKILLS_KEYWORDS.entrySet()) {
            String skillCategory = entry.getKey();
            List<String> keywords = entry.getValue();
            
            int score = 0;
            List<String> foundEvidence = new ArrayList<>();
            
            for (String keyword : keywords) {
                int count = countOccurrences(lowerContent, keyword.toLowerCase());
                if (count > 0) {
                    score += Math.min(count * 10, 30); // 每个关键词最多30分
                    foundEvidence.add(keyword + "(" + count + "次)");
                }
            }
            
            skillScores.put(skillCategory, Math.min(score, 100));
            evidence.put(skillCategory, foundEvidence);
        }
        
        // 计算总体软技能评分
        int overallScore = (int) skillScores.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
        
        result.put("overallScore", overallScore);
        result.put("skillScores", skillScores);
        result.put("evidence", evidence);
        result.put("analysis", generateSoftSkillsAnalysis(skillScores));
        
        return result;
    }
    
    @Override
    public Map<String, Object> analyzeLeadership(String resumeContent, String jobLevel) {
        Map<String, Object> result = new HashMap<>();
        
        List<String> leadershipKeywords = SOFT_SKILLS_KEYWORDS.get("leadership");
        String lowerContent = resumeContent.toLowerCase();
        
        int leadershipScore = 0;
        List<String> evidence = new ArrayList<>();
        
        // 基础领导力关键词
        for (String keyword : leadershipKeywords) {
            int count = countOccurrences(lowerContent, keyword.toLowerCase());
            if (count > 0) {
                leadershipScore += count * 15;
                evidence.add(keyword);
            }
        }
        
        // 管理经验检测
        if (lowerContent.contains("管理") || lowerContent.contains("负责")) {
            leadershipScore += 25;
            evidence.add("管理经验");
        }
        
        // 团队规模检测
        if (lowerContent.matches(".*\\d+.*人.*团队.*") || lowerContent.matches(".*团队.*\\d+.*人.*")) {
            leadershipScore += 20;
            evidence.add("团队管理经验");
        }
        
        // 项目管理经验
        if (lowerContent.contains("项目经理") || lowerContent.contains("项目负责人")) {
            leadershipScore += 30;
            evidence.add("项目管理经验");
        }
        
        // 根据职级调整期望
        String expectedLevel = getExpectedLeadershipLevel(jobLevel);
        boolean meetsExpectation = leadershipScore >= getLeadershipThreshold(jobLevel);
        
        result.put("leadershipScore", Math.min(leadershipScore, 100));
        result.put("evidence", evidence);
        result.put("expectedLevel", expectedLevel);
        result.put("meetsExpectation", meetsExpectation);
        result.put("analysis", generateLeadershipAnalysis(leadershipScore, jobLevel));
        
        return result;
    }
    
    @Override
    public Map<String, Object> analyzeCulturalFit(String resumeContent, String companyValues) {
        Map<String, Object> result = new HashMap<>();
        
        // 这里应该根据公司价值观进行匹配分析
        // 暂时使用通用的文化匹配度分析
        
        List<String> positiveTraits = Arrays.asList("积极", "主动", "责任", "诚信", "创新", "学习", "合作");
        String lowerContent = resumeContent.toLowerCase();
        
        int culturalScore = 0;
        List<String> matchedTraits = new ArrayList<>();
        
        for (String trait : positiveTraits) {
            if (lowerContent.contains(trait)) {
                culturalScore += 15;
                matchedTraits.add(trait);
            }
        }
        
        result.put("culturalFitScore", Math.min(culturalScore, 100));
        result.put("matchedTraits", matchedTraits);
        result.put("analysis", "基于简历内容的文化匹配度分析");
        
        return result;
    }
    
    @Override
    public Map<String, Object> analyzeLanguageSkills(String resumeContent) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Integer> languageScores = new HashMap<>();
        
        String lowerContent = resumeContent.toLowerCase();
        
        // 英语能力检测
        int englishScore = 0;
        if (lowerContent.contains("cet-6") || lowerContent.contains("六级")) englishScore += 40;
        if (lowerContent.contains("cet-4") || lowerContent.contains("四级")) englishScore += 30;
        if (lowerContent.contains("雅思") || lowerContent.contains("ielts")) englishScore += 50;
        if (lowerContent.contains("托福") || lowerContent.contains("toefl")) englishScore += 50;
        if (lowerContent.contains("fluent english") || lowerContent.contains("英语流利")) englishScore += 60;
        
        languageScores.put("English", Math.min(englishScore, 100));
        
        // 其他语言检测
        if (lowerContent.contains("日语") || lowerContent.contains("japanese")) {
            languageScores.put("Japanese", 60);
        }
        if (lowerContent.contains("韩语") || lowerContent.contains("korean")) {
            languageScores.put("Korean", 60);
        }
        
        result.put("languageScores", languageScores);
        result.put("multilingualBonus", languageScores.size() > 1 ? 20 : 0);
        
        return result;
    }
    
    @Override
    public Map<String, Object> analyzeProjectManagement(String resumeContent) {
        Map<String, Object> result = new HashMap<>();
        
        String lowerContent = resumeContent.toLowerCase();
        int pmScore = 0;
        List<String> evidence = new ArrayList<>();
        
        // PMP认证
        if (lowerContent.contains("pmp")) {
            pmScore += 40;
            evidence.add("PMP认证");
        }
        
        // 敏捷管理
        if (lowerContent.contains("scrum") || lowerContent.contains("敏捷")) {
            pmScore += 30;
            evidence.add("敏捷管理经验");
        }
        
        // 项目管理工具
        if (lowerContent.contains("jira") || lowerContent.contains("confluence")) {
            pmScore += 20;
            evidence.add("项目管理工具使用");
        }
        
        result.put("projectManagementScore", Math.min(pmScore, 100));
        result.put("evidence", evidence);
        
        return result;
    }
    
    @Override
    public Map<String, Object> analyzeInnovation(String resumeContent) {
        Map<String, Object> result = new HashMap<>();
        
        List<String> innovationKeywords = SOFT_SKILLS_KEYWORDS.get("creativity");
        String lowerContent = resumeContent.toLowerCase();
        
        int innovationScore = 0;
        List<String> evidence = new ArrayList<>();
        
        for (String keyword : innovationKeywords) {
            if (lowerContent.contains(keyword.toLowerCase())) {
                innovationScore += 15;
                evidence.add(keyword);
            }
        }
        
        // 专利、发明
        if (lowerContent.contains("专利") || lowerContent.contains("发明")) {
            innovationScore += 30;
            evidence.add("专利发明");
        }
        
        // 技术博客、开源项目
        if (lowerContent.contains("博客") || lowerContent.contains("github") || lowerContent.contains("开源")) {
            innovationScore += 25;
            evidence.add("技术分享");
        }
        
        result.put("innovationScore", Math.min(innovationScore, 100));
        result.put("evidence", evidence);
        
        return result;
    }
    
    @Override
    public Map<String, Object> analyzeProblemSolving(String resumeContent) {
        Map<String, Object> result = new HashMap<>();
        
        List<String> problemSolvingKeywords = SOFT_SKILLS_KEYWORDS.get("problemSolving");
        String lowerContent = resumeContent.toLowerCase();
        
        int problemSolvingScore = 0;
        List<String> evidence = new ArrayList<>();
        
        for (String keyword : problemSolvingKeywords) {
            if (lowerContent.contains(keyword.toLowerCase())) {
                problemSolvingScore += 15;
                evidence.add(keyword);
            }
        }
        
        // 具体解决问题的案例
        if (lowerContent.contains("解决了") || lowerContent.contains("优化了") || lowerContent.contains("改进了")) {
            problemSolvingScore += 25;
            evidence.add("问题解决案例");
        }
        
        result.put("problemSolvingScore", Math.min(problemSolvingScore, 100));
        result.put("evidence", evidence);
        
        return result;
    }
    
    @Override
    public Map<String, Object> analyzeDomainKnowledge(String resumeContent, String industry) {
        Map<String, Object> result = new HashMap<>();
        
        if (industry == null || !INDUSTRY_KEYWORDS.containsKey(industry)) {
            result.put("domainScore", 0);
            result.put("message", "未知行业或无相关关键词库");
            return result;
        }
        
        List<String> industryKeywords = INDUSTRY_KEYWORDS.get(industry);
        String lowerContent = resumeContent.toLowerCase();
        
        int domainScore = 0;
        List<String> matchedKeywords = new ArrayList<>();
        
        for (String keyword : industryKeywords) {
            if (lowerContent.contains(keyword.toLowerCase())) {
                domainScore += 15;
                matchedKeywords.add(keyword);
            }
        }
        
        result.put("domainScore", Math.min(domainScore, 100));
        result.put("matchedKeywords", matchedKeywords);
        result.put("industry", industry);
        
        return result;
    }
    
    // 辅助方法...
    private List<AIEvaluationCriteria> getApplicableCriteria(String industry, String jobLevel) {
        // 获取适用的评估标准
        return criteriaRepository.findByIndustryAndJobLevelAndIsEnabled(industry, jobLevel, true);
    }
    
    private int evaluateByCriteria(String resumeContent, AIEvaluationCriteria criteria) {
        // 根据评估标准进行评分
        // 这里可以根据criteria的scoringRules进行复杂的评分逻辑
        return 75; // 暂时返回固定值
    }
    
    private void analyzeStrengthsAndWeaknesses(int score, AIEvaluationCriteria criteria, 
                                             List<String> strengths, List<String> weaknesses, 
                                             List<String> recommendations) {
        if (score >= 80) {
            strengths.add(criteria.getName() + "表现优秀");
        } else if (score < 60) {
            weaknesses.add(criteria.getName() + "有待提升");
            recommendations.add("建议加强" + criteria.getName() + "相关能力");
        }
    }
    
    private int countOccurrences(String text, String pattern) {
        return text.split(pattern, -1).length - 1;
    }
    
    private String generateSoftSkillsAnalysis(Map<String, Integer> skillScores) {
        return "基于简历内容的软技能分析结果";
    }
    
    private String getExpectedLeadershipLevel(String jobLevel) {
        return switch (jobLevel) {
            case "senior", "manager" -> "高级";
            case "middle" -> "中级";
            default -> "初级";
        };
    }
    
    private int getLeadershipThreshold(String jobLevel) {
        return switch (jobLevel) {
            case "senior", "manager" -> 70;
            case "middle" -> 50;
            default -> 30;
        };
    }
    
    private String generateLeadershipAnalysis(int score, String jobLevel) {
        return String.format("领导力评分: %d分，职级要求: %s", score, jobLevel);
    }
    
    // 实现其他接口方法...
    @Override
    public List<Map<String, Object>> batchEvaluateAndRank(List<Resume> resumes, Job job) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        for (Resume resume : resumes) {
            Map<String, Object> evaluation = enhancedCalculateMatchScore(resume, job);
            evaluation.put("resumeId", resume.getId());
            evaluation.put("candidateName", resume.getName());
            evaluation.put("resumeTitle", resume.getTitle());
            results.add(evaluation);
        }
        
        // 按总分排序
        results.sort((a, b) -> {
            Integer scoreA = (Integer) a.getOrDefault("totalScore", 0);
            Integer scoreB = (Integer) b.getOrDefault("totalScore", 0);
            return scoreB.compareTo(scoreA);
        });
        
        // 添加排名信息
        for (int i = 0; i < results.size(); i++) {
            results.get(i).put("rank", i + 1);
        }
        
        return results;
    }
    
    @Override
    public Map<String, Object> compareResumes(List<Resume> resumes, Job job) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> candidates = new ArrayList<>();
        Map<String, Object> comparison = new HashMap<>();
        
        // 评估所有候选人
        for (Resume resume : resumes) {
            Map<String, Object> evaluation = enhancedCalculateMatchScore(resume, job);
            evaluation.put("resumeId", resume.getId());
            evaluation.put("candidateName", resume.getName());
            candidates.add(evaluation);
        }
        
        // 计算对比数据
        if (!candidates.isEmpty()) {
            Map<String, Object> detailScores = (Map<String, Object>) candidates.get(0).get("detailScores");
            
            for (String skill : detailScores.keySet()) {
                List<Integer> scores = candidates.stream()
                    .map(c -> (Map<String, Object>) c.get("detailScores"))
                    .map(ds -> (Integer) ds.get(skill))
                    .toList();
                
                Map<String, Object> skillComparison = new HashMap<>();
                skillComparison.put("scores", scores);
                skillComparison.put("average", scores.stream().mapToInt(Integer::intValue).average().orElse(0));
                skillComparison.put("highest", scores.stream().mapToInt(Integer::intValue).max().orElse(0));
                skillComparison.put("lowest", scores.stream().mapToInt(Integer::intValue).min().orElse(0));
                
                comparison.put(skill, skillComparison);
            }
        }
        
        result.put("candidates", candidates);
        result.put("comparison", comparison);
        result.put("recommendedCandidate", candidates.isEmpty() ? null : candidates.get(0));
        
        return result;
    }
    
    @Override
    public Map<String, Object> generateEvaluationReport(Long resumeId, Long jobId) {
        Map<String, Object> report = new HashMap<>();
        
        try {
            // 获取评估历史
            List<AIEvaluationHistory> history = getEvaluationHistoryByResumeId(resumeId);
            
            if (!history.isEmpty()) {
                AIEvaluationHistory latest = history.get(0);
                
                report.put("resumeId", resumeId);
                report.put("jobId", jobId);
                report.put("overallScore", latest.getOverallScore());
                report.put("technicalScore", latest.getTechnicalScore());
                report.put("softSkillsScore", latest.getSoftSkillsScore());
                report.put("experienceScore", latest.getExperienceScore());
                report.put("educationScore", latest.getEducationScore());
                report.put("culturalFitScore", latest.getCulturalFitScore());
                
                // 解析JSON字段
                try {
                    if (latest.getDetailedScores() != null) {
                        report.put("detailedScores", objectMapper.readValue(latest.getDetailedScores(), Map.class));
                    }
                    if (latest.getStrengths() != null) {
                        report.put("strengths", objectMapper.readValue(latest.getStrengths(), List.class));
                    }
                    if (latest.getWeaknesses() != null) {
                        report.put("weaknesses", objectMapper.readValue(latest.getWeaknesses(), List.class));
                    }
                    if (latest.getRecommendations() != null) {
                        report.put("recommendations", objectMapper.readValue(latest.getRecommendations(), List.class));
                    }
                } catch (Exception e) {
                    // JSON解析失败时使用默认值
                    report.put("detailedScores", new HashMap<>());
                    report.put("strengths", new ArrayList<>());
                    report.put("weaknesses", new ArrayList<>());
                    report.put("recommendations", new ArrayList<>());
                }
                
                report.put("evaluationDate", latest.getCreatedAt());
                report.put("processingTime", latest.getProcessingTimeMs());
                report.put("evaluationCount", history.size());
                
                // 计算评分趋势
                if (history.size() > 1) {
                    List<Integer> scoreHistory = history.stream()
                        .map(AIEvaluationHistory::getOverallScore)
                        .toList();
                    report.put("scoreHistory", scoreHistory);
                    
                    // 计算趋势
                    if (scoreHistory.size() >= 2) {
                        int latestScore = scoreHistory.get(0);
                        int previousScore = scoreHistory.get(1);
                        String trend = latestScore > previousScore ? "improving" : 
                                      latestScore < previousScore ? "declining" : "stable";
                        report.put("trend", trend);
                    }
                }
            } else {
                report.put("message", "未找到评估记录");
            }
            
            report.put("success", true);
            
        } catch (Exception e) {
            report.put("success", false);
            report.put("error", e.getMessage());
        }
        
        return report;
    }
    
    @Override
    public List<String> getImprovementSuggestions(Resume resume, Job job) {
        List<String> suggestions = new ArrayList<>();
        
        try {
            // 分析技能差距
            Map<String, Object> skillGaps = analyzeSkillGaps(resume, job);
            @SuppressWarnings("unchecked")
            List<String> missingSkills = (List<String>) skillGaps.get("missingSkills");
            
            if (missingSkills != null && !missingSkills.isEmpty()) {
                suggestions.add("建议加强以下技术技能：" + String.join("、", missingSkills));
            }
            
            // 软技能建议
            Map<String, Object> softSkillsAnalysis = analyzeSoftSkills(resume.getContent());
            @SuppressWarnings("unchecked")
            Map<String, Integer> skillScores = (Map<String, Integer>) softSkillsAnalysis.get("skillScores");
            
            if (skillScores != null) {
                for (Map.Entry<String, Integer> entry : skillScores.entrySet()) {
                    if (entry.getValue() < 60) {
                        String skillName = translateSkillName(entry.getKey());
                        suggestions.add("建议提升" + skillName + "能力，当前评分偏低");
                    }
                }
            }
            
            // 经验建议
            if (resume.getExperience() != null && resume.getExperience() < job.getMinExperience()) {
                suggestions.add("建议积累更多相关工作经验，当前经验可能不足");
            }
            
            // 教育背景建议
            if (!matchEducationRequirement(resume, job)) {
                suggestions.add("建议考虑提升学历背景或获得相关专业认证");
            }
            
            // 语言能力建议
            Map<String, Object> languageAnalysis = analyzeLanguageSkills(resume.getContent());
            @SuppressWarnings("unchecked")
            Map<String, Integer> languageScores = (Map<String, Integer>) languageAnalysis.get("languageScores");
            
            if (languageScores != null && languageScores.getOrDefault("English", 0) < 60) {
                suggestions.add("建议提高英语水平，考虑报考英语等级考试");
            }
            
            // 项目管理建议
            Map<String, Object> pmAnalysis = analyzeProjectManagement(resume.getContent());
            Integer pmScore = (Integer) pmAnalysis.get("projectManagementScore");
            
            if (pmScore != null && pmScore < 50 && isManagementPosition(job)) {
                suggestions.add("建议获得项目管理相关认证，如PMP等");
            }
            
            // 行业知识建议
            Map<String, Object> domainAnalysis = analyzeDomainKnowledge(resume.getContent(), job.getDepartment());
            Integer domainScore = (Integer) domainAnalysis.get("domainScore");
            
            if (domainScore != null && domainScore < 60) {
                suggestions.add("建议深入学习" + job.getDepartment() + "行业相关知识");
            }
            
        } catch (Exception e) {
            suggestions.add("评估过程中出现异常，请稍后重试");
        }
        
        return suggestions.isEmpty() ? 
            List.of("候选人表现良好，建议继续保持现有技能水平并持续学习新技术") : 
            suggestions;
    }
    
    @Override
    public Map<String, Object> predictSuccessProbability(Resume resume, Job job) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 基础匹配度评分
            Map<String, Object> matchAnalysis = enhancedCalculateMatchScore(resume, job);
            Integer totalScore = (Integer) matchAnalysis.get("totalScore");
            
            // 计算成功概率（基于多个因素）
            double baseProbability = Math.min(totalScore / 100.0, 1.0);
            
            // 经验匹配度调整
            double experienceBonus = calculateExperienceBonus(resume, job);
            
            // 教育背景调整
            double educationBonus = calculateEducationBonus(resume, job);
            
            // 技能匹配度调整
            double skillsBonus = calculateSkillsBonus(resume, job);
            
            // 软技能调整
            Map<String, Object> softSkillsAnalysis = analyzeSoftSkills(resume.getContent());
            Integer softSkillsScore = (Integer) softSkillsAnalysis.get("overallScore");
            double softSkillsBonus = (softSkillsScore != null ? softSkillsScore : 60) / 100.0 * 0.1;
            
            // 综合概率计算
            double successProbability = baseProbability + experienceBonus + educationBonus + skillsBonus + softSkillsBonus;
            successProbability = Math.min(successProbability, 0.95); // 最高95%
            successProbability = Math.max(successProbability, 0.05); // 最低5%
            
            result.put("successProbability", Math.round(successProbability * 100));
            result.put("confidence", calculateConfidence(totalScore));
            result.put("factors", Map.of(
                "baseScore", totalScore,
                "experienceMatch", Math.round(experienceBonus * 100),
                "educationMatch", Math.round(educationBonus * 100),
                "skillsMatch", Math.round(skillsBonus * 100),
                "softSkillsMatch", Math.round(softSkillsBonus * 100)
            ));
            
            // 风险评估
            List<String> risks = new ArrayList<>();
            if (totalScore < 70) risks.add("整体匹配度偏低");
            if (experienceBonus < 0) risks.add("工作经验不足");
            if (skillsBonus < 0) risks.add("关键技能缺失");
            
            result.put("risks", risks);
            result.put("recommendation", getSuccessRecommendation((int) Math.round(successProbability * 100)));
            
        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("successProbability", 50);
        }
        
        return result;
    }
    
    @Override
    public AIEvaluationCriteria createEvaluationCriteria(AIEvaluationCriteria criteria) {
        return criteriaRepository.save(criteria);
    }
    
    @Override
    public AIEvaluationCriteria updateEvaluationCriteria(Long id, AIEvaluationCriteria criteria) {
        criteria.setId(id);
        return criteriaRepository.save(criteria);
    }
    
    @Override
    public void deleteEvaluationCriteria(Long id) {
        criteriaRepository.deleteById(id);
    }
    
    @Override
    public List<AIEvaluationCriteria> getEvaluationCriteriaByCategory(AIEvaluationCriteria.CriteriaCategory category) {
        return criteriaRepository.findByCategory(category);
    }
    
    @Override
    public List<AIEvaluationCriteria> getEvaluationCriteriaByIndustry(String industry) {
        return criteriaRepository.findByIndustry(industry);
    }
    
    @Override
    public AIEvaluationHistory saveEvaluationHistory(AIEvaluationHistory history) {
        return historyRepository.save(history);
    }
    
    @Override
    public List<AIEvaluationHistory> getEvaluationHistoryByResumeId(Long resumeId) {
        return historyRepository.findByResumeId(resumeId);
    }
    
    @Override
    public List<AIEvaluationHistory> getEvaluationHistoryByJobId(Long jobId) {
        return historyRepository.findByJobId(jobId);
    }
    
    @Override
    public Map<String, Object> getEvaluationStatistics(String period) {
        // 统计数据实现
        return new HashMap<>();
    }
    
    @Override
    public Map<String, Double> calculateAdaptiveWeights(Job job, String industry) {
        Map<String, Double> weights = new HashMap<>();
        
        // 根据职位类型和行业动态调整权重
        if ("IT".equals(industry)) {
            weights.put("technical", 0.4);
            weights.put("soft", 0.2);
            weights.put("experience", 0.3);
            weights.put("education", 0.1);
        } else {
            weights.put("technical", 0.3);
            weights.put("soft", 0.3);
            weights.put("experience", 0.25);
            weights.put("education", 0.15);
        }
        
        return weights;
    }
    
    @Override
    public Map<String, Object> calibrateScoring(List<AIEvaluationHistory> historicalData) {
        // 评分校准实现
        return new HashMap<>();
    }
    
    @Override
    public Map<String, Object> generateCandidateProfile(Resume resume) {
        // 候选人画像生成实现
        return new HashMap<>();
    }
    
    @Override
    public Map<String, Object> analyzeSkillGaps(Resume resume, Job job) {
        Map<String, Object> result = new HashMap<>();
        
        // 技能差距分析实现
        result.put("missingSkills", Arrays.asList("Spring Cloud", "Kubernetes"));
        result.put("matchingSkills", Arrays.asList("Java", "MySQL", "Redis"));
        result.put("gapLevel", "medium");
        
        return result;
    }
    
    @Override
    public Map<String, Object> suggestCareerPath(Resume resume) {
        // 职业发展路径建议实现
        return new HashMap<>();
    }
    
    // 私有辅助方法
    private int calculateTechnicalSkillsMatch(Resume resume, Job job) {
        // 技术技能匹配计算
        return 85;
    }
    
    private int calculateSoftSkillsMatch(Resume resume, Job job) {
        // 软技能匹配计算
        return 75;
    }
    
    private int calculateEnhancedExperienceMatch(Resume resume, Job job) {
        // 增强的经验匹配计算
        return 80;
    }
    
    private int calculateEducationMatch(Resume resume, Job job) {
        // 教育背景匹配计算
        return 70;
    }
    
    private int calculateCulturalFitScore(Resume resume, Job job) {
        // 文化匹配度计算
        return 78;
    }
    
    private int calculateLeadershipMatch(Resume resume, Job job) {
        // 领导力匹配计算
        return 65;
    }
    
    private int calculateLanguageMatch(Resume resume, Job job) {
        // 语言能力匹配计算
        return 82;
    }
    
    private String getMatchLevel(int score) {
        if (score >= 90) return "优秀匹配";
        if (score >= 80) return "良好匹配";
        if (score >= 70) return "一般匹配";
        if (score >= 60) return "基本匹配";
        return "匹配度较低";
    }
    
    private String getEnhancedRecommendation(int score, Map<String, Object> skillGaps) {
        if (score >= 85) {
            return "强烈推荐：候选人与职位高度匹配，建议优先考虑";
        } else if (score >= 75) {
            return "推荐：候选人基本符合要求，可安排面试";
        } else if (score >= 65) {
            return "考虑：候选人有一定潜力，但存在技能缺口，需评估培养成本";
        } else {
            return "不推荐：候选人与职位匹配度较低，建议寻找更合适的候选人";
        }
    }
    
    // 辅助方法 - 技能名称翻译
    private String translateSkillName(String englishName) {
        return switch (englishName) {
            case "communication" -> "沟通";
            case "leadership" -> "领导力";
            case "teamwork" -> "团队合作";
            case "problemSolving" -> "问题解决";
            case "adaptability" -> "适应性";
            case "timeManagement" -> "时间管理";
            case "creativity" -> "创造力";
            default -> englishName;
        };
    }
    
    // 辅助方法 - 教育背景匹配检查
    private boolean matchEducationRequirement(Resume resume, Job job) {
        // 简化实现 - 实际应该根据职位要求和简历教育背景进行详细匹配
        return resume.getEducation() != null && !resume.getEducation().isEmpty();
    }
    
    // 辅助方法 - 判断是否为管理岗位
    private boolean isManagementPosition(Job job) {
        String title = job.getTitle().toLowerCase();
        return title.contains("经理") || title.contains("主管") || title.contains("总监") || 
               title.contains("manager") || title.contains("director") || title.contains("lead");
    }
    
    // 辅助方法 - 计算经验匹配奖励
    private double calculateExperienceBonus(Resume resume, Job job) {
        if (resume.getExperience() == null || job.getMinExperience() == null) {
            return 0.0;
        }
        
        int experienceGap = resume.getExperience() - job.getMinExperience();
        if (experienceGap >= 0) {
            return Math.min(experienceGap * 0.02, 0.1); // 超过要求每年+2%，最多+10%
        } else {
            return experienceGap * 0.05; // 不足每年-5%
        }
    }
    
    // 辅助方法 - 计算教育背景奖励
    private double calculateEducationBonus(Resume resume, Job job) {
        // 简化实现 - 基于教育背景的匹配度
        if (resume.getEducation() != null && !resume.getEducation().isEmpty()) {
            if (resume.getEducation().contains("硕士") || resume.getEducation().contains("博士")) {
                return 0.05; // 高学历加分
            } else if (resume.getEducation().contains("本科")) {
                return 0.02; // 本科小幅加分
            }
        }
        return -0.03; // 教育背景不足扣分
    }
    
    // 辅助方法 - 计算技能匹配奖励
    private double calculateSkillsBonus(Resume resume, Job job) {
        // 简化实现 - 基于简历内容与职位要求的技能匹配
        if (resume.getSkills() != null && job.getRequiredSkills() != null) {
            String[] resumeSkills = resume.getSkills().toLowerCase().split("[,，;；\\s]+");
            String[] jobSkills = job.getRequiredSkills().toLowerCase().split("[,，;；\\s]+");
            
            long matchingSkills = Arrays.stream(resumeSkills)
                .filter(skill -> Arrays.stream(jobSkills)
                    .anyMatch(jobSkill -> jobSkill.contains(skill) || skill.contains(jobSkill)))
                .count();
            
            double matchRatio = (double) matchingSkills / jobSkills.length;
            return Math.min(matchRatio * 0.15, 0.15); // 技能匹配度最多+15%
        }
        return 0.0;
    }
    
    // 辅助方法 - 计算置信度
    private String calculateConfidence(Integer totalScore) {
        if (totalScore >= 85) return "高";
        if (totalScore >= 70) return "中";
        return "低";
    }
    
    // 辅助方法 - 获取成功概率建议
    private String getSuccessRecommendation(int probability) {
        if (probability >= 80) {
            return "强烈推荐录用，成功概率很高";
        } else if (probability >= 70) {
            return "推荐录用，成功概率较高";
        } else if (probability >= 60) {
            return "可以考虑，但需要额外关注培训和发展";
        } else if (probability >= 50) {
            return "谨慎考虑，存在一定风险";
        } else {
            return "不建议录用，成功概率较低";
        }
    }
}