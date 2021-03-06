package nure.knt.database.dao.mysql.goods;

import nure.knt.database.dao.HandlerSqlDAO;
import nure.knt.database.dao.mysql.tools.MySQLCore;
import nure.knt.database.idao.goods.IDAOTourAd;
import nure.knt.database.idao.goods.ScriptTourAdWhere;
import nure.knt.entity.enums.ConditionCommodity;
import nure.knt.entity.enums.TypeState;
import nure.knt.entity.goods.TourAd;
import nure.knt.tools.WorkWithCountries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static nure.knt.database.dao.HandlerSqlDAO.*;

@Repository("DAO_Tour_Ad_MySQL")
public class DAOTourAdMySQL extends MySQLCore implements IDAOTourAd<TourAd> {

    private static final String INSERT_INSIDE_EDIT_TOUR_AD = "INSERT INTO edit_tour_ad(confirmed,need_delete,whom_need_change_id,what_to_change) VALUE(?,?,?,?);";
    private static final String FIND_ID = "SELECT id FROM tour_ad " +
            "WHERE place = ? AND city = ? AND date_start = ? " +
            "AND date_end = ? " +
            "AND cost_one_customer = ? AND travel_agency_id = ? AND type_state_id = ? " +
            "AND date_registration >= ? and date_registration <= ?";
    @Override
    public boolean editing(Long id, TourAd entity) {

        if( !this.save(entity)){
            return false;
        }

        try{
            long editId = 0;
            try(PreparedStatement statement = super.conn.getSqlPreparedStatement(FIND_ID)){
                HandlerDAOtoMYSQL.tourAdToMySqlScriptForFindId(statement,entity);
                try(ResultSet result = statement.executeQuery()){
                    if (!result.next()){
                        return false;
                    }
                    editId = result.getLong("id");
                }
            }

            try(PreparedStatement statement = super.conn.getSqlPreparedStatement(INSERT_INSIDE_EDIT_TOUR_AD)){
                int position = 0;
                statement.setBoolean(++position,false);
                statement.setBoolean(++position,false);
                statement.setLong(++position,id);
                statement.setLong(++position,editId);

                return statement.executeUpdate() != 0;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static final String UPDATE_EDIT = "UPDATE tour_ad\n" +
            "left join edit_tour_ad on edit_tour_ad.whom_need_change_id = tour_ad.id\n" +
            "left join tour_ad as changes on edit_tour_ad.what_to_change = changes.id\n" +
            "SET \n" +
            "tour_ad.place =changes.place,\n" +
            "tour_ad.city = changes.city ,\n" +
            "tour_ad.date_start =changes.date_start, \n" +
            "tour_ad.date_end = changes.date_end ,\n" +
            "tour_ad.cost_one_customer = changes.cost_one_customer, \n" +
            "tour_ad.discount_size_people = changes.discount_size_people ,\n" +
            "tour_ad.discount_percentage =changes.discount_percentage, \n" +
            "tour_ad.country_id = changes.country_id \n" +
            " WHERE changes.id = ?;";
    private static final String UPDATE_EDITE_TOUR_AD_AFTER_SAVE = "UPDATE edit_tour_ad SET confirmed = true,need_delete = true WHERE what_to_change = %d;";
    private static final String UPDATE_EDITE_TOUR_AD_FOR_REMOVE = "UPDATE edit_tour_ad SET need_delete = true WHERE what_to_change = ?;";
    @Override
    public boolean saveEdit(Long id){
        try(PreparedStatement statement = super.conn.getSqlPreparedStatement(UPDATE_EDIT)){
            statement.setLong(1,id);
            if(statement.executeUpdate() == 0){
                return false;
            }

            return statement.executeUpdate(String.format(UPDATE_EDITE_TOUR_AD_AFTER_SAVE,id)) != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeEdit(Long id) {
        try(PreparedStatement statement = super.conn.getSqlPreparedStatement(UPDATE_EDITE_TOUR_AD_FOR_REMOVE)){
            statement.setLong(1,id);
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static final String INSERT_TOUR_AD =
            " INSERT INTO tour_ad (place,city,date_start,date_end,date_registration,cost_one_customer,cost_service,discount_size_people,discount_percentage,hidden,travel_agency_id,condition_commodity_id,type_state_id,country_id)" +
                    " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

    @Override
    public boolean saveAll(Iterable<TourAd> entities) {
        try(PreparedStatement preparedStatement = super.conn.getSqlPreparedStatement(INSERT_TOUR_AD)){
            for (TourAd tourAd: entities) {
                HandlerDAOtoMYSQL.tourAdToMySqlScript(preparedStatement, tourAd);
                preparedStatement.addBatch();
            }
            return HandlerSqlDAO.arrayHasOnlyOne(preparedStatement.executeBatch());

        }catch(Exception exc){
            exc.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean save(TourAd entity) {
        return this.saveAll(List.of(entity));
    }

    private static final String UPDATE_TYPE_STATE_BY_ID = "UPDATE tour_ad left join travel_agency on travel_agency_id = travel_agency.id  " +
            "SET type_state_id = ? WHERE user.id = ?;";

    @Override
    public boolean updateTypeStateById(Long id, TypeState typeState) {
        try(PreparedStatement preStatement = super.conn.getSqlPreparedStatement(UPDATE_TYPE_STATE_BY_ID)){
            preStatement.setInt(1,typeState.getId());
            preStatement.setLong(2, id);
            return preStatement.executeUpdate()!=0;

        }catch(Exception exc){
            exc.printStackTrace();
        }
        return ERROR_BOOLEAN_ANSWER;
    }

    @Override
    public ScriptTourAdWhere where() {
        return new ScriptTourAdWhereMySQL();
    }



    private static final String CONFIRM_TOUR_AD = "UPDATE tour_ad " +
            "SET condition_commodity_id = ? , cost_service = ? WHERE id = ?;";

    @Override
    public boolean updateConditionCommodityAndCostServiceById(TourAd tourAd) {
        try(PreparedStatement preStatement = super.conn.getSqlPreparedStatement(CONFIRM_TOUR_AD)){
            preStatement.setInt(1,tourAd.getConditionCommodity().getId());
            preStatement.setInt(2, tourAd.getCostService());
            preStatement.setLong(3, tourAd.getId());
            return preStatement.executeUpdate()!=0;

        }catch(Exception exc){
            exc.printStackTrace();
        }
        return ERROR_BOOLEAN_ANSWER;
    }

    String FIND_BY_COST_ONE_CUSTOMER_BETWEEN = " WHERE cost_one_customer BETWEEN ? AND ? ";

    @Override
    public List<TourAd> findByCostOneCustomerBetween(int startCostOneCustomer, int endCostOneCustomer, Supplier<String> script) {
        return this.wrapperForUseSelectList(FIND_BY_COST_ONE_CUSTOMER_BETWEEN, script.get(), startCostOneCustomer,  endCostOneCustomer);
    }

    private List<TourAd> wrapperForUseSelectList(String part, String dopScript, Object ...arrayField){
        return HandlerSqlDAO.useSelectScript(conn, HandlerSqlDAO.concatScriptToEnd(SELECT_TOUR_AD,part, (dopScript.isEmpty())?"":" AND "+dopScript, " ORDER BY tour_ad.date_registration DESC;"),
                HandlerDAOtoMYSQL::resultSetToTourAd,arrayField);
    }

    private static final String FIND_BY_COST_SERVICE_BETWEEN = " WHERE cost_service BETWEEN ? AND ? ";

    @Override
    public List<TourAd> findByCostServiceBetween(int startCostService, int endCostService, Supplier<String> script) {
        return this.wrapperForUseSelectList(FIND_BY_COST_SERVICE_BETWEEN, script.get(), startCostService,  endCostService);

    }


    String FIND_BY_DISCOUNT_SIZE_PEOPLE_BETWEEN = " WHERE discount_size_people BETWEEN ? AND ? ";


    @Override
    public List<TourAd> findByDiscountSizePeopleBetween(int startDiscountSizePeople, int endDiscountSizePeople, Supplier<String> script) {
        return this.wrapperForUseSelectList(FIND_BY_DISCOUNT_SIZE_PEOPLE_BETWEEN, script.get(), startDiscountSizePeople,  endDiscountSizePeople);
    }

    String FIND_BY_ORDER_QUANTITY_BETWEEN = " WHERE (select count(*) from order_tour " +
            " where tour_ad_id = tour_ad.id ) BETWEEN ? AND ? ";

    @Override
    public List<TourAd> findByOrderQuantityBetween(int startOrderQuantity, int endOrderQuantity, Supplier<String> script) {
        return HandlerSqlDAO.useSelectScript(super.conn,HandlerSqlDAO.concatScriptToEnd(SELECT_TOUR_AD_WITH_COUNT_ORDERS,
                        FIND_BY_ORDER_QUANTITY_BETWEEN,( script.get().isEmpty())?"":" AND "+ script.get(), " ORDER BY tour_ad.date_registration;"),
                HandlerDAOtoMYSQL::resultSetToTourAdWithCountingOrders, startOrderQuantity, endOrderQuantity);
    }

    String FIND_BY_DISCOUNT_PERCENTAGE_BETWEEN = " WHERE discount_percentage BETWEEN ? AND ? ";

    @Override
    public List<TourAd> findByDiscountPercentageBetween(float startDiscountPercentage, float endDiscountPercentage, Supplier<String> script) {
        return this.wrapperForUseSelectList(FIND_BY_DISCOUNT_PERCENTAGE_BETWEEN, script.get(), startDiscountPercentage,  endDiscountPercentage);
    }

    String FIND_BY_RATING_AGENCY_BETWEEN = " WHERE travel_agency.rating BETWEEN ? AND ? ";

    @Override
    public List<TourAd> findByRatingAgencyBetween(float startRatingAgency, float endRatingAgency, Supplier<String> script) {
        return this.wrapperForUseSelectList(FIND_BY_RATING_AGENCY_BETWEEN, script.get(), startRatingAgency,  endRatingAgency);
    }

    String FIND_BY_HIDDEN = " WHERE hidden = ? ";

    @Override
    public List<TourAd> findByHidden(boolean hidden, Supplier<String> script) {
        return this.wrapperForUseSelectList(FIND_BY_HIDDEN, script.get(), hidden);
    }

    String FIND_BY_DATE_REGISTRATION_BETWEEN = " WHERE tour_ad.date_registration BETWEEN ? AND ? ";

    @Override
    public List<TourAd> findByDateRegistrationBetween(LocalDateTime startDateRegistration, LocalDateTime endDateRegistration, Supplier<String> script) {
        return this.wrapperForUseSelectList(FIND_BY_DATE_REGISTRATION_BETWEEN, script.get(), startDateRegistration,  endDateRegistration);
    }

    String FIND_BY_START_DATE_AFTER = " WHERE date_start <= ? ";

    @Override
    public List<TourAd> findByStartDateTourAdAfter(LocalDate startDateTourAd, Supplier<String> script) {
        return this.wrapperForUseSelectList(FIND_BY_START_DATE_AFTER, script.get(), startDateTourAd);
    }

    String FIND_BY_END_DATE_BEFORE = " WHERE  date_end >= ? ";

    @Override
    public List<TourAd> findByEndDateTourAdBefore(LocalDate endDateTourAd, Supplier<String> script) {
        return this.wrapperForUseSelectList(FIND_BY_END_DATE_BEFORE, script.get(), endDateTourAd);
    }

    String FIND_BY_START_DATE_AFTER_AND_END_DATE_BEFORE = " WHERE date_start <= ? AND date_end >= ? ";

    @Override
    public List<TourAd> findByStartDateTourAdAfterAndEndDateOrderBefore(LocalDate startDateTourAd, LocalDate endDateTourAd, Supplier<String> script) {
        return this.wrapperForUseSelectList(FIND_BY_START_DATE_AFTER_AND_END_DATE_BEFORE, script.get(), startDateTourAd, endDateTourAd);
    }

    private static final String WHERE_PLACE_CONTAINING = " WHERE place LIKE ? ";

    @Override
    public List<TourAd> findByPlaceContaining(String place, Supplier<String> script) {
        return wrapperForUseSelectList(WHERE_PLACE_CONTAINING,script.get(), HandlerSqlDAO.containingString(place));

    }

    private static final String WHERE_CITY_CONTAINING = " WHERE city LIKE ? ";

    @Override
    public List<TourAd> findByCityContaining(String city, Supplier<String> script) {

        return wrapperForUseSelectList(WHERE_CITY_CONTAINING,script.get(), HandlerSqlDAO.containingString(city));

    }

    private static final String WHERE_COUNTRY_CONTAINING = " WHERE country.name LIKE ? ";

    @Override
    public List<TourAd> findByCountryContaining(String country, Supplier<String> script) {
     return wrapperForUseSelectList(WHERE_COUNTRY_CONTAINING,script.get(), HandlerSqlDAO.containingString(country));
    }

    private static final String WHERE_NAME_AGENCY_CONTAINING = " WHERE user.name LIKE ? ";

    @Override
    public List<TourAd> findByNameAgencyContaining(String nameAgency, Supplier<String> script) {
        return wrapperForUseSelectList(WHERE_NAME_AGENCY_CONTAINING,script.get(), HandlerSqlDAO.containingString(nameAgency));

    }

    @Override
    public List<TourAd> findAll(Supplier<String> script) {
        return HandlerSqlDAO.useSelectScript(this.conn, HandlerSqlDAO.concatScriptToEnd(SELECT_TOUR_AD,( script.get().isEmpty())?"":" WHERE "+ script.get(), " ORDER BY tour_ad.date_registration;"), HandlerDAOtoMYSQL::resultSetToTourAd);
    }

    private static final String SELECT_COUNT_ORDER_TOUR= "SELECT COUNT(order_tour.tour_ad_id) AS count_orders,  tour_ad.id FROM order_tour" +
            " right join tour_ad ON order_tour.tour_ad_id = tour_ad.id " +
            " GROUP BY tour_ad.id " +
            " HAVING tour_ad.id in("+REPLACE_SYMBOL+")" +
            " ORDER BY tour_ad.date_registration;";

    @Override
    public List<TourAd> setOrderQuantity(List<TourAd> tourAds) {

        String script = HandlerSqlDAO.setInInsideScript(SELECT_COUNT_ORDER_TOUR,tourAds );
        try(PreparedStatement preparedStatement = super.conn.getSqlPreparedStatement(script)){

            int position = 0;
            for (TourAd tourAd: tourAds) {
                preparedStatement.setLong(++position, tourAd.getId());
            }

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                for (TourAd tourAd : tourAds) {
                    if (resultSet.next()) {
                        tourAd.setOrderQuantity(resultSet.getInt("count_orders"));

                    }
                }
            }

        }catch(Exception exc){
            exc.printStackTrace();
        }

        return tourAds;

    }

    private static final String WHERE_TOUR_AD_ID_IS = " WHERE tour_ad.id = ? ";
    @Override
    public TourAd findByTourAdId(Long id, Supplier<String> script) {
        return HandlerSqlDAO.useSelectScriptAndGetOneObject(conn, HandlerSqlDAO.concatScriptToEnd(SELECT_TOUR_AD,WHERE_TOUR_AD_ID_IS, (script.get().isEmpty())?"":" AND "+script.get(), " ORDER BY tour_ad.date_registration DESC;"),
                HandlerDAOtoMYSQL::resultSetToTourAd,id);
    }


    private static final String SELECT_TOUR_AD= "SELECT " +
       "tour_ad.id," +
       "place,city," +
       "country.name AS country," +
       "user.name AS travel_agency_name," +
       "date_start," +
       "date_end," +
       "tour_ad.date_registration," +
       "cost_one_customer," +
       "cost_service," +
       "discount_size_people," +
       "discount_percentage," +
       "hidden," +
       "rating," +
       "type_state.name AS type_state," +
       "condition_commodity.name AS condition_commodity," +
       "travel_agency_id" +
       " from tour_ad" +
       " left join travel_agency on tour_ad.travel_agency_id = travel_agency.id" +
       " left join user on travel_agency.user_id = user.id" +
       " left join type_state on tour_ad.type_state_id = type_state.id" +
       " left join condition_commodity on tour_ad.condition_commodity_id = condition_commodity.id" +
       " left join country on tour_ad.country_id = country.id;";



    private static final String SELECT_TOUR_AD_WITH_COUNT_ORDERS= "select " +
            "tour_ad.id," +
            "place,city," +
            "country.name AS country," +
            "user.name AS travel_agency_name," +
            "date_start," +
            "date_end," +
            "tour_ad.date_registration," +
            "cost_one_customer," +
            "cost_service," +
            "discount_size_people," +
            "discount_percentage," +
            "hidden," +
            "rating," +
            "type_state.name AS type_state," +
            "condition_commodity.name AS condition_commodity," +
            "travel_agency_id, " +
            " (select count(*) from order_tour" +
            " where tour_ad_id = tour_ad.id) AS count_orders"+
            " from tour_ad" +
            " left join travel_agency on tour_ad.travel_agency_id = travel_agency.id" +
            " left join user on travel_agency.user_id = user.id" +
            " left join type_state on tour_ad.type_state_id = type_state.id" +
            " left join condition_commodity on tour_ad.condition_commodity_id = condition_commodity.id" +
            " left join country on tour_ad.country_id = country.id;";

    @Component
    class HandlerDAOtoMYSQL {

        private static final int PLACE_TOUR_AD_FOR_INSERT = 1;
        private static final int CITY_TOUR_AD_FOR_INSERT = 2;
        private static final int DATE_START_TOUR_AD_FOR_INSERT = 3;
        private static final int DATE_END_TOUR_AD_FOR_INSERT = 4;
        private static final int DATE_REGISTRATION_TOUR_AD_FOR_INSERT = 5;
        private static final int COST_ONE_CUSTOMER_TOUR_AD_FOR_INSERT = 6;
        private static final int COST_SERVICE_TOUR_AD_FOR_INSERT = 7;
        private static final int DISCOUNT_SIZE_PEOPLE_TOUR_AD_FOR_INSERT = 8;
        private static final int DISCOUNT_PERCENTAGE_TOUR_AD_FOR_INSERT = 9;
        private static final int HIDDEN_TOUR_AD_FOR_INSERT = 10;
        private static final int TRAVEL_AGENCY_ID_TOUR_AD_FOR_INSERT = 11;
        private static final int CONDITION_COMMODITY_ID_TOUR_AD_FOR_INSERT = 12;
        private static final int TYPE_STATE_ID_TOUR_AD_FOR_INSERT = 13;
        private static final int COUNTRY_ID_TOUR_AD_FOR_INSERT = 14;

        private static WorkWithCountries countries;

        @Autowired
        public void setCountries(WorkWithCountries countries){
            HandlerDAOtoMYSQL.countries = countries;
        }

        static void tourAdToMySqlScript(PreparedStatement preStat, TourAd tourAd) {
            try {
                preStat.setString(PLACE_TOUR_AD_FOR_INSERT, tourAd.getPlace());
                preStat.setString(CITY_TOUR_AD_FOR_INSERT, tourAd.getCity());
                preStat.setDate(DATE_START_TOUR_AD_FOR_INSERT, Date.valueOf(tourAd.getDateStart()));
                preStat.setDate(DATE_END_TOUR_AD_FOR_INSERT, Date.valueOf(tourAd.getDateEnd()));
                preStat.setTimestamp(DATE_REGISTRATION_TOUR_AD_FOR_INSERT, Timestamp.valueOf(tourAd.getDateRegistration()));
                preStat.setInt(COST_ONE_CUSTOMER_TOUR_AD_FOR_INSERT, tourAd.getCostOneCustomer());
                preStat.setInt(COST_SERVICE_TOUR_AD_FOR_INSERT, tourAd.getCostService());
                preStat.setInt(DISCOUNT_SIZE_PEOPLE_TOUR_AD_FOR_INSERT, tourAd.getDiscountSizePeople());
                preStat.setFloat(DISCOUNT_PERCENTAGE_TOUR_AD_FOR_INSERT, tourAd.getDiscountPercentage());
                preStat.setBoolean(HIDDEN_TOUR_AD_FOR_INSERT, tourAd.isHidden());
                preStat.setLong(TRAVEL_AGENCY_ID_TOUR_AD_FOR_INSERT, tourAd.getTravelAgencyId());
                preStat.setLong(CONDITION_COMMODITY_ID_TOUR_AD_FOR_INSERT, tourAd.getConditionCommodity().getId());
                preStat.setLong(TYPE_STATE_ID_TOUR_AD_FOR_INSERT, tourAd.getTypeState().getId());
                preStat.setLong(COUNTRY_ID_TOUR_AD_FOR_INSERT, countries.getIdByCountry(tourAd.getCountry()));

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        static void tourAdToMySqlScriptForFindId(PreparedStatement preStat, TourAd tourAd) {
            try {
                int position = 0;
                preStat.setString(++position, tourAd.getPlace());
                preStat.setString(++position, tourAd.getCity());
                preStat.setDate(++position, Date.valueOf(tourAd.getDateStart()));
                preStat.setDate(++position, Date.valueOf(tourAd.getDateEnd()));

                preStat.setInt(++position, tourAd.getCostOneCustomer());
                preStat.setLong(++position, tourAd.getTravelAgencyId());
                preStat.setLong(++position, tourAd.getTypeState().getId());
                preStat.setTimestamp(++position, Timestamp.valueOf(LocalDateTime.now().minusMinutes(1)));
                preStat.setTimestamp(++position, Timestamp.valueOf(LocalDateTime.now().plusMinutes(1)));
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        static TourAd resultSetToTourAd(ResultSet resultSet){
            TourAd tourAd = new TourAd();

            try {

                tourAd.setPlace(resultSet.getString(("place")));
                tourAd.setId(resultSet.getLong(("id")));
                tourAd.setCity(resultSet.getString(("city")));
                tourAd.setCountry(resultSet.getString(("country")));
                tourAd.setNameAgency(resultSet.getString(("travel_agency_name")));
                tourAd.setDateStart(resultSet.getDate(("date_start")).toLocalDate());
                tourAd.setDateEnd(resultSet.getDate(("date_end")).toLocalDate());
                tourAd.setDateRegistration(resultSet.getTimestamp(("date_registration")).toLocalDateTime());
                tourAd.setCostOneCustomer(resultSet.getInt(("cost_one_customer")));

                tourAd.setDiscountPercentage(resultSet.getFloat(("discount_percentage")));
                tourAd.setDiscountSizePeople(resultSet.getInt(("discount_size_people")));
                tourAd.setHidden(resultSet.getBoolean(("hidden")));
                tourAd.setRatingAgency(resultSet.getFloat(("rating")));
                tourAd.setTypeState(TypeState.valueOf(resultSet.getString(("type_state"))));
                tourAd.setConditionCommodity(ConditionCommodity.valueOf(resultSet.getString(("condition_commodity"))));
                tourAd.setTravelAgencyId(resultSet.getLong(("travel_agency_id")));

                try{
                    tourAd.setCostService(resultSet.getInt(("cost_service")));
                }catch (SQLException e){

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return tourAd;
        }

        static TourAd resultSetToTourAdWithCountingOrders(ResultSet resultSet){
           TourAd tourAd = resultSetToTourAd(resultSet);

            try {
                tourAd.setOrderQuantity(resultSet.getInt("count_orders"));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return tourAd;
        }
    }

    static class ScriptTourAdWhereMySQL implements ScriptTourAdWhere{
    private String script = "";

        @Override
        public ScriptTourAdWhere idTravelAgencyIs(Long id) {

            script+=" travel_agency_id = "+id+" ";
            return this;
        }

        @Override
        public ScriptTourAdWhere typeStateIn(Set<TypeState> types) {
            String script =" type_state.name IN (";
            script+=this.enumToScript(types);
            this.addScript(script);
            return this;
        }

        private void addScript(String script){
            if (this.script.isEmpty()){
                this.script = script;
            }
            else{
                this.script +=" AND "+script;
            }
        }

        @Override
        public ScriptTourAdWhere conditionCommodityIn(Set<ConditionCommodity> conditions) {
            String script =" condition_commodity.name IN (";
            script+=this.enumToScript(conditions);
            this.addScript(script);
            return this;
        }

        private static final String WHERE_HIDDEN_ID = " tour_ad.hidden = ? ";
        @Override
        public ScriptTourAdWhere hiddenIs(boolean hidden) {

            String script = WHERE_HIDDEN_ID.replace("?",""+hidden);
            this.addScript(script);
            return this;
        }


        private String enumToScript(Set<?> objects){
            String script="";
            int coun=0;

            for (Object object: objects) {
                if( coun == objects.size()-1){
                    script+=" '"+object+"') ";
                }
                else {
                    script+=" '"+object+"' , ";
                    coun++;
                }

            }
            return script;
        }


        @Override
        public String get() {
            return script;
        }


        public static void main(String[] args) {

            ScriptTourAdWhereMySQL scr = new ScriptTourAdWhereMySQL();

            String str = scr.idTravelAgencyIs(1l).conditionCommodityIn(Set.of(ConditionCommodity.CANCELED)).typeStateIn(Set.of(TypeState.REGISTERED, TypeState.EDITING)).get();
            System.out.println(str);
        }
    }

}



