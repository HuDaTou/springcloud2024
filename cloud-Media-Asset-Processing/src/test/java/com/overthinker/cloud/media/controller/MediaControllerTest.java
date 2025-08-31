package com.overthinker.cloud.media.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 本地文件上传参数生成器。
 * <p>
 * 用于为分片上传接口准备所有必需的参数。
 * 只需修改 main 方法中的 filePath 变量，然后运行即可。
 */
public class MediaControllerTest {

    /**
     * --- 用户配置区域 ---
     */
    // 定义每个分片的大小 (这里设置为5MB)
    private static final long CHUNK_SIZE = 5 * 1024 * 1024;

    public static void main(String[] args) {
        // *******************************************************************
        // * 请在这里填入您要上传的本地文件的完整路径
        // * 例如: "D:\\videos\\my-test-video.mp4" (Windows)
        // * 或: "/home/user/videos/my-test-video.mp4" (Linux/Mac)
        // *******************************************************************
        String filePath = "Z://OneDrive - MSFT/视频/SteelSeries Moments/Apex-Legends__2025-01-18__12-52-23.mp4";

        File file = new File(filePath);

        if (!file.exists() || !file.isFile()) {
            System.err.println("错误：文件未找到或路径不是一个有效文件。");
            System.err.println("请检查路径：" + filePath);
            return;
        }

        try {
            // 1. 获取文件名
            String filename = file.getName();

            // 2. 获取文件大小
            long fileSize = file.length();

            // 3. 计算总分片数
            int totalParts = (int) Math.ceil((double) fileSize / CHUNK_SIZE);
            if (totalParts == 0) {
                totalParts = 1;
            }

            // 4. 探测文件MIME类型
            String contentType = Files.probeContentType(file.toPath());
            if (contentType == null) {
                contentType = "application/octet-stream"; // 默认类型
            }

            // 5. 计算文件MD5
            System.out.println("正在计算文件MD5值，大文件可能需要一些时间...");
            String fileMd5 = calculateMd5(file);
            System.out.println("MD5 计算完成！");


            // 6. 打印所有计算出的参数
            printParameters(filename, fileSize, totalParts, contentType, fileMd5);

        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println("处理文件时出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 计算文件的MD5哈希值。
     *
     * @param file 要计算的文件
     * @return 文件的MD5哈希值 (小写)
     * @throws NoSuchAlgorithmException 如果MD5算法不可用
     * @throws IOException            如果读取文件失败
     */
    private static String calculateMd5(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (FileInputStream fis = new FileInputStream(file);
             DigestInputStream dis = new DigestInputStream(fis, md)) {
            // 通过读取整个文件来更新摘要，缓冲区大小不影响最终结果
            byte[] buffer = new byte[8192];
            while (dis.read(buffer) != -1) ;
        }
        byte[] digest = md.digest();
        return bytesToHex(digest);
    }

    /**
     * 将字节数组转换为十六进制字符串。
     *
     * @param hash 字节数组
     * @return 十六进制字符串
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 格式化并打印参数。
     */
    private static void printParameters(String filename, long fileSize, int totalParts, String contentType, String fileMd5) {
        System.out.println("\n========================================================================");
        System.out.println(" 文件上传初始化所需参数已生成");
        System.out.println("========================================================================\n");
        System.out.printf("%-15s: %s%n", "filename", filename);
        System.out.printf("%-15s: %d (%.2f MB)%n", "fileSize", fileSize, (double) fileSize / (1024 * 1024));
        System.out.printf("%-15s: %s%n", "contentType", contentType);
        System.out.printf("%-15s: %s%n", "fileMd5", fileMd5);
        System.out.printf("%-15s: %d (based on %.2f MB/chunk)%n", "totalParts", totalParts, (double) CHUNK_SIZE / (1024 * 1024));
        System.out.println("\n========================================================================");
        System.out.println(" 您现在可以使用这些参数去调用 /media/initiate 接口了。");
        System.out.println("========================================================================\n");
    }
}