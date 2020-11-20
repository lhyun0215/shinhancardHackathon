package shcard.shcard.model.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "restaurant_list")
@Getter
public class RestaurantLists {
    @Id
    @GeneratedValue
    @Column(name = "restaurant_no")
    private int no;
    private int plcId;
    private String plcNm;
    private int sksnCgrSclsCd;
    private String addr;
    private int hotplcAreaId;
    private String hotplcAreaNm;
    private int ctpvCd;
    private int ctggCd;
    private int emdCd;
    private Double lat;
    private Double lng;
    private int telNo;
    private String sglnIntdCn;
    private String intdCn;
    private String bzTimeCn;
    private String hvofInfoCn;
    private String menuInfoCn;
    private int inqCnt;
    private int likeCnt;
    private int curCnt;
    private int bkmkCont;

    @OneToOne
    @JoinColumn(name = "card_no")
    private RestaurantCardCompany restaurantCardCompany;
}
