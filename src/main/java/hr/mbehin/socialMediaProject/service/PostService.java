package hr.mbehin.socialMediaProject.service;

import hr.mbehin.socialMediaProject.auth.AuthService;
import hr.mbehin.socialMediaProject.dto.CommentDTO;
import hr.mbehin.socialMediaProject.dto.GroupDTO;
import hr.mbehin.socialMediaProject.dto.PostDTO;
import hr.mbehin.socialMediaProject.dto.UserDTO;
import hr.mbehin.socialMediaProject.model.Group;
import hr.mbehin.socialMediaProject.model.Post;
import hr.mbehin.socialMediaProject.model.User;
import hr.mbehin.socialMediaProject.repository.GroupRepository;
import hr.mbehin.socialMediaProject.repository.PostRepository;
import hr.mbehin.socialMediaProject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    public void save(Post post){
        postRepository.save(post);
    }

    public void deletePostById(Long id){postRepository.deletePostById(id);}

    public List<Post> showTopPosts(Group group, String param) {

        List<Post> posts;
        Instant date = Instant.now().truncatedTo(ChronoUnit.DAYS);

        switch (param){
            case "day": {
                posts = postRepository.findAllByDateCreatedAfterAndAndGroup_Id(date, group.getId());
                break;
            }
            case "week":{
                date = date.minus(Duration.ofDays(7));
                posts = postRepository.findAllByDateCreatedAfterAndAndGroup_Id(date,group.getId());
                break;
            }
            case "month":{
                date = date.minus(Duration.ofDays(30));
                posts = postRepository.findAllByDateCreatedAfterAndAndGroup_Id(date,group.getId());
                break;
            }
            case "year":{
                date = date.minus(Duration.ofDays(365));
                posts = postRepository.findAllByDateCreatedAfterAndAndGroup_Id(date,group.getId());
                break;
            }
            case "all":{
                posts = postRepository.findAllByGroup_Id(group.getId());
                break;
            }
            default:posts = postRepository.findAllByDateCreatedAfterAndAndGroup_Id(date,group.getId());;
        }

        return posts;
    }

    public List<PostDTO> getUserPosts(Long id){
        return postRepository.findByUser_IdOrderByDateCreatedDesc(id).stream().map(this::mapPostToDTO).collect(Collectors.toList());
    }

    public List<PostDTO> getHomeFeed(){
        Long id = authService.getCurrentUser().getId();
        List<Post> posts = new ArrayList<>();
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("Username has not been found."));

        for(Group group : user.getGroups()){
            posts.addAll(postRepository.findTop10ByGroup_IdOrderByDateCreatedDesc(group.getId()));
        }
        posts.sort(Comparator.comparing(Post::getDateCreated).reversed());

        return posts.stream().map(this::mapPostToDTO).collect(Collectors.toList());
    }

    PostDTO mapPostToDTO(Post post){

        Set<GroupDTO> groupDTOList = new HashSet<>();
        for(Group group : post.getUser().getGroups()){
            groupDTOList.add(new GroupDTO(group.getId(), group.getName(), group.getDescription()));
        }

        UserDTO userDTO = new UserDTO(post.getUser().getId(), post.getUser().getUsername(), post.getUser().getDateCreated(), groupDTOList);
        GroupDTO groupDTO = new GroupDTO(post.getGroup().getId(), post.getGroup().getName(), post.getGroup().getDescription());
        List<CommentDTO> commentDTO = post.getComments()
                .stream()
                .map(comment -> {
                    Set<GroupDTO> groupDTOs = new HashSet<>();
                    for(Group group : comment.getUser().getGroups())
                        groupDTOs.add(new GroupDTO(group.getId(), group.getName(), group.getDescription()));
                    UserDTO userDTO_comment = new UserDTO(comment.getUser().getId(), comment.getUser().getUsername(), comment.getUser().getDateCreated(), groupDTOs);
                    return new CommentDTO(comment.getId(), comment.getText(), userDTO_comment);
                })
                .collect(Collectors.toList());

        return new PostDTO(post.getId(), post.getTitle(), post.getText(), post.getDateCreated(), post.getUpvotes(), commentDTO, groupDTO, userDTO);
    }
}
