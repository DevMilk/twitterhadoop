package com.hadoop.twitterhadoop.all.services;

import com.hadoop.twitterhadoop.all.dto.Settings;

import java.io.File;
import java.io.IOException;


public interface HadoopService {
    public String executeCommand(Settings settings);
    public File downloadLastResult() throws IOException;
    public String uploadIntoHDFS(String file_path);

}
