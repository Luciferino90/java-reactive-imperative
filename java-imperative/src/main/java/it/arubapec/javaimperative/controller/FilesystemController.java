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

    @GetMapping("/create/{filename:.+}")
    public String createfile(@PathVariable("filename") String filename){
        String res = filesystemService.createfile(filename);
        return res;
    }

    @GetMapping("/create/noerror/{filename:.+}")
    public String createfileManagedError(@PathVariable("filename") String filename){
        String res = filesystemService.createfileManagedError(filename);
        return res;
    }

    @GetMapping("/delete/{filename:.+}")
    public String deletefile(@PathVariable("filename") String filename){
        String res = filesystemService.deletefile(filename);
        return res;
    }

}
