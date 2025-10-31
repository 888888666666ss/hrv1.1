package com.hrms.dto;

import com.hrms.entity.Job;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String department;
    private String location;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private Job.EmploymentType employmentType;
    private Job.JobStatus status;
    private String recruiterName;
    private Long recruiterId;
    private String requirements;
    private String benefits;
    private LocalDateTime applicationDeadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public JobResponse() {}
    
    public JobResponse(Job job) {
        this.id = job.getId();
        this.title = job.getTitle();
        this.description = job.getDescription();
        this.department = job.getDepartment();
        this.location = job.getLocation();
        this.salaryMin = job.getSalaryMin();
        this.salaryMax = job.getSalaryMax();
        this.employmentType = job.getEmploymentType();
        this.status = job.getStatus();
        this.recruiterId = job.getRecruiter().getId();
        this.recruiterName = job.getRecruiter().getRealName();
        this.requirements = job.getRequirements();
        this.benefits = job.getBenefits();
        this.applicationDeadline = job.getApplicationDeadline();
        this.createdAt = job.getCreatedAt();
        this.updatedAt = job.getUpdatedAt();
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
    
    public Job.EmploymentType getEmploymentType() {
        return employmentType;
    }
    
    public void setEmploymentType(Job.EmploymentType employmentType) {
        this.employmentType = employmentType;
    }
    
    public Job.JobStatus getStatus() {
        return status;
    }
    
    public void setStatus(Job.JobStatus status) {
        this.status = status;
    }
    
    public String getRecruiterName() {
        return recruiterName;
    }
    
    public void setRecruiterName(String recruiterName) {
        this.recruiterName = recruiterName;
    }
    
    public Long getRecruiterId() {
        return recruiterId;
    }
    
    public void setRecruiterId(Long recruiterId) {
        this.recruiterId = recruiterId;
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