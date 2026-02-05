package com.study.my_spring_study_diary.service;

import com.study.my_spring_study_diary.dto.request.StudyLogCreateRequest;
import com.study.my_spring_study_diary.dto.response.StudyLogResponse;
import com.study.my_spring_study_diary.entity.Category;
import com.study.my_spring_study_diary.entity.StudyLog;
import com.study.my_spring_study_diary.entity.Understanding;
import com.study.my_spring_study_diary.global.common.PageRequest;
import com.study.my_spring_study_diary.global.common.PageResponse;
import com.study.my_spring_study_diary.repository.StudyLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 학습 일지 서비스
 *
 * @Service 어노테이션 설명:
 * - 이 클래스를 Spring Bean으로 등록합니다
 * - 비즈니스 로직을 담당하는 서비스 계층임을 명시합니다
 * - @Component와 기능적으로 동일하지만, 역할을 명확히 표현합니다
 */
@Service
public class StudyLogService {

    // 의존성 주입: Repository를 주입받음
    private final StudyLogRepository studyLogRepository;

    /**
     * 생성자 주입 (Constructor Injection)
     * <p>
     * Spring이 StudyLogRepository Bean을 찾아서 자동으로 주입해줍니다.
     * 생성자가 1개만 있으면 @Autowired 생략 가능!
     */
    public StudyLogService(StudyLogRepository studyLogRepository) {
        this.studyLogRepository = studyLogRepository;
    }

    /**
     * 학습 일지 생성
     * @param request 생성 요청 DTO
     * @return 생성된 학습 일지 응답 DTO
     */
    public StudyLogResponse createStudyLog(StudyLogCreateRequest request) {

        // 1. 요청 데이터 유효성 검증
        validateCreateRequest(request);

        // 2. DTO → Entity 변환
        StudyLog studyLog = new StudyLog (
                null,  // ID는 Repository에서 자동 생성
                request.getTitle(),
                request.getContent(),
                Category.valueOf(request.getCategory()),
                Understanding.valueOf(request.getUnderstanding()),
                request.getStudyTime(),
                request.getStudyDate() != null ? request.getStudyDate() : LocalDate.now()
        );

        // 3. 저장
        StudyLog savedStudyLog = studyLogRepository.save(studyLog);

        // 4. Entity → Response DTO 변환 후 반환
        return StudyLogResponse.from(savedStudyLog);
    }

    /**
     * 전체 학습 일지 목록 조회
     */
    public List<StudyLogResponse> getAllStudyLogs() {
        List<StudyLog> studyLogs = studyLogRepository.findAll();

        //Entity 리스트 -> Response DTO 리스트로 반환
        return studyLogs.stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());
    }


    /**
     * ID로 학습 일지 단건 조회
     */
    public StudyLogResponse getStudyLogById(Long id) {
        StudyLog studyLog = studyLogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 학습 일지를 찾을 수 없습니다. (id: " + id + ")"));

        return StudyLogResponse.from(studyLog);
    }

    /**
     * 날짜별 학습 일지 조회
     */
    public List<StudyLogResponse> getStudyLogsByDate(LocalDate date) {
        List<StudyLog> studyLogs = studyLogRepository.findByStudyDate(date);

        return studyLogs.stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리 학습 일지 조회
     */
    public List<StudyLogResponse> getStudyLogsByCategory(String categoryName) {
        // 문자열 -> Enum 반환(유효성 검증 포함)
        Category category;
        try {
            category = Category.valueOf(categoryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 카테고리입니다: " + categoryName);
        }

        List<StudyLog> studyLogs = studyLogRepository.findByCategory(category);

        return studyLogs.stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 페이징 처리된 학습 일지 목록 조회
     */
    public PageResponse<StudyLogResponse> getStudyLogsWithPaging(PageRequest pageRequest) {

        //Repositoty에서 페이징 처리된 데이터 조회
        PageResponse<StudyLog> pageResult = studyLogRepository.findAllWithPaging(pageRequest);

        //Entity를 Response DTO로 변환
        List<StudyLogResponse> responses = pageResult.getContent().stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());

        // 페이징 정보를 유지하면서 DTO로 변환
        return PageResponse.of(
                responses,
                pageResult.getPageNumber(),
                pageResult.getPageSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages()
        );
    }

    /**
     * 카테고리별 페이징 조회
     */
    public PageResponse<StudyLogResponse> getStudyLogsByCategoryWithPaging(String categoryName, PageRequest pageRequest) {

        Category category;
        try {
            category = Category.valueOf(categoryName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 카테고리: " + categoryName);
        }

        PageResponse<StudyLog> pageResult = studyLogRepository.findByCategoryWithPaging(category, pageRequest);

        List<StudyLogResponse> responses = pageResult.getContent().stream()
                .map(StudyLogResponse::from)
                .collect(Collectors.toList());

        return PageResponse.of(
                responses,
                pageResult.getPageNumber(),
                pageResult.getPageSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages()
        );
    }

    /**
     * 생성 요청 유효성 검증
     */
    private void validateCreateRequest(StudyLogCreateRequest request) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("학습 주제는 필수입니다.");
        }
        if (request.getTitle().length() > 100) {
            throw new IllegalArgumentException("학습 주제는 100자를 초과할 수 없습니다.");
        }
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("학습 내용은 필수입니다.");
        }
        if (request.getContent().length() > 1000) {
            throw new IllegalArgumentException("학습 내용은 1000자를 초과할 수 없습니다.");
        }
        if (request.getStudyTime() == null || request.getStudyTime() < 1) {
            throw new IllegalArgumentException("학습 시간은 1분 이상이어야 합니다.");
        }
    }



}
