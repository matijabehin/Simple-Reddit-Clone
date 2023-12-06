package hr.mbehin.socialMediaProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    private Long id;
    private String title;
    private String text;
    private Instant dateCreated;
    private Long upvotes;
    private List<CommentDTO> comments;
    private GroupDTO group;
    private UserDTO user;
}
