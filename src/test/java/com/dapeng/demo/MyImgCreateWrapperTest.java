package com.dapeng.demo;

import com.dapeng.demo.base.FileReadUtil;
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
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MyImgCreateWrapperTest {

    @Test
    public void testMutiPng() throws IOException, ReadOnlyFileException, TagException, InvalidAudioFrameException, CannotReadException {
        int w = 400;
        int leftPadding = 10;
        int topPadding = 20;
        int bottomPadding = 10;
        int linePadding = 10;
        Font font = new Font("手札体", Font.PLAIN, 18);



        BufferedReader reader = FileReadUtil.createLineRead("text/poem.txt");
        String line;
        List<String> content = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            content.add(line);
        }
        Integer allText = countTextNum(content);
        Integer mp3Time = getMp3Times();
        List<List<String>> sprateList = ListUtils.partition(content,20);
        AtomicReference<Integer> solder = new AtomicReference<>(1);
        sprateList.forEach(c->{

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
            c.forEach(b->build.drawContent(b));

            build.setAlignStyle(ImgCreateOptions.AlignStyle.RIGHT).drawImage("D:\\background.png");
            BufferedImage img = build.asImage();
            try {
                ImageIO.write(img, "png", new File("D:\\files\\"+solder+"_"+GeneralTime(mp3Time,allText,countTextNum(c))+"out.png"));
                solder.getAndSet(solder.get() + 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Integer getMp3Times() throws TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException, IOException {
        File file = new File("E:\\03environment\\ffmpeg\\bin\\test3.mp3");
        try {
            MP3File f = (MP3File) AudioFileIO.read(file);
            MP3AudioHeader audioHeader = (MP3AudioHeader)f.getAudioHeader();
            return audioHeader.getTrackLength();
        } catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private Integer countTextNum(List<String> c){
        return c.stream().map(s->s.length()).reduce(0,Integer::sum);
    }

    private Integer GeneralTime(Integer totalTime,Integer totalText,Integer currentText){
      double unitTime = Double.parseDouble(totalTime.toString())/Double.parseDouble(totalText.toString());
      Double longTime =  unitTime*currentText;
      return longTime.intValue();
    }


}
