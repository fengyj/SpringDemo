package me.fengyj.springdemo.dao.database;

import me.fengyj.springdemo.dao.ShareClassOperationDao;
import me.fengyj.springdemo.models.ShareClassOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class ShareClassOperationDbDao implements ShareClassOperationDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public ShareClassOperationDbDao(@Autowired DataSource dataSource) {

        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public ShareClassOperation getByKey(String key) {

        var sql = "SELECT * FROM ShareClassOperation WHERE ShareClassId = :ShareClassId";
        var list = jdbcTemplate.query(sql, Collections.singletonMap("ShareClassId", key), this::mapToEntity);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public void save(ShareClassOperation data) {

        var sql = "INSERT INTO ShareClassOperation (ShareClassId, Name, CreatedTime, LastUpdatedTime) " +
                "VALUES(:ShareClassId, :Name, now()::timestamp(0), now()::timestamp(0)) " +
                "ON CONFLICT (ShareClassId) DO UPDATE SET " +
                "Name = EXCLUDED.Name, LastUpdatedTime = EXCLUDED.LastUpdatedTime;" +
                "INSERT INTO ShareClassOperationHistory (ShareClassId, Name, CreatedTime, LastUpdatedTime, IsDeleted) " +
                "(SELECT ShareClassId, Name, CreatedTime, LastUpdatedTime, false AS IsDeleted FROM ShareClassOperation WHERE ShareClassId = :ShareClassId) " +
                "ON CONFLICT (ShareClassId, LastUpdatedTime) DO UPDATE SET " +
                "Name = EXCLUDED.Name, CreatedTime = EXCLUDED.CreatedTime, LastUpdatedTime = EXCLUDED.LastUpdatedTime;";

        jdbcTemplate.update(sql, Map.of(
                "ShareClassId", data.getShareClassId(),
                "Name", data.getName(),
                "CreatedTime", data.getCreatedTime(),
                "LastUpdatedTime", data.getLastUpdatedTime()));
    }

    @Override
    public boolean delete(String key) {

        var sql = "INSERT INTO ShareClassOperationHistory (ShareClassId, Name, CreatedTime, LastUpdatedTime, IsDeleted) " +
                "(SELECT ShareClassId, Name, CreatedTime, now()::timestamp(0) AS LastUpdatedTime, true AS IsDeleted FROM ShareClassOperation WHERE ShareClassId = :ShareClassId) " +
                "ON CONFLICT (ShareClassId, LastUpdatedTime) DO UPDATE SET " +
                "Name = EXCLUDED.Name, CreatedTime = EXCLUDED.CreatedTime, LastUpdatedTime = EXCLUDED.LastUpdatedTime;" +
                "DELETE FROM ShareClassOperation WHERE ShareClassId = :ShareClassId;";

        return jdbcTemplate.update(sql, Map.of("ShareClassId", key)) == 1;
    }

    @Override
    public List<ShareClassOperation> getAll() {

        var sql = "SELECT * FROM ShareClassOperation";
        return jdbcTemplate.query(sql, this::mapToEntity);
    }

    private ShareClassOperation mapToEntity(ResultSet resultSet, int rowNum) throws SQLException {

        var entity = new ShareClassOperation();

        entity.setShareClassId(resultSet.getString("ShareClassId"));
        entity.setName(resultSet.getString("Name"));
        entity.setCreatedTime(resultSet.getTimestamp("CreatedTime").toLocalDateTime());
        entity.setLastUpdatedTime(resultSet.getTimestamp("LastUpdatedTime").toLocalDateTime());

        return entity;
    }
}
