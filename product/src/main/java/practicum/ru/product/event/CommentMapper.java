package practicum.ru.product.event;

import org.springframework.stereotype.Component;
import practicum.ru.product.dto.CommentDto;
import practicum.ru.product.dto.NewCommentDto;
import practicum.ru.product.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class CommentMapper {

    public Comment toComment(NewCommentDto newCommentDto, User user, Event event) {
        Comment comment = new Comment();
        comment.setText(newCommentDto.getText());
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

    public CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setAuthorId(comment.getAuthor().getId());
        commentDto.setText(comment.getText());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }

    public List<CommentDto> toListCommentDto(List<Comment> comments) {
        List<CommentDto> result = new ArrayList<>();
        if (!comments.isEmpty()) {
            for (Comment c : comments) {
                result.add(toCommentDto(c));
            }
        }
        return result;
    }
}
