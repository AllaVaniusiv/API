package ua.lviv.iot.spring.first.project.dataaccess;


import ua.lviv.iot.spring.first.project.rest.model.Transport;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TransportWriter {
    List<Transport> findAll();

    Transport findById(Integer transportId);

    Transport save(Transport transport);

    void delete(Transport transport);

    Transport update(Transport transport);

    int getLastId();


    Map<Integer, Transport> loadTransports(String path);

    void writeTransports(String path, List<Transport> transports);

    List<String> getMonthPaths(String basePath,
                               LocalDateTime date) throws IOException;
}
