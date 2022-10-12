package surfy.comfy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.Grid;

import java.util.List;

public interface GridRepository extends JpaRepository<Grid, Long> {
    List<Grid> findAllByQuestion_Id(Long QuestionId);
    List<Grid> findAllBySurvey_Id(Long surveyId);
}
