package surfy.comfy.entity;

import lombok.Getter;
import lombok.Setter;
import surfy.comfy.type.QuestionType;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="question")
public class Question {

    @Id @GeneratedValue
    @Column(name="question_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @ManyToOne
    @JoinColumn(name="survey_id")
    private Survey survey;

    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    private List<Option> options;

    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    private List<Grid> grids;

    @OneToMany(mappedBy="question",cascade = CascadeType.ALL)
    private List<Answer> answers;

    @OneToMany(mappedBy = "question",cascade = CascadeType.ALL)
    private List<Essay> essays;

    @OneToOne(mappedBy = "question")
    private Linear linear;

    private String contents;
}
