package hr.mbehin.socialMediaProject.auth;

import hr.mbehin.socialMediaProject.dto.GroupDTO;
import hr.mbehin.socialMediaProject.dto.UserDTO;
import hr.mbehin.socialMediaProject.model.Group;
import hr.mbehin.socialMediaProject.model.User;
import hr.mbehin.socialMediaProject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public String register(UserCredentials userCredentials){
        if(userService.getUserByUsername(userCredentials.getUsername()).isEmpty()){
            var user = new User(userCredentials.getUsername(), passwordEncoder.encode(userCredentials.getPassword()));
            userService.createUser(user);
            var token = jwtService.generateToken(user);
            return token;
        }
        return null;
    }

    public String login(UserCredentials userCredentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userCredentials.getUsername(), userCredentials.getPassword()));
        var user = userService.getUserByUsername(userCredentials.getUsername()).orElseThrow();
        var token = jwtService.generateToken(user);
        return token;
    }

    public UserDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return mapUserToDTO((User) authentication.getPrincipal());
        }

        return null;
    }

    public UserDTO mapUserToDTO(User user){
        Set<GroupDTO> groupSet = new HashSet<>();
        for(Group group : user.getGroups())
            groupSet.add(new GroupDTO(group.getId(), group.getName(), group.getDescription()));
        return new UserDTO(user.getId(), user.getUsername(), user.getDateCreated(),groupSet);
    }
}
