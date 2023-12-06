package hr.mbehin.socialMediaProject.repository;

import hr.mbehin.socialMediaProject.model.Post;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByGroup_Id(Long id);

    List<Post> findAllByDateCreatedAfterAndAndGroup_Id(Instant day, Long id);

    Optional<Post> findByIdAndAndGroup_Name(Long id, String groupName);

    List<Post> findByUser_IdOrderByDateCreatedDesc(Long id);

    List<Post> findTop10ByGroup_IdOrderByDateCreatedDesc(Long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Post p WHERE p.id = :id")
    void deletePostById(@Param("id") Long id);
}
