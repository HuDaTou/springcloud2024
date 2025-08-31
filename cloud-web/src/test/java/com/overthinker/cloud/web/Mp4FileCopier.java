package com.overthinker.cloud.web;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class Mp4FileCopier {

    public static void main(String[] args) {
        Path sourceDir = Paths.get("I:\\Program Files (x86)\\Steam\\steamapps\\workshop\\content\\431960");
        Path targetDir = Paths.get("Z:\\OneDrive - MSFT\\视频\\sq");

        try (Stream<Path> fileStream = Files.walk(sourceDir)) {
            fileStream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".mp4"))
                    .forEach(source -> copyFile(source, targetDir));
        } catch (IOException e) {
            System.err.println("遍历文件时发生错误: " + e.getMessage());
        }
    }

    private static void copyFile(Path source, Path targetDir) {
        try {
            // 确保目标目录存在
            Files.createDirectories(targetDir);

            // 构建目标路径（直接使用文件名，忽略源目录结构）
            Path destination = targetDir.resolve(source.getFileName().toString());

            // 复制文件并覆盖已存在的文件
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("已复制: " + source + " -> " + destination);
        } catch (FileAlreadyExistsException e) {
            System.err.println("文件已存在跳过覆盖: " + targetDir.resolve(source.getFileName()));
        } catch (IOException e) {
            System.err.println("复制失败 [" + source + "]: " + e.getMessage());
        }
    }
}