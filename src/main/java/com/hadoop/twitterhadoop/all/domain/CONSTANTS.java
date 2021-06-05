package com.hadoop.twitterhadoop.all.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

public final class CONSTANTS {
    public static String jar_path = "jars/";
    public static String output_path = "out/";
    public static String output_name = "part-r-00000";
    public static String full_output_path_hdfs = output_path+ output_name;
    public static String most_likes = jar_path + "mp-likes.jar";
    public static String most_rts = jar_path + "mp-retweets.jar";
    public static String most_reps = jar_path + "mp-replies.jar";
    public static String most_times = jar_path + "mp-time.jar";
    public static String length = jar_path + "mp-length.jar";
    public static String dataset_path = "datasets/";
    public static String default_dataset_path = dataset_path + "tweets.sample.csv";
    public static Boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

}
