package com.hrms.controller;

import com.hrms.entity.Candidate;
import com.hrms.service.CandidateService;
import com.hrms.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/candidates")
@CrossOrigin(origins = "*")
public class CandidateController {
    
    @Autowired
    private CandidateService candidateService;
    
    @Autowired
    private JwtService jwtService;
    
    /**
     * 创建候选人
     */
    @PostMapping
    public ResponseEntity<?> createCandidate(@Valid @RequestBody Candidate candidate, HttpServletRequest request) {
        try {
            String username = getCurrentUsername(request);
            Candidate createdCandidate = candidateService.createCandidate(candidate);
            return ResponseEntity.ok(createSuccessResponse("候选人创建成功", createdCandidate));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                createErrorResponse("候选人创建失败", e.getMessage())
            );
        }
    }
    
    /**
     * 获取所有候选人（分页）
     */
    @GetMapping
    public ResponseEntity<?> getAllCandidates(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            HttpServletRequest request) {
        try {
            getCurrentUsername(request); // 验证token
            
            Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<Candidate> candidates = candidateService.getAllCandidates(pageable);
            return ResponseEntity.ok(createSuccessResponse("获取候选人列表成功", candidates));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                createErrorResponse("获取候选人列表失败", e.getMessage())
            );
        }
    }
    
    /**
     * 根据ID获取候选人详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCandidateById(@PathVariable Long id, HttpServletRequest request) {
        try {
            getCurrentUsername(request); // 验证token
            
            Optional<Candidate> candidate = candidateService.getCandidateById(id);
            if (candidate.isPresent()) {
                return ResponseEntity.ok(createSuccessResponse("获取候选人信息成功", candidate.get()));
            } else {
                return ResponseEntity.badRequest().body(
                    createErrorResponse("候选人不存在", "未找到ID为" + id + "的候选人")
                );
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                createErrorResponse("获取候选人信息失败", e.getMessage())
            );
        }
    }
    
    /**
     * 更新候选人信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCandidate(@PathVariable Long id, 
                                             @Valid @RequestBody Candidate candidate, 
                                             HttpServletRequest request) {
        try {
            getCurrentUsername(request); // 验证token
            
            Candidate updatedCandidate = candidateService.updateCandidate(id, candidate);
            return ResponseEntity.ok(createSuccessResponse("候选人信息更新成功", updatedCandidate));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                createErrorResponse("候选人信息更新失败", e.getMessage())
            );
        }
    }
    
    /**
     * 删除候选人
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCandidate(@PathVariable Long id, HttpServletRequest request) {
        try {
            getCurrentUsername(request); // 验证token
            
            candidateService.deleteCandidate(id);
            return ResponseEntity.ok(createSuccessResponse("候选人删除成功", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                createErrorResponse("候选人删除失败", e.getMessage())
            );
        }
    }
    
    /**
     * 搜索候选人
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchCandidates(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String currentPosition,
            @RequestParam(required = false) String currentCompany,
            @RequestParam(required = false) String skill,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        try {
            getCurrentUsername(request); // 验证token
            
            Candidate.CandidateStatus candidateStatus = null;
            if (status != null && !status.trim().isEmpty()) {
                candidateStatus = Candidate.CandidateStatus.valueOf(status.toUpperCase());
            }
            
            Candidate.CandidateSource candidateSource = null;
            if (source != null && !source.trim().isEmpty()) {
                candidateSource = Candidate.CandidateSource.valueOf(source.toUpperCase());
            }
            
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            
            Page<Candidate> candidates = candidateService.searchCandidates(
                name, email, phone, candidateStatus, candidateSource, 
                currentPosition, currentCompany, skill, pageable
            );
            
            return ResponseEntity.ok(createSuccessResponse("搜索候选人成功", candidates));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                createErrorResponse("搜索候选人失败", e.getMessage())
            );
        }
    }
    
    /**
     * 获取候选人统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getCandidateStatistics(HttpServletRequest request) {
        try {
            getCurrentUsername(request); // 验证token
            
            Map<String, Object> statistics = Map.of(
                "total", candidateService.getTotalCandidatesCount(),
                "statusStats", candidateService.getCandidateStatusStatistics(),
                "sourceStats", candidateService.getCandidateSourceStatistics(),
                "recentCandidates", candidateService.getRecentCandidates(7).size()
            );
            
            return ResponseEntity.ok(createSuccessResponse("获取候选人统计信息成功", statistics));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                createErrorResponse("获取候选人统计信息失败", e.getMessage())
            );
        }
    }
    
    private String getCurrentUsername(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("未提供有效的认证令牌");
        }
        
        String token = authHeader.substring(7);
        return jwtService.extractUsername(token);
    }
    
    private Map<String, Object> createSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    private Map<String, Object> createErrorResponse(String message, String error) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 400);
        response.put("message", message);
        response.put("error", error);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}