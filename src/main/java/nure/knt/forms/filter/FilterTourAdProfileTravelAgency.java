package nure.knt.forms.filter;

import nure.knt.database.idao.goods.IDAOTourAd;
import nure.knt.entity.goods.TourAd;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FilterTourAdProfileTravelAgency extends FilterTourAdCore {
    private Long tourAdId;
    private Integer startCountOrders,endCountOrders;

    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateRegistration, endDateRegistration;

    public List<TourAd> filtering(Supplier<String> script, IDAOTourAd<TourAd> dao){

        if(tourAdId != null){
            return dao.setOrderQuantity(List.of(dao.findByTourAdId(this.tourAdId,script)));
        }

        List<Predicate<TourAd>> filterList = new ArrayList<>();

        List<TourAd> list = super.filteringCore(script,dao,filterList);

        if(list != HandlerFilter.LIST_IS_NOT_CREATED_FROM_DATABASE){
            dao.setOrderQuantity(list);
        }

        list = HandlerFilter.checkNumberBetween(this.startCountOrders,this.endCountOrders,Integer.MIN_VALUE,Integer.MAX_VALUE,list,
                (startCount,endCount) -> dao.findByOrderQuantityBetween(startCount,endCount,script),
                (startCount,endCount) -> filterList.add(tourAd -> tourAd.getOrderQuantity() >=startCount &&   tourAd.getOrderQuantity() <= endCount));


        list = HandlerFilter.checkDateTime(this.getStartDateRegistration(),this.getEndDateRegistration(),
                HandlerFilter.MIN_LOCAL_DATE_TIME, HandlerFilter.MAX_LOCAL_DATE_TIME,list,
                (start,end) -> dao.findByDateRegistrationBetween(startDateRegistration, endDateRegistration, script),
                (start,end) -> filterList.add((order -> order.getDateRegistration().isAfter(start) && order.getDateRegistration().isBefore(end))));


        if(list == HandlerFilter.LIST_IS_NOT_CREATED_FROM_DATABASE){
            return dao.findAll(script);
        }


        return HandlerFilter.endFiltering(list,filterList);
    }

    public Long getTourAdId() {
        return tourAdId;
    }

    public void setTourAdId(Long tourAdId) {
        this.tourAdId = tourAdId;
    }

    public Integer getStartCountOrders() {
        return startCountOrders;
    }

    public void setStartCountOrders(Integer startCountOrders) {
        this.startCountOrders = startCountOrders;
    }

    public Integer getEndCountOrders() {
        return endCountOrders;
    }

    public void setEndCountOrders(Integer endCountOrders) {
        this.endCountOrders = endCountOrders;
    }

    public LocalDateTime getStartDateRegistration() {
        return startDateRegistration;
    }

    public void setStartDateRegistration(LocalDateTime startDateRegistration) {
        this.startDateRegistration = startDateRegistration;
    }

    public LocalDateTime getEndDateRegistration() {
        return endDateRegistration;
    }

    public void setEndDateRegistration(LocalDateTime endDateRegistration) {
        this.endDateRegistration = endDateRegistration;
    }
}
