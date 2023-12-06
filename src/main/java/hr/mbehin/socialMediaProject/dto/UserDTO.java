package hr.mbehin.socialMediaProject.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private Instant dateCreated;
    @JsonIgnore
    private Set<GroupDTO> groups;

    public UserDTO(Long id, String username, Instant dateCreated) {
        this.id = id;
        this.username = username;
        this.dateCreated = dateCreated;
    }
}
