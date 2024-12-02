package com.example;


import com.example.service.impl.MinioFileService;
import io.minio.MinioClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class MinioTests {
    private static final MinioFileService fileService = BeanUtil.getBean(MinioFileService.class);

    public static void testMinio1() throws Exception {
        assert fileService != null;
        MinioClient minioClient = fileService.getMinioClient();
        InputStream pic = minioClient.getObject("archived", "1/BV11J4m1G7tJ/0.jpg");
        BufferedImage sourceImg = ImageIO.read(pic);
        int width = sourceImg.getWidth();
        int height = sourceImg.getHeight();
        System.out.println(width + "," + height);
    }

    public static void testMinio2() throws Exception {
        String url = "archived/1/BV11J4m1G7tJ/0.jpg";
        assert fileService != null;
        String imageWidthHeight = fileService.getImageWidthHeight(url);
        System.out.println(imageWidthHeight);
    }
}
