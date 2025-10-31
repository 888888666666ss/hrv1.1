package com.hrms.repository;

import com.hrms.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    
    /**
     * 根据候选人ID查找简历
     */
    List<Resume> findByCandidateId(Long candidateId);
    
    /**
     * 根据候选人ID和状态查找简历
     */
    List<Resume> findByCandidateIdAndStatus(Long candidateId, Resume.ResumeStatus status);
    
    /**
     * 根据状态查找简历（分页）
     */
    Page<Resume> findByStatus(Resume.ResumeStatus status, Pageable pageable);
    
    /**
     * 根据AI评分范围查找简历
     */
    @Query("SELECT r FROM Resume r WHERE r.aiScore BETWEEN :minScore AND :maxScore")
    List<Resume> findByAiScoreBetween(@Param("minScore") Integer minScore, @Param("maxScore") Integer maxScore);
    
    /**
     * 根据文件类型查找简历
     */
    List<Resume> findByFileType(String fileType);
    
    /**
     * 查找已解析的简历
     */
    List<Resume> findByStatusAndParsedContentIsNotNull(Resume.ResumeStatus status);
    
    /**
     * 查找解析失败的简历
     */
    List<Resume> findByStatusAndParseErrorIsNotNull(Resume.ResumeStatus status);
    
    /**
     * 根据时间范围查找简历
     */
    @Query("SELECT r FROM Resume r WHERE r.createdAt BETWEEN :startDate AND :endDate")
    List<Resume> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * 查找最近上传的简历
     */
    @Query("SELECT r FROM Resume r WHERE r.createdAt >= :startDate ORDER BY r.createdAt DESC")
    List<Resume> findRecentResumes(@Param("startDate") LocalDateTime startDate);
    
    /**
     * 根据提取的姓名搜索简历
     */
    @Query("SELECT r FROM Resume r WHERE r.extractedName LIKE %:name%")
    List<Resume> findByExtractedNameContaining(@Param("name") String name);
    
    /**
     * 根据提取的邮箱搜索简历
     */
    Optional<Resume> findByExtractedEmail(String email);
    
    /**
     * 根据提取的电话搜索简历
     */
    Optional<Resume> findByExtractedPhone(String phone);
    
    /**
     * 根据提取的技能搜索简历
     */
    @Query("SELECT r FROM Resume r WHERE r.extractedSkills LIKE %:skill%")
    List<Resume> findByExtractedSkillsContaining(@Param("skill") String skill);
    
    /**
     * 根据提取的教育背景搜索简历
     */
    @Query("SELECT r FROM Resume r WHERE r.extractedEducation LIKE %:education%")
    List<Resume> findByExtractedEducationContaining(@Param("education") String education);
    
    /**
     * 根据提取的工作经验搜索简历
     */
    @Query("SELECT r FROM Resume r WHERE r.extractedExperience LIKE %:experience%")
    List<Resume> findByExtractedExperienceContaining(@Param("experience") String experience);
    
    /**
     * 多条件搜索简历
     */
    @Query("SELECT r FROM Resume r WHERE " +
           "(:candidateId IS NULL OR r.candidateId = :candidateId) AND " +
           "(:status IS NULL OR r.status = :status) AND " +
           "(:fileType IS NULL OR r.fileType = :fileType) AND " +
           "(:minScore IS NULL OR r.aiScore >= :minScore) AND " +
           "(:maxScore IS NULL OR r.aiScore <= :maxScore) AND " +
           "(:name IS NULL OR r.extractedName LIKE %:name%) AND " +
           "(:email IS NULL OR r.extractedEmail LIKE %:email%) AND " +
           "(:skill IS NULL OR r.extractedSkills LIKE %:skill%)")
    Page<Resume> findResumesWithCriteria(
            @Param("candidateId") Long candidateId,
            @Param("status") Resume.ResumeStatus status,
            @Param("fileType") String fileType,
            @Param("minScore") Integer minScore,
            @Param("maxScore") Integer maxScore,
            @Param("name") String name,
            @Param("email") String email,
            @Param("skill") String skill,
            Pageable pageable
    );
    
    /**
     * 统计简历状态分布
     */
    @Query("SELECT r.status, COUNT(r) FROM Resume r GROUP BY r.status")
    List<Object[]> countResumesByStatus();
    
    /**
     * 统计简历文件类型分布
     */
    @Query("SELECT r.fileType, COUNT(r) FROM Resume r GROUP BY r.fileType")
    List<Object[]> countResumesByFileType();
    
    /**
     * 统计AI评分分布
     */
    @Query("SELECT " +
           "SUM(CASE WHEN r.aiScore >= 90 THEN 1 ELSE 0 END) as excellent, " +
           "SUM(CASE WHEN r.aiScore >= 80 AND r.aiScore < 90 THEN 1 ELSE 0 END) as good, " +
           "SUM(CASE WHEN r.aiScore >= 70 AND r.aiScore < 80 THEN 1 ELSE 0 END) as average, " +
           "SUM(CASE WHEN r.aiScore >= 60 AND r.aiScore < 70 THEN 1 ELSE 0 END) as fair, " +
           "SUM(CASE WHEN r.aiScore < 60 THEN 1 ELSE 0 END) as poor " +
           "FROM Resume r WHERE r.aiScore IS NOT NULL")
    Object getAiScoreDistribution();
    
    /**
     * 查找重复简历（根据文件大小和文件名）
     */
    @Query("SELECT r FROM Resume r WHERE r.fileName = :fileName AND r.fileSize = :fileSize AND r.id != :excludeId")
    List<Resume> findDuplicateResumes(@Param("fileName") String fileName, @Param("fileSize") Long fileSize, @Param("excludeId") Long excludeId);
    
    /**
     * 查找指定时间范围内处理的简历
     */
    @Query("SELECT r FROM Resume r WHERE r.processedAt BETWEEN :startDate AND :endDate")
    List<Resume> findByProcessedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * 查找待处理的简历
     */
    @Query("SELECT r FROM Resume r WHERE r.status = 'UPLOADED' ORDER BY r.createdAt ASC")
    List<Resume> findPendingResumes();
    
    /**
     * 根据文件路径查找简历
     */
    Optional<Resume> findByFilePath(String filePath);
    
    /**
     * 批量更新简历状态
     */
    @Query("UPDATE Resume r SET r.status = :status WHERE r.id IN :ids")
    void batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Resume.ResumeStatus status);
    
    /**
     * 检查文件是否已存在
     */
    boolean existsByFilePathAndFileName(String filePath, String fileName);
    
    /**
     * 根据候选人ID统计简历数量
     */
    @Query("SELECT COUNT(r) FROM Resume r WHERE r.candidateId = :candidateId")
    long countByCandidateId(@Param("candidateId") Long candidateId);
    
    /**
     * 查找高分简历（AI评分 >= 80）
     */
    @Query("SELECT r FROM Resume r WHERE r.aiScore >= 80 ORDER BY r.aiScore DESC")
    List<Resume> findHighScoreResumes();
    
    /**
     * 查找最新的简历（每个候选人的最新简历）
     */
    @Query("SELECT r FROM Resume r WHERE r.id IN (" +
           "SELECT MAX(r2.id) FROM Resume r2 GROUP BY r2.candidateId)")
    List<Resume> findLatestResumesByCandidate();
    
    /**
     * 根据简历来源（平台）统计
     * 通过候选人来源来推断简历来源
     */
    @Query("SELECT c.source, COUNT(r) FROM Resume r " +
           "JOIN Candidate c ON r.candidateId = c.id " +
           "GROUP BY c.source")
    List<Object[]> countResumesBySource();
    
    /**
     * 根据状态统计简历数量
     */
    long countByStatus(Resume.ResumeStatus status);
    
    /**
     * 根据文件名搜索简历（忽略大小写）
     */
    Page<Resume> findByFileNameContainingIgnoreCase(String fileName, Pageable pageable);
}