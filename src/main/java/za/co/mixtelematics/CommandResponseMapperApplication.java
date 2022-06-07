package za.co.mixtelematics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@RefreshScope
public class CommandResponseMapperApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommandResponseMapperApplication.class, args);

	}

}
