package com.hrms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "candidates")
public class Candidate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "姓名不能为空")
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    @Column(name = "email", length = 100)
    private String email;
    
    @Size(max = 20, message = "电话长度不能超过20个字符")
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "source", length = 50)
    private CandidateSource source = CandidateSource.UPLOAD;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private CandidateStatus status = CandidateStatus.NEW;
    
    @Column(name = "current_position", length = 100)
    private String currentPosition;
    
    @Column(name = "current_company", length = 100)
    private String currentCompany;
    
    @Column(name = "work_experience")
    private Integer workExperience; // 工作年限
    
    @Column(name = "education_level", length = 50)
    private String educationLevel;
    
    @Column(name = "graduation_school", length = 100)
    private String graduationSchool;
    
    @Column(name = "major", length = 100)
    private String major;
    
    @Column(name = "skills", columnDefinition = "TEXT")
    private String skills;
    
    @Column(name = "self_introduction", columnDefinition = "TEXT")
    private String selfIntroduction;
    
    @Column(name = "expected_salary_min")
    private Integer expectedSalaryMin;
    
    @Column(name = "expected_salary_max")
    private Integer expectedSalaryMax;
    
    @Column(name = "expected_position", length = 100)
    private String expectedPosition;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 枚举类型
    public enum CandidateSource {
        UPLOAD,     // 简历上传
        BOSS,       // Boss直聘
        LIEPIN,     // 猎聘
        ZHILIAN,    // 智联招聘
        LAGOU,      // 拉勾
        REFERRAL,   // 内推
        OTHER       // 其他
    }
    
    public enum CandidateStatus {
        NEW,        // 新建
        SCREENING,  // 筛选中
        INTERVIEW,  // 面试中
        OFFER,      // 已发offer
        HIRED,      // 已入职
        REJECTED,   // 已拒绝
        WITHDRAWN   // 已撤销
    }
    
    // 构造函数
    public Candidate() {}
    
    public Candidate(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = CandidateStatus.NEW;
        this.source = CandidateSource.UPLOAD;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public CandidateSource getSource() {
        return source;
    }
    
    public void setSource(CandidateSource source) {
        this.source = source;
    }
    
    public CandidateStatus getStatus() {
        return status;
    }
    
    public void setStatus(CandidateStatus status) {
        this.status = status;
    }
    
    public String getCurrentPosition() {
        return currentPosition;
    }
    
    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }
    
    public String getCurrentCompany() {
        return currentCompany;
    }
    
    public void setCurrentCompany(String currentCompany) {
        this.currentCompany = currentCompany;
    }
    
    public Integer getWorkExperience() {
        return workExperience;
    }
    
    public void setWorkExperience(Integer workExperience) {
        this.workExperience = workExperience;
    }
    
    public String getEducationLevel() {
        return educationLevel;
    }
    
    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }
    
    public String getGraduationSchool() {
        return graduationSchool;
    }
    
    public void setGraduationSchool(String graduationSchool) {
        this.graduationSchool = graduationSchool;
    }
    
    public String getMajor() {
        return major;
    }
    
    public void setMajor(String major) {
        this.major = major;
    }
    
    public String getSkills() {
        return skills;
    }
    
    public void setSkills(String skills) {
        this.skills = skills;
    }
    
    public String getSelfIntroduction() {
        return selfIntroduction;
    }
    
    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }
    
    public Integer getExpectedSalaryMin() {
        return expectedSalaryMin;
    }
    
    public void setExpectedSalaryMin(Integer expectedSalaryMin) {
        this.expectedSalaryMin = expectedSalaryMin;
    }
    
    public Integer getExpectedSalaryMax() {
        return expectedSalaryMax;
    }
    
    public void setExpectedSalaryMax(Integer expectedSalaryMax) {
        this.expectedSalaryMax = expectedSalaryMax;
    }
    
    public String getExpectedPosition() {
        return expectedPosition;
    }
    
    public void setExpectedPosition(String expectedPosition) {
        this.expectedPosition = expectedPosition;
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
}