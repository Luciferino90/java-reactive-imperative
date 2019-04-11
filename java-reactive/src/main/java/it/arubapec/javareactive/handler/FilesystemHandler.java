package it.arubapec.javareactive.handler;

import it.arubapec.javareactive.service.FilesystemService;
import it.arubapec.javareactive.utils.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.nio.file.Paths;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FilesystemHandler {


    private final FilesystemService filesystemService;

    public Mono<ServerResponse> createfile(ServerRequest serverRequest){
        return filesystemService.createfile(serverRequest.pathVariable("filename"))
                .flatMap(res -> ServerResponse.ok().body(BodyInserters.fromObject(res)));
    }

    public Mono<ServerResponse> createFileManagedError(ServerRequest serverRequest){
        return filesystemService.createfileManagedError(serverRequest.pathVariable("filename"))
                .flatMap(res -> ServerResponse.ok().body(BodyInserters.fromObject(res)));
    }

    public Mono<ServerResponse> deletefile(ServerRequest serverRequest){
        return filesystemService.deletefile(serverRequest.pathVariable("filename"))
                .flatMap(res -> ServerResponse.ok().body(BodyInserters.fromObject(res)));
    }

}
