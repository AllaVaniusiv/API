package ua.lviv.iot.spring.first.project.rest.controller;

import org.springframework.http.ResponseEntity;
import ua.lviv.iot.spring.first.project.rest.model.Driver;


import java.util.List;

public interface DriverController {
    List<Driver> getDrivers();

    Driver getDriver(Integer driverId);

    Driver createDriver(Driver driver);

    ResponseEntity<Driver> deleteDriver(Integer driverId);

    Driver updateDriver(Integer driverId, Driver driver);
}
