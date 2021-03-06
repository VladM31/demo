package nure.knt.database.service.realization.goods;

import nure.knt.database.idao.goods.IDAOTourAdWithTerms;
import nure.knt.database.idao.terms.ITermCore;
import nure.knt.database.idao.terms.ITermTourAd;
import nure.knt.database.service.implement.goods.IServiceTourAd;
import nure.knt.entity.enums.TypeState;
import nure.knt.entity.goods.TourAd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@PropertySource("classpath:property/goods/WorkerWithTourAds.properties")
public class ServiceTourAd implements IServiceTourAd<TourAd> {

    private IDAOTourAdWithTerms<TourAd> dao;

    @Autowired
    public void setDao(ApplicationContext context,@Value("${dao.tour.ad.for.service}") String daoBeanName) {
        this.dao = context.getBean(daoBeanName,IDAOTourAdWithTerms.class);
    }

    @Override
    public boolean editing(Long id, TourAd entity) {
        return dao.editing(id,entity);
    }

    @Override
    public boolean saveEdit(Long id) {
        return dao.saveEdit(id);
    }

    @Override
    public boolean removeEdit(Long id) {
        return dao.removeEdit(id);
    }

    @Override
    public boolean saveAll(Iterable<TourAd> entities) {
        return dao.saveAll(entities);
    }

    @Override
    public boolean save(TourAd entity) {
        return dao.save(entity);
    }

    @Override
    public boolean updateTypeStateById(Long id, TypeState typeState) {
        return dao.updateTypeStateById(id,typeState);
    }

    @Override
    public boolean updateConditionCommodityAndCostServiceById(TourAd tourAd) {
        return dao.updateConditionCommodityAndCostServiceById(tourAd);
    }

    @Override
    public ITermTourAd term() {
        return dao.term();
    }

    @Override
    public List<TourAd> findByTerms(ITermCore iTermCore) {
        return dao.findByTerms(iTermCore);
    }

    @Override
    public TourAd findOneByTerms(ITermCore iTermCore) {
        return dao.findOneByTerms(iTermCore);
    }
}
