package com.example.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.file.FileInboundChannelAdapterSpec;
import org.springframework.integration.dsl.file.Files;
import org.springframework.integration.file.transformer.FileToStringTransformer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@EnableBinding(Source.class)
@SpringBootApplication
@Slf4j
@RestController
public class ProducerApplication {

    private final MessageChannel loggingServiceChannel;

    public ProducerApplication(Source binding) {
        this.loggingServiceChannel = binding.output();
    }

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    @GetMapping("/log/{me}")
    void write(@PathVariable String me) {
        loggingServiceChannel.send(MessageBuilder.withPayload(me).build());
    }

    @Bean
    IntegrationFlow incomingFiles(@Value("${file:///home/jlong/Desktop/in}") File in) {
        FileInboundChannelAdapterSpec inboundAdapter = Files.inboundAdapter(in).autoCreateDirectory(true);
        return IntegrationFlows
                .from(inboundAdapter, pollerConfig -> pollerConfig.poller(pm -> pm.fixedRate(1000)))
                .transform(new FileToStringTransformer())
                .channel(this.loggingServiceChannel)
                .get();
    }
}