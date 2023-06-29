package ua.lviv.iot.spring.first.project.dataaccess;

import ua.lviv.iot.spring.first.project.rest.model.Driver;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface DriverWriter {
    List<Driver> findAll();

    Driver findById(Integer driverId);

    Driver save(Driver driver);

    void delete(Driver driver);

    int getLastId();

    Driver update(Driver driver);

    Map<Integer, Driver> loadDrivers(String path);

    void writeDrivers(String path, List<Driver> drivers);

    List<String> getMonthPaths(String basePath,
                               LocalDateTime date) throws IOException;
}
