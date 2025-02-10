package org.codingdsa.quotegenesis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QuoteGenesisApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuoteGenesisApplication.class, args);
    }

}
