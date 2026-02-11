package com.study.my_spring_study_diary.dao;

import com.study.my_spring_study_diary.entity.Category;
import com.study.my_spring_study_diary.entity.StudyLog;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StudyLogDao {

    StudyLog save(StudyLog studyLog);

//    Optional<StudyLog> findById(Long id);
//
//    List<StudyLog> findAll();
//
//    List<StudyLog> findByCategory(String category);
//
//    List<StudyLog> findByCategory(Category category);
//
//    List<StudyLog> findByStudyDate(LocalDate date);
//
//    boolean existsById(Long id);
//
//    long count();
//
//    StudyLog update(StudyLog studyLog);
//
//    boolean deleteById(Long id);
//
//    void deleteAll();
}
