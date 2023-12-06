package hr.mbehin.socialMediaProject.controller;

import hr.mbehin.socialMediaProject.auth.AuthService;
import hr.mbehin.socialMediaProject.dto.CommentDTO;
import hr.mbehin.socialMediaProject.dto.GroupDTO;
import hr.mbehin.socialMediaProject.dto.PostDTO;
import hr.mbehin.socialMediaProject.dto.UserDTO;
import hr.mbehin.socialMediaProject.model.Comment;
import hr.mbehin.socialMediaProject.model.Post;
import hr.mbehin.socialMediaProject.model.User;
import hr.mbehin.socialMediaProject.service.CommentService;
import hr.mbehin.socialMediaProject.service.GroupService;
import hr.mbehin.socialMediaProject.service.PostService;
import hr.mbehin.socialMediaProject.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
//@CrossOrigin(origins = "http://localhost:4200")
public class APIController {
    private final GroupService groupService;
    private final CommentService commentService;
    private final PostService postService;
    private final UserService userService;

    // group-api
    @GetMapping("/groups/getAll")
    public List<GroupDTO> getAllGroups(){
        return groupService.getAllGroups();
    }
    @GetMapping("/groups/getFollowedGroups")
    public Set<GroupDTO> getFollowedGroups(){
        return groupService.getFollowedGroups();
    }

    @GetMapping("/getGroupByName/{groupName}")
    public GroupDTO getGroupByName(@PathVariable String groupName){
        return groupService.getGroupDTOByName(groupName);
    }

    @PostMapping("/createGroup")
    public ResponseEntity<Void> createGroup(@RequestBody GroupDTO groupDTO){
        groupService.createGroup(groupDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("groups/followGroup")
    public void followGroup(@RequestBody GroupDTO groupDTO){
        groupService.followGroup(groupDTO.getId());
    }

    @PostMapping("groups/unfollowGroup")
    public void unfollowGroup(@RequestBody GroupDTO groupDTO){
        groupService.unfollowGroup(groupDTO.getId());
    }
    // comment-api
    @GetMapping("/comment/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id){
        return new ResponseEntity<>(commentService.getCommentById(id), HttpStatus.OK);
    }
    @GetMapping("/comment/getUserComments/{id}")
    public List<CommentDTO> getUserComments(@PathVariable Long id){
        return commentService.getAll(id);
    }
    @DeleteMapping("/deleteComment/{id}")
    public void deleteComment(@PathVariable Long id){
        commentService.deleteComment(id);
    }

    // post-api
    @GetMapping("/post/getUserPosts/{id}")
    public List<PostDTO> getUserPosts(@PathVariable Long id){
        return postService.getUserPosts(id);
    }

    @GetMapping ("/post/getHomeFeed")
    public List<PostDTO> getHomeFeed(){
        return postService.getHomeFeed();
    }

    // user-api
    @GetMapping("/user/getUserByName/{name}")
    public UserDTO getUser(@PathVariable String name){
        return userService.getUserDTOByUsername(name);
    }
}
