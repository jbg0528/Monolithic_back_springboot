package surfy.comfy.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="linear_option")
public class Linear {

    @Id @GeneratedValue
    @Column(name="linear_id")
    private Long id;

    private Long size;

    private String start;
    private String end;

    @OneToOne
    @JoinColumn(name="question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name="survey_id")
    private Survey survey;
}
