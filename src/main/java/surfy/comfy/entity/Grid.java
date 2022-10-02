package surfy.comfy.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="grid")
// 소주제
public class Grid {

    @Id @GeneratedValue
    @Column(name="grid_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name="survey_id")
    private Survey survey;

    private String contents;
}
