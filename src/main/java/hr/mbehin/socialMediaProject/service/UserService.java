package hr.mbehin.socialMediaProject.service;

import hr.mbehin.socialMediaProject.dto.GroupDTO;
import hr.mbehin.socialMediaProject.dto.UserDTO;
import hr.mbehin.socialMediaProject.model.Group;
import hr.mbehin.socialMediaProject.model.Post;
import hr.mbehin.socialMediaProject.model.User;
import hr.mbehin.socialMediaProject.repository.PostRepository;
import hr.mbehin.socialMediaProject.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    public void createUser(User user){
        userRepository.save(user);
    }
    public Optional<User> getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public UserDTO getUserDTOByUsername(String username){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("Username has not been found."));
        return mapUserToDTO(user);
    }

    public UserDTO mapUserToDTO(User user){
        Set<GroupDTO> groupSet = new HashSet<>();
        for(Group group : user.getGroups())
            groupSet.add(new GroupDTO(group.getId(), group.getName(), group.getDescription()));
        return new UserDTO(user.getId(), user.getUsername(), user.getDateCreated(), groupSet);
    }
}
