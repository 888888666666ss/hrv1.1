package com.hrms.service;

import com.hrms.entity.InterviewQuestion;
import com.hrms.entity.InterviewAnswer;
import com.hrms.entity.Interview;

import java.util.List;
import java.util.Map;

public interface InterviewQuestionService {
    
    /**
     * 问题库管理
     */
    InterviewQuestion createQuestion(InterviewQuestion question);
    InterviewQuestion updateQuestion(Long id, InterviewQuestion question);
    void deleteQuestion(Long id);
    InterviewQuestion getQuestionById(Long id);
    List<InterviewQuestion> getAllQuestions();
    List<InterviewQuestion> getActiveQuestions();
    
    /**
     * 问题分类和筛选
     */
    List<InterviewQuestion> getQuestionsByCategory(InterviewQuestion.QuestionCategory category);
    List<InterviewQuestion> getQuestionsByDifficulty(InterviewQuestion.DifficultyLevel difficulty);
    List<InterviewQuestion> getQuestionsByDepartment(String department);
    List<InterviewQuestion> getQuestionsByPositionLevel(String positionLevel);
    List<InterviewQuestion> getQuestionsBySkill(String skill);
    
    /**
     * 智能问题推荐
     */
    List<InterviewQuestion> recommendQuestions(Long jobId, Long candidateId, int count);
    List<InterviewQuestion> getRandomQuestions(int count);
    List<InterviewQuestion> getRandomQuestionsByCategory(InterviewQuestion.QuestionCategory category, int count);
    List<InterviewQuestion> getQuestionsByJobRequirements(Long jobId);
    Map<String, Object> generateQuestionSet(Long jobId, Interview.InterviewType interviewType);
    
    /**
     * 问题搜索和查询
     */
    List<InterviewQuestion> searchQuestions(String keyword);
    List<InterviewQuestion> getPopularQuestions();
    List<InterviewQuestion> getHighQualityQuestions();
    List<InterviewQuestion> getUnusedQuestions();
    List<InterviewQuestion> getLowQualityQuestions(Double threshold);
    
    /**
     * 问题统计和分析
     */
    Map<String, Object> getQuestionStatistics();
    Map<String, Object> getQuestionUsageStats();
    Map<String, Object> getQuestionPerformanceStats(Long questionId);
    Double getQuestionAverageScore(Long questionId);
    
    /**
     * 问题使用管理
     */
    void incrementQuestionUsage(Long questionId);
    void updateQuestionAverageScore(Long questionId, Double newScore);
    void batchUpdateQuestionStatus(List<Long> questionIds, Boolean isActive);
    
    /**
     * 问题导入导出
     */
    List<InterviewQuestion> importQuestions(List<Map<String, Object>> questionData);
    List<Map<String, Object>> exportQuestions(List<Long> questionIds);
    Map<String, Object> validateQuestionData(Map<String, Object> questionData);
    
    /**
     * 面试答案管理
     */
    InterviewAnswer saveAnswer(InterviewAnswer answer);
    InterviewAnswer updateAnswer(Long id, InterviewAnswer answer);
    InterviewAnswer getAnswerById(Long id);
    List<InterviewAnswer> getAnswersByInterviewId(Long interviewId);
    List<InterviewAnswer> getAnswersByQuestionId(Long questionId);
    
    /**
     * 答案评估
     */
    InterviewAnswer evaluateAnswer(Long answerId, Map<String, Object> evaluation);
    Map<String, Object> analyzeAnswer(Long answerId);
    Map<String, Object> compareAnswers(List<Long> answerIds);
    Double calculateAnswerScore(Long answerId);
    
    /**
     * AI辅助评估
     */
    Map<String, Object> aiAnalyzeAnswer(String answerText, Long questionId);
    String generateFollowUpQuestions(String answer, Long questionId);
    Map<String, Object> detectKeywords(String answer, Long questionId);
    Integer suggestAnswerScore(String answer, Long questionId);
}