package se.kry.codetest;

import io.vertx.core.Vertx;

import java.util.List;

import static java.util.Arrays.asList;

public class ServiceDBRepository implements ServiceRepository {

    private DBConnector connector;

    public ServiceDBRepository(Vertx vertx) {
        this.connector = new DBConnector(vertx);
    }

    @Override
    public List<String> getServices() {
        return asList("https://www.kry.se");
    }

    @Override
    public void addService(String url) {
        System.out.println(url);
    }
}
