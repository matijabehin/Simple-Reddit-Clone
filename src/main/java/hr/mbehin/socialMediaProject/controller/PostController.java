package hr.mbehin.socialMediaProject.controller;

import hr.mbehin.socialMediaProject.dto.PostDTO;
import hr.mbehin.socialMediaProject.model.Post;
import hr.mbehin.socialMediaProject.service.GroupService;
import hr.mbehin.socialMediaProject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // get mapping

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostById(id));
    }

    @GetMapping("/getUserPosts/{id}")
    public ResponseEntity<List<PostDTO>> getUserPosts(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getUserPosts(id));
    }

    @GetMapping ("/getHomeFeed")
    public ResponseEntity<List<PostDTO>> getHomeFeed(){
        return ResponseEntity.status(HttpStatus.OK).body(postService.getHomeFeed());
    }

    @GetMapping("/getGroupPosts/{groupName}")
    public ResponseEntity<List<PostDTO>> getPostsByGroup(@PathVariable String groupName){
        List<PostDTO> list = postService.getPostsByGroupName(groupName);
        list.sort(Comparator.comparing(PostDTO::getDateCreated).reversed());
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @GetMapping("/getGroupPosts/top/{groupName}")
    public ResponseEntity<List<PostDTO>> showTopPosts(@PathVariable String groupName,
                                   @RequestParam(value = "t", defaultValue = "day") String param){
        return ResponseEntity.status(HttpStatus.OK).body(postService.showTopPosts(groupName, param));
    }

    // post mapping

    @PostMapping("/create")
    public ResponseEntity<Void> createPost(@RequestBody PostDTO postDTO){
        postService.createPost(postDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // delete mapping

    @DeleteMapping("/delete/{id}")
    public void deletePostById(@PathVariable("id") Long id){
        postService.deletePostById(id);
    }


}
