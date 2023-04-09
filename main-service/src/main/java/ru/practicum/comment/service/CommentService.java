package ru.practicum.comment.service;

import ru.practicum.comment.CommentDto;
import ru.practicum.comment.NewCommentDto;
import ru.practicum.comment.UpdateCommentDto;

import java.util.List;


public interface CommentService {

    CommentDto addComment(Long eventId, NewCommentDto comment, Long userId);

    List<CommentDto> getAllUserComments(Long userId);

    CommentDto updateComment(Long comId, UpdateCommentDto comment);

    CommentDto getCommentById(Long comId);

    List<CommentDto> getAllComments();

    void deleteComment(Long userId, Long comId);
}
