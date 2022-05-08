package com.example.demo.database.idao.entity;

import com.example.demo.entity.important.TravelAgency;
import org.springframework.lang.NonNull;

import java.util.List;

public interface IDAOTravelAgency<TA extends TravelAgency> extends IDAOUser<TA>{

     public  List<TA> findByTravelAgencyIdIn(@NonNull Iterable<Long> ids);
     public  List<TA> findByTravelAgencyObjectIdIn(@NonNull Iterable<TA> ids);
     public  TA findByTravelAgencyId(@NonNull Long id);

     public List<TA> findByRating(float rating);
     public List<TA> findByRatingLessThanEqual(float rating);
     public List<TA> findByRatingGreaterThanEqual(float rating);

     public List<TA> findByRatingGreaterThanEqualAndLessThanEqual(float ratingF,float ratingE);

     public List<TA> findByKVED(long kved);
     public List<TA> findByKVEDIn(@NonNull Iterable<Long> kveds);

     public TA findByEGRPOY(Long egrpoy);
     public List<TA> findByEGRPOYIn(@NonNull Iterable<Long> egrpoys);
     public List<TA> findByEGRPOY(boolean isEGRPOY);

     public TA findByRNEKPN(Long rnekpn);
     public List<TA> findByRNEKPNIn(@NonNull Iterable<Long> rnekpns);

     public TA findByAddress(String address);
     public List<TA> findByAddressLike(String script);

     public List<TA> findByAllNameDirector(String allNameDirector);
     public List<TA> findByAllNameDirectorLike(String script);

}