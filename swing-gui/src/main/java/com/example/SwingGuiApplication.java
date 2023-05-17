package com.example;

import com.example.gui.MainFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableFeignClients
public class SwingGuiApplication implements CommandLineRunner {

    @Autowired
    private MainFrame mainFrame;

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(SwingGuiApplication.class);
        builder.headless(false);
        ConfigurableApplicationContext context = builder.run(args);
    }

    @Override
    public void run(String... args) {
        mainFrame.loadMessagesToFrame();
    }
}
