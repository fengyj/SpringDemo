package me.fengyj.springdemo.dao;


import me.fengyj.springdemo.models.DailyReturn;
import me.fengyj.springdemo.models.ShareClassDailyKey;

public interface DailyReturnDao extends SeriesDataDao<ShareClassDailyKey, DailyReturn> {

}
