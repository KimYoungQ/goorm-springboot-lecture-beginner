package com.study.my_spring_study_diary.dao;

import com.study.my_spring_study_diary.entity.Category;
import com.study.my_spring_study_diary.entity.StudyLog;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudyLogDao {

    // ========== CREATE ==========
    StudyLog save(StudyLog studyLog);


    // ========== READ ==========
    Optional<StudyLog> findById(Long id);

    List<StudyLog> findAll();

    List<StudyLog> findByCategory(String category);

    List<StudyLog> findByStudyDate(LocalDate date);



    // ========== UPDATE ==========
    StudyLog update(StudyLog studyLog);


    // ========== DELETE ==========
    boolean deleteById(Long id);

    void deleteAll();


    boolean existsById(Long id);

    long count();
}
