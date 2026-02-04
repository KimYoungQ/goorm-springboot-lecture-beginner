package com.study.my_spring_study_diary.controller;

import com.study.my_spring_study_diary.dto.request.StudyLogCreateRequest;
import com.study.my_spring_study_diary.dto.response.StudyLogResponse;
import com.study.my_spring_study_diary.global.common.ApiResponse;
import com.study.my_spring_study_diary.service.StudyLogService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 학습 일지 컨트롤러
 *
 * @RestController 어노테이션 설명:
 * - @Controller + @ResponseBody 의 조합
 * - 이 클래스의 모든 메서드 반환값을 JSON으로 변환하여 응답
 * - REST API 개발 시 사용
 * @RequestMapping 어노테이션 설명:
 * - 이 컨트롤러의 기본 URL 경로를 설정
 * - 모든 메서드의 URL 앞에 "/api/v1/logs"가 붙음
 */

@RestController
@RequestMapping("/api/v1/logs")
public class StudyLogController {

    private final StudyLogService studyLogService;

    public StudyLogController(StudyLogService studyLogService) {
        this.studyLogService = studyLogService;
    }

    /**
     * 학습 일지 생성 (CREATE)
     *
     * @PostMapping: POST 요청을 처리
     * @RequestBody: HTTP Body의 JSON을 객체로 변환
     * <p>
     * POST /api/v1/logs
     */
    @PostMapping
    public ResponseEntity<ApiResponse<StudyLogResponse>> createStudyLog(@RequestBody StudyLogCreateRequest request) {
        //Service 호출하여 학습 일지 생성
        StudyLogResponse response = studyLogService.createStudyLog(request);

        //201 Created 상태 코드와 함께 응답
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    /**
     * 모든 학습 일지 조회 (READ - All)
     *
     * @GetMapping: GET 요청을 처리
     * <p>
     * GET /api/v1/logs
     */
    @GetMapping
    public List<StudyLogResponse> getAllStudyLogs() {

        // Service 호출하여 모든 학습 일지 조회
        return studyLogService.getAllStudyLogs();
    }

    /**
     * 특정 학습 일지 조회 (READ - Single)
     *
     * @GetMapping("/{id}"): GET 요청을 처리 (경로 변수 포함)
     * @PathVariable: URL 경로의 {id} 값을 매개변수로 받음
     * <p>
     * GET /api/v1/logs/{id}
     */
    @GetMapping("/id")
    public StudyLogResponse getStudyLogById(@PathVariable Long id) {
        return studyLogService.getStudyLogById(id);
    }

    /**
     * 날짜별 학습 일지 조회 (READ - By Date)
     *
     * @GetMapping("/date/{date}"): GET 요청을 처리 (날짜 경로 변수 포함)
     * @PathVariable: URL 경로의 {date} 값을 매개변수로 받음
     * <p>
     * GET /api/v1/logs/date/{date}
     * 예시: GET /api/v1/logs/date/2025-01-15
     */
    @GetMapping("/date/{date}")
    public List<StudyLogResponse> getStudyLogByDate(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {
        return studyLogService.getStudyLogsByDate(date);
    }

    /**
     * 카테고리별 학습 일지 조회 (READ - By Category)
     *
     * @GetMapping("/category/{category}"): GET 요청을 처리 (카테고리 경로 변수 포함)
     * @PathVariable: URL 경로의 {category} 값을 매개변수로 받음
     * <p>
     * GET /api/v1/logs/category/{category}
     * 예시: GET /api/v1/logs/category/SPRING
     * GET /api/v1/logs/category/JAVA
     */
    @GetMapping("/category/{category}")
    public List<StudyLogResponse> getStudyLogsByCategory(@PathVariable String category) {
        return studyLogService.getStudyLogsByCategory(category);
    }
}
