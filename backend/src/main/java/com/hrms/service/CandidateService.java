package com.hrms.service;

import com.hrms.entity.Candidate;
import com.hrms.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CandidateService {
    
    @Autowired
    private CandidateRepository candidateRepository;
    
    /**
     * 创建候选人
     */
    public Candidate createCandidate(Candidate candidate) {
        // 检查邮箱是否已存在
        if (candidate.getEmail() != null && candidateRepository.existsByEmail(candidate.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 检查电话是否已存在
        if (candidate.getPhone() != null && candidateRepository.existsByPhone(candidate.getPhone())) {
            throw new RuntimeException("电话号码已存在");
        }
        
        return candidateRepository.save(candidate);
    }
    
    /**
     * 更新候选人
     */
    public Candidate updateCandidate(Long id, Candidate candidateUpdates) {
        Optional<Candidate> existingCandidate = candidateRepository.findById(id);
        if (!existingCandidate.isPresent()) {
            throw new RuntimeException("候选人不存在");
        }
        
        Candidate candidate = existingCandidate.get();
        
        // 检查邮箱是否与其他候选人重复
        if (candidateUpdates.getEmail() != null && 
            !candidateUpdates.getEmail().equals(candidate.getEmail()) &&
            candidateRepository.existsByEmail(candidateUpdates.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }
        
        // 检查电话是否与其他候选人重复
        if (candidateUpdates.getPhone() != null && 
            !candidateUpdates.getPhone().equals(candidate.getPhone()) &&
            candidateRepository.existsByPhone(candidateUpdates.getPhone())) {
            throw new RuntimeException("电话号码已存在");
        }
        
        // 更新字段
        if (candidateUpdates.getName() != null) {
            candidate.setName(candidateUpdates.getName());
        }
        if (candidateUpdates.getEmail() != null) {
            candidate.setEmail(candidateUpdates.getEmail());
        }
        if (candidateUpdates.getPhone() != null) {
            candidate.setPhone(candidateUpdates.getPhone());
        }
        if (candidateUpdates.getSource() != null) {
            candidate.setSource(candidateUpdates.getSource());
        }
        if (candidateUpdates.getStatus() != null) {
            candidate.setStatus(candidateUpdates.getStatus());
        }
        if (candidateUpdates.getCurrentPosition() != null) {
            candidate.setCurrentPosition(candidateUpdates.getCurrentPosition());
        }
        if (candidateUpdates.getCurrentCompany() != null) {
            candidate.setCurrentCompany(candidateUpdates.getCurrentCompany());
        }
        if (candidateUpdates.getWorkExperience() != null) {
            candidate.setWorkExperience(candidateUpdates.getWorkExperience());
        }
        if (candidateUpdates.getEducationLevel() != null) {
            candidate.setEducationLevel(candidateUpdates.getEducationLevel());
        }
        if (candidateUpdates.getGraduationSchool() != null) {
            candidate.setGraduationSchool(candidateUpdates.getGraduationSchool());
        }
        if (candidateUpdates.getMajor() != null) {
            candidate.setMajor(candidateUpdates.getMajor());
        }
        if (candidateUpdates.getSkills() != null) {
            candidate.setSkills(candidateUpdates.getSkills());
        }
        if (candidateUpdates.getSelfIntroduction() != null) {
            candidate.setSelfIntroduction(candidateUpdates.getSelfIntroduction());
        }
        if (candidateUpdates.getExpectedSalaryMin() != null) {
            candidate.setExpectedSalaryMin(candidateUpdates.getExpectedSalaryMin());
        }
        if (candidateUpdates.getExpectedSalaryMax() != null) {
            candidate.setExpectedSalaryMax(candidateUpdates.getExpectedSalaryMax());
        }
        if (candidateUpdates.getExpectedPosition() != null) {
            candidate.setExpectedPosition(candidateUpdates.getExpectedPosition());
        }
        
        return candidateRepository.save(candidate);
    }
    
    /**
     * 删除候选人
     */
    public void deleteCandidate(Long id) {
        if (!candidateRepository.existsById(id)) {
            throw new RuntimeException("候选人不存在");
        }
        candidateRepository.deleteById(id);
    }
    
    /**
     * 根据ID获取候选人
     */
    public Optional<Candidate> getCandidateById(Long id) {
        return candidateRepository.findById(id);
    }
    
    /**
     * 获取所有候选人（分页）
     */
    public Page<Candidate> getAllCandidates(Pageable pageable) {
        return candidateRepository.findAll(pageable);
    }
    
    /**
     * 根据状态获取候选人
     */
    public Page<Candidate> getCandidatesByStatus(Candidate.CandidateStatus status, Pageable pageable) {
        return candidateRepository.findByStatus(status, pageable);
    }
    
    /**
     * 根据来源获取候选人
     */
    public Page<Candidate> getCandidatesBySource(Candidate.CandidateSource source, Pageable pageable) {
        return candidateRepository.findBySource(source, pageable);
    }
    
    /**
     * 根据姓名搜索候选人
     */
    public Page<Candidate> searchCandidatesByName(String name, Pageable pageable) {
        return candidateRepository.findByNameContainingIgnoreCase(name, pageable);
    }
    
    /**
     * 多条件搜索候选人
     */
    public Page<Candidate> searchCandidates(String name, String email, String phone, 
                                            Candidate.CandidateStatus status, 
                                            Candidate.CandidateSource source,
                                            String currentPosition, String currentCompany, 
                                            String skill, Pageable pageable) {
        return candidateRepository.findCandidatesWithCriteria(
            name, email, phone, status, source, 
            currentPosition, currentCompany, skill, pageable
        );
    }
    
    /**
     * 获取最近N天的新候选人
     */
    public List<Candidate> getRecentCandidates(int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        return candidateRepository.findRecentCandidates(startDate);
    }
    
    /**
     * 获取候选人状态统计
     */
    public Map<String, Long> getCandidateStatusStatistics() {
        List<Object[]> results = candidateRepository.countCandidatesByStatus();
        return results.stream().collect(
            Collectors.toMap(
                result -> result[0].toString(),
                result -> (Long) result[1]
            )
        );
    }
    
    /**
     * 获取候选人来源统计
     */
    public Map<String, Long> getCandidateSourceStatistics() {
        List<Object[]> results = candidateRepository.countCandidatesBySource();
        return results.stream().collect(
            Collectors.toMap(
                result -> result[0].toString(),
                result -> (Long) result[1]
            )
        );
    }
    
    /**
     * 根据技能搜索候选人
     */
    public List<Candidate> searchCandidatesBySkill(String skill) {
        return candidateRepository.findBySkillsContaining(skill);
    }
    
    /**
     * 根据工作年限范围搜索候选人
     */
    public List<Candidate> searchCandidatesByExperience(Integer minExperience, Integer maxExperience) {
        return candidateRepository.findByWorkExperienceBetween(minExperience, maxExperience);
    }
    
    /**
     * 根据薪资范围搜索候选人
     */
    public List<Candidate> searchCandidatesBySalary(Integer minSalary, Integer maxSalary) {
        return candidateRepository.findBySalaryRange(minSalary, maxSalary);
    }
    
    /**
     * 更新候选人状态
     */
    public Candidate updateCandidateStatus(Long id, Candidate.CandidateStatus status) {
        Optional<Candidate> candidateOpt = candidateRepository.findById(id);
        if (!candidateOpt.isPresent()) {
            throw new RuntimeException("候选人不存在");
        }
        
        Candidate candidate = candidateOpt.get();
        candidate.setStatus(status);
        return candidateRepository.save(candidate);
    }
    
    /**
     * 批量更新候选人状态
     */
    public void batchUpdateCandidateStatus(List<Long> candidateIds, Candidate.CandidateStatus status) {
        List<Candidate> candidates = candidateRepository.findAllById(candidateIds);
        for (Candidate candidate : candidates) {
            candidate.setStatus(status);
        }
        candidateRepository.saveAll(candidates);
    }
    
    /**
     * 批量删除候选人
     */
    public void batchDeleteCandidates(List<Long> candidateIds) {
        candidateRepository.deleteAllById(candidateIds);
    }
    
    /**
     * 获取候选人总数
     */
    public long getTotalCandidatesCount() {
        return candidateRepository.count();
    }
    
    /**
     * 检查邮箱是否存在
     */
    public boolean existsByEmail(String email) {
        return candidateRepository.existsByEmail(email);
    }
    
    /**
     * 检查电话是否存在
     */
    public boolean existsByPhone(String phone) {
        return candidateRepository.existsByPhone(phone);
    }
}