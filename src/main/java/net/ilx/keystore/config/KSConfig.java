package net.ilx.keystore.config;

import net.ilx.keystore.commands.Base64Commands;
import net.ilx.keystore.commands.KeyStoreCommands;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class KSConfig {

	@Bean(autowire=Autowire.BY_NAME)
	public KeyStoreCommands keyStoreCommands() {
		return new KeyStoreCommands();
	}

	@Bean(autowire=Autowire.BY_NAME)
	public Base64Commands base64Commands() {
		return new Base64Commands();
	}

}
