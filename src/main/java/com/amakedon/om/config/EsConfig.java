package com.amakedon.om.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.mapping.MappingException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class EsConfig extends AbstractElasticsearchConfiguration {

    //@Value("#{'${elasticsearch.cluster-nodes}'.split(',')}")
    //@Value("${elasticsearch.cluster-nodes}")
    @Value("${elasticsearch.host}")
    private String host; //hosts

    @Value("${elasticsearch.port}")
    private int port;

    private final String elasticScheme = "http";

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        return RestClients.create(ClientConfiguration.localhost()).rest();
    }


    @Bean
    @Override
    public EntityMapper entityMapper() {
/*        ElasticsearchEntityMapper entityMapper = new ElasticsearchEntityMapper(elasticsearchMappingContext(),
                new DefaultConversionService());
        entityMapper.setConversions(elasticsearchCustomConversions());*/

        return new CustomEntityMapper();
    }


    public class CustomEntityMapper implements EntityMapper {

        private final ObjectMapper objectMapper;

        public CustomEntityMapper() {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            objectMapper.registerModule(new JavaTimeModule());
        }

        @Override
        public String mapToString(Object object) throws IOException {
            return objectMapper.writeValueAsString(object);
        }

        @Override
        public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
            return objectMapper.readValue(source, clazz);
        }

        @Override
        public Map<String, Object> mapObject(Object o) {
            try {
                return (Map) objectMapper.readValue(this.mapToString(o), HashMap.class);
            } catch (IOException ex) {
                throw new MappingException(ex.getMessage(), ex);
            }
        }

        @Override
        public <T> T readObject(Map<String, Object> source, Class<T> targetType) {
            try {
                return this.mapToObject(this.mapToString(source), targetType);
            } catch (IOException ex) {
                throw new MappingException(ex.getMessage(), ex);
            }
        }
    }

}
