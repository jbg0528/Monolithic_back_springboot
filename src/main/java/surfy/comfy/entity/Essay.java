package surfy.comfy.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="essay")
public class Essay {

    @Id @GeneratedValue
    @Column(name="essay_id")
    private Long id;

    private String contents;

    @OneToOne
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="question_id")
    private Question question;

}
