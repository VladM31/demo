package com.example.demo.dao.idao;

import com.example.demo.entity.TravelAgency;
import org.springframework.lang.NonNull;

import java.util.List;

public interface IDAOTravelAgency<TA extends TravelAgency> extends IDAOUser<TA>{


     public  List<TA> findByTravelAgencyIdIn(@NonNull Iterable<Long> ids);
     public  List<TA> findByTravelAgencyObjectIdIn(@NonNull Iterable<TA> ids);
     public  TA findByTravelAgencyId(@NonNull Long id);

     public List<TA> findByRating(float rating);
     public List<TA> findByRatingLessThan(float rating);
     public List<TA> findByRatingLessThanEqual(float rating);
     public List<TA> findByRatingGreaterThan(float rating);
     public List<TA> findByRatingGreaterThanEqual(float rating);

     public List<TA> findByRatingGreaterThanAndLessThan(float ratingF,float ratingE);
     public List<TA> findByRatingGreaterThanEqualAndLessThan(float ratingF,float ratingE);
     public List<TA> findByRatingGreaterThanAndLessThanEqual(float ratingF,float ratingE);
     public List<TA> findByRatingGreaterThanEqualAndLessThanEqual(float ratingF,float ratingE);

     public TA findByKVED(long kved);
     public List<TA> findByKVEDIn(@NonNull Iterable<Long> kveds);

     public TA findByEGRPOY(Long egrpoy);
     public List<TA> findByEGRPOYIn(@NonNull Iterable<Long> egrpoys);
     public List<TA> findByEGRPOYIsNull();
     public List<TA> findByEGRPOYIsNotNull();

     public TA findByRNEKPN(Long rnekpn);
     public List<TA> findByRNEKPNIn(@NonNull Iterable<Long> rnekpns);
     public List<TA> findByRNEKPNIsNull();
     public List<TA> findByRNEKPNIsNotNull();

     public TA findByAddress(String address);
     public List<TA> findByAddressLike(String script);

     public List<TA> findByAllNameDirector(String allNameDirector);
     public List<TA> findByAllNameDirectorLike(String script);

     public List<TA> findByCanUse(boolean canU);
     public TA findByNameTravelAgency(String nameTravel);
     public List<TA> findByNameTravelAgencyLike(String nameTravel);
     public List<TA> findByNameTravelAgencyContaining(String nameTravel);
     public List<TA> findByNameTravelAgencyStartingWith(String nameTravel);
     public List<TA> findByNameTravelAgencyEndingWith(String nameTravel);
}
