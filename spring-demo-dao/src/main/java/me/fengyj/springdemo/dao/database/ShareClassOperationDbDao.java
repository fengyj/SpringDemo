package me.fengyj.springdemo.dao.database;

import me.fengyj.common.exceptions.ErrorSeverity;
import me.fengyj.springdemo.dao.ShareClassOperationDao;
import me.fengyj.springdemo.models.ShareClassOperation;
import me.fengyj.springdemo.utils.exceptions.DataIssueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
    public List<ShareClassOperation> getBySymbol(String symbol) {

        var sql = "SELECT * FROM ShareClassOperation WHERE Symbol = :Symbol";
        return jdbcTemplate.query(sql, Collections.singletonMap("Symbol", symbol), this::mapToEntity);
    }

    @Override
    public void save(ShareClassOperation data) {

        var sql = """
                INSERT INTO ShareClassOperation (ShareClassId, Name, Symbol, CreatedTime, LastUpdatedTime)
                VALUES(:ShareClassId, :Name, :Symbol, now()::timestamp(0), now()::timestamp(0))
                ON CONFLICT (ShareClassId)
                DO UPDATE SET
                    Name = EXCLUDED.Name,
                    Symbol = EXCLUDED.Symbol,
                    LastUpdatedTime = EXCLUDED.LastUpdatedTime;
                                
                INSERT INTO ShareClassOperationHistory (ShareClassId, Name, Symbol, CreatedTime, LastUpdatedTime, IsDeleted)
                SELECT ShareClassId, Name, Symbol, CreatedTime, LastUpdatedTime, false AS IsDeleted
                FROM ShareClassOperation WHERE ShareClassId = :ShareClassId
                ON CONFLICT (ShareClassId, LastUpdatedTime)
                DO UPDATE SET
                    Name = EXCLUDED.Name,
                    Symbol = EXCLUDED.Symbol,
                    CreatedTime = EXCLUDED.CreatedTime,
                    LastUpdatedTime = EXCLUDED.LastUpdatedTime,
                    IsDeleted = false;
                """;

        try {
            jdbcTemplate.update(sql, Map.of(
                    "ShareClassId", data.getShareClassId(),
                    "Name", data.getName(),
                    "Symbol", data.getSymbol()));
        } catch (DuplicateKeyException ex) {
            if (ex.getMessage().contains("shareclassoperation_unique_symbol"))
                throw new DataIssueException(ErrorSeverity.Error, ShareClassOperation.class, data.getShareClassId(), "symbol", "The symbol has been used by other share.");
            else if (ex.getMessage().contains("shareclassoperation_pkey"))
                throw new DataIssueException(ErrorSeverity.Error, ShareClassOperation.class, data.getShareClassId(), "shareClassId", "The shareClassId has been used by other share.");
            else
                throw ex;
        }
    }

    @Override
    public boolean delete(String key) {

        var sql = """
                INSERT INTO ShareClassOperationHistory (ShareClassId, Name, Symbol, CreatedTime, LastUpdatedTime, IsDeleted)
                SELECT ShareClassId, Name, Symbol, CreatedTime, now()::timestamp(0) AS LastUpdatedTime, true AS IsDeleted
                FROM ShareClassOperation WHERE ShareClassId = :ShareClassId
                ON CONFLICT (ShareClassId, LastUpdatedTime)
                DO UPDATE SET
                    Name = EXCLUDED.Name,
                    Symbol = EXCLUDED.Symbol,
                    CreatedTime = EXCLUDED.CreatedTime,
                    LastUpdatedTime = EXCLUDED.LastUpdatedTime,
                    IsDeleted = true;
                                
                DELETE FROM ShareClassOperation WHERE ShareClassId = :ShareClassId;
                """;

        return jdbcTemplate.update(sql, Map.of("ShareClassId", key)) == 1;
    }

    @Override
    public List<ShareClassOperation> getAll() {

        var sql = "SELECT * FROM ShareClassOperation";
        return jdbcTemplate.query(sql, this::mapToEntity);
    }

    @Override
    public int getShareClassIdSeed() {

        var sql = "SELECT NEXTVAL('seq_share_class_id_seed');";

        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
    }

    private ShareClassOperation mapToEntity(ResultSet resultSet, int rowNum) throws SQLException {

        var entity = new ShareClassOperation();

        entity.setShareClassId(resultSet.getString("ShareClassId"));
        entity.setName(resultSet.getString("Name"));
        entity.setSymbol(resultSet.getString("Symbol"));
        entity.setCreatedTime(resultSet.getTimestamp("CreatedTime").toLocalDateTime());
        entity.setLastUpdatedTime(resultSet.getTimestamp("LastUpdatedTime").toLocalDateTime());

        return entity;
    }
}
