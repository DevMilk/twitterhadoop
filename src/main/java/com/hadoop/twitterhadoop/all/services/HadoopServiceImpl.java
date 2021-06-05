package com.hadoop.twitterhadoop.all.services;

import com.hadoop.twitterhadoop.all.domain.CONSTANTS;
import com.hadoop.twitterhadoop.all.domain.OPTIONS;
import com.hadoop.twitterhadoop.all.dto.Settings;
import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
@Service
public class HadoopServiceImpl implements HadoopService{

    @Autowired
    ResourceLoader resourceLoader;


    private String runCommand(String command){
        System.out.println(command);
        String out = "";
        try {

            Process process = null;

            if(CONSTANTS.isWindows){
                ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);
                builder.redirectErrorStream(true);
                process = builder.start();
            } else {
                process = Runtime.getRuntime().exec(command);
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                out += (line + "\n");
            }
            System.out.println(out);
            process.waitFor();
            reader.close();
            return out;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getPathFromResources(String path){
        try {
            String absolute_path = "\""+resourceLoader.getResource("classpath:"+path).getFile().getPath() +"\"";
            System.out.println(absolute_path);
            return absolute_path;
        } catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }
    public File downloadLastResult() throws IOException{
        String command = "hdfs dfs -get " + CONSTANTS.full_output_path_hdfs;
        runCommand(command);
        return resourceLoader.getResource("classpath:" + CONSTANTS.output_name).getFile();
    }
    public String uploadIntoHDFS(String file_path) {
        String command = "hdfs dfs -put -f " + file_path + " /"; ;
        runCommand(command);
        return FileNameUtils.getBaseName(file_path)+"."+FileNameUtils.getExtension(file_path).split("\"")[0];
    }
    private String getResults() {
        String command = "hdfs dfs -cat " + CONSTANTS.full_output_path_hdfs;
        return runCommand(command);
    }
    @Override
    public String executeCommand(Settings settings) {
        String dataset_path = settings.getDataset_path();
        OPTIONS option = settings.getOption();
        String jar_path = option.path;
        try {
            String arg = Settings.class.getDeclaredField(option.argType).get(settings).toString();
            String fileNameInHdfs = uploadIntoHDFS(dataset_path);
            String command ="hadoop jar " + getPathFromResources(jar_path) + " /" + fileNameInHdfs
                    + " " + CONSTANTS.output_path + " " + arg;
            runCommand(command);
            return getResults();
        }catch(Exception e){
            e.printStackTrace();
        }
        return "None";
    }
}
