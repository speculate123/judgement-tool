/*
 * Copyright (c) 2025 LY Corporation. All rights reserved.
 * LY Corporation PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.law.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
public class ElasticSearchConfig extends ElasticsearchConfiguration {
    @Value("${elasticsearch.rest.protocol:https}")
    public String protocol;

    @Value("${elasticsearch.rest.host:localhost}")
    public String host;

    @Value("${elasticsearch.rest.port:9200}")
    public int port;

    @Value("${elasticsearch.rest.username}")
    public String username;

    @Value("${elasticsearch.rest.password}")
    public String password;

    @Value("${elasticsearch.rest.ssl}")
    public String ssl;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                                  .connectedTo(String.format("%s:%d", host, port))
                                  .usingSsl(ssl)
                                  .withBasicAuth(username, password)
                                  .build();
    }
}
