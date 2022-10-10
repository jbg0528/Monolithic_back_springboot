package surfy.comfy.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import surfy.comfy.entity.Bookmark;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
    List<Bookmark> findAllByMember_Id(Long memberId);
    Bookmark findByMember_IdAndPost_Id(Long memberId,Long postId);
    List<Bookmark> findAllByPost_Id(Long postId);

}
