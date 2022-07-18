package nure.knt.database.dao.mysql.terms;

import nure.knt.database.dao.HandlerSqlDAO;
import nure.knt.database.idao.terms.ITermInformation;
import nure.knt.database.idao.terms.ITermTourAd;
import nure.knt.entity.enums.ConditionCommodity;
import nure.knt.entity.enums.HowSortSQL;
import nure.knt.entity.enums.TypeState;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class TermTourAdMySQL implements ITermTourAd {

    private String privateLimit;
    private Integer[] privateLimitValue;

    private String privateJoin;
    private String privateWhere;
    private String privateField;
    private String privateGroupBy;
    private String privateOrderBy;

    private String privateHaving;
    private final List<Object> privateParametersForWhere;
    private final List<Object> privateParametersForHaving;
    private final Map<OrderByValue,String> orderByValueStringMap;

    public TermTourAdMySQL(Map<OrderByValue,String> orderByValueStringMap){
        privateOrderBy = privateHaving = privateGroupBy = privateField = "";
        privateWhere = privateJoin = privateLimit = "";

        privateParametersForWhere = new LinkedList<>();
        privateParametersForHaving = new LinkedList<>();
        this.orderByValueStringMap = orderByValueStringMap;
    }

    private static final String TOUR_AD_ID_IN = " tour_ad.id in( " + HandlerSqlDAO.REPLACE_SYMBOL + " ) ";
    @Override
    public ITermTourAd idIn(@NotNull Long... ids) {
        privateWhere = HandlerTerm.setFieldsIn(privateWhere,TOUR_AD_ID_IN,privateParametersForWhere,ids);
        return this;
    }

    private static final String TRAVEL_AGENCY_ID_IN = " tour_ad.travel_agency_id in( " + HandlerSqlDAO.REPLACE_SYMBOL + " ) ";
    @Override
    public ITermTourAd travelAgencyIdIn(@NotNull Long... ids) {
        privateWhere = HandlerTerm.setFieldsIn(privateWhere,TRAVEL_AGENCY_ID_IN,privateParametersForWhere,ids);
        return this;
    }

    private static final String COST_ONE_CUSTOMER_BETWEEN = " tour_ad.cost_one_customer BETWEEN ? AND ? ";
    @Override
    public ITermTourAd costOneCustomerBetween(int startCostOneCustomer, int endCostOneCustomer) {
        privateWhere = HandlerTerm.setScript(this.privateWhere,COST_ONE_CUSTOMER_BETWEEN,this.privateParametersForWhere,startCostOneCustomer,endCostOneCustomer);
        return this;
    }

    private static final String COST_SERVICE_BETWEEN = " tour_ad.cost_service BETWEEN ? AND ? ";
    @Override
    public ITermTourAd costServiceBetween(int startCostService, int endCostService) {
        privateWhere = HandlerTerm.setScript(this.privateWhere,COST_SERVICE_BETWEEN,this.privateParametersForWhere,startCostService,endCostService);
        return this;
    }

    private static final String DISCOUNT_SIZE_PEOPLE_BETWEEN = " tour_ad.discount_size_people BETWEEN ? AND ? ";
    @Override
    public ITermTourAd discountSizePeopleBetween(int startDiscountSizePeople, int endDiscountSizePeople) {
        privateWhere = HandlerTerm.setScript(this.privateWhere,DISCOUNT_SIZE_PEOPLE_BETWEEN,this.privateParametersForWhere,startDiscountSizePeople,endDiscountSizePeople);
        return this;
    }

    private static final String COUNT_ORDER_BETWEEN = " COUNT(order_tour.id) BETWEEN ? AND ? ";
    @Override
    public ITermTourAd orderQuantityBetween(int startOrderQuantity, int endOrderQuantity) {
        this.addJoinOrderTour();
        privateHaving = HandlerTerm.setScript(privateHaving,COUNT_ORDER_BETWEEN,this.privateParametersForHaving,startOrderQuantity,endOrderQuantity);
        return this;
    }

    private static final String DISCOUNT_PERCENTAGE_BETWEEN = " tour_ad.discount_percentage BETWEEN ? AND ? ";
    @Override
    public ITermTourAd discountPercentageBetween(float startDiscountPercentage, float endDiscountPercentage) {
        privateWhere = HandlerTerm.setScript(this.privateWhere,DISCOUNT_PERCENTAGE_BETWEEN,this.privateParametersForWhere,startDiscountPercentage,endDiscountPercentage);
        return this;
    }

    private static final String RATING_AGENCY_BETWEEN = " travel_agency.rating BETWEEN ? AND ? ";
    @Override
    public ITermTourAd ratingAgencyBetween(float startRatingAgency, float endRatingAgency) {
        privateWhere = HandlerTerm.setScript(this.privateWhere,RATING_AGENCY_BETWEEN,this.privateParametersForWhere,startRatingAgency,endRatingAgency);
        return this;
    }

    private static final String HIDDEN_IS= " tour_ad.hidden = ? ";
    @Override
    public ITermTourAd hiddenIs(boolean hidden) {
        privateWhere = HandlerTerm.setScript(this.privateWhere,HIDDEN_IS,this.privateParametersForWhere,hidden);
        return this;
    }

    private static final String DATE_REGISTRATION_BETWEEN = " tour_ad.date_registration BETWEEN ? AND ? ";
    @Override
    public ITermTourAd dateRegistrationBetween(LocalDateTime startDateRegistration, LocalDateTime endDateRegistration) {
        privateWhere = HandlerTerm.setScript(this.privateWhere,DATE_REGISTRATION_BETWEEN,this.privateParametersForWhere,startDateRegistration,endDateRegistration);
        return this;
    }

    private static final String START_DATE_AFTER = " tour_ad.date_start <= ? ";
    @Override
    public ITermTourAd startDateTourAdAfter(LocalDate startDateTourAd) {
        privateWhere = HandlerTerm.setScript(this.privateWhere,START_DATE_AFTER,this.privateParametersForWhere,startDateTourAd);
        return this;
    }

    private static final String START_DATE_BETWEEN = " tour_ad.date_start BETWEEN ? AND ? ";
    @Override
    public ITermTourAd startDateTourAdBetween(LocalDate startDateTourAdS, LocalDate startDateTourAdE) {
        privateWhere = HandlerTerm.setScript(this.privateWhere,START_DATE_BETWEEN,this.privateParametersForWhere,startDateTourAdS,startDateTourAdE);
        return this;
    }

    private static final String END_DATE_BEFORE = " tour_ad.date_end >= ? ";
    @Override
    public ITermTourAd endDateTourAdBefore(LocalDate endDateTourAd) {
        privateWhere = HandlerTerm.setScript(this.privateWhere,END_DATE_BEFORE,this.privateParametersForWhere,endDateTourAd);
        return this;
    }

    private static final String END_DATE_BETWEEN = " tour_ad.date_end BETWEEN ? AND ? ";
    @Override
    public ITermTourAd endDateTourAdBetween(LocalDate endDateTourAdS, LocalDate endDateTourAdE) {
        privateWhere = HandlerTerm.setScript(this.privateWhere,END_DATE_BETWEEN,this.privateParametersForWhere,endDateTourAdS,endDateTourAdE);
        return this;
    }

    private static final String START_DATE_AFTER_AND_END_DATE_BEFORE = " tour_ad.date_start <= ? AND tour_ad.date_end >= ? ";
    @Override
    public ITermTourAd startDateTourAdAfterAndEndDateOrderBefore(LocalDate startDateTourAd, LocalDate endDateTourAd) {
        privateWhere = HandlerTerm.setScript(this.privateWhere,START_DATE_AFTER_AND_END_DATE_BEFORE,this.privateParametersForWhere,startDateTourAd,endDateTourAd);
        return this;
    }

    private static final String PLACE_CONTAINING = " tour_ad.place LIKE ? ";
    @Override
    public ITermTourAd placeContaining(String place) {
        privateWhere = HandlerTerm.setScript(this.privateWhere,PLACE_CONTAINING,this.privateParametersForWhere,HandlerSqlDAO.containingString(place));
        return this;
    }

    private static final String CITY_CONTAINING = " tour_ad.city LIKE ? ";
    @Override
    public ITermTourAd cityContaining(String city) {
        privateWhere = HandlerTerm.setScript(this.privateWhere,CITY_CONTAINING,this.privateParametersForWhere,HandlerSqlDAO.containingString(city));
        return this;
    }

    private static final String COUNTRY_CONTAINING = " country.name LIKE ? ";
    @Override
    public ITermTourAd countryContaining(String country) {
        privateWhere = HandlerTerm.setScript(this.privateWhere,COUNTRY_CONTAINING,this.privateParametersForWhere,HandlerSqlDAO.containingString(country));
        return this;
    }

    private static final String NAME_AGENCY_CONTAINING = " user.name LIKE ? ";
    @Override
    public ITermTourAd nameAgencyContaining(String nameAgency) {
        privateWhere = HandlerTerm.setScript(this.privateWhere,NAME_AGENCY_CONTAINING,this.privateParametersForWhere,HandlerSqlDAO.containingString(nameAgency));
        return this;
    }

    private static final String NAME_COUNT_ORDER_TOUR = " , COUNT(order_tour.id)  AS count_orders ";
    @Override
    public ITermTourAd takeOrderQuantity() {
        this.addJoinOrderTour();
        if(!this.privateField.contains(NAME_COUNT_ORDER_TOUR)){
            this.privateField += NAME_COUNT_ORDER_TOUR;
        }
        return this;
    }

    private static final String NAME_COST_SERVICE = " , tour_ad.cost_service ";
    @Override
    public ITermTourAd takeCostService() {
        if(!this.privateField.contains(NAME_COST_SERVICE)){
            this.privateField += NAME_COST_SERVICE;
        }
        return this;
    }

    private static final  String TYPE_STATE_ID_IN = " tour_ad.type_state_id IN(" + HandlerSqlDAO.REPLACE_SYMBOL + ") ";
    @Override
    public ITermTourAd typeStateIn(TypeState... types) {
        privateWhere = HandlerTerm.setFieldsIn(privateWhere,TYPE_STATE_ID_IN,privateParametersForWhere,Arrays
                .stream(types)
                .distinct()
                .map( t -> t.getId())
                .toArray(Integer[]::new));
        return this;
    }

    private static final  String CONDITION_COMMODITY_ID_IN = " tour_ad.condition_commodity_id IN(" + HandlerSqlDAO.REPLACE_SYMBOL + ") ";
    @Override
    public ITermTourAd conditionCommodityIn(ConditionCommodity... conditions) {
        privateWhere = HandlerTerm
                .setFieldsIn(privateWhere,CONDITION_COMMODITY_ID_IN,privateParametersForWhere,Arrays
                        .stream(conditions)
                        .distinct()
                        .map( t -> t.getId())
                        .toArray(Integer[]::new));
        return this;
    }

    private static final String LIMIT_BEFORE_THIS = " LIMIT ? ";
    private static final String LIMIT_BETWEEN_THIS = " LIMIT ?,? ";
    @Override
    public ITermTourAd limitIs(@NotNull Integer... limits) {
        if(limits.length == 1){
            this.privateLimit = LIMIT_BEFORE_THIS;
        }else if(limits.length == 2){
            this.privateLimit = LIMIT_BETWEEN_THIS;
        }
        this.privateLimitValue = limits;
        return this;
    }

    private static final String ORDER_BY = " ORDER BY %s %s ";

    @Override
    public ITermTourAd orderBy(@NotNull OrderByValue orderByValue,@NotNull HowSortSQL sort) {
        this.privateOrderBy = String.format(ORDER_BY,this.orderByValueStringMap.getOrDefault(orderByValue,"tour_ad.id"),sort.name());
        return this;
    }

    @Override
    public ITermInformation end() {

        if (HandlerTerm.hasErrorInOrderBy(this.orderByValueStringMap,this.privateOrderBy, this.privateField,NAME_COUNT_ORDER_TOUR)){
            this.privateOrderBy = "";
        }

        if(!this.privateLimit.isEmpty()){
            this.privateParametersForWhere.add(this.privateLimitValue);
        }

        Object[] _privateParameters = HandlerTerm.concatList(privateParametersForWhere,privateParametersForHaving);

        ITermInformation information = HandlerTerm.toEnd(this.privateLimit,this.privateWhere,this.privateJoin,
                this.privateField,this.privateGroupBy,this.privateHaving,this.privateOrderBy,_privateParameters);

        this.privateGroupBy = this.privateLimit = this.privateHaving = "";
        this.privateWhere = this.privateJoin = this.privateField = "";
        this.privateOrderBy = "";

        return information;
    }

    @Override
    public String toString() {
        return "TermTourAdMySQL :" +
                "\nprivateLimit = '" + privateLimit + '\'' +
                "\nprivateLimitValue = " + Arrays.toString(privateLimitValue) +
                "\nprivateJoin = '" + privateJoin + '\'' +
                "\nprivateWhere = '" + privateWhere + '\'' +
                "\nprivateField = '" + privateField + '\'' +
                "\nprivateGroupBy = '" + this.privateGroupBy + '\'' +
                "\nprivateHaving = '" + this.privateHaving + '\'' +
                "\nprivateParametersForWhere = " + HandlerTerm.printParameters(privateParametersForWhere)  +
                "\nprivateParametersForHaving = "  + HandlerTerm.printParameters(this.privateParametersForHaving);
    }

    private static final String LEFT_JOIN_ORDER_TOUR = " left join order_tour on tour_ad.id = order_tour.tour_ad_id ";
    private static final String GROUP_BY_TOUR_AD_ID = " tour_ad.id ";
    private void addJoinOrderTour(){
        this.privateJoin = HandlerTerm.addJoin(this.privateJoin,LEFT_JOIN_ORDER_TOUR);
        this.privateGroupBy = HandlerTerm.addGroupBy(this.privateGroupBy,GROUP_BY_TOUR_AD_ID);
    }


}

