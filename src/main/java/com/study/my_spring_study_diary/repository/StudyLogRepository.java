package com.study.my_spring_study_diary.repository;

import com.study.my_spring_study_diary.entity.Category;
import com.study.my_spring_study_diary.entity.StudyLog;
import com.study.my_spring_study_diary.exception.InvalidPageRequestException;
import com.study.my_spring_study_diary.global.common.PageRequest;
import com.study.my_spring_study_diary.global.common.PageResponse;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 학습 일지 저장소
 *
 * @Repository 어노테이션 설명:
 * - 이 클래스를 Spring Bean으로 등록합니다
 * - 데이터 접근 계층임을 명시합니다
 * - 데이터 접근 관련 예외를 Spring의 DataAccessException으로 변환해줍니다
 *
 * 실제 프로젝트에서는 JPA, MyBatis 등을 사용하지만,
 * 이번 강의에서는 Map을 사용하여 데이터를 저장합니다.
 */

@Repository
public class StudyLogRepository {

    // 데이터 저장소 (실제 DB 대신 Map 사용)
    private final Map<Long, StudyLog> database = new HashMap<>();

    // ID 자동 증가를 위한 시퀀스
    private final AtomicLong sequence = new AtomicLong();

    @PostConstruct
    public void init() {
        System.out.println("🚀 StudyLogRepository 초기화 완료!");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("🔚 StudyLogRepository 종료! 저장된 데이터: " + database.size() + "개");
    }

    /**
     * 학습 일지 저장
     *
     * @param studyLog 저장할 학습 일지
     * @return 저장된 학습 일지 (ID 포함)
     */
    public StudyLog save(StudyLog studyLog) {
        // ID가 없으면 새로운 ID 부여
        if (studyLog.getId() == null) {
            studyLog.setId(sequence.getAndIncrement());
        }

        //Map에 저장
        database.put(studyLog.getId(), studyLog);

        return studyLog;
    }

    /**
     * 학습 일지 수정 (Update)
     * Map은 같은 키로 put하면 덮어쓰므로 save와 동일하게 동작
     * 하지만 의미를 명확히 하기 위해 별도 메서드로 분리
     */
    public StudyLog update(StudyLog studyLog) {
        validationStudyLogById(studyLog);
        database.put(studyLog.getId(), studyLog);
        return studyLog;
    }

    /**
     * studyLog의 ID 값 검증
     */
    private void validationStudyLogById(StudyLog studyLog) {
        if (studyLog.getId() == null) {
            throw new IllegalArgumentException("수정할 학습 일지의 ID가 없습니다.");
        }

        if (!database.containsKey(studyLog.getId())) {
            throw new IllegalArgumentException(
                    "해당 학습 일지를 찾을 수 없습니다. (id: " + studyLog.getId() + ")"
            );
        }
    }

    /**
     * 전체 학습 일지 조회 (최신순 정렬)
     */
    public List<StudyLog> findAll() {
        return database.values().stream()
                .sorted(Comparator.comparing(StudyLog::getCreatedAt))
                //.sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    /**
     * ID로 학습 일지 조회
     */
    public Optional<StudyLog> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    /**
     * 날짜로 학습 일지 조회
     */
    public List<StudyLog> findByStudyDate(LocalDate date) {
        return database.values().stream()
                .filter(log -> log.getStudyDate().equals(date))
                .sorted(Comparator.comparing(StudyLog::getCreatedAt))
                .collect(Collectors.toList());
    }

    /**
     * 카테고리 학습 일지 조회
     */
    public List<StudyLog> findByCategory(Category category) {
        return database.values().stream()
                .filter(log -> log.getCategory().equals(category))
                .sorted(Comparator.comparing(StudyLog::getCreatedAt))
                .collect(Collectors.toList());
    }

    /**
     * 저장된 데이터 개수 조회
     */
    public long count() {
        return database.size();
    }

    /**
     * 페이징 처리된 학습 일지 조회
     *
     * @param pageRequest 페이징 요청 정보
     * @return 페이징 처리된 결과
     */
    public PageResponse<StudyLog> findAllWithPaging(PageRequest pageRequest) {

        // 1.전체 데이터를 정렬
        List<StudyLog> allLogs = database.values().stream()
                .sorted((a,b) -> {
                    // 정렬 기준에 따라 정렬
                    int result = switch (pageRequest.getSortBy()) {
                        case "title" -> a.getTitle().compareTo(b.getTitle());
                        case "studyTime" -> a.getStudyTime().compareTo(b.getStudyTime());
                        case "studyDate" -> a.getStudyDate().compareTo(b.getStudyDate());
                        default -> a.getCreatedAt().compareTo(b.getCreatedAt());
                    };

                    // 정렬 방향 적용
                    return "ASC".equals(pageRequest.getSortDirection()) ? result : -result;
                })
                .collect(Collectors.toList());

        // 2. 전체 개수
        long totalElements = allLogs.size();

        // 3. 총 페이지 수 계산
        int totalPages = calculateTotalPages(totalElements, pageRequest.getSize());

        // 4. 요청한 페이지 번호 유효성 검증
        int requestedPage = pageRequest.getPage();

        if (requestedPage < 0) {
            throw new InvalidPageRequestException(requestedPage, totalPages);
        }

        if (totalElements > 0 && requestedPage >= totalPages) {
            throw new InvalidPageRequestException(requestedPage, totalPages);
        }

        // 5. 페이징 적용
        int start = pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getSize(), allLogs.size());

        List<StudyLog> pagedLogs = allLogs.subList(start, end);

        // 6. PageResponse 생성
        return PageResponse.of(
                pagedLogs,
                pageRequest.getPage(),
                pageRequest.getSize(),
                totalElements,
                totalPages
        );
    }

    /**
     * 카테고리별 페이징 조회
     *
     * @param category    카테고리
     * @param pageRequest 페이징 요청 정보
     * @return 페이징 처리된 결과
     */
    public PageResponse<StudyLog> findByCategoryWithPaging(Category category, PageRequest pageRequest) {

        // 1. 카테코리로 필터링 및 정렬
        List<StudyLog> filteredLogs = database.values().stream()
                .filter(log -> log.getCategory() == category)
                .sorted((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()))
                .collect(Collectors.toList());

        // 2. 전체 개수
        long totalElements = filteredLogs.size();

        // 3. 총 페이지 수 계산
        int totalPages = calculateTotalPages(totalElements, pageRequest.getSize());

        // 4. 요청한 페이지 번호 유효성 검증
        int requestedPage = pageRequest.getPage();

        if (requestedPage < 0) {
            throw new InvalidPageRequestException(requestedPage, totalPages);
        }

        if (totalElements > 0 && requestedPage >= totalPages) {
            throw new InvalidPageRequestException(requestedPage, totalPages);
        }

        // 5. 페이징 적용
        int start = pageRequest.getOffset();
        int end = Math.min(start + pageRequest.getSize(), filteredLogs.size());

        List<StudyLog> pagedLogs = filteredLogs.subList(start, end);

        // 6. PageResponse 생성
        return PageResponse.of(
                pagedLogs,
                pageRequest.getPage(),
                pageRequest.getSize(),
                totalElements,
                totalPages
        );

    }

    /**
     * 총 페이지 수 계산
     * @param totalElements 전체 데이터 개수
     * @param pageSize      페이지 크기
     * @return 총 페이지 수
     */
    private int calculateTotalPages(long totalElements, int pageSize) {
        return (int) Math.ceil((double) totalElements / pageSize);
    }
}
