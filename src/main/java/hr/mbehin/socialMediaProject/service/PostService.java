package hr.mbehin.socialMediaProject.service;

import hr.mbehin.socialMediaProject.auth.AuthService;
import hr.mbehin.socialMediaProject.dto.CommentDTO;
import hr.mbehin.socialMediaProject.dto.GroupDTO;
import hr.mbehin.socialMediaProject.dto.PostDTO;
import hr.mbehin.socialMediaProject.dto.UserDTO;
import hr.mbehin.socialMediaProject.exception.GroupNotFoundException;
import hr.mbehin.socialMediaProject.exception.PostNotFoundException;
import hr.mbehin.socialMediaProject.exception.UserNotFoundException;
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
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public void deletePostById(Long id){postRepository.deletePostById(id);}

    public List<PostDTO> showTopPosts(String groupName, String param) {
        Group group = groupRepository.findByName(groupName)
                .orElseThrow(() -> new GroupNotFoundException("Group with name " + groupName + " has not been found."));

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

        return posts.stream().map(this::mapPostToDTO).collect(Collectors.toList());
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

    public List<PostDTO> getPostsByGroupName(String groupName){
        Group group = groupRepository.findByName(groupName)
                .orElseThrow(() -> new GroupNotFoundException("Group with name " + groupName + " has not been found."));

        return postRepository.findAllByGroup_Id(group.getId()).stream()
                    .map(this::mapPostToDTO)
                    .collect(Collectors.toList());
    }

    public PostDTO getPostById(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(()->new PostNotFoundException("Post with id " + id + " has not been found."));
        return mapPostToDTO(post);
    }

    public void createPost(PostDTO postDTO) {
        postRepository.save(mapDTOtoPost(postDTO));
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

    Post mapDTOtoPost(PostDTO postDTO){

        Long groupId = postDTO.getGroup().getId();
        Long userId = postDTO.getUser().getId();

        return new Post(postDTO.getTitle(), postDTO.getText(),
                groupRepository.findById(groupId)
                        .orElseThrow(() -> new GroupNotFoundException("Group with id " + postDTO.getGroup().getId() + " has not been found.")),
                userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException("User with id " + postDTO.getUser().getId() + " has not been found.")),
                postDTO.getUpvotes());
    }

}
