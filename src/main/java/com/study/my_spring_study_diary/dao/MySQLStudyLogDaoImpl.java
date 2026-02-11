package com.study.my_spring_study_diary.dao;

import com.study.my_spring_study_diary.entity.StudyLog;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
@Primary  // 이 구현체를 기본으로 사용
public class MySQLStudyLogDaoImpl implements StudyLogDao {

    private final JdbcTemplate jdbcTemplate;

    public MySQLStudyLogDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ========== CREATE ==========
    public StudyLog save(StudyLog studyLog) {
        String sql = """
            INSERT INTO study_logs (title, content, category, understanding, study_time, study_date)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        // KeyHolder: Object to receive auto-generated ID
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, studyLog.getTitle());
            ps.setString(2, studyLog.getContent());
            ps.setString(3, studyLog.getCategory().name());
            ps.setString(4, studyLog.getUnderstanding().name());
            ps.setInt(5, studyLog.getStudyTime());
            ps.setDate(6, Date.valueOf(studyLog.getStudyDate()));
            return ps;
        }, keyHolder);

        // Set the generated ID to StudyLog object
        Number generatedId = keyHolder.getKey();
        if (generatedId != null) {
            studyLog.setId(generatedId.longValue());
        }

        return studyLog;
    }

}
