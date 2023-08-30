package me.fengyj.springdemo.dao.database;

import me.fengyj.common.utils.ThreadUtils;
import me.fengyj.springdemo.models.ShareClassOperation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class ShareClassOperationDbDaoIT {

    @Autowired
    private ShareClassOperationDbDao dao;
    @Test
    public void test() {

        var expected = new ShareClassOperation();
        expected.setShareClassId("0P0000TEST");
        expected.setName("IBM");
        expected.setCreatedTime(LocalDateTime.now());
        expected.setLastUpdatedTime(LocalDateTime.now());

        var actual = dao.getByKey(expected.getKey());

        Assertions.assertNull(actual);

        var deleted = dao.delete(expected.getKey());

        Assertions.assertFalse(deleted);

        var list = dao.getAll();

        Assertions.assertTrue(list.isEmpty());

        dao.save(expected);

        actual = dao.getByKey(expected.getKey());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);

        list = dao.getAll();

        Assertions.assertFalse(list.isEmpty());
        Assertions.assertEquals(1, list.stream().filter(i -> i.getShareClassId().equals(expected.getShareClassId())).count());

        ThreadUtils.sleep(1000);

        expected.setName("NAS:IBM");
        dao.save(expected);

        actual = dao.getByKey(expected.getKey());

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected, actual);

        ThreadUtils.sleep(1000);

        deleted = dao.delete(expected.getKey());

        Assertions.assertTrue(deleted);

        actual = dao.getByKey(expected.getKey());

        Assertions.assertNull(actual);

        list = dao.getAll();

        Assertions.assertTrue(list.isEmpty());
    }
}
