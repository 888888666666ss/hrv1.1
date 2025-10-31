package com.hrms.dto;

import com.hrms.entity.Job;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class JobCreateRequest {
    
    @NotBlank(message = "职位标题不能为空")
    @Size(max = 100, message = "职位标题长度不能超过100个字符")
    private String title;
    
    @NotBlank(message = "职位描述不能为空")
    private String description;
    
    @Size(max = 100, message = "部门名称长度不能超过100个字符")
    private String department;
    
    @Size(max = 100, message = "工作地点长度不能超过100个字符")
    private String location;
    
    @NotNull(message = "薪资范围最小值不能为空")
    @DecimalMin(value = "0.0", message = "薪资范围最小值不能小于0")
    private BigDecimal salaryMin;
    
    @NotNull(message = "薪资范围最大值不能为空")
    @DecimalMin(value = "0.0", message = "薪资范围最大值不能小于0")
    private BigDecimal salaryMax;
    
    private Job.EmploymentType employmentType = Job.EmploymentType.FULL_TIME;
    
    private String requirements;
    
    private String benefits;
    
    private LocalDateTime applicationDeadline;
    
    // 构造函数
    public JobCreateRequest() {}
    
    // Getters and Setters
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
}