package com.dapeng.demo.utils;

import lombok.Data;

@Data
public class GeneratePatten {
    private String textMessage;
    private Integer singlePageTextNum;
    private Boolean hasBasePic = true;
    private String basePicName;
    private Boolean hasMp3 = true;
    private String mp3Path;
    private String savePath;
    private String suffix;
}
