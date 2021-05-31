package com.hadoop.twitterhadoop.all.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

public final class CONSTANTS {
    public static String jar_path = "classpath:jars/";
    public static String output_path = "out/";
    public static String output_name = "out_file";
    public static String most_likes = jar_path + "mp-likes.jar";
    public static String most_rts = jar_path + "mp-retweets.jar";
    public static String most_reps = jar_path + "mp-replies.jar";
    public static String most_times = jar_path + "mp-time.jar";
    public static String length = jar_path + "mp-length.jar";
    public static String dataset_path = "classpath:datasets/";
    public static String default_dataset_name = "tweets.sample.csv";

}
