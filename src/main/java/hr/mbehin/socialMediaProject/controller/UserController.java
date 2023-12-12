package hr.mbehin.socialMediaProject.controller;

import hr.mbehin.socialMediaProject.dto.UserDTO;
import hr.mbehin.socialMediaProject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // get mapping

    @GetMapping("/getUserByName/{name}")
    public UserDTO getUser(@PathVariable String name){
        return userService.getUserDTOByUsername(name);
    }
}
