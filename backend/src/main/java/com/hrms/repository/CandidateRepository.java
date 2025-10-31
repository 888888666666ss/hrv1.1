package com.hrms.repository;

import com.hrms.entity.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    
    // 根据状态查找候选人
    List<Candidate> findByStatus(Candidate.CandidateStatus status);
    Page<Candidate> findByStatus(Candidate.CandidateStatus status, Pageable pageable);
    
    // 根据来源查找候选人
    List<Candidate> findBySource(Candidate.CandidateSource source);
    Page<Candidate> findBySource(Candidate.CandidateSource source, Pageable pageable);
    
    // 根据姓名模糊搜索
    List<Candidate> findByNameContainingIgnoreCase(String name);
    Page<Candidate> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    // 根据邮箱查找
    List<Candidate> findByEmail(String email);
    
    // 根据电话查找
    List<Candidate> findByPhone(String phone);
    
    // 根据当前职位搜索
    List<Candidate> findByCurrentPositionContainingIgnoreCase(String position);
    
    // 根据当前公司搜索
    List<Candidate> findByCurrentCompanyContainingIgnoreCase(String company);
    
    // 根据技能搜索
    @Query("SELECT c FROM Candidate c WHERE LOWER(c.skills) LIKE LOWER(CONCAT('%', :skill, '%'))")
    List<Candidate> findBySkillsContaining(@Param("skill") String skill);
    
    // 根据工作年限范围查找
    List<Candidate> findByWorkExperienceBetween(Integer minExperience, Integer maxExperience);
    
    // 根据期望薪资范围查找
    @Query("SELECT c FROM Candidate c WHERE " +
           "(c.expectedSalaryMin IS NULL OR c.expectedSalaryMin <= :maxSalary) AND " +
           "(c.expectedSalaryMax IS NULL OR c.expectedSalaryMax >= :minSalary)")
    List<Candidate> findBySalaryRange(@Param("minSalary") Integer minSalary, @Param("maxSalary") Integer maxSalary);
    
    // 多条件搜索
    @Query("SELECT c FROM Candidate c WHERE " +
           "(:name IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:email IS NULL OR c.email = :email) AND " +
           "(:phone IS NULL OR c.phone = :phone) AND " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:source IS NULL OR c.source = :source) AND " +
           "(:currentPosition IS NULL OR LOWER(c.currentPosition) LIKE LOWER(CONCAT('%', :currentPosition, '%'))) AND " +
           "(:currentCompany IS NULL OR LOWER(c.currentCompany) LIKE LOWER(CONCAT('%', :currentCompany, '%'))) AND " +
           "(:skill IS NULL OR LOWER(c.skills) LIKE LOWER(CONCAT('%', :skill, '%')))")
    Page<Candidate> findCandidatesWithCriteria(
        @Param("name") String name,
        @Param("email") String email,
        @Param("phone") String phone,
        @Param("status") Candidate.CandidateStatus status,
        @Param("source") Candidate.CandidateSource source,
        @Param("currentPosition") String currentPosition,
        @Param("currentCompany") String currentCompany,
        @Param("skill") String skill,
        Pageable pageable
    );
    
    // 统计各状态候选人数量
    @Query("SELECT c.status, COUNT(c) FROM Candidate c GROUP BY c.status")
    List<Object[]> countCandidatesByStatus();
    
    // 统计各来源候选人数量
    @Query("SELECT c.source, COUNT(c) FROM Candidate c GROUP BY c.source")
    List<Object[]> countCandidatesBySource();
    
    // 获取最近N天的新候选人
    @Query("SELECT c FROM Candidate c WHERE c.createdAt >= :startDate ORDER BY c.createdAt DESC")
    List<Candidate> findRecentCandidates(@Param("startDate") java.time.LocalDateTime startDate);
    
    // 检查邮箱是否已存在
    boolean existsByEmail(String email);
    
    // 检查电话是否已存在
    boolean existsByPhone(String phone);
}