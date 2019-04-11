package it.arubapec.javaimperative.service;

import it.arubapec.javaimperative.utils.FilesystemUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class FilesystemService {

    private Integer size = 1024 * 1024 * 1000;

    @Value("${working.root.path}")
    private String workingRootPath;

    @PostConstruct
    public void createTmpDir(){
        new File(workingRootPath).mkdirs();
    }

    public String createfile(String filename){
        Long startTime = new Date().getTime();
        Path path = Paths.get(workingRootPath, filename);
        if (path.toFile().exists())
            throw new RuntimeException("Il file risulta già esistente: " + filename);
        FilesystemUtils.writeRandomFile(path, size);
        return "Durata in ms: " + (new Date().getTime() - startTime);
    }

    public String deletefile(String filename){
        Path path = Paths.get(workingRootPath, filename);
        if (path.toFile().exists())
            throw new RuntimeException("Il file non esiste: " + filename);
        boolean res = path.toFile().delete();
        return new Boolean(res).toString();
    }

}
