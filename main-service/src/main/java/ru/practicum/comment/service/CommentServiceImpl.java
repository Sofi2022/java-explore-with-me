package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.comment.CommentDto;
import ru.practicum.comment.NewCommentDto;
import ru.practicum.comment.UpdateCommentDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.requests.repository.ParticipationRequestsRepository;
import ru.practicum.state.State;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;

    private final EventsRepository eventsRepository;

    private final CommentMapper commentMapper;

    private final CommentRepository commentRepository;

    private final ParticipationRequestsRepository partRepository;

    @Override
    @Transactional
    public CommentDto addComment(Long eventId, NewCommentDto comment, Long userId) {
        User author = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя нет "
                + userId));
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Такого события нет "
                + eventId));

        if (event.getState().equals(State.PUBLISHED)) {
            Comment comment1 = commentMapper.toComment(comment);
            comment1.setAuthor(author);
            comment1.setEvent(event);
            comment1.setCreated(LocalDateTime.now());
            Comment savedCom = commentRepository.save(comment1);
            return commentMapper.toDtoResponse(savedCom);
        } else {
            throw new NotFoundException("Событие еще не опубликовано " + eventId);
        }
    }


    @Override
    public List<CommentDto> getAllUserComments(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя нет "
                + userId));
        List<Comment> comments = commentRepository.findAllByAuthor_Id(userId);
        if (comments.size() == 0) {
            return Collections.emptyList();
        }
        return commentMapper.toDtoList(comments);
    }


    @Override
    @Transactional
    public CommentDto updateComment(Long comId, UpdateCommentDto comment) {
        Comment savedComment = commentRepository.findById(comId).orElseThrow(() -> new NotFoundException("Такого " +
                "комментария нет " + comId));
        if (comment.getText() != null) {
            savedComment.setText(comment.getText());
        }
        savedComment.setCreated(LocalDateTime.now());
        return commentMapper.toDtoResponse(commentRepository.save(savedComment));
    }


    @Override
    public CommentDto getCommentById(Long comId) {
        Comment savedComment = commentRepository.findById(comId).orElseThrow(() -> new NotFoundException("Такого " +
                "комментария нет "
                + comId));
        return commentMapper.toDtoResponse(commentRepository.save(savedComment));
    }


    @Override
    public List<CommentDto> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return commentMapper.toDtoList(comments);
    }


    @Override
    public void deleteComment(Long userId, Long comId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Такого пользователя нет "
                + userId));
        commentRepository.findById(comId).orElseThrow(() ->
                new NotFoundException("Такого комментария нет " + comId));
        commentRepository.deleteById(comId);
    }
}
