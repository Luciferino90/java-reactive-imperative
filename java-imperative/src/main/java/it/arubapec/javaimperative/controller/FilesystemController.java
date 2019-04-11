package it.arubapec.javaimperative.controller;

import it.arubapec.javaimperative.service.FilesystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FilesystemController {

    private final FilesystemService filesystemService;

    @GetMapping("/create/{filename}")
    public String createfile(@PathVariable("filename") String filename){
        return filesystemService.createfile(filename);
    }

    @GetMapping("/delete/{filename}")
    public String deletefile(@PathVariable("filename") String filename){
        return filesystemService.deletefile(filename);
    }

}
