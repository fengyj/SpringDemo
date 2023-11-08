package me.fengyj.springdemo.service.share;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@AutoConfiguration
// without the line below, the beans like @Service cannot be scanned.
@ComponentScan
public class Configuration {
}
