package surfy.comfy.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="answer_option")
public class Option {

    @Id @GeneratedValue
    @Column(name="option_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name="survey_id")
    private Survey survey;

    private String contents;
}
