package hr.mbehin.socialMediaProject.service;

import hr.mbehin.socialMediaProject.auth.AuthService;
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

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    public List<GroupDTO> getAllGroups(){
        return groupRepository.findAll().stream()
                .map(this::mapGroupToDTO)
                .collect(Collectors.toList());
    }

    public List<Post> showPosts(String groupName){
        Optional<Group> groupOptional = groupRepository.findByName(groupName);
        Group group = groupOptional.orElseThrow(() -> new IllegalArgumentException("Group not found: " + groupName));

        return postRepository.findAllByGroup_Id(group.getId());
    }

    public Post createPost(String groupName, PostDTO postDTO) {
        Optional<Group> groupOptional = groupRepository.findByName(groupName);
        Group group = groupOptional.orElseThrow(() -> new IllegalArgumentException("Group not found: " + groupName));

        User user = userRepository.findByUsername(postDTO.getUser().getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User was not found."));

        Post post = new Post(postDTO.getTitle(), postDTO.getText(), group, user, postDTO.getUpvotes());

        postRepository.save(post);

        return post;
    }
    public void followGroup(Long groupId){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("No group has been found."));

        User user = userRepository.findById(authService.getCurrentUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("No user has been found."));

        user.getGroups().add(group);

        userRepository.save(user);
    }

    public void unfollowGroup(Long groupId){
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("No group has been found."));

        User user = userRepository.findById(authService.getCurrentUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("No user has been found."));

        user.getGroups().remove(group);

        userRepository.save(user);
    }
    public void createGroup(GroupDTO groupDTO) {
        if (groupRepository.findByName(groupDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Group with name " + groupDTO.getName() +
                    " already exists");
        }
        Group group = new Group(groupDTO.getName(), groupDTO.getDescription());
        groupRepository.save(group);
    }

    public Group getGroupByName(String groupName) {
        Optional<Group> groupOptional = groupRepository.findByName(groupName);
        return groupOptional
                .orElseThrow(() -> new IllegalArgumentException("No group has been found with name " + groupName));
    }
    public GroupDTO getGroupDTOByName(String groupName) {
        Optional<Group> groupOptional = groupRepository.findByName(groupName);
        return groupOptional
                .map((group) -> new GroupDTO(group.getId(), group.getName(), group.getDescription()))
                .orElseThrow(() -> new IllegalArgumentException("No group has been found with name " + groupName));
    }

    public Post showPostByPostIdAndGroupName(Long id, String groupName) {
        Optional<Post> postOptional = postRepository.findByIdAndAndGroup_Name(id, groupName);
        return postOptional.orElseThrow(() ->
                new IllegalArgumentException("No post has been found with id " + id + " in /g/" + groupName));
    }

    public Set<GroupDTO> getFollowedGroups(){
        Long id = authService.getCurrentUser().getId();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("No user has been found."));
        return user.getGroups().stream()
                .map(this::mapGroupToDTO)
                .collect(Collectors.toSet());
    }

    public GroupDTO mapGroupToDTO(Group group){
        return new GroupDTO(group.getId(), group.getName(), group.getDescription());
    }
}
