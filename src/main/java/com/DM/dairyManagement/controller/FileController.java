package com.DM.dairyManagement.controller;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uploads")
public class FileController {

    @GetMapping("/{fileName:.+}")
    public Resource getFile(@PathVariable String fileName) throws IOException {
        return new UrlResource(Paths.get(System.getProperty("user.dir") + "/uploads/" + fileName).toUri());
    }
}
