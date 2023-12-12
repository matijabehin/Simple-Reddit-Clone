package hr.mbehin.socialMediaProject.controller;

import hr.mbehin.socialMediaProject.dto.CommentDTO;
import hr.mbehin.socialMediaProject.model.Comment;
import hr.mbehin.socialMediaProject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // get mapping

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id){
        return new ResponseEntity<>(commentService.getCommentById(id), HttpStatus.OK);
    }

    @GetMapping("/getUserComments/{id}")
    public ResponseEntity<List<CommentDTO>> getUserComments(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAll(id));
    }

    // post mapping

    @PostMapping("/create/{postId}")
    public ResponseEntity<Void> addComment(@PathVariable Long postId, @RequestBody CommentDTO commentDTO){

        commentService.createComment(postId, commentDTO);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // delete mapping

    @DeleteMapping("/deleteComment/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
