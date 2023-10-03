package com.javajober.core.config;

import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class FileDirectoryConfig {

    public String getDirectoryPath() {

        String separator = File.separator;  // OS별 separator
        LocalDate today = LocalDate.now();

        String directoryName = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));   // 20230928
        String directoryPath = System.getProperty("user.home") + separator + "JavaJober" + separator +
                directoryName + separator;
        /**
         * OS 별 directoryPath 예시
         * (window) : C:\Users\[username]\JavaJober\20230928\
         * (max) : /Users/[username]/JavaJober/20230928/
         * (Linux) : /home/[username]/JavaJober/20230928/
         */
        createDirectory(directoryPath);
        return directoryPath;
    }

    public void createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        // 디렉토리가 없으면 생성
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}
