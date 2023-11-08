package me.fengyj.springdemo.dao;

import me.fengyj.springdemo.dao.database.DatabaseConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@AutoConfiguration()
@Import(DatabaseConfiguration.class)
// without the line below, the beans like @Repository cannot be scanned.
@ComponentScan
public class DaoConfiguration {

    public DaoConfiguration() {
        System.out.println("Invoked DaoConfiguration constructor");
    }
}
