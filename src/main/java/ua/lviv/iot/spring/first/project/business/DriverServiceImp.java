package ua.lviv.iot.spring.first.project.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.iot.spring.first.project.dataaccess.DriverWriter;
import ua.lviv.iot.spring.first.project.rest.model.Driver;

import java.util.List;

@Service
public final class DriverServiceImp implements DriverService {
    @Autowired
    private DriverWriter driverWriter;

    public List<Driver> findAllDrivers() {
        return driverWriter.findAll();
    }

    public Driver findDriverById(final Integer driverId) {
        return driverWriter.findById(driverId);
    }

    public Driver createDriver(final Driver driver) {
        driver.setId(driverWriter.getLastId() + 1);
        return driverWriter.save(driver);
    }

    public Driver remove(final Integer driverId) {
        Driver driver = driverWriter.findById(driverId);
        if (driver != null) {
            driverWriter.delete(driver);
        }
        return driver;
    }

    public Driver updateDriver(final Integer id, final Driver driver) {
        driver.setId(id);
        return driverWriter.update(driver);
    }
}


