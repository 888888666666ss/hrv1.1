package com.hrms.service.impl;

import com.hrms.entity.Resume;
import com.hrms.repository.ResumeRepository;
import com.hrms.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResumeServiceImpl implements ResumeService {
    
    @Autowired
    private ResumeRepository resumeRepository;
    
    @Override
    public Resume getResumeById(Long id) {
        Optional<Resume> resume = resumeRepository.findById(id);
        if (resume.isPresent()) {
            return resume.get();
        }
        throw new RuntimeException("简历不存在");
    }
    
    @Override
    public Page<Resume> getResumes(Pageable pageable) {
        return resumeRepository.findAll(pageable);
    }
    
    @Override
    public List<Resume> getResumesByCandidateId(Long candidateId) {
        return resumeRepository.findByCandidateId(candidateId);
    }
    
    @Override
    public Resume saveResume(Resume resume) {
        return resumeRepository.save(resume);
    }
    
    @Override
    public void deleteResume(Long id) {
        resumeRepository.deleteById(id);
    }
    
    @Override
    public long countByStatus(String status) {
        // 将字符串转换为枚举
        Resume.ResumeStatus resumeStatus = Resume.ResumeStatus.valueOf(status.toUpperCase());
        return resumeRepository.countByStatus(resumeStatus);
    }
    
    @Override
    public Page<Resume> searchResumes(String keyword, Pageable pageable) {
        return resumeRepository.findByFileNameContainingIgnoreCase(keyword, pageable);
    }
}