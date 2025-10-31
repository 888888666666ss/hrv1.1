package com.hrms.service;

import com.hrms.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ResumeService {
    
    /**
     * 根据ID获取简历
     */
    Resume getResumeById(Long id);
    
    /**
     * 分页获取简历列表
     */
    Page<Resume> getResumes(Pageable pageable);
    
    /**
     * 根据候选人ID获取简历
     */
    List<Resume> getResumesByCandidateId(Long candidateId);
    
    /**
     * 保存简历
     */
    Resume saveResume(Resume resume);
    
    /**
     * 删除简历
     */
    void deleteResume(Long id);
    
    /**
     * 根据状态获取简历数量
     */
    long countByStatus(String status);
    
    /**
     * 搜索简历
     */
    Page<Resume> searchResumes(String keyword, Pageable pageable);
}