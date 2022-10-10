package surfy.comfy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import surfy.comfy.entity.Survey;

import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey,Long> {
    Optional<Survey> findById(Long surveyId);
    Survey findSurveysById(Long surveyId);
}
