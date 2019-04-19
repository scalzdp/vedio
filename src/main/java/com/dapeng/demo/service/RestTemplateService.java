package com.dapeng.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

//import org.springframework.cloud.client.loadbalancer.LoadBalanced;

@Service
public class RestTemplateService {

    @Autowired
    private RestTemplate restTemplate;


//    @LoadBalanced
//    @Autowired(required = false)
//    private List<RestTemplate> restTemplates = Collections.emptyList();

//    String url = "http://www.zgshige.com/upload/resources/audio/2019/04/03/278038.mp3";

    final String APPLICATION_PDF = "application/pdf";

    public void crawl(String url,String storepath,String filename) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            List list = new ArrayList<>();
//            list.add(MediaType.valueOf(APPLICATION_PDF));
            headers.setAccept(list);
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<byte[]>(headers),
                    byte[].class);
            byte[] result = response.getBody();
            inputStream = new ByteArrayInputStream(result);

            File file = new File(storepath+filename);
            if (!file.exists()) {
                file.createNewFile();
            }

            outputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
       }
    }

}
