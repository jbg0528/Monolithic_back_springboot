package surfy.comfy.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="answer")
public class Answer {

    @Id @GeneratedValue
    @Column(name="answer_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name="survey_id")
    private Survey survey;

    @OneToOne
    @JoinColumn(name="option_id")
    private Option option;

    @OneToOne
    @JoinColumn(name="essay_id")
    private Essay essay;

    @OneToOne
    @JoinColumn(name="grid_id")
    private Grid grid;

    private String filePath;

    //TODO: 선형 배율 어떻게 저장?
    private Long linear_answer;
}
