package com.hrms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "resumes")
public class Resume {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "候选人ID不能为空")
    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;
    
    @NotBlank(message = "文件名不能为空")
    @Size(max = 255, message = "文件名长度不能超过255个字符")
    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;
    
    @NotBlank(message = "文件路径不能为空")
    @Size(max = 500, message = "文件路径长度不能超过500个字符")
    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;
    
    @NotNull(message = "文件大小不能为空")
    @Min(value = 1, message = "文件大小必须大于0")
    @Column(name = "file_size", nullable = false)
    private Long fileSize;
    
    @Size(max = 50, message = "文件类型长度不能超过50个字符")
    @Column(name = "file_type", length = 50)
    private String fileType; // PDF, DOC, DOCX
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private ResumeStatus status = ResumeStatus.UPLOADED;
    
    // 解析后的结构化数据 (JSON格式)
    @Column(name = "parsed_content", columnDefinition = "TEXT")
    private String parsedContent;
    
    // AI评分 (0-100)
    @Min(value = 0, message = "AI评分不能小于0")
    @Max(value = 100, message = "AI评分不能大于100")
    @Column(name = "ai_score")
    private Integer aiScore;
    
    // 解析后提取的关键信息
    @Column(name = "extracted_name", length = 100)
    private String extractedName;
    
    @Column(name = "extracted_email", length = 100)
    private String extractedEmail;
    
    @Column(name = "extracted_phone", length = 20)
    private String extractedPhone;
    
    @Column(name = "extracted_education", length = 200)
    private String extractedEducation;
    
    @Column(name = "extracted_experience", columnDefinition = "TEXT")
    private String extractedExperience;
    
    @Column(name = "extracted_skills", columnDefinition = "TEXT")
    private String extractedSkills;
    
    // 解析失败时的错误信息
    @Column(name = "parse_error", columnDefinition = "TEXT")
    private String parseError;
    
    // 解析处理时间
    @Column(name = "processed_at")
    private LocalDateTime processedAt;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 枚举类型
    public enum ResumeStatus {
        UPLOADED,       // 已上传
        PROCESSING,     // 解析中
        PROCESSED,      // 已解析
        PARSE_FAILED,   // 解析失败
        ARCHIVED        // 已归档
    }
    
    // 构造函数
    public Resume() {}
    
    public Resume(Long candidateId, String fileName, String filePath, Long fileSize, String fileType) {
        this.candidateId = candidateId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.status = ResumeStatus.UPLOADED;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getCandidateId() {
        return candidateId;
    }
    
    public void setCandidateId(Long candidateId) {
        this.candidateId = candidateId;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getFileType() {
        return fileType;
    }
    
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    
    public ResumeStatus getStatus() {
        return status;
    }
    
    public void setStatus(ResumeStatus status) {
        this.status = status;
    }
    
    public String getParsedContent() {
        return parsedContent;
    }
    
    public void setParsedContent(String parsedContent) {
        this.parsedContent = parsedContent;
    }
    
    public Integer getAiScore() {
        return aiScore;
    }
    
    public void setAiScore(Integer aiScore) {
        this.aiScore = aiScore;
    }
    
    public String getExtractedName() {
        return extractedName;
    }
    
    public void setExtractedName(String extractedName) {
        this.extractedName = extractedName;
    }
    
    public String getExtractedEmail() {
        return extractedEmail;
    }
    
    public void setExtractedEmail(String extractedEmail) {
        this.extractedEmail = extractedEmail;
    }
    
    public String getExtractedPhone() {
        return extractedPhone;
    }
    
    public void setExtractedPhone(String extractedPhone) {
        this.extractedPhone = extractedPhone;
    }
    
    public String getExtractedEducation() {
        return extractedEducation;
    }
    
    public void setExtractedEducation(String extractedEducation) {
        this.extractedEducation = extractedEducation;
    }
    
    public String getExtractedExperience() {
        return extractedExperience;
    }
    
    public void setExtractedExperience(String extractedExperience) {
        this.extractedExperience = extractedExperience;
    }
    
    public String getExtractedSkills() {
        return extractedSkills;
    }
    
    public void setExtractedSkills(String extractedSkills) {
        this.extractedSkills = extractedSkills;
    }
    
    public String getParseError() {
        return parseError;
    }
    
    public void setParseError(String parseError) {
        this.parseError = parseError;
    }
    
    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
    
    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // 辅助方法
    public String getFormattedFileSize() {
        if (fileSize == null) return "0 B";
        
        if (fileSize < 1024) return fileSize + " B";
        if (fileSize < 1024 * 1024) return String.format("%.1f KB", fileSize / 1024.0);
        if (fileSize < 1024 * 1024 * 1024) return String.format("%.1f MB", fileSize / (1024.0 * 1024.0));
        return String.format("%.1f GB", fileSize / (1024.0 * 1024.0 * 1024.0));
    }
    
    public boolean isParsed() {
        return status == ResumeStatus.PROCESSED;
    }
    
    public boolean isFailed() {
        return status == ResumeStatus.PARSE_FAILED;
    }
}