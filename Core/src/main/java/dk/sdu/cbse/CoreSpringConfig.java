package dk.sdu.cbse;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.sdu.cbse.spi.IEntityProcessorService;
import dk.sdu.cbse.spi.IGamePluginService;
import dk.sdu.cbse.spi.IPostEntityProcessorService;
import java.time.Duration;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration(proxyBeanMethods = false)
public class CoreSpringConfig {

    @Bean
    PluginServiceRegistry pluginServiceRegistry() {
        return new PluginServiceRegistry();
    }

    @Bean
    List<IGamePluginService> gamePluginServices(PluginServiceRegistry registry) {
        return registry.loadServices(IGamePluginService.class);
    }

    @Bean
    List<IEntityProcessorService> entityProcessorServices(PluginServiceRegistry registry) {
        return registry.loadServices(IEntityProcessorService.class);
    }

    @Bean
    List<IPostEntityProcessorService> postEntityProcessorServices(PluginServiceRegistry registry) {
        return registry.loadServices(IPostEntityProcessorService.class);
    }

    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder().requestFactory(clientHttpRequestFactory());
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    ScoringClient scoringClient(RestClient.Builder restClientBuilder, ObjectMapper objectMapper) {
        String baseUrl = System.getProperty("scoring.service.base-url", "http://localhost:8080");
        return new ScoringClient(restClientBuilder, objectMapper, baseUrl);
    }

    @Bean
    Game game(
            List<IGamePluginService> gamePlugins,
            List<IEntityProcessorService> processors,
            List<IPostEntityProcessorService> postProcessors,
            ScoringClient scoringClient
    ) {
        return new Game(gamePlugins, processors, postProcessors, scoringClient);
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout((int) Duration.ofMillis(500).toMillis());
        requestFactory.setReadTimeout((int) Duration.ofMillis(500).toMillis());
        return requestFactory;
    }
}
