package com.dapeng.demo.service.image.imp;

import com.dapeng.demo.base.FileReadUtil;
import com.dapeng.demo.service.image.GeneratePicService;
import com.dapeng.demo.utils.DataPatten;
import com.dapeng.demo.utils.GeneratePatten;
import com.dapeng.demo.utils.TextSpiltUtil;
import com.dapeng.demo.wrapper.create.ImgCreateOptions;
import com.dapeng.demo.wrapper.create.ImgCreateWrapper;
import org.apache.commons.collections4.ListUtils;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.TagException;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class GeneratePicServiceImpl implements GeneratePicService {


    @Override
    public String readFileMessage(String path, String filename) {
        return null;
    }

    @Override
    public String setTextFormat(DataPatten patten, String text) {
        return null;
    }

    @Override
    public Map<String,Object> generatePic(GeneratePatten patten) throws IOException, ReadOnlyFileException, TagException, InvalidAudioFrameException, CannotReadException {
        int w = 400;
        int leftPadding = 10;
        int topPadding = 20;
        int bottomPadding = 10;
        int linePadding = 10;
        Font font = new Font("手札体", Font.PLAIN, 18);
        Map<String,Object> map = new HashMap<>();

//        BufferedReader reader = FileReadUtil.createLineRead("text/poem.txt");
//        String line;
//        java.util.List<String> content = new ArrayList<>();
//        while ((line = reader.readLine()) != null) {
//            content.add(line);
//        }
        Integer allText = countTextNum(patten.getTextMessage());
        Integer mp3Time = getMp3Times(patten.getMp3Path());
//        List<List<String>> sprateList = ListUtils.partition(content, 20);

        List<String> sprateList = TextSpiltUtil.getStrList(patten.getTextMessage(),patten.getSinglePageTextNum());

        AtomicReference<Integer> solder = new AtomicReference<>(1);
        sprateList.forEach(c -> {

            ImgCreateWrapper.Builder build = ImgCreateWrapper.build()
                    .setImgW(w)
                    .setLeftPadding(leftPadding)
                    .setRightPadding(leftPadding)
                    .setTopPadding(topPadding)
                    .setBottomPadding(bottomPadding)
                    .setLinePadding(linePadding)
                    .setFont(font)
                    .setAlignStyle(ImgCreateOptions.AlignStyle.CENTER)
                    .setDrawStyle(ImgCreateOptions.DrawStyle.HORIZONTAL)
                    .setBgColor(Color.WHITE)
                    .setBorder(true)
                    .setBorderColor(0xFFF7EED6);
           build.drawContent(c);

            build.setAlignStyle(ImgCreateOptions.AlignStyle.RIGHT).drawImage(patten.getBasePicName());
            BufferedImage img = build.asImage();
            try {
                Integer showTime = GeneralTime(mp3Time, allText, countTextNum(c));
                String path =patten.getSavePath() ;
                String full_name = path + solder + "-" + showTime+"."+patten.getSuffix();
                ImageIO.write(img, patten.getSuffix(), new File(full_name));
                map.put("sort"+solder.toString(),full_name);
                solder.getAndSet(solder.get() + 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return map;
    }

    private Integer getMp3Times(String path) throws TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException, IOException {
        File file = new File(path);
        try {
            MP3File f = (MP3File) AudioFileIO.read(file);
            MP3AudioHeader audioHeader = (MP3AudioHeader) f.getAudioHeader();
            return audioHeader.getTrackLength();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private Integer countTextNum(String c) {
        return c.length();
//        return c.stream().map(s -> s.length()).reduce(0, Integer::sum);
    }

    private Integer GeneralTime(Integer totalTime, Integer totalText, Integer currentText) {
        double unitTime = Double.parseDouble(totalTime.toString()) / Double.parseDouble(totalText.toString());
        Double longTime = unitTime * currentText;
        return longTime.intValue();
    }
}