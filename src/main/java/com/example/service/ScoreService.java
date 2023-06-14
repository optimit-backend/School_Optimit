package com.example.service;

import com.example.entity.*;
import com.example.exception.RecordNotFoundException;
import com.example.exception.UserNotFoundException;
import com.example.model.common.ApiResponse;
import com.example.model.request.ScoreDto;
import com.example.model.request.ScoreRequestDto;
import com.example.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.example.enums.Constants.*;

@Service
@RequiredArgsConstructor
public class ScoreService implements BaseService<ScoreRequestDto, UUID> {

    private final ScoreRepository scoreRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final JournalRepository journalRepository;


    @Override
    public ApiResponse create(ScoreRequestDto dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new UserNotFoundException(STUDENT_NOT_FOUND));
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RecordNotFoundException(SUBJECT_NOT_FOUND));
        User teacher = userRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new UserNotFoundException(TEACHER_NOT_FOUND));
        Journal journal = journalRepository.findById(dto.getJournalId())
                .orElseThrow(() -> new UserNotFoundException(JOURNAL_NOT_FOUND));
        Score score = Score.builder()
                .score(dto.getScore())
                .student(student)
                .teacher(teacher)
                .subject(subject)
                .journal(journal)
                .createdDate(dto.getCreatedDate())
                .build();
        scoreRepository.save(score);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @Override
    public ApiResponse getById(UUID uuid) {
        return null;
    }

    @Override
    public ApiResponse update(ScoreRequestDto dto) {
        Score score = scoreRepository.findById(dto.getId())
                .orElseThrow(() -> new RecordNotFoundException(SCORE_NOT_FOUND));
        score.setScore(dto.getScore());
        scoreRepository.save(score);
        return new ApiResponse(SUCCESSFULLY, true);
    }

    @Override
    public ApiResponse delete(UUID id) {
        scoreRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(SCORE_NOT_FOUND));
        scoreRepository.deleteById(id);
        return new ApiResponse(DELETED, true);
    }

    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getAll(ScoreDto scoreDto) {
        int dayOfWeek = LocalDate.now().atStartOfDay().getDayOfWeek().getValue();
        LocalDateTime startWeek = LocalDateTime.now().minusDays(dayOfWeek);
        LocalDateTime endWeek = startWeek.plusDays(7);
//        Pageable pageable = PageRequest.of(scoreDto.getPage(), scoreDto.getSize());
//        Page<Score> scoreList = scoreRepository.findAllByJournalIdAndTeacherIdAndSubjectId(scoreDto.getJournalId(), scoreDto.getTeacherId(), scoreDto.getSubjectId(), pageable);
//        return new ApiResponse(new ScoreResponseList(
//                scoreList.getContent(), scoreList.getTotalElements(), scoreList.getTotalPages(), scoreList.getNumber()), true);
        List<Score> all = scoreRepository.findAllByJournalIdAndTeacherIdAndSubjectIdAndCreatedDateBetween(scoreDto.getJournalId(), scoreDto.getTeacherId(), scoreDto.getSubjectId(), startWeek, endWeek);
        return new ApiResponse(all, true);
    }
}
