package surfy.comfy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.Bookmark;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
    List<Bookmark> findAllByMember_Id(Long memberId);
}
