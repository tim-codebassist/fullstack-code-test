package se.kry.codetest;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.sql.ResultSet;

import java.time.ZonedDateTime;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class ServiceDBRepository implements ServiceRepository {

    private DBConnector connector;

    public ServiceDBRepository(Vertx vertx) {
        this.connector = new DBConnector(vertx);
    }

    @Override
    public List<Service> getServices() {
        ResultSet rs = connector.query("SELECT * FROM service").result();
        if (rs != null) {
            return rs.getResults().stream().map(this::toService).collect(toList());
        }
        else {
            return emptyList();
        }
    }

    private Service toService(JsonArray jsonArray) {
        //TODO use instant for "added" not string
        return new Service(jsonArray.getString(0), jsonArray.getString(1), ZonedDateTime.parse(jsonArray.getString(2)));
    }

    @Override
    public void addService(Service service) {
        System.out.println("Adding service: " + service.getUrl());
        connector.query("INSERT INTO service VALUES (?, ?, ?)", new JsonArray(
                asList(service.getUrl(), service.getName(), service.getAdded().toString())));
    }

    @Override
    public void deleteService(String url) {
        System.out.println("Deleting service: " + url);
        connector.query("DELETE FROM service WHERE url = ?", new JsonArray(singletonList(url)));
    }


}
