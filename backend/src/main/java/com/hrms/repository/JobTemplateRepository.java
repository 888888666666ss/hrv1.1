package com.hrms.repository;

import com.hrms.entity.JobTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobTemplateRepository extends JpaRepository<JobTemplate, Long> {
    
    // 查找用户创建的模板
    Page<JobTemplate> findByCreatedByOrderByCreatedAtDesc(Long createdBy, Pageable pageable);
    
    // 查找公共模板
    Page<JobTemplate> findByIsPublicTrueOrderByCreatedAtDesc(Pageable pageable);
    
    // 查找用户可访问的模板（自己创建的或公共的）
    @Query("SELECT t FROM JobTemplate t WHERE t.createdBy = :userId OR t.isPublic = true ORDER BY t.createdAt DESC")
    Page<JobTemplate> findAccessibleTemplates(@Param("userId") Long userId, Pageable pageable);
    
    // 根据名称搜索模板
    @Query("SELECT t FROM JobTemplate t WHERE (t.createdBy = :userId OR t.isPublic = true) " +
           "AND (t.name LIKE %:keyword% OR t.title LIKE %:keyword% OR t.department LIKE %:keyword%) " +
           "ORDER BY t.createdAt DESC")
    Page<JobTemplate> searchTemplates(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);
    
    // 获取部门的模板统计
    @Query("SELECT t.department, COUNT(t) FROM JobTemplate t WHERE t.createdBy = :userId OR t.isPublic = true GROUP BY t.department")
    List<Object[]> getTemplateStatsByDepartment(@Param("userId") Long userId);
}