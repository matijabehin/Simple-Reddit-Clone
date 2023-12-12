package hr.mbehin.socialMediaProject.controller;

import hr.mbehin.socialMediaProject.dto.GroupDTO;
import hr.mbehin.socialMediaProject.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    // get mapping

    @GetMapping("/getAll")
    public ResponseEntity<List<GroupDTO>> getAllGroups(){
        return ResponseEntity.status(HttpStatus.OK).body(groupService.getAllGroups());
    }

    @GetMapping("/getFollowedGroups")
    public ResponseEntity<Set<GroupDTO>> getFollowedGroups(){
        return ResponseEntity.status(HttpStatus.OK).body(groupService.getFollowedGroups());
    }

    @GetMapping("/getGroupByName/{groupName}")
    public ResponseEntity<GroupDTO> getGroupByName(@PathVariable String groupName){
        return ResponseEntity.status(HttpStatus.OK).body(groupService.getGroupDTOByName(groupName));
    }

    // post mapping

    @PostMapping("/createGroup")
    public ResponseEntity<Void> createGroup(@RequestBody GroupDTO groupDTO){
        groupService.createGroup(groupDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/followGroup")
    public ResponseEntity<Void> followGroup(@RequestBody GroupDTO groupDTO){
        groupService.followGroup(groupDTO.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/unfollowGroup")
    public ResponseEntity<Void> unfollowGroup(@RequestBody GroupDTO groupDTO){
        groupService.unfollowGroup(groupDTO.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
