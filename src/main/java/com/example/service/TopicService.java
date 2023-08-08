package com.example.service;

import com.example.entity.Topic;
import com.example.exception.RecordAlreadyExistException;
import com.example.exception.RecordNotFoundException;
import com.example.model.common.ApiResponse;
import com.example.model.request.TopicRequest;
import com.example.model.response.TopicResponse;
import com.example.repository.LevelRepository;
import com.example.repository.SubjectRepository;
import com.example.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

import static com.example.enums.Constants.*;

@Service
@RequiredArgsConstructor
public class TopicService implements BaseService<TopicRequest, Integer> {

    private final TopicRepository topicRepository;
    private final SubjectRepository subjectRepository;
    private final AttachmentService attachmentService;
    private final LevelRepository levelRepository;

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse create(TopicRequest dto) {
        if (topicRepository.existsByNameAndSubjectIdAndLevelId(dto.getName(), dto.getSubjectId(), dto.getLevelId())) {
            throw new RecordAlreadyExistException(TOPIC_ALREADY_EXIST);
        }
        Topic topic = Topic.toEntity(dto);
        setTopic(dto, topic);
        topicRepository.save(topic);
        return new ApiResponse(SUCCESSFULLY, true);
    }


    @Override
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse getById(Integer id) {
        Topic topic = topicRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(TOPIC_NOT_FOUND));
        TopicResponse topicResponse = TopicResponse.from(topic, attachmentService.getUrlList(topic.getLessonFiles()));
        return new ApiResponse(SUCCESSFULLY, true, topicResponse);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse update(TopicRequest dto) {
        checkingTopicByExists(dto);
        Topic topic = Topic.toEntity(dto);
        topic.setId(dto.getId());
        setTopic(dto, topic);
        topicRepository.save(topic);
        TopicResponse responseDto = TopicResponse.from(topic, attachmentService.getUrlList(topic.getLessonFiles()));
        return new ApiResponse(SUCCESSFULLY, true, responseDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse delete(Integer id) {
        Topic oldTopic = topicRepository.findById(id).orElseThrow(() -> new RecordNotFoundException(TOPIC_NOT_FOUND));
        oldTopic.getLessonFiles().forEach(attachmentService::deleteNewName);
        topicRepository.deleteById(id);
        return new ApiResponse(DELETED, true, TopicResponse.from(oldTopic, null));
    }

    public List<TopicResponse> findALLBySubjectId(Integer subjectId, Integer levelId) {
        List<Topic> all = topicRepository.findAllBySubjectIdAndLevelId(subjectId, levelId, Sort.by(Sort.Direction.DESC, "id"));
        List<TopicResponse> responses = new ArrayList<>();
        all.forEach(topic -> {
            responses.add(TopicResponse.from(topic, attachmentService.getUrlList(topic.getLessonFiles())));
        });
        return responses;
    }

    private void setTopic(TopicRequest dto, Topic topic) {
        topic.setLevel(levelRepository.findById(dto.getLevelId()).orElseThrow(() -> new RecordNotFoundException(LEVEL_NOT_FOUND)));
        topic.setSubject(subjectRepository.findById(dto.getSubjectId()).orElseThrow(() -> new RecordNotFoundException(SUBJECT_NOT_FOUND)));
        topic.setLessonFiles(attachmentService.saveToSystemListFile(dto.getLessonFiles()));
    }
    private void checkingTopicByExists(TopicRequest dto) {
        Topic old = topicRepository.findById(dto.getId()).orElseThrow(() -> new RecordNotFoundException(TOPIC_NOT_FOUND));
        if (!dto.getName().equals(old.getName()) || !dto.getSubjectId().equals(old.getSubject().getId()) || !dto.getLevelId().equals(old.getLevel().getId())
                && topicRepository.existsByNameAndSubjectIdAndLevelId(dto.getName(), dto.getSubjectId(), dto.getLevelId())) {
            throw new RecordNotFoundException(TOPIC_ALREADY_EXIST);
        }
    }
}
