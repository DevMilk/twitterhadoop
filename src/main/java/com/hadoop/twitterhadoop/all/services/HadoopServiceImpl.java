package com.hadoop.twitterhadoop.all.services;

import com.hadoop.twitterhadoop.all.domain.CONSTANTS;
import com.hadoop.twitterhadoop.all.domain.OPTIONS;
import com.hadoop.twitterhadoop.all.dto.Settings;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
@Service
public class HadoopServiceImpl implements HadoopService{
    private void downloadFromHDFS(File localDir) {
        String command = "hdfs dfs -get " + CONSTANTS.output_path + "outt" + " " + localDir;
        System.out.println(command);
        try {
            // Execute terminal command
            Process proc = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            proc.waitFor();
            System.out.println("Downloaded.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void removeOlderFile(File file) {
        String command = "hdfs dfs -rm /" + file.getName();
        System.out.println(command);
        try {
            // Execute terminal command
            Process proc = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            proc.waitFor();
            System.out.println("Deleted.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    private String uploadIntoHDFS(File file) {
        removeOlderFile(file);
        String command = "hdfs dfs -put " + file.toString() + " /";
        System.out.println(command);
        try {
            // Execute terminal command
            Process proc = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            proc.waitFor();
            System.out.println("Uploaded.");
            return "/" + file.getName();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
    private String getResults(String output_path) {
        String command = "hdfs dfs -cat " + output_path;
        try {
            // Execute terminal command
            Process proc = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = "", output = "";
            while ((line = reader.readLine()) != null) {
                output += (line + "\n");
            }
            proc.waitFor();

            return output;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "None";
    }
    @Override
    public String executeCommand(Settings settings) {
        String dataset_path = settings.getDataset_name();
        Runtime runtime = Runtime.getRuntime();
        OPTIONS option = settings.getOption();
        String jar_path = option.path;
        String full_out_path = (CONSTANTS.output_path + "outt");
        String arg = "";
        try {
            arg = Settings.class.getDeclaredField(option.argType).get(settings).toString();
            File file = new File(dataset_path);
            String hdfs_path = uploadIntoHDFS(file);
            String command ="hadoop jar " + jar_path + " " + hdfs_path + " " + full_out_path + " " + arg;
            Process proc = runtime.exec(command);
            return getResults(full_out_path);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "None";
    }
}
