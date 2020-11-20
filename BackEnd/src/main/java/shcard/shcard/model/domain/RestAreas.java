package shcard.shcard.model.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "rest_area")
@Getter
public class RestAreas implements Comparable<RestAreas>{
    @Id
    @GeneratedValue
    @Column(name = "restarea_no")
    private int no;
    private String name;
    private String roadKind;
    private String roadNo;
    private String roadName;
    private String roadDir;
    private Double lat;
    private Double lng;
    private String areaKind;
    private String areaOpen;
    private String areaClose;
    private Integer areaSize;
    @Column(nullable = true)
    private Integer parkingCnt;
    private String fixAvail;
    private String gasStatus;
    private String lpgStatus;
    private String elecStatus;
    private String busTrans;
    private String shelterStatus;
    private String toiletStatus;
    private String phraseStatus;
    private String nursingStatus;
    private String storeStatus;
    private String foodStatus;
    private String etcStore;
    private String repFood;
    private String areaNumber;
    private String dataDate;
    private String providerCode;
    private String provider;

    @OneToOne
    @JoinColumn(name = "card_no")
    private CardCompany cardCompany;

    @Override
    public int compareTo(RestAreas o) {
        if(this.getCardCompany().getShanCard() < o.getCardCompany().getShanCard()){
            return 1;
        }else if(this.getCardCompany().getShanCard() > o.getCardCompany().getShanCard()){
            return -1;
        }else
            return 0;
    }
}
