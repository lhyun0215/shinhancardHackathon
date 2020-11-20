package shcard.shcard.model.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rest_card")
@Getter
@Setter
public class CardCompany {
    @Id
    @GeneratedValue
    @Column(name = "card_no")
    private Long no;
    private String restName;
    private Long shanCard;

}
