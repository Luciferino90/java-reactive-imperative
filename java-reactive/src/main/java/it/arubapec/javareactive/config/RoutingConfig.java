package it.arubapec.javareactive.config;

import it.arubapec.javareactive.handler.FilesystemHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

@Configuration
@RequiredArgsConstructor
public class RoutingConfig {

    private final FilesystemHandler filesystemHandle;

    @Bean
    public RouterFunction getRoutingConfiguration(){
        return RouterFunctions.route()
                .GET("/create/{filename}", filesystemHandle::createfile)
                .GET("/failurecreate/{filename}", filesystemHandle::failureCreatefile)
                .GET("/delete/{filename}", filesystemHandle::deletefile)
                .build();
    }

}
