package surfy.comfy.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Token {

    @Id @GeneratedValue
    private Long id;

    //private String accessToken;
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;
}
