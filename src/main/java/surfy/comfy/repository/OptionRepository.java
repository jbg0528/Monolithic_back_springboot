package surfy.comfy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.Option;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findAllByQuestion_Id(Long QuestionId);
}
