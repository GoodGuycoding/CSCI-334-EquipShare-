// Always run system from this file to see all the new changes. After clicking on the play button, you need to go any browser installed on your machine
//and go to http://localhost:8080/ . To see the equipshare in your local system.
package com.equipshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan("com.equipshare.model")
public class EquipshareApplication {

	public static void main(String[] args) {

		SpringApplication.run(EquipshareApplication.class, args);
	}

}
