package com.hrms.service;

import com.hrms.dto.JobCreateRequest;
import com.hrms.dto.JobResponse;
import com.hrms.entity.Job;
import com.hrms.entity.User;
import com.hrms.repository.JobRepository;
import com.hrms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobService {
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public JobResponse createJob(JobCreateRequest request, String recruiterUsername) {
        // 获取当前用户作为负责人
        User recruiter = userRepository.findByUsername(recruiterUsername)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 验证薪资范围
        if (request.getSalaryMax().compareTo(request.getSalaryMin()) < 0) {
            throw new RuntimeException("薪资范围最大值不能小于最小值");
        }
        
        // 创建职位
        Job job = new Job();
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setDepartment(request.getDepartment());
        job.setLocation(request.getLocation());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setEmploymentType(request.getEmploymentType());
        job.setRecruiter(recruiter);
        job.setRequirements(request.getRequirements());
        job.setBenefits(request.getBenefits());
        job.setApplicationDeadline(request.getApplicationDeadline());
        job.setStatus(Job.JobStatus.ACTIVE);
        
        Job savedJob = jobRepository.save(job);
        return new JobResponse(savedJob);
    }
    
    public JobResponse updateJob(Long jobId, JobCreateRequest request, String recruiterUsername) {
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("职位不存在"));
        
        // 检查权限：只有职位负责人可以修改
        if (!job.getRecruiter().getUsername().equals(recruiterUsername)) {
            throw new RuntimeException("无权限修改此职位");
        }
        
        // 验证薪资范围
        if (request.getSalaryMax().compareTo(request.getSalaryMin()) < 0) {
            throw new RuntimeException("薪资范围最大值不能小于最小值");
        }
        
        // 更新字段
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setDepartment(request.getDepartment());
        job.setLocation(request.getLocation());
        job.setSalaryMin(request.getSalaryMin());
        job.setSalaryMax(request.getSalaryMax());
        job.setEmploymentType(request.getEmploymentType());
        job.setRequirements(request.getRequirements());
        job.setBenefits(request.getBenefits());
        job.setApplicationDeadline(request.getApplicationDeadline());
        
        Job savedJob = jobRepository.save(job);
        return new JobResponse(savedJob);
    }
    
    public void deleteJob(Long jobId, String recruiterUsername) {
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("职位不存在"));
        
        // 检查权限：只有职位负责人可以删除
        if (!job.getRecruiter().getUsername().equals(recruiterUsername)) {
            throw new RuntimeException("无权限删除此职位");
        }
        
        // 软删除：设置状态为CANCELLED
        job.setStatus(Job.JobStatus.CANCELLED);
        jobRepository.save(job);
    }
    
    @Transactional(readOnly = true)
    public JobResponse getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("职位不存在"));
        return new JobResponse(job);
    }
    
    @Transactional(readOnly = true)
    public Job getJobEntityById(Long jobId) {
        return jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("职位不存在"));
    }
    
    @Transactional(readOnly = true)
    public Page<JobResponse> getJobs(int page, int size, String sortBy, String sortDir, 
                                   String title, String department, Job.JobStatus status, Job.EmploymentType employmentType) {
        // 创建排序规则
        Sort sort = Sort.by(sortDir.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // 执行查询
        Page<Job> jobPage = jobRepository.findJobsWithCriteria(title, department, status, employmentType, pageable);
        
        // 转换为Response DTO
        return jobPage.map(JobResponse::new);
    }
    
    @Transactional(readOnly = true)
    public List<JobResponse> getActiveJobs() {
        List<Job> jobs = jobRepository.findActiveJobs();
        return jobs.stream()
                  .map(JobResponse::new)
                  .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<JobResponse> getJobsByRecruiter(String recruiterUsername) {
        User recruiter = userRepository.findByUsername(recruiterUsername)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        List<Job> jobs = jobRepository.findByRecruiter(recruiter);
        return jobs.stream()
                  .map(JobResponse::new)
                  .collect(Collectors.toList());
    }
    
    public JobResponse updateJobStatus(Long jobId, Job.JobStatus newStatus, String recruiterUsername) {
        Job job = jobRepository.findById(jobId)
            .orElseThrow(() -> new RuntimeException("职位不存在"));
        
        // 检查权限：只有职位负责人可以修改状态
        if (!job.getRecruiter().getUsername().equals(recruiterUsername)) {
            throw new RuntimeException("无权限修改此职位状态");
        }
        
        job.setStatus(newStatus);
        Job savedJob = jobRepository.save(job);
        return new JobResponse(savedJob);
    }
}