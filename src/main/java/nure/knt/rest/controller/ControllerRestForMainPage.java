package nure.knt.rest.controller;

import nure.knt.database.idao.goods.IDAOTourAd;
import nure.knt.database.idao.terms.ITermTourAd;
import nure.knt.database.service.implement.goods.IServiceTourAd;
import nure.knt.entity.enums.ConditionCommodity;
import nure.knt.entity.enums.TypeState;
import nure.knt.entity.goods.TourAd;
import nure.knt.forms.filter.terms.FilterTourAdTerms;
import nure.knt.tools.WorkWithCountries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value="/free/rest/api")
public class ControllerRestForMainPage {

    @Autowired
    private IServiceTourAd<TourAd> daoTourAd;
    @Autowired
    private WorkWithCountries countries;

    @RequestMapping(value = "/tour", //
            method = RequestMethod.GET, headers = "Accept=application/json",produces = "application/json")
    public ResponseEntity<List<TourAd>> getTourAd(FilterTourAdTerms filter) {

        System.out.println("getTourAd from nure/knt/controller/ControllerRestForMainPage.java");

        return new ResponseEntity<>(daoTourAd.findByTerms(restrictFilter(filter)
                .filtering(daoTourAd.term())), HttpStatus.OK);
    }

    @RequestMapping(value = "/countries", //
            method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String[]> getCountries(String lan){
        return new ResponseEntity<>(countries.getCountry(), HttpStatus.OK);
    }

    private static FilterTourAdTerms restrictFilter(FilterTourAdTerms filterTourAdTerms){
        // убираю фільтрацию по стоимости
        filterTourAdTerms.setStartCostService(null);
        filterTourAdTerms.setEndCostService(null);
        // убираю фільтрацию по количеству заказов
        filterTourAdTerms.setStartCountOrders(null);
        filterTourAdTerms.setEndCountOrders(null);
        // убираю вівод ціну оголошення та кількість заказів
        filterTourAdTerms.setTakeCountOrders(false);
        filterTourAdTerms.setTakeCostTourAd(false);
        // встановлюю стан оголошень
        filterTourAdTerms.setConditionCommodities(new ConditionCommodity[]{ConditionCommodity.CONFIRMED});
        filterTourAdTerms.setTypeStates(new TypeState[]{TypeState.REGISTERED});

        if(filterTourAdTerms.getOrderBy() != null &&
                (filterTourAdTerms.getOrderBy().equals(ITermTourAd.OrderByValue.COUNT_ORDERS) ||
                filterTourAdTerms.getOrderBy().equals(ITermTourAd.OrderByValue.COST_SERVICE))){
            filterTourAdTerms.setOrderBy(null);
        }

        return filterTourAdTerms;
    }
}
