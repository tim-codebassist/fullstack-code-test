package se.kry.codetest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MainVerticle extends AbstractVerticle {

  private Map<String,Service> services = new HashMap<>();
  private ServiceRepository serviceRepository;
  private BackgroundPoller poller = new BackgroundPoller();

  @Override
  public void start(Future<Void> startFuture) {
    this.serviceRepository = new ServiceDBRepository(vertx);
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    setServices(serviceRepository.getServices());

    vertx.setPeriodic(1000 * 60, timerId -> poller.pollServices(new ArrayList<>(services.values())));
    setRoutes(router);
    vertx
        .createHttpServer()
        .requestHandler(router)
        .listen(8080, result -> {
          if (result.succeeded()) {
            System.out.println("KRY code test service started");
            startFuture.complete();
          } else {
            startFuture.fail(result.cause());
          }
        });
  }

  private void setRoutes(Router router){
    router.route("/*").handler(StaticHandler.create());
    // list services
    router.get("/service").handler(req -> {
      List<JsonObject> jsonServices = services
          .values()
          .stream()
          .map(service ->
              new JsonObject()
                  .put("url", service.getUrl())
                  .put("name", service.getName())
                  .put("status", service.getStatus()))
          .collect(Collectors.toList());
      req.response()
          .putHeader("content-type", "application/json")
          .end(new JsonArray(jsonServices).encode());
    });
    // add service
    router.post("/service").handler(req -> {
      JsonObject jsonBody = req.getBodyAsJson();
      Service service = new Service(
              jsonBody.getString("url"),
              jsonBody.getString("name"),
              ZonedDateTime.now());
      services.put(service.getUrl(), service);
      serviceRepository.addService(service);
      req.response()
          .putHeader("content-type", "text/plain")
          .end("OK");
    });
    // delete service
    router.delete("/service").handler(req -> {
      JsonObject jsonBody = req.getBodyAsJson();
      String url = jsonBody.getString("url");
      services.remove(url);
      serviceRepository.deleteService(url);
      req.response()
              .putHeader("content-type", "text/plain")
              .end("OK");
    });
  }

  private void setServices(List<Service> services) {
    this.services = services.stream().collect(Collectors.toMap(Service::getUrl, Function.identity()));
  }

}



