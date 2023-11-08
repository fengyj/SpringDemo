package me.fengyj.springdemo.dao;

import me.fengyj.springdemo.models.ShareClassOperation;

import java.util.List;

public interface ShareClassOperationDao extends RelationDataDao<String, ShareClassOperation> {

    List<ShareClassOperation> getBySymbol(String symbol);

    int getShareClassIdSeed();
}
