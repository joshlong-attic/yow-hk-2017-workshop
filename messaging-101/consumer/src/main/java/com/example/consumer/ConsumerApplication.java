package com.example.consumer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.support.GenericHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;

import java.io.File;

@Slf4j
@EnableBinding(Sink.class)
@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Bean
    IntegrationFlow incomingStringsToLog(
            Sink binding,
            @Value("${file:///home/jlong/Desktop/out}") File out) {
        return IntegrationFlows
                .from(binding.input())
                .handle((GenericHandler<byte[]>) (bodyOfFile, map) -> {
                    log.info("new file: " + bodyOfFile);
                    return MessageBuilder.withPayload(bodyOfFile).copyHeadersIfAbsent(map).build();
                })
                .handleWithAdapter(a -> a.file(out).autoCreateDirectory(true))
                .get();
    }
}
