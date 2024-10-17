package com.o365.O365.IMAP.Application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class O365ImapApplicationUsingOAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(O365ImapApplicationUsingOAuthApplication.class, args);
		System.out.println("Started......");
	}

}
