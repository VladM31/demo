package nure.knt.entity.important;

import nure.knt.entity.enums.Role;
import nure.knt.entity.enums.TypeState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TravelAgency extends User{
    private Long travelId;
    private float rating;
    private String kved;
    private Long egrpoyOrRnekpn;
    private boolean isEgrpoy;
    private String address;
    private String fullNameDirector;
    private String describeAgency;
    private String urlPhoto;

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public Long getTravelId() {
        return travelId;
    }

    public void setTravelId(Long travelId) {
        this.travelId = travelId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getKved() {
        return kved;
    }

    public void setKved(String kved) {
        this.kved = kved;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFullNameDirector() {
        return fullNameDirector;
    }

    public void setFullNameDirector(String fullNameDirector) {
        this.fullNameDirector = fullNameDirector;
    }

    public String getDescribeAgency() {
        return describeAgency;
    }

    public void setDescribeAgency(String describeAgency) {
        this.describeAgency = describeAgency;
    }

    public Long getEgrpoyOrRnekpn() {
        return egrpoyOrRnekpn;
    }

    public void setEgrpoyOrRnekpn(Long egrpoyOrRnekpn) {
        this.egrpoyOrRnekpn = egrpoyOrRnekpn;
    }

    public boolean isEgrpoy() {
        return isEgrpoy;
    }

    public void setEgrpoy(boolean egrpoy) {
        isEgrpoy = egrpoy;
    }

    public TravelAgency(Long id, String number, String email, String username, String password, boolean active,
                        LocalDateTime dateRegistration, Role role, String country, String name, TypeState typeState,
                        Long travelId, float rating, String kved, Long egrpoyOrRnekpn, boolean isEgrpoy, String address,
                        String fullNameDirector, String describeAgency, String urlPhoto) {
        super(id, number, email, username, password, active, dateRegistration, role, country, name, typeState);
        this.travelId = travelId;
        this.rating = rating;
        this.kved = kved;
        this.egrpoyOrRnekpn = egrpoyOrRnekpn;
        this.isEgrpoy = isEgrpoy;
        this.address = address;
        this.fullNameDirector = fullNameDirector;
        this.describeAgency = describeAgency;
        this.urlPhoto = urlPhoto;
    }

    public TravelAgency(){
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof TravelAgency)) return false;
        if (!super.equals(o)) return false;

        TravelAgency that = (TravelAgency) o;

        return Objects.equals(this.travelId,that.travelId);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (travelId != null ? travelId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "TravelAgency{" +
                "travelId=" + travelId +
                ", rating=" + rating +
                ", kved='" + kved + '\'' +
                ", egrpoyOrRnekpn=" + egrpoyOrRnekpn +
                ", isEgrpoy=" + isEgrpoy +
                ", address='" + address + '\'' +
                ", fullNameDirector='" + fullNameDirector + '\'' +
                ", describeAgency='" + describeAgency + '\'' +
                ", urlPhoto='" + urlPhoto + '\'' +
                "} " + super.toString();
    }

    private static final String FULL_STAR = "&starf;";
    private static final String EMPTY_STAR = "&star;";
    private static final float MIN_RATING = 0.76f;
    private static final int  NUMBER_OF_STARS = 5;

    public List<String> getStars(){
        return TravelAgency.getRetingStars(this.rating);
    }


    public static List<String> getRetingStars(float rating){
        ArrayList<String> list = new ArrayList<>(NUMBER_OF_STARS);
        for (int i = 0; i < NUMBER_OF_STARS; i++) {
            if(rating-- > MIN_RATING){
                list.add(FULL_STAR);
            }else{
                for (int j = i; j < NUMBER_OF_STARS; j++) {
                    list.add(EMPTY_STAR);
                }
                break;
            }
        }
        return list;
    }
}
