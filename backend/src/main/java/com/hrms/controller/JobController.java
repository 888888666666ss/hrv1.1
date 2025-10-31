package com.hrms.controller;

import com.hrms.dto.JobCreateRequest;
import com.hrms.dto.JobResponse;
import com.hrms.entity.Job;
import com.hrms.service.JobService;
import com.hrms.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
@CrossOrigin(origins = "http://localhost:5173")
public class JobController {
    
    @Autowired
    private JobService jobService;
    
    @Autowired
    private JwtService jwtService;
    
    @PostMapping
    public ResponseEntity<?> createJob(@Valid @RequestBody JobCreateRequest request, HttpServletRequest httpRequest) {
        try {
            String username = getCurrentUsername(httpRequest);
            JobResponse response = jobService.createJob(request, username);
            return ResponseEntity.ok(createSuccessResponse("职位创建成功", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("职位创建失败", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @Valid @RequestBody JobCreateRequest request, HttpServletRequest httpRequest) {
        try {
            String username = getCurrentUsername(httpRequest);
            JobResponse response = jobService.updateJob(id, request, username);
            return ResponseEntity.ok(createSuccessResponse("职位更新成功", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("职位更新失败", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id, HttpServletRequest httpRequest) {
        try {
            String username = getCurrentUsername(httpRequest);
            jobService.deleteJob(id, username);
            return ResponseEntity.ok(createSuccessResponse("职位删除成功", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("职位删除失败", e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Long id) {
        try {
            JobResponse response = jobService.getJobById(id);
            return ResponseEntity.ok(createSuccessResponse("获取职位详情成功", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("获取职位详情失败", e.getMessage()));
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Job.JobStatus status,
            @RequestParam(required = false) Job.EmploymentType employmentType
    ) {
        try {
            Page<JobResponse> response = jobService.getJobs(page, size, sortBy, sortDir, title, department, status, employmentType);
            
            Map<String, Object> result = new HashMap<>();
            result.put("content", response.getContent());
            result.put("page", response.getNumber());
            result.put("size", response.getSize());
            result.put("totalElements", response.getTotalElements());
            result.put("totalPages", response.getTotalPages());
            result.put("first", response.isFirst());
            result.put("last", response.isLast());
            
            return ResponseEntity.ok(createSuccessResponse("获取职位列表成功", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("获取职位列表失败", e.getMessage()));
        }
    }
    
    @GetMapping("/active")
    public ResponseEntity<?> getActiveJobs() {
        try {
            List<JobResponse> response = jobService.getActiveJobs();
            return ResponseEntity.ok(createSuccessResponse("获取活跃职位列表成功", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("获取活跃职位列表失败", e.getMessage()));
        }
    }
    
    @GetMapping("/my")
    public ResponseEntity<?> getMyJobs(HttpServletRequest httpRequest) {
        try {
            String username = getCurrentUsername(httpRequest);
            List<JobResponse> response = jobService.getJobsByRecruiter(username);
            return ResponseEntity.ok(createSuccessResponse("获取我的职位列表成功", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("获取我的职位列表失败", e.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateJobStatus(@PathVariable Long id, @RequestBody Map<String, String> requestBody, HttpServletRequest httpRequest) {
        try {
            String username = getCurrentUsername(httpRequest);
            Job.JobStatus newStatus = Job.JobStatus.valueOf(requestBody.get("status"));
            JobResponse response = jobService.updateJobStatus(id, newStatus, username);
            return ResponseEntity.ok(createSuccessResponse("职位状态更新成功", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("职位状态更新失败", e.getMessage()));
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