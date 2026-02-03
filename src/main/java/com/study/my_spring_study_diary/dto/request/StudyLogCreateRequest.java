package com.study.my_spring_study_diary.dto.request;

import java.time.LocalDate;

public class StudyLogCreateRequest {

    private String title;
    private String content;
    private String category;
    private String understanding;
    private Integer studyTime;
    private LocalDate studyDate;

    // 기본 생성자 (JSON 역직렬화를 위해 필요)
    public StudyLogCreateRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUnderstanding() {
        return understanding;
    }

    public void setUnderstanding(String understanding) {
        this.understanding = understanding;
    }

    public Integer getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(Integer studyTime) {
        this.studyTime = studyTime;
    }

    public LocalDate getStudyDate() {
        return studyDate;
    }

    public void setStudyDate(LocalDate studyDate) {
        this.studyDate = studyDate;
    }
}
