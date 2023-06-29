package ua.lviv.iot.spring.first.project.business;

import ua.lviv.iot.spring.first.project.rest.model.Driver;

import java.util.List;

public interface DriverService {
    List<Driver> findAllDrivers();

    Driver findDriverById(Integer driverId);

    Driver createDriver(Driver driver);

    Driver remove(Integer driverId);

    Driver updateDriver(Integer id, Driver driver);
}

