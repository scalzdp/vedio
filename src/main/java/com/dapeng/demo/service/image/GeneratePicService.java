package com.dapeng.demo.service.image;

import com.dapeng.demo.utils.DataPatten;
import com.dapeng.demo.utils.GeneratePatten;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface GeneratePicService {
    //生成图片文字
    /**
     * 读取指定文件的内容
     * @param path 文件存放路径
     * @param filename 文件名称
     * @return 文件内容
     * */
    String readFileMessage(String path, String filename);

    /**
     *根据指定规则将制定的文本进行整理
     * @param patten 文件整理规则
     * @param text 待整理的内容
     * @return 整理之后的文本
     * */
    String setTextFormat(DataPatten patten, String text);

    /**
     *根据指定规则将文本进行生成为指定图片
     * @param patten 图片生成规则
     * @return 图片名称以及存放地址
     * */
    Map<String,Object> generatePic(GeneratePatten patten) throws IOException, ReadOnlyFileException, TagException, InvalidAudioFrameException, CannotReadException;
}
