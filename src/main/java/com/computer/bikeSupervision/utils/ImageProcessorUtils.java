package com.computer.bikeSupervision.utils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class ImageProcessorUtils {
    private static final String TEMPLATE_IMAGE_PATH = "path/to/your/template.png";

    public MultipartFile generatePassImage(String licensePlate, String passNumber) throws IOException {
        // 读取本地图片模板
        BufferedImage templateImage = ImageIO.read(new File(TEMPLATE_IMAGE_PATH));

        Graphics2D g2d = templateImage.createGraphics();
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.setColor(Color.BLACK);

        // 在图片的固定位置添加车牌号和通行证号
        g2d.drawString("车牌号: " + licensePlate, 50, 50);
        g2d.drawString("通行证号: " + passNumber, 50, 100);

        g2d.dispose();

        // 将 BufferedImage 转换为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(templateImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        // 使用 UUID 生成唯一的文件名
        String fileName = UUID.randomUUID() + ".png";

        // 创建一个基于字节数组的 FileItem

        /*
        DiskFileItemFactory factory = new DiskFileItemFactory();
        DiskFileItem fileItem = (DiskFileItem) factory.createItem("passImage", "image/png", false, fileName);
        fileItem.getOutputStream().write(imageBytes);
        */

        FileItem fileItem = new DiskFileItem(fileName, "image/png", false, fileName, imageBytes.length, null);
        fileItem.getOutputStream().write(imageBytes);

        // 通过 FileItem 创建 MultipartFile
        return new CommonsMultipartFile(fileItem);
    }

    public void saveImage(BufferedImage image, String outputPath) throws IOException {
        File output = new File(outputPath);
        ImageIO.write(image, "png", output);
    }
}