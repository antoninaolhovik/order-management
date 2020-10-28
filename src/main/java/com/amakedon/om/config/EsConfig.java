package com.amakedon.om.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EsConfig { //extends AbstractElasticsearchConfiguration

    //@Value("#{'${elasticsearch.cluster-nodes}'.split(',')}")
    //@Value("${elasticsearch.cluster-nodes}")
    @Value("${elasticsearch.host}")
    private String host; //hosts

    @Value("${elasticsearch.port}")
    private int port;

    private final String elasticScheme = "http";

/*    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        //ClientConfiguration.localhost()
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(host, port, elasticScheme)));
        return client;
    }*/


    @Bean(destroyMethod = "close")
    public RestHighLevelClient client() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(host, port, "http")));
        return client;
    }

/*    @Bean
    @Override
    public EntityMapper entityMapper() {
        return new CustomEntityMapper();
    }*/


/*    public class CustomEntityMapper implements EntityMapper {

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
    }*/

}
