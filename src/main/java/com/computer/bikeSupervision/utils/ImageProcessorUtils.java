package com.computer.bikeSupervision.utils;

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
    private static final String TEMPLATE_IMAGE_PATH = "F:\\Image\\机设\\通行证模板.png";

    public MultipartFile generatePassImage(String licensePlate, String passNumber, MultipartFile qrImage) throws IOException {
        // 读取本地图片模板
        BufferedImage templateImage = ImageIO.read(new File(TEMPLATE_IMAGE_PATH));
        // 读取二维码图片
        BufferedImage qrBufferedImage = ImageIO.read(qrImage.getInputStream());

        // 定义最大宽度和高度，这里以模板宽度的三分之一为例
        int maxWidth = templateImage.getWidth() / 3;
        int maxHeight = templateImage.getHeight() / 3;

        // 计算缩放比例
        double widthScale = (double) maxWidth / qrBufferedImage.getWidth();
        double heightScale = (double) maxHeight / qrBufferedImage.getHeight();
        double scale = Math.min(widthScale, heightScale);

        // 计算缩放后的尺寸
        int scaledWidth = (int) (qrBufferedImage.getWidth() * scale);
        int scaledHeight = (int) (qrBufferedImage.getHeight() * scale);

        // 创建缩放后的二维码图片
        BufferedImage scaledQrImage = new BufferedImage(scaledWidth, scaledHeight, qrBufferedImage.getType());
        Graphics2D qrG2d = scaledQrImage.createGraphics();
        qrG2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        qrG2d.drawImage(qrBufferedImage, 0, 0, scaledWidth, scaledHeight, null);
        qrG2d.dispose();

        Graphics2D g2d = templateImage.createGraphics();

        // 开启抗锯齿功能，使字体边缘更平滑
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 开启字体渲染的分数度量，提高字体渲染质量
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        // 使用支持中文的字体，例如宋体
        Font font = new Font("宋体", Font.BOLD, 20);
        g2d.setFont(font);
        g2d.setColor(Color.BLACK);

        // 在图片的固定位置添加车牌号和通行证号
        g2d.drawString(licensePlate, 345, 210);
        g2d.drawString(passNumber, 159, 210);

        // 计算二维码图片放置的位置（右上角）
        int x = templateImage.getWidth() - scaledWidth;
        int y = 0;
        // 在模板图片上绘制缩放后的二维码图片
        g2d.drawImage(scaledQrImage, x, y, null);

        g2d.dispose();

        // 将 BufferedImage 转换为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(templateImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        // 使用 UUID 生成唯一的文件名
        String fileName = UUID.randomUUID() + ".png";

        // 创建一个基于字节数组的 FileItem
        DiskFileItem fileItem = new DiskFileItem(fileName, "image/png", false, fileName, imageBytes.length, null);
        fileItem.getOutputStream().write(imageBytes);

        // 通过 FileItem 创建 MultipartFile
        return new CommonsMultipartFile(fileItem);
    }

    public void saveImage(BufferedImage image, String outputPath) throws IOException {
        File output = new File(outputPath);
        ImageIO.write(image, "png", output);
    }
}