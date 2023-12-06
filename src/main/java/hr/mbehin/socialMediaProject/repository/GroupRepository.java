package hr.mbehin.socialMediaProject.repository;

import hr.mbehin.socialMediaProject.model.Group;
import hr.mbehin.socialMediaProject.model.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByName(String groupName);
}
