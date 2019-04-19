package com.dapeng.demo.controller;

import com.dapeng.demo.service.image.GeneratePicService;
import com.dapeng.demo.utils.GeneratePatten;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ImageController {

    @Autowired
    private GeneratePicService generatePicService;

    @RequestMapping("/welcome")
    public String home(@RequestParam String name) {
        return name+" 欢迎您，这里有一些话想对你说:    ";
    }

    @RequestMapping("/getImage")
    @ResponseBody
    public Map<String,Object> getBufferedImage(@RequestBody GeneratePatten generatePatten) throws ReadOnlyFileException, CannotReadException, TagException, InvalidAudioFrameException, IOException {
        //进行参数的验证。
        Map<String,Object> errorMap = new HashMap<>();
        if(generatePatten.getSuffix()==null){
            errorMap.put("suffix_error","未指定生成文件的后缀");
        }
        if(generatePatten.getTextMessage()==null){
            errorMap.put("text_error","未指定生成文件的文字内容");
        }
        if(generatePatten.getSavePath()==null){
            errorMap.put("savepath_error","未指定生成图片存放地址");
        }
        if(generatePatten.getSinglePageTextNum()==null){
            errorMap.put("textnum_error","未指定每张图片需要保存文字个数");
        }
        if(errorMap.keySet().size()>0){
            //如果检查存在错误信息，将错误收集的HashMap进行返回
            return errorMap;
        }else {
            //调用正确图像生成方法，进行图像生成。
            return generatePicService.generatePic(generatePatten);
        }
    }
}
