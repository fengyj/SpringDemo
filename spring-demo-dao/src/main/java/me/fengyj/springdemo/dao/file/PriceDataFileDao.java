package me.fengyj.springdemo.dao.file;


import org.springframework.stereotype.Repository;
import me.fengyj.springdemo.dao.PriceDao;
import me.fengyj.springdemo.models.Price;
import me.fengyj.springdemo.models.ShareClassDailyKey;

@Repository
public class PriceDataFileDao extends DataFileDao<ShareClassDailyKey, Price> implements PriceDao {

    public PriceDataFileDao() {
        super(Price.class.getSimpleName(), Price.class);
    }    
}
