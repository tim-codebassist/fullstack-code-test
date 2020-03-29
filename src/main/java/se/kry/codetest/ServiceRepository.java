package se.kry.codetest;

import java.util.List;

public interface ServiceRepository {
    List<Service> getServices();
    void addService(Service service);
    void deleteService(String url);
}
