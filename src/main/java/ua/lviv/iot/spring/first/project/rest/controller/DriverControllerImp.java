package ua.lviv.iot.spring.first.project.rest.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import ua.lviv.iot.spring.first.project.business.DriverService;
import ua.lviv.iot.spring.first.project.rest.model.Driver;

import java.util.List;


@RequestMapping("/drivers")
@RestController
public final class DriverControllerImp implements DriverController {
    private  int lastId = 0;

    @Autowired
    private DriverService driverService;

    @GetMapping
    public List<Driver> getDrivers() {
        return driverService.findAllDrivers();
    }

    @GetMapping(path = "/{id}")
    public Driver getDriver(@PathVariable("id") final Integer driverId) {
        return driverService.findDriverById(driverId);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public Driver createDriver(@RequestBody final Driver driver) {
        driver.setId(++lastId);
        return driverService.createDriver(driver);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Driver> deleteDriver(
            @PathVariable("id") final Integer driverId) {

        HttpStatus status = driverService.remove(driverId)
                == null ? HttpStatus.NOT_FOUND : HttpStatus.OK;
        return ResponseEntity.status(status).build();
    }

    @PutMapping(path = "/{id}")
    public Driver updateDriver(@PathVariable("id") final Integer driverId,
                               @RequestBody final Driver driver) {
        return driverService.updateDriver(driverId, driver);
    }
}
