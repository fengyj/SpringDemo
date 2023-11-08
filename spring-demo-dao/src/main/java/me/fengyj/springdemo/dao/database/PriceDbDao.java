package me.fengyj.springdemo.dao.database;

import me.fengyj.springdemo.dao.PriceDao;
import me.fengyj.springdemo.models.Price;
import me.fengyj.springdemo.models.ShareClassDailyKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class PriceDbDao implements PriceDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PriceDbDao(@Autowired DataSource dataSource) {

        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public TreeMap<ShareClassDailyKey, Price> getList(String shareClassId) {

        var sql = "SELECT * FROM Price WHERE ShareClassId = :ShareClassId";

        var list = jdbcTemplate.query(sql, Collections.singletonMap("ShareClassId", shareClassId), this::mapToEntity);
        var result = new TreeMap<ShareClassDailyKey, Price>();
        list.forEach(p -> result.put(p.getKey(), p));
        return result;
    }

    @Override
    public void save(String shareClassId, TreeMap<ShareClassDailyKey, Price> historicalList) {

        var originalList = getList(shareClassId);

        var deleted = new ArrayList<ShareClassDailyKey>();
        var changed = new ArrayList<Price>();

        if (historicalList != null) {
            historicalList.values().forEach(p -> {

                if (originalList != null) {
                    var o = originalList.get(p.getKey());
                    p.setCreatedTime(o == null ? LocalDateTime.now() : o.getCreatedTime());
                    p.setLastUpdatedTime(LocalDateTime.now());
                    if (!p.equals(o))
                        changed.add(p);
                } else {
                    changed.add(p);
                }
            });
        }
        if (originalList != null) {
            originalList.keySet().forEach(o -> {
                if (historicalList == null || !historicalList.containsKey(o))
                    deleted.add(o);
            });
        }

        var updateSql = """
                INSERT INTO Price (ShareClassId, AsOfDate, High, Low, Open, Close, AdjustedPrice, Volume, CreatedTime, LastUpdatedTime)
                VALUES(:ShareClassId, :AsOfDate, :High, :Low, :Open, :Close, :AdjustedPrice, :Volume, now()::timestamp(0), now()::timestamp(0))
                ON CONFLICT (ShareClassId, AsOfDate)
                DO UPDATE SET
                    High = EXCLUDED.High,
                    Low = EXCLUDED.Low,
                    Open = EXCLUDED.Open,
                    Close = EXCLUDED.Close,
                    AdjustedPrice = EXCLUDED.AdjustedPrice,
                    Volume = EXCLUDED.Volume,
                    LastUpdatedTime = EXCLUDED.LastUpdatedTime;
                """;
        var deleteSql = "DELETE FROM Price WHERE ShareClassId = :ShareClassId AND AsOfDate = :AsOfDate;";

        if (!deleted.isEmpty()) {

            Map<String, Object>[] inputs = new HashMap[deleted.size()];
            for (int i = 0; i < inputs.length; i++) {
                inputs[i] = new HashMap<>();
                ShareClassDailyKey key = deleted.get(i);
                inputs[i].put("ShareClassId", key.getShareClassId());
                inputs[i].put("AsOfDate", Date.valueOf(key.getAsOfDate()));
            }

            jdbcTemplate.batchUpdate(deleteSql, inputs);
        }

        if (!changed.isEmpty()) {

            Map<String, Object>[] inputs = new HashMap[changed.size()];
            for (int i = 0; i < inputs.length; i++) {
                inputs[i] = new HashMap<>();
                Price data = changed.get(i);
                inputs[i].put("ShareClassId", data.getKey().getShareClassId());
                inputs[i].put("AsOfDate", Date.valueOf(data.getKey().getAsOfDate()));
                inputs[i].put("High", data.getHigh());
                inputs[i].put("Low", data.getLow());
                inputs[i].put("Open", data.getOpenPrice());
                inputs[i].put("Close", data.getClosePrice());
                inputs[i].put("AdjustedPrice", data.getAdjustedPrice());
                inputs[i].put("Volume", data.getVolume());
            }

            jdbcTemplate.batchUpdate(updateSql, inputs);
        }
    }

    private Price mapToEntity(ResultSet resultSet, int rowNum) throws SQLException {

        var entity = new Price();

        entity.setKey(new ShareClassDailyKey());
        entity.getKey().setShareClassId(resultSet.getString("ShareClassId"));
        entity.getKey().setAsOfDate(resultSet.getDate("AsOfDate").toLocalDate());
        entity.setHigh(resultSet.getBigDecimal("High"));
        entity.setLow(resultSet.getBigDecimal("Low"));
        entity.setOpenPrice(resultSet.getBigDecimal("Open"));
        entity.setClosePrice(resultSet.getBigDecimal("Close"));
        entity.setAdjustedPrice(resultSet.getBigDecimal("AdjustedPrice"));
        entity.setVolume(resultSet.getLong("Volume"));
        entity.setCreatedTime(resultSet.getTimestamp("CreatedTime").toLocalDateTime());
        entity.setLastUpdatedTime(resultSet.getTimestamp("LastUpdatedTime").toLocalDateTime());

        return entity;
    }
}
