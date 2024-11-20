package org.ecom.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EcomServiceApplication {
   public static void main(String[] args) {
      SpringApplication.run(EcomServiceApplication.class, args);
      }
   }