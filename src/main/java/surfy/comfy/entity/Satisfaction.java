package surfy.comfy.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Satisfaction {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="survey_id")
    private Survey survey;

    private Integer percent;

}
