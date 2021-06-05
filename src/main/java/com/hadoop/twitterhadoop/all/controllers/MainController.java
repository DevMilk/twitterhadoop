package com.hadoop.twitterhadoop.all.controllers;


import com.hadoop.twitterhadoop.all.domain.CONSTANTS;
import com.hadoop.twitterhadoop.all.dto.Settings;
import com.hadoop.twitterhadoop.all.services.HadoopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    @PostMapping("/get_results")
    public ResponseEntity<String> getResults(@RequestBody @Valid Settings settings ) throws FileNotFoundException, IOException {

         if(settings.getDataset_path()==null)
             settings.setDataset_path(resourceLoader.getResource("classpath:"+CONSTANTS.default_dataset_path).getFile().getPath());
        System.out.println(settings.getDataset_path());

         if (!new File(settings.getDataset_path()).exists())
            throw new FileNotFoundException("Dataset Not Found");

        settings.setDataset_path("\"" + settings.getDataset_path() + "\"");
        String results = hadoopService.executeCommand(settings);

        return  ResponseEntity.ok(results);
    }

    @GetMapping("/upload")
    public ResponseEntity uploadFile(String file_path)throws FileNotFoundException{

        if (!new File(file_path).exists())
            throw new FileNotFoundException("Dataset Not Found");
        hadoopService.uploadIntoHDFS(file_path);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/download_results")
    public ResponseEntity downloadResults() throws FileNotFoundException, IOException{
        File file = resourceLoader.getResource("classpath:"+CONSTANTS.output_name).getFile();
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping
    public String index(){
        return "index";
    }
}