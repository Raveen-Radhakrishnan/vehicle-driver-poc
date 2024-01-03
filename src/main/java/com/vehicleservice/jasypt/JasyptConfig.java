package com.vehicleservice.jasypt;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

    @Bean("jasyptStringEncryptor")
    StringEncryptor stringEncryptor() {
		
		PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
		SimpleStringPBEConfig config = new SimpleStringPBEConfig();
		config.setPassword("secret");
		config.setPoolSize("1");
//		config.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
//		config.setKeyObtentionIterations("1000");
//		config.setProviderName("SunJCE");
//		config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
//		config.setIvGeneratorClassName("org.jasypt.iv.RandomIvGenerator");
//		config.setStringOutputType("base64");
		encryptor.setConfig(config);
		return encryptor;
	}

}
