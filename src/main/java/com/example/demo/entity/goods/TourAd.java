package com.example.demo.entity.goods;

import com.example.demo.entity.enums.ConditionCommodity;
import com.example.demo.entity.enums.TypeState;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TourAd {
    private Long id;

    private int costOneCustomer;
    private int costService;
    private int discountSizePeople;

    private float discountPercentage;

    private boolean hidden;

    private LocalDate dateStart;
    private LocalDate dateEnd;
    private LocalDateTime dateRegistration;

    private String place;
    private String city;
    private String country;

    private TypeState typeState;
    private ConditionCommodity conditionCommodity;
}