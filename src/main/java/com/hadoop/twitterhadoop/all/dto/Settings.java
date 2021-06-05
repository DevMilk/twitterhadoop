package com.hadoop.twitterhadoop.all.dto;

import com.hadoop.twitterhadoop.all.domain.CONSTANTS;
import com.hadoop.twitterhadoop.all.domain.OPTIONS;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Settings {

    public String dataset_path;
    public int number_of_top_users = 0;
    public String date = "0";
    public int length = -1;
    public OPTIONS option = OPTIONS.RT; //tmp-length, mp-likes

}
