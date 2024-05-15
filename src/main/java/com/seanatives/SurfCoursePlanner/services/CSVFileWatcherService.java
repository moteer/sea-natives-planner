package com.seanatives.SurfCoursePlanner.services;

import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.*;

@Service
public class CSVFileWatcherService {

    public Path waitForFileToBeWritten(String directoryPath, String fileNamePrefix) {
        File latestFile = null;
        try {
            Path dir = Paths.get(directoryPath);
            File[] files = dir.toFile().listFiles((d, name) -> name.startsWith(fileNamePrefix));

            if (files != null && files.length > 0) {
                latestFile = files[0];
                for (File file : files) {
                    if (file.lastModified() > latestFile.lastModified()) {
                        latestFile = file;
                    }
                }

                long lastModifiedTime = latestFile.lastModified();
                long fileSize = latestFile.length();

                while (true) {
                    System.out.println("wait for file ...");
                    Thread.sleep(1000); // Kleine Verzögerung zur Vermeidung von intensiver CPU-Nutzung
                    long newLastModifiedTime = latestFile.lastModified();
                    long newFileSize = latestFile.length();

                    if (newLastModifiedTime == lastModifiedTime && newFileSize == fileSize && fileSize > 0) {
                        System.out.println("File write has completed for: " + latestFile.getName());
                        break;
                    } else {
                        lastModifiedTime = newLastModifiedTime;
                        fileSize = newFileSize;
                    }
                }

                System.out.println("File is ready to be processed: " + latestFile.getName());
            } else {
                System.out.println("No 'bookings' files found in the directory.");
            }
        } catch (InterruptedException e) {
            System.err.println("Thread was interrupted: " + e.getMessage());
        }
        return latestFile.toPath();
    }
}
