package com.study.my_spring_study_diary.dto.response;

public class StudyLogDeleteResponse {

    private String message;
    private Long deletedId;

    public static StudyLogDeleteResponse of(Long deletedId) {
        StudyLogDeleteResponse studyLogDeleteResponse = new StudyLogDeleteResponse();
        studyLogDeleteResponse.message = "학습 일지가 성공적으로 삭제되었습니다.";
        studyLogDeleteResponse.deletedId = deletedId;

        return  studyLogDeleteResponse;
    }

    public String getMessage() {
        return message;
    }

    public Long getDeletedId() {
        return deletedId;
    }
}
