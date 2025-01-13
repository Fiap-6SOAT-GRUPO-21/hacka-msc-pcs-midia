package br.com.fiap.pcsmidia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HackaMscPcsMidiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(HackaMscPcsMidiaApplication.class, args);
    }

}
