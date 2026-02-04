package com.study.my_spring_study_diary.repository;

import com.study.my_spring_study_diary.entity.Category;
import com.study.my_spring_study_diary.entity.StudyLog;
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
}
