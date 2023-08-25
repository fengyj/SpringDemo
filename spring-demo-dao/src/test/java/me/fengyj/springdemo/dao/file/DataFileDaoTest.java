package me.fengyj.springdemo.dao.file;

import me.fengyj.springdemo.dao.DailyReturnDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

public class DataFileDaoTest {

    @SpringBootTest(properties = { "application.datafile.serde-type=DataFileXmlSerDe" })
    @Nested
    public class XmlSerDeTester {

        @Autowired
        private DailyReturnDao dao;

        @Test
        public void verify_serde() {

            Assertions.assertTrue(dao instanceof DailyReturnDataFileDao, "dao should be DailyReturnDataFileDao.");
            var fileDao = (DailyReturnDataFileDao) dao;
            Assertions.assertNotNull(fileDao.getSerDe(), "serDe shouldn't be null.");
            Assertions.assertTrue(fileDao.getSerDe() instanceof DataFileXmlSerDe, "serDe should be DataFileXmlSerDe type.");
        }
    }

    @SpringBootTest
    @Nested
    public class JsonSerDeTester {

        @Autowired
        private DailyReturnDao dao;

        @Test
        public void verify_serde() {

            Assertions.assertTrue(dao instanceof DailyReturnDataFileDao, "dao should be DailyReturnDataFileDao.");
            var fileDao = (DailyReturnDataFileDao) dao;
            Assertions.assertNotNull(fileDao.getSerDe(), "serDe shouldn't be null.");
            Assertions.assertTrue(fileDao.getSerDe() instanceof DataFileJsonSerDe, "serDe should be DataFileJsonSerDe type.");
        }
    }

    @SpringBootTest
    @Nested
    public class MemoryFileTester {

        @Autowired
        private DailyReturnDao dao;

        @Test
        public void verify_accessor() {

            Assertions.assertTrue(dao instanceof DailyReturnDataFileDao, "dao should be DailyReturnDataFileDao.");
            var fileDao = (DailyReturnDataFileDao) dao;
            Assertions.assertNotNull(fileDao.getAccessor(), "accessor shouldn't be null.");
            Assertions.assertTrue(fileDao.getAccessor() instanceof FileAccessor.MemoryFileAccessor, "accessor should be MemoryFileAccessor type.");
        }
    }

    @SpringBootTest(properties = { "application.datafile.accessor-type=LocalFileAccessor" })
    @Nested
    public class LocalFileFileTester {

        @Autowired
        private DailyReturnDao dao;

        @Test
        public void verify_accessor() {

            Assertions.assertTrue(dao instanceof DailyReturnDataFileDao, "dao should be DailyReturnDataFileDao.");
            var fileDao = (DailyReturnDataFileDao) dao;
            Assertions.assertNotNull(fileDao.getAccessor(), "accessor shouldn't be null.");
            Assertions.assertTrue(fileDao.getAccessor() instanceof FileAccessor.LocalFileAccessor, "accessor should be LocalFileAccessor type.");
        }
    }
}
