package me.fengyj.springdemo.dao.file;

import me.fengyj.common.utils.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;

@SpringBootTest(properties = { "application.datafile.root-dir=${java.io.tmpdir}", "application.datafile.extension=.json" })
public class FileAccessorTest {

    @Autowired
    private FileAccessor.MemoryFileAccessor memoryFileAccessor;
    @Autowired
    private FileAccessor.LocalFileAccessor localFileAccessor;

    @Test
    public void test_memory_file_accessor() {

        Assertions.assertNotNull(memoryFileAccessor);

        var bytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
        memoryFileAccessor.uploadToFile("Category", "File_1", bytes);
        var loaded = memoryFileAccessor.downloadFromFile("Category", "File_1");

        Assertions.assertArrayEquals(bytes, loaded);

        Assertions.assertThrows(FileAccessException.class, () -> memoryFileAccessor.uploadToFile("Category", "File_2", null));

        var bytes2 = new byte[] { 0, 1, 2, 3 };
        memoryFileAccessor.uploadToFile("Category", "File_2", bytes2);
        var loaded2 = memoryFileAccessor.downloadFromFile("Category", "File_2");

        Assertions.assertArrayEquals(bytes2, loaded2);

        loaded = memoryFileAccessor.downloadFromFile("Category", "File_1");
        Assertions.assertArrayEquals(bytes, loaded);
    }

    @Test
    public void test_local_file_accessor() {

        Assertions.assertNotNull(localFileAccessor);

        var pathExp = Path.of(System.getProperty("java.io.tmpdir"), "Category\\File_1.json");
        var pathAct = localFileAccessor.getFilePath("Category", "File_1");

        Assertions.assertEquals(pathExp, pathAct);

        var bytes = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8 };
        localFileAccessor.uploadToFile("Category", "File_1", bytes);
        var loaded = localFileAccessor.downloadFromFile("Category", "File_1");

        Assertions.assertArrayEquals(bytes, loaded);

        IOUtils.deleteFile(pathAct);
    }
}
