package com.hadoop.twitterhadoop.all.controllers;


import com.hadoop.twitterhadoop.all.domain.CONSTANTS;
import com.hadoop.twitterhadoop.all.dto.Settings;
import com.hadoop.twitterhadoop.all.services.HadoopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/api/v1")
public class MainController {
    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    HadoopService hadoopService;

    @GetMapping("/test")
    public void getResults() {

    }
    @PostMapping("/get_results")
    public ResponseEntity<String> getResults(@RequestBody @Valid Settings settings) throws FileNotFoundException {
         System.out.println(CONSTANTS.dataset_path + settings.getDataset_name());
         if (!resourceLoader.getResource(CONSTANTS.dataset_path + settings.getDataset_name()).exists())
            throw new FileNotFoundException("Dataset Not Found In Resources");

        String results = hadoopService.executeCommand(settings);

        return  ResponseEntity.ok(results);
    }

    @PostMapping("/upload")
    public ResponseEntity uploadFile(MultipartFile file) {

        try {
            Path copyLocation = Paths
                    .get(CONSTANTS.dataset_path + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.unprocessableEntity().build();
        }
        return ResponseEntity.ok().build();
    }
}