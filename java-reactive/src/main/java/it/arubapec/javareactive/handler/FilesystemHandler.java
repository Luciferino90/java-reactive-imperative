package it.arubapec.javareactive.handler;

import it.arubapec.javareactive.service.FilesystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FilesystemHandler {

    private final FilesystemService filesystemService;

    public Mono<ServerResponse> createfile(ServerRequest serverRequest){
        return filesystemService.createfile(serverRequest.pathVariable("filename"))
                .flatMap(res -> ServerResponse.ok().body(BodyInserters.fromObject(res)));
    }

    public Mono<ServerResponse> failureCreatefile(ServerRequest serverRequest){
        return filesystemService.createfile(serverRequest.pathVariable("filename"))
                .doOnError(e -> System.out.println(e.getMessage()))
                .onErrorResume(e -> Mono.just("Eccezione ricevuta: " + e.getMessage()))
                .flatMap(message -> filesystemService.createfile(serverRequest.pathVariable("filename"), "/tmp/reactive-test/temp"))
                .flatMap(res -> ServerResponse.ok().body(BodyInserters.fromObject(res)));
    }

    public Mono<ServerResponse> deletefile(ServerRequest serverRequest){
        return filesystemService.deletefile(serverRequest.pathVariable("filename"))
                .flatMap(res -> ServerResponse.ok().body(BodyInserters.fromObject(res)));
    }

}
