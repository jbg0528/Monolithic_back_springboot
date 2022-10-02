package surfy.comfy.entity;

import lombok.Getter;
import lombok.Setter;
import surfy.comfy.type.SurveyType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@Table(name="survey")
public class Survey {

    @Id @GeneratedValue
    @Column(name="survey_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member; // 설문 제작자

    @OneToMany(mappedBy="survey",cascade = CascadeType.ALL)
    private List<Question> questions;

    @OneToMany(mappedBy = "survey",cascade = CascadeType.ALL)
    private List<Satisfaction> satisfactions;

    private LocalDateTime start;
    private LocalDateTime end;

    private String title;
    private String contents;

    @Enumerated(EnumType.STRING)
    private SurveyType status;

}
