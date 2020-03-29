package se.kry.codetest;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class ServiceDBRepository implements ServiceRepository {

    private DBConnector connector;

    public ServiceDBRepository(Vertx vertx) {
        this.connector = new DBConnector(vertx);
    }

    @Override
    public List<String> getServices() {
        ResultSet rs = connector.query("SELECT * FROM service").result();
        if (rs != null) {
            return rs.getResults().stream()
                    .map(result -> result.getString(0)).collect(toList());
        }
        else {
            return emptyList();
        }
    }

    @Override
    public void addService(String url) {
        System.out.println("Adding service: " + url);
        connector.query("INSERT INTO service VALUES (?)", new JsonArray(singletonList(url)));
    }
}
