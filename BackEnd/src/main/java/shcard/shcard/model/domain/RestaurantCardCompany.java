package shcard.shcard.model.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "restaurant_card")
@Getter
@Setter
public class RestaurantCardCompany {
    @Id
    @GeneratedValue
    @Column(name = "card_no")
    private Long no;
    private String restaurantName;
    private Long shanCard;

}
