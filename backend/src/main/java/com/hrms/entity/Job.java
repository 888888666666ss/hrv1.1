package com.hrms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
public class Job {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "职位标题不能为空")
    @Size(max = 100, message = "职位标题长度不能超过100个字符")
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    
    @NotBlank(message = "职位描述不能为空")
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Size(max = 100, message = "部门名称长度不能超过100个字符")
    @Column(name = "department", length = 100)
    private String department;
    
    @Size(max = 100, message = "工作地点长度不能超过100个字符")
    @Column(name = "location", length = 100)
    private String location;
    
    @NotNull(message = "薪资范围最小值不能为空")
    @DecimalMin(value = "0.0", message = "薪资范围最小值不能小于0")
    @Column(name = "salary_min", precision = 10, scale = 2)
    private BigDecimal salaryMin;
    
    @NotNull(message = "薪资范围最大值不能为空")
    @DecimalMin(value = "0.0", message = "薪资范围最大值不能小于0")
    @Column(name = "salary_max", precision = 10, scale = 2)
    private BigDecimal salaryMax;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "employment_type", length = 20)
    private EmploymentType employmentType;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private JobStatus status = JobStatus.ACTIVE;
    
    @NotNull(message = "负责人不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiter_id", nullable = false)
    private User recruiter;
    
    @Column(name = "requirements", columnDefinition = "TEXT")
    private String requirements;
    
    @Column(name = "benefits", columnDefinition = "TEXT")
    private String benefits;
    
    @Column(name = "application_deadline")
    private LocalDateTime applicationDeadline;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // 枚举类型
    public enum EmploymentType {
        FULL_TIME,    // 全职
        PART_TIME,    // 兼职
        CONTRACT,     // 合同工
        INTERN        // 实习
    }
    
    public enum JobStatus {
        ACTIVE,       // 活跃招聘中
        PAUSED,       // 暂停招聘
        CLOSED,       // 已关闭
        CANCELLED     // 已取消
    }
    
    // 构造函数
    public Job() {}
    
    public Job(String title, String description, BigDecimal salaryMin, BigDecimal salaryMax, User recruiter) {
        this.title = title;
        this.description = description;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
        this.recruiter = recruiter;
        this.status = JobStatus.ACTIVE;
        this.employmentType = EmploymentType.FULL_TIME;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public BigDecimal getSalaryMin() {
        return salaryMin;
    }
    
    public void setSalaryMin(BigDecimal salaryMin) {
        this.salaryMin = salaryMin;
    }
    
    public BigDecimal getSalaryMax() {
        return salaryMax;
    }
    
    public void setSalaryMax(BigDecimal salaryMax) {
        this.salaryMax = salaryMax;
    }
    
    public EmploymentType getEmploymentType() {
        return employmentType;
    }
    
    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }
    
    public JobStatus getStatus() {
        return status;
    }
    
    public void setStatus(JobStatus status) {
        this.status = status;
    }
    
    public User getRecruiter() {
        return recruiter;
    }
    
    public void setRecruiter(User recruiter) {
        this.recruiter = recruiter;
    }
    
    public String getRequirements() {
        return requirements;
    }
    
    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }
    
    public String getBenefits() {
        return benefits;
    }
    
    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }
    
    public LocalDateTime getApplicationDeadline() {
        return applicationDeadline;
    }
    
    public void setApplicationDeadline(LocalDateTime applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
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