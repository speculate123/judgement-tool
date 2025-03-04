package com.law.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class DataPathConfig {

    @Value("${data.path}")
    public String dataPath;

    public String getArchivedPath() {
        return dataPath + "/archived";
    }
}
