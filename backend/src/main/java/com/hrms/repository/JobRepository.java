package com.hrms.repository;

import com.hrms.entity.Job;
import com.hrms.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    
    // 根据状态查找职位
    List<Job> findByStatus(Job.JobStatus status);
    
    // 根据负责人查找职位
    List<Job> findByRecruiter(User recruiter);
    Page<Job> findByRecruiter(User recruiter, Pageable pageable);
    
    // 根据部门查找职位
    List<Job> findByDepartment(String department);
    Page<Job> findByDepartment(String department, Pageable pageable);
    
    // 根据职位类型查找
    List<Job> findByEmploymentType(Job.EmploymentType employmentType);
    
    // 根据标题模糊搜索
    List<Job> findByTitleContainingIgnoreCase(String title);
    Page<Job> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    // 多条件搜索
    @Query("SELECT j FROM Job j WHERE " +
           "(:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:department IS NULL OR j.department = :department) AND " +
           "(:status IS NULL OR j.status = :status) AND " +
           "(:employmentType IS NULL OR j.employmentType = :employmentType)")
    Page<Job> findJobsWithCriteria(
        @Param("title") String title,
        @Param("department") String department, 
        @Param("status") Job.JobStatus status,
        @Param("employmentType") Job.EmploymentType employmentType,
        Pageable pageable
    );
    
    // 获取活跃职位
    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' ORDER BY j.createdAt DESC")
    List<Job> findActiveJobs();
    
    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' ORDER BY j.createdAt DESC")
    Page<Job> findActiveJobs(Pageable pageable);
    
    // 统计各部门职位数量
    @Query("SELECT j.department, COUNT(j) FROM Job j WHERE j.status = 'ACTIVE' GROUP BY j.department")
    List<Object[]> countJobsByDepartment();
    
    // 统计各状态职位数量
    @Query("SELECT j.status, COUNT(j) FROM Job j GROUP BY j.status")
    List<Object[]> countJobsByStatus();
    
    // 即将到期的职位（7天内截止申请）
    @Query("SELECT j FROM Job j WHERE j.applicationDeadline <= :deadline AND j.status = 'ACTIVE'")
    List<Job> findJobsWithDeadlineApproaching(@Param("deadline") java.time.LocalDateTime deadline);
}