package com.example.demo.entity.important;

import java.time.LocalDateTime;
import java.util.Objects;

public class TravelAgency extends User{
    private Long travelId;
    private float rating;
    private long kved;
    private Long egrpoy;
    private Long rnekpn;
    private String address;
    private String allNameDirector;
    private String describeAgency;
    private boolean can_use;

    public boolean isCan_use() {
        return can_use;
    }

    public void setCan_use(boolean can_use) {
        this.can_use = can_use;
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

    public long getKved() {
        return kved;
    }

    public void setKved(long kved) {
        this.kved = kved;
    }

    public Long getEgrpoy() {
        return egrpoy;
    }

    public void setEgrpoy(Long egrpoy) {
        this.egrpoy = egrpoy;
    }

    public Long getRnekpn() {
        return rnekpn;
    }

    public void setRnekpn(Long rnekpn) {
        this.rnekpn = rnekpn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAllNameDirector() {
        return allNameDirector;
    }

    public void setAllNameDirector(String allNameDirector) {
        this.allNameDirector = allNameDirector;
    }

    public String getDescribeAgency() {
        return describeAgency;
    }

    public void setDescribeAgency(String describeAgency) {
        this.describeAgency = describeAgency;
    }

    public TravelAgency(Long id, long number, String email, String username, String password, boolean active, LocalDateTime dateRegistration,
                        Role role, String country, String name, Long travelId, float rating, long kved, Long egrpoy, Long rnekpn,
                        String address, String allNameDirector, String describeAgency, boolean can_use) {
        super(id, number, email, username, password, active, dateRegistration, role, country, name);
        this.travelId = travelId;
        this.rating = rating;
        this.kved = kved;
        this.egrpoy = egrpoy;
        this.rnekpn = rnekpn;
        this.address = address;
        this.allNameDirector = allNameDirector;
        this.describeAgency = describeAgency;
        this.can_use = can_use;
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
}