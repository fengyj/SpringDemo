package me.fengyj.springdemo.dao.file;

import org.springframework.stereotype.Repository;
import me.fengyj.springdemo.dao.DailyReturnDao;
import me.fengyj.springdemo.models.DailyReturn;
import me.fengyj.springdemo.models.ShareClassDailyKey;

@Repository
public class DailyReturnDataFileDao extends DataFileDao<ShareClassDailyKey, DailyReturn> implements DailyReturnDao {

    public DailyReturnDataFileDao() {
        super(DailyReturn.class.getSimpleName(), DailyReturn.class);
    }    
}
