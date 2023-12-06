package hr.mbehin.socialMediaProject.controller;

import hr.mbehin.socialMediaProject.dto.CommentDTO;
import hr.mbehin.socialMediaProject.dto.PostDTO;
import hr.mbehin.socialMediaProject.model.Comment;
import hr.mbehin.socialMediaProject.model.Post;
import hr.mbehin.socialMediaProject.service.CommentService;
import hr.mbehin.socialMediaProject.service.GroupService;
import hr.mbehin.socialMediaProject.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/g")
@AllArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200")
public class GroupController {

    private final GroupService groupService;
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/{groupName}")
    public List<Post> showPosts(@PathVariable String groupName){
        List<Post> list = groupService.showPosts(groupName);
        list.sort(Comparator.comparing(Post::getDateCreated).reversed());
        return list;
    }

    @GetMapping("/{groupName}/top/")
    public List<Post> showTopPosts(@PathVariable String groupName,
                                  @RequestParam(value = "t", defaultValue = "day") String param){
        return postService.showTopPosts(groupService.getGroupByName(groupName), param);
    }
    @GetMapping("/{groupName}/post/{id}")
    public Post showPost(@PathVariable Long id, @PathVariable String groupName){
        Post post = groupService.showPostByPostIdAndGroupName(id, groupName);
        return post;
    }

    @PostMapping("/{groupName}/post/{id}")
    public ResponseEntity<Comment> addComment(@PathVariable("id") Long postId, @RequestBody CommentDTO commentDTO){
        return new ResponseEntity<>(commentService.createComment(postId, commentDTO), HttpStatus.CREATED);
    }

    @PostMapping("/{groupName}/createPost")
    public ResponseEntity<Post> createPost(@PathVariable String groupName, @RequestBody PostDTO postDTO){
        Post post = groupService.createPost(groupName, postDTO);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @DeleteMapping("/{groupName}/deletePost/{id}")
    public void deletePostById(@PathVariable("id") Long id){
        postService.deletePostById(id);
    }

}
