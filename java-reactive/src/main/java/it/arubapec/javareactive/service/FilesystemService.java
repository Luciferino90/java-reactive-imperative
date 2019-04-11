package it.arubapec.javareactive.service;

import it.arubapec.javareactive.utils.FilesystemUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FilesystemService {

    private Integer size = 1024 * 1024 * 1000;

    @Value("${working.root.path}")
    private String workingRootPath;

    private void createEmptyDir(String path){
        Path.of(path).toFile().mkdirs();
    }

    public Mono<String> createfile(String filename){
        return Mono.just(filename)
                .map(fn -> Path.of(workingRootPath, fn))
                .doOnNext(path -> createEmptyDir(path.getParent().toString()))
                .filter(path -> !path.toFile().exists())
                .switchIfEmpty(Mono.error(new RuntimeException("Il file risulta già esistente: " + filename)))
                .flatMap(path -> FilesystemUtils.writeRandomFile(path, size));
    }

    public Mono<String> createfile(String filename, String otherPath){
        return Mono.just(filename)
                .map(fn -> Path.of(otherPath, fn))
                .doOnNext(path -> createEmptyDir(path.getParent().toString()))
                .filter(path -> !path.toFile().exists())
                .switchIfEmpty(Mono.error(new RuntimeException("Il file risulta già esistente: " + filename)))
                .flatMap(path -> FilesystemUtils.writeRandomFile(path, size));
    }

    public Mono<String> deletefile(String filename){
        return Mono.just(filename)
                .map(fn -> Path.of(workingRootPath, fn))
                .filter(path -> path.toFile().exists())
                .switchIfEmpty(Mono.error(new RuntimeException("Il file non esiste: " + filename)))
                .map(path -> path.toFile().delete())
                .map(Object::toString);
    }

}
