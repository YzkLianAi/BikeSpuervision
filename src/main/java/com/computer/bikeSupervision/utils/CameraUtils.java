package com.computer.bikeSupervision.utils;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class CameraUtils {
    @Autowired
    AliOSSUtils aliOSSUtils;

    public BufferedImage captureImageFromCamera() throws Exception {
        // 创建一个 OpenCVFrameGrabber 对象，0 表示默认摄像头
        OpenCVFrameGrabber grabber = new OpenCVFrameGrabber(0);
        try {
            // 开始抓取帧
            grabber.start();
            // 抓取一帧图像
            Frame frame = grabber.grab();
            // 将帧转换为 BufferedImage
            Java2DFrameConverter converter = new Java2DFrameConverter();

            BufferedImage convert = converter.convert(frame);

            /*// 将BufferedImage转换为字节数组
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(convert, "png", baos);
            byte[] imageBytes = baos.toByteArray();

            // 使用UUID生成唯一的文件名
            String fileName = UUID.randomUUID() + ".png";

            // 创建一个基于字节数组的FileItem
            FileItem fileItem = new DiskFileItem(fileName, "image/png", false, fileName, imageBytes.length, null);
            fileItem.getOutputStream().write(imageBytes);

            // 通过FileItem创建MultipartFile
            CommonsMultipartFile commonsMultipartFile = new CommonsMultipartFile(fileItem);
            aliOSSUtils.upload(commonsMultipartFile);*/

            return convert;
        } finally {
            try {
                // 停止抓取并释放资源
                grabber.stop();
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }
    }
}
