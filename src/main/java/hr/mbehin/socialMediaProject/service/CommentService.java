package hr.mbehin.socialMediaProject.service;

import hr.mbehin.socialMediaProject.dto.CommentDTO;
import hr.mbehin.socialMediaProject.dto.UserDTO;
import hr.mbehin.socialMediaProject.model.Comment;
import hr.mbehin.socialMediaProject.model.Post;
import hr.mbehin.socialMediaProject.model.User;
import hr.mbehin.socialMediaProject.repository.CommentRepository;
import hr.mbehin.socialMediaProject.repository.PostRepository;
import hr.mbehin.socialMediaProject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    public List<CommentDTO> getAll(Long id){
        return commentRepository.getAllByUser_Id(id).stream()
                .map((comment) -> {
                    User user = comment.getUser();
                    UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getDateCreated());
                    return new CommentDTO(comment.getId(), comment.getText(), userDTO);
                })
                .collect(Collectors.toList());
    }
    public Comment createComment(Long postId, CommentDTO commentDTO){
        Optional<Post> postOptional = postRepository.findById(postId);

        Post post = postOptional.orElseThrow(
                () -> new IllegalArgumentException("Post with id " + postId + "has not been found."));

        User user = userRepository.findByUsername(commentDTO.getUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username was not found."));

        Comment comment = mapDTOtoComment(commentDTO, post, user);

        commentRepository.save(comment);

        return comment;
    }

    public void deleteComment(Long id){
        commentRepository.deleteCommentById(id);
    }
    public Comment getCommentById(Long id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);

        return commentOptional.orElseThrow(
                () -> new IllegalArgumentException("No comment with id " + id + " has been found."));
    }
    public Comment mapDTOtoComment(CommentDTO commentDTO, Post post, User user){
        return new Comment(commentDTO.getText(), user ,post);
    }

}
