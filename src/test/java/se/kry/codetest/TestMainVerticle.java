package se.kry.codetest;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

    private static final int PORT = 8080;
    private static final String HOST = "::1";
    private static final String REQUEST_URI = "/service";
    private WebClient webClient;

    @BeforeEach
    void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
        this.webClient = WebClient.create(vertx);
    }

    @Test
    @DisplayName("Start a web server on localhost responding to GET path /service on port 8080")
    @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
    void shouldGetServices(VertxTestContext testContext) {
        webClient.get(PORT, HOST, REQUEST_URI)
                .send(response -> testContext.verify(() -> {
                    assertEquals(200, response.result().statusCode());
                    //TODO add body validation
                    testContext.completeNow();
                }));
    }

    @Test
    @DisplayName("Start a web server on localhost responding to POST path /service on port 8080")
    @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
    void shouldAddService(VertxTestContext testContext) {
        JsonObject body = new JsonObject()
                .put("url", "https://www.test.com");
        webClient.post(PORT, HOST, REQUEST_URI)
                .sendJson(body, response -> testContext.verify(() -> {
                    assertEquals(200, response.result().statusCode());
                    testContext.completeNow();
                }));
    }

}
