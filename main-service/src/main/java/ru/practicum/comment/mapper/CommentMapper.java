package ru.practicum.comment.mapper;

import org.mapstruct.Mapper;
import ru.practicum.comment.CommentDto;
import ru.practicum.comment.NewCommentDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.events.mapper.EventsMapper;
import ru.practicum.user.mapper.UserMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EventsMapper.class, UserMapper.class})
public interface CommentMapper {

    Comment toComment(NewCommentDto commentDto);

    CommentDto toDtoResponse(Comment comment);

    List<CommentDto> toDtoList(List<Comment> comments);
}
