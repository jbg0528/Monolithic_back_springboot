package surfy.comfy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAllByMember_Id(Long memberId);
}
