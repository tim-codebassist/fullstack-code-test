package se.kry.codetest;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Service {
    private final String url; // id
    private final String name;
    private final ZonedDateTime added;
    private Status status = Status.UNKNOWN;

    public Service(String url, String name, ZonedDateTime added) {
        this.url = url;
        this.name = name;
        this.added = added;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public ZonedDateTime getAdded() {
        return added;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return url.equals(service.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }
}
