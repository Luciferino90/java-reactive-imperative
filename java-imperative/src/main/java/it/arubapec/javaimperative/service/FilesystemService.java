package it.arubapec.javaimperative.service;

import it.arubapec.javareactive.utils.FilesystemUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Service
public class FilesystemService {

    private Integer size = 1024 * 1024 * 1000;

    @Value("${working.root.path}")
    private String workingRootPath;

    public void createTmpDir(Path folder){
        folder.toFile().mkdirs();
    }

    public String createfile(String filename){
        Long startTime = new Date().getTime();
        Path path = Paths.get(workingRootPath, filename);
        if (path.toFile().exists())
            throw new RuntimeException("Il file risulta già esistente: " + filename);
        createTmpDir(path.getParent());
        FilesystemUtils.imperativeEriteRandomFile(path, size);
        return "Durata in ms: " + (new Date().getTime() - startTime) + " path: " + path.toString();
    }

    public String createfileManagedError(String filename){
        return createfileManagedError(filename, workingRootPath);
    }

    public String createfileManagedError(String filename, String rootPath){
        Long startTime = new Date().getTime();
        Path path = Paths.get(rootPath, filename);
        if (path.toFile().exists())
            return createfileManagedError(filename, Paths.get(workingRootPath, UUID.randomUUID().toString()).toString());
        else
            createTmpDir(path.getParent());
            FilesystemUtils.imperativeEriteRandomFile(path, size);
            return "Durata in ms: " + (new Date().getTime() - startTime) + " path: " + path.toString();
    }

    public String deletefile(String filename){
        Path path = Paths.get(workingRootPath, filename);
        if (!path.toFile().exists())
            throw new RuntimeException("Il file non esiste: " + filename);
        return Boolean.toString(path.toFile().delete());
    }

}
