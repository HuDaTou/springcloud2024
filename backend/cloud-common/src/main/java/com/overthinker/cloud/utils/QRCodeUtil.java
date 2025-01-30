//package com.overthinker.cloud.utils;
//
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.WriterException;
//import com.google.zxing.client.j2se.MatrixToImageWriter;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
//import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.Base64;
//import java.util.EnumMap;
//import java.util.Map;
//
///**
// * @author Mr.M
// * @version 1.0
// * @description 二维码生成工具
// * @date 2022/10/3 0:03
// */
//public class QRCodeUtil {
//    /**
//     * 生成二维码
//     *
//     * @param content 二维码对应的URL
//     * @param width   二维码图片宽度
//     * @param height  二维码图片高度
//     * @return
//     */
//    public String createQRCode(String content, int width, int height) throws IOException {
//        // 传入内容不能为空
//        if (content == null || content.isEmpty()) {
//            throw new IllegalArgumentException("Content cannot be null or empty");
//        }
//
//        // 二维码参数
//        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
//        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
//        hints.put(EncodeHintType.MARGIN, 1);
//
//        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
//            // zxing生成二维码核心类
//            QRCodeWriter writer = new QRCodeWriter();
//            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
//            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
//
//            // 将图片写入输出流
//            ImageIO.write(bufferedImage, "png", os);
//
//            // 将图片字节流转换为Base64编码的字符串
//            return "data:image/png;base64," + Base64.getEncoder().encodeToString(os.toByteArray());
//        } catch (WriterException e) {
//            throw new IOException("Failed to generate QR code", e);
//        }
//    }
//
//
//    public static void main(String[] args) throws IOException {
//        QRCodeUtil qrCodeUtil = new QRCodeUtil();
//        System.out.println(qrCodeUtil.createQRCode("http://www.baidu.com", 200, 200));
//    }
//
//}