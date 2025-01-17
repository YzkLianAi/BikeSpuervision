package com.computer.bikeSupervision.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class QRCodeGenerator {

    /**
     * 生成二维码
     * @param content
     * @return
     * @throws WriterException
     * @throws IOException
     */
    public MultipartFile generateQRCodeAsMultipartFile(String content) throws WriterException, IOException {
        // 设置二维码的参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        // 创建QRCodeWriter对象
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        // 生成二维码矩阵
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);

        // 将矩阵转换为BufferedImage
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // 将BufferedImage转换为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        // 使用UUID生成唯一的文件名
        String fileName = UUID.randomUUID() + ".png";

        // 创建一个基于字节数组的FileItem
        FileItem fileItem = new DiskFileItem(fileName, "image/png", false, fileName, imageBytes.length, null);
        fileItem.getOutputStream().write(imageBytes);

        // 通过FileItem创建MultipartFile
        return new CommonsMultipartFile(fileItem);
    }


    /**
     * 解析二维码
     * @param qrCodeImage
     * @return
     * @throws IOException
     * @throws NotFoundException
     * @throws FormatException
     * @throws ChecksumException
     */
    public String parseQRCodeData(MultipartFile qrCodeImage) throws IOException, NotFoundException, FormatException, ChecksumException {
        // 将MultipartFile转换为BufferedImage
        BufferedImage bufferedImage = ImageIO.read(qrCodeImage.getInputStream());

        // 创建二维码读取器
        QRCodeReader qrCodeReader = new QRCodeReader();

        // 将BufferedImage转换为可被读取器识别的格式
        BufferedImageLuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedImage);

        HybridBinarizer binarization = new HybridBinarizer(luminanceSource);

        BinaryBitmap binaryBitmap = new BinaryBitmap(binarization);

        // 读取二维码内容
        Result result = qrCodeReader.decode(binaryBitmap);

        return result.getText();
    }
}