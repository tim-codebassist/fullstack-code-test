package se.kry.codetest;

import java.util.List;

public interface ServiceRepository {
    List<String> getServices();
    void addService(String url);
}
