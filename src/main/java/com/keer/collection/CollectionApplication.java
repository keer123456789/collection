package com.keer.collection;

import com.keer.collection.Listener.MyApplicationEvent;
import com.keer.collection.Listener.MyApplicationListener;
import org.glassfish.tyrus.core.wsadl.model.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CollectionApplication {

    public static void main(String[] args) {
        SpringApplication app =new SpringApplication(CollectionApplication.class);
        app.addListeners(new MyApplicationListener());
        app.run(args);


    }

}
