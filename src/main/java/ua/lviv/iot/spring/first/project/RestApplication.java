package ua.lviv.iot.spring.first.project;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;


@SpringBootApplication
public class RestApplication {
    RestApplication() {

    }

    public static void main(final String[] args) throws IOException {
        SpringApplication.run(RestApplication.class, args);
    }

}
