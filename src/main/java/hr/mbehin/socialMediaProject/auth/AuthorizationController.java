package hr.mbehin.socialMediaProject.auth;

import hr.mbehin.socialMediaProject.dto.TokenDTO;
import hr.mbehin.socialMediaProject.dto.UserDTO;
import hr.mbehin.socialMediaProject.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<TokenDTO> register(@RequestBody UserCredentials userCredentials){
        String token = authService.register(userCredentials);
        return new ResponseEntity<>(new TokenDTO(token), HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody UserCredentials userCredentials){
        String token = authService.login(userCredentials);
        return new ResponseEntity<>(new TokenDTO(token), HttpStatus.OK);
    }

    @GetMapping("/user")
    public UserDTO getCurrentUser(){
        return authService.getCurrentUser();
    }
}
