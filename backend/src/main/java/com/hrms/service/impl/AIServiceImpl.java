package com.hrms.service.impl;

import com.hrms.entity.Resume;
import com.hrms.entity.Job;
import com.hrms.service.AIService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
public class AIServiceImpl implements AIService {
    
    @Value("${ai.openai.api-key:}")
    private String openaiApiKey;
    
    @Value("${ai.openai.base-url:https://api.openai.com/v1}")
    private String openaiBaseUrl;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // 技能关键词库
    private final Set<String> SKILL_KEYWORDS = Set.of(
        // 编程语言
        "Java", "Python", "JavaScript", "TypeScript", "C++", "C#", "Go", "Rust", "PHP", "Ruby",
        // 前端技术
        "React", "Vue", "Angular", "HTML", "CSS", "jQuery", "Bootstrap", "Webpack", "Vite",
        // 后端技术
        "Spring", "SpringBoot", "Node.js", "Express", "Django", "Flask", "Laravel", "Rails",
        // 数据库
        "MySQL", "PostgreSQL", "MongoDB", "Redis", "Oracle", "SQL Server", "SQLite",
        // 云服务
        "AWS", "Azure", "阿里云", "腾讯云", "Docker", "Kubernetes", "Jenkins",
        // 其他技能
        "Git", "Linux", "Nginx", "Apache", "Elasticsearch", "Kafka", "RabbitMQ"
    );
    
    @Override
    public Map<String, Object> parseResume(String filePath, String fileType) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 这里应该实现实际的文件解析逻辑
            // 为演示目的，我们返回模拟数据
            String content = extractTextFromFile(filePath, fileType);
            
            // 使用正则表达式提取基本信息
            Map<String, Object> basicInfo = extractBasicInfo(content);
            Map<String, Object> education = extractEducation(content);
            Map<String, Object> experience = extractExperience(content);
            Map<String, Object> skills = extractSkills(content);
            
            result.put("basicInfo", basicInfo);
            result.put("education", education);
            result.put("experience", experience);
            result.put("skills", skills);
            result.put("rawContent", content);
            result.put("success", true);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> analyzeResumeQuality(String resumeContent) {
        Map<String, Object> result = new HashMap<>();
        
        int score = 0;
        List<String> suggestions = new ArrayList<>();
        Map<String, Integer> scores = new HashMap<>();
        
        // 1. 完整性评分 (30分)
        int completenessScore = evaluateCompleteness(resumeContent);
        scores.put("completeness", completenessScore);
        score += completenessScore;
        if (completenessScore < 20) {
            suggestions.add("简历信息不够完整，建议补充联系方式、教育背景、工作经验等");
        }
        
        // 2. 技能匹配度 (25分)
        int skillScore = evaluateSkills(resumeContent);
        scores.put("skills", skillScore);
        score += skillScore;
        if (skillScore < 15) {
            suggestions.add("技能描述较少，建议详细列出掌握的技术栈和工具");
        }
        
        // 3. 工作经验 (25分)
        int experienceScore = evaluateExperience(resumeContent);
        scores.put("experience", experienceScore);
        score += experienceScore;
        if (experienceScore < 15) {
            suggestions.add("工作经验描述不够详细，建议补充具体项目经历和成果");
        }
        
        // 4. 格式规范性 (20分)
        int formatScore = evaluateFormat(resumeContent);
        scores.put("format", formatScore);
        score += formatScore;
        if (formatScore < 12) {
            suggestions.add("简历格式需要优化，建议使用清晰的段落结构");
        }
        
        result.put("totalScore", score);
        result.put("detailScores", scores);
        result.put("suggestions", suggestions);
        result.put("level", getQualityLevel(score));
        
        return result;
    }
    
    @Override
    public Map<String, Object> calculateMatchScore(Resume resume, Job job) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 解析简历内容
            Map<String, Object> resumeData = parseResumeContent(resume.getParsedContent());
            
            int totalScore = 0;
            Map<String, Object> detailScores = new HashMap<>();
            List<String> matchedSkills = new ArrayList<>();
            List<String> missingSkills = new ArrayList<>();
            
            // 1. 技能匹配 (40%)
            int skillMatch = calculateSkillMatch(resumeData, job, matchedSkills, missingSkills);
            detailScores.put("skills", skillMatch);
            totalScore += (int)(skillMatch * 0.4);
            
            // 2. 经验匹配 (30%)
            int experienceMatch = calculateExperienceMatch(resumeData, job);
            detailScores.put("experience", experienceMatch);
            totalScore += (int)(experienceMatch * 0.3);
            
            // 3. 教育背景匹配 (20%)
            int educationMatch = calculateEducationMatch(resumeData, job);
            detailScores.put("education", educationMatch);
            totalScore += (int)(educationMatch * 0.2);
            
            // 4. 期望薪资匹配 (10%)
            int salaryMatch = calculateSalaryMatch(resume, job);
            detailScores.put("salary", salaryMatch);
            totalScore += (int)(salaryMatch * 0.1);
            
            result.put("totalScore", Math.min(100, totalScore));
            result.put("detailScores", detailScores);
            result.put("matchedSkills", matchedSkills);
            result.put("missingSkills", missingSkills);
            result.put("recommendation", getRecommendation(totalScore));
            
        } catch (Exception e) {
            result.put("error", e.getMessage());
            result.put("totalScore", 0);
        }
        
        return result;
    }
    
    @Override
    public Map<String, Object> extractSkills(String content) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Integer> skillFrequency = new HashMap<>();
        
        String lowerContent = content.toLowerCase();
        
        // 提取技能关键词
        for (String skill : SKILL_KEYWORDS) {
            String lowerSkill = skill.toLowerCase();
            int count = countOccurrences(lowerContent, lowerSkill);
            if (count > 0) {
                skillFrequency.put(skill, count);
            }
        }
        
        // 按频率排序
        List<Map<String, Object>> skills = skillFrequency.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(20) // 取前20个技能
            .map(entry -> {
                Map<String, Object> skillMap = new HashMap<>();
                skillMap.put("name", entry.getKey());
                skillMap.put("frequency", entry.getValue());
                skillMap.put("weight", calculateSkillWeight(entry.getValue()));
                return skillMap;
            })
            .toList();
        
        result.put("skills", skills);
        result.put("totalSkills", skills.size());
        
        return result;
    }
    
    @Override
    public Map<String, Object> analyzeWorkExperience(String experienceText) {
        Map<String, Object> result = new HashMap<>();
        
        // 提取工作年限
        int yearCount = extractYearCount(experienceText);
        
        // 提取公司信息
        List<String> companies = extractCompanies(experienceText);
        
        // 提取职位信息
        List<String> positions = extractPositions(experienceText);
        
        // 分析项目经验
        List<String> projects = extractProjects(experienceText);
        
        result.put("totalYears", yearCount);
        result.put("companies", companies);
        result.put("positions", positions);
        result.put("projects", projects);
        result.put("experienceLevel", getExperienceLevel(yearCount));
        
        return result;
    }
    
    @Override
    public Map<String, Object> generateCandidateProfile(Resume resume) {
        Map<String, Object> profile = new HashMap<>();
        
        try {
            // 解析简历内容
            Map<String, Object> resumeData = parseResumeContent(resume.getParsedContent());
            
            // 技能分析
            Map<String, Object> skills = extractSkills(resume.getParsedContent());
            
            // 经验分析
            Map<String, Object> experience = analyzeWorkExperience(resume.getParsedContent());
            
            // 质量评分
            Map<String, Object> quality = analyzeResumeQuality(resume.getParsedContent());
            
            profile.put("skills", skills);
            profile.put("experience", experience);
            profile.put("quality", quality);
            profile.put("resumeData", resumeData);
            profile.put("aiScore", resume.getAiScore());
            
        } catch (Exception e) {
            profile.put("error", e.getMessage());
        }
        
        return profile;
    }
    
    @Override
    public Map<String, Object> recommendJobs(Long candidateId) {
        Map<String, Object> result = new HashMap<>();
        
        // 这里应该实现基于候选人技能和经验的职位推荐算法
        // 为演示目的，返回模拟数据
        List<Map<String, Object>> recommendations = new ArrayList<>();
        
        result.put("recommendations", recommendations);
        result.put("totalCount", recommendations.size());
        
        return result;
    }
    
    // 私有辅助方法
    
    private String extractTextFromFile(String filePath, String fileType) {
        // 这里应该实现实际的文件解析逻辑
        // 可以使用Apache POI (for Office docs) 或 PDFBox (for PDF)
        return "示例简历内容：姓名：张三，联系方式：13812345678，邮箱：zhangsan@example.com，" +
               "教育背景：计算机科学本科，工作经验：3年Java开发经验，熟悉Spring框架，MySQL数据库";
    }
    
    private Map<String, Object> extractBasicInfo(String content) {
        Map<String, Object> basicInfo = new HashMap<>();
        
        // 提取姓名 (简单正则)
        Pattern namePattern = Pattern.compile("姓名[：:](\\S+)");
        Matcher nameMatcher = namePattern.matcher(content);
        if (nameMatcher.find()) {
            basicInfo.put("name", nameMatcher.group(1));
        }
        
        // 提取电话
        Pattern phonePattern = Pattern.compile("(1[3-9]\\d{9})");
        Matcher phoneMatcher = phonePattern.matcher(content);
        if (phoneMatcher.find()) {
            basicInfo.put("phone", phoneMatcher.group(1));
        }
        
        // 提取邮箱
        Pattern emailPattern = Pattern.compile("([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})");
        Matcher emailMatcher = emailPattern.matcher(content);
        if (emailMatcher.find()) {
            basicInfo.put("email", emailMatcher.group(1));
        }
        
        return basicInfo;
    }
    
    private Map<String, Object> extractEducation(String content) {
        Map<String, Object> education = new HashMap<>();
        
        // 提取学历信息
        if (content.contains("博士")) {
            education.put("level", "博士");
        } else if (content.contains("硕士")) {
            education.put("level", "硕士");
        } else if (content.contains("本科")) {
            education.put("level", "本科");
        } else if (content.contains("专科")) {
            education.put("level", "专科");
        }
        
        return education;
    }
    
    private Map<String, Object> extractExperience(String content) {
        Map<String, Object> experience = new HashMap<>();
        
        // 提取工作年限
        Pattern yearPattern = Pattern.compile("(\\d+)年");
        Matcher yearMatcher = yearPattern.matcher(content);
        int totalYears = 0;
        while (yearMatcher.find()) {
            totalYears = Math.max(totalYears, Integer.parseInt(yearMatcher.group(1)));
        }
        experience.put("years", totalYears);
        
        return experience;
    }
    
    private int evaluateCompleteness(String content) {
        int score = 0;
        
        // 检查基本信息
        if (content.contains("姓名") || content.contains("联系方式")) score += 5;
        if (content.contains("@")) score += 5; // 邮箱
        if (content.matches(".*1[3-9]\\d{9}.*")) score += 5; // 电话
        if (content.contains("教育") || content.contains("学历")) score += 5;
        if (content.contains("工作") || content.contains("经验")) score += 5;
        if (content.contains("技能") || content.contains("专业")) score += 5;
        
        return Math.min(30, score);
    }
    
    private int evaluateSkills(String content) {
        int skillCount = 0;
        for (String skill : SKILL_KEYWORDS) {
            if (content.toLowerCase().contains(skill.toLowerCase())) {
                skillCount++;
            }
        }
        return Math.min(25, skillCount * 2);
    }
    
    private int evaluateExperience(String content) {
        int score = 0;
        
        if (content.contains("项目")) score += 8;
        if (content.contains("负责")) score += 5;
        if (content.contains("开发")) score += 5;
        if (content.contains("管理")) score += 4;
        if (content.contains("团队")) score += 3;
        
        return Math.min(25, score);
    }
    
    private int evaluateFormat(String content) {
        int score = 20; // 基础分
        
        // 简单的格式检查
        if (content.length() < 100) score -= 5;
        if (!content.contains("\n") && content.length() > 200) score -= 3;
        
        return Math.max(0, score);
    }
    
    private String getQualityLevel(int score) {
        if (score >= 80) return "优秀";
        if (score >= 60) return "良好";
        if (score >= 40) return "一般";
        return "待改进";
    }
    
    private Map<String, Object> parseResumeContent(String parsedContent) {
        try {
            if (parsedContent != null && !parsedContent.isEmpty()) {
                return objectMapper.readValue(parsedContent, new TypeReference<Map<String, Object>>() {});
            }
        } catch (Exception e) {
            // 如果解析失败，返回空Map
        }
        return new HashMap<>();
    }
    
    private int calculateSkillMatch(Map<String, Object> resumeData, Job job, 
                                   List<String> matchedSkills, List<String> missingSkills) {
        // 简化的技能匹配算法
        Set<String> resumeSkills = extractSkillsFromResumeData(resumeData);
        Set<String> jobSkills = extractSkillsFromJobDescription(job.getRequirements());
        
        int matchCount = 0;
        for (String jobSkill : jobSkills) {
            if (resumeSkills.contains(jobSkill)) {
                matchedSkills.add(jobSkill);
                matchCount++;
            } else {
                missingSkills.add(jobSkill);
            }
        }
        
        return jobSkills.isEmpty() ? 50 : (matchCount * 100) / jobSkills.size();
    }
    
    private int calculateExperienceMatch(Map<String, Object> resumeData, Job job) {
        // 简化的经验匹配算法
        return 70; // 示例返回值
    }
    
    private int calculateEducationMatch(Map<String, Object> resumeData, Job job) {
        // 简化的教育背景匹配算法
        return 80; // 示例返回值
    }
    
    private int calculateSalaryMatch(Resume resume, Job job) {
        // 薪资匹配算法
        if (resume.getCandidateId() != null) {
            // 这里应该获取候选人的期望薪资进行比较
            return 90; // 示例返回值
        }
        return 50;
    }
    
    private String getRecommendation(int score) {
        if (score >= 80) return "强烈推荐";
        if (score >= 60) return "推荐";
        if (score >= 40) return "考虑";
        return "不推荐";
    }
    
    private int countOccurrences(String text, String pattern) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(pattern, index)) != -1) {
            count++;
            index += pattern.length();
        }
        return count;
    }
    
    private double calculateSkillWeight(int frequency) {
        return Math.min(1.0, frequency * 0.2);
    }
    
    private int extractYearCount(String text) {
        Pattern pattern = Pattern.compile("(\\d+)年");
        Matcher matcher = pattern.matcher(text);
        int maxYears = 0;
        while (matcher.find()) {
            maxYears = Math.max(maxYears, Integer.parseInt(matcher.group(1)));
        }
        return maxYears;
    }
    
    private List<String> extractCompanies(String text) {
        // 简化的公司提取逻辑
        return Arrays.asList("示例公司A", "示例公司B");
    }
    
    private List<String> extractPositions(String text) {
        // 简化的职位提取逻辑
        return Arrays.asList("高级开发工程师", "技术主管");
    }
    
    private List<String> extractProjects(String text) {
        // 简化的项目提取逻辑
        return Arrays.asList("电商平台开发", "用户管理系统");
    }
    
    private String getExperienceLevel(int years) {
        if (years >= 5) return "资深";
        if (years >= 2) return "中级";
        return "初级";
    }
    
    private Set<String> extractSkillsFromResumeData(Map<String, Object> resumeData) {
        Set<String> skills = new HashSet<>();
        // 从简历数据中提取技能
        Object skillsObj = resumeData.get("skills");
        if (skillsObj instanceof Map) {
            Map<String, Object> skillsMap = (Map<String, Object>) skillsObj;
            Object skillsList = skillsMap.get("skills");
            if (skillsList instanceof List) {
                for (Object skill : (List<?>) skillsList) {
                    if (skill instanceof Map) {
                        Object name = ((Map<?, ?>) skill).get("name");
                        if (name instanceof String) {
                            skills.add((String) name);
                        }
                    }
                }
            }
        }
        return skills;
    }
    
    private Set<String> extractSkillsFromJobDescription(String jobRequirements) {
        Set<String> skills = new HashSet<>();
        if (jobRequirements != null) {
            String lowerRequirements = jobRequirements.toLowerCase();
            for (String skill : SKILL_KEYWORDS) {
                if (lowerRequirements.contains(skill.toLowerCase())) {
                    skills.add(skill);
                }
            }
        }
        return skills;
    }
}