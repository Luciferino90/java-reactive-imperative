package it.arubapec.javareactive.service;

import it.arubapec.javareactive.utils.FilesystemUtils;
import it.arubapec.javareactive.utils.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FilesystemService {

    @Value("${working.root.path}")
    private String workingRootPath;

    private Integer size = 1024 * 1024 * 1000;

    private void createEmptyDir(String path){
        Path.of(path).toFile().mkdirs();
    }

    public Mono<Response> createfile(String filename){
        return Mono.just(filename)
                .map(fn -> Path.of(workingRootPath, fn))
                .doOnNext(path -> createEmptyDir(path.getParent().toString()))
                .filter(path -> !path.toFile().exists())
                .switchIfEmpty(Mono.error(new RuntimeException("Il file risulta già esistente: " + filename)))
                .flatMap(path -> FilesystemUtils.writeRandomFile(path, size))
                .map(res -> Response.builder().response(res).build());
    }

    public Mono<Response> createfileManagedError(String filename){
        return createfileManagedError(filename, workingRootPath);
    }

    public Mono<Response> createfileManagedError(String filename, String rootPath){
        return Mono.just(filename)
                .map(fn -> Path.of(rootPath, fn))
                .filter(path -> !path.toFile().exists())
                .switchIfEmpty(Mono.error(new RuntimeException("Il file risulta già esistente: " + filename)))
                .doOnError(e -> System.out.println(e.getMessage()))
                .onErrorResume(e -> Mono.empty())
                .switchIfEmpty(Mono.just(Paths.get(workingRootPath, UUID.randomUUID().toString(), filename)))
                .doOnNext(path -> createEmptyDir(path.getParent().toString()))
                .flatMap(path -> FilesystemUtils.writeRandomFile(path, size))
                .map(res -> Response.builder().response(res).build());
    }

    public Mono<Response> deletefile(String filename){
        return Mono.just(filename)
                .map(fn -> Path.of(workingRootPath, fn))
                .filter(path -> path.toFile().exists())
                .switchIfEmpty(Mono.error(new RuntimeException("Il file non esiste: " + filename)))
                .map(path -> path.toFile().delete())
                .map(Object::toString)
                .map(res -> Response.builder().response(res).build());
    }

}
