package cn.ksmcbrigade.ysm_ws;

import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.Nullable;
import oshi.util.FileUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Utils {

    public static int moveYSGPContentFiles(String sourceDirectory, String destinationDirectory) throws IOException {
        final File[] files = getFiles(sourceDirectory, destinationDirectory);

        int count = 0;
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        boolean t = isFileContentStartsWithYSGP(file);
                        if (t || file.getName().toLowerCase().endsWith(".zip")) {
                            File rFile = file;
                            if(t){
                                File nFile = new File(file.getPath().substring(0, file.getPath().length() - 3) + "ysm");
                                file.renameTo(nFile);
                                rFile = nFile;
                                YSMWorkshop.LOGGER.info("Renamed the file {} to {}",file.getName(),nFile.getName());
                            }
                            Path sourcePath = Paths.get(rFile.getAbsolutePath());
                            Path destinationPath = Paths.get(destinationDirectory, rFile.getName());

                            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                            count++;
                            YSMWorkshop.LOGGER.info("Moved file: {} to {}", rFile.getName(), destinationDirectory);
                        }
                    } catch (IOException e) {
                        System.err.println("Failed to process file " + file.getName() + ": " + e.getMessage());
                    }
                }
            }
        } else {
            YSMWorkshop.LOGGER.info("Source directory is empty: {}", sourceDirectory);
        }
        return count;
    }

    private static File @Nullable [] getFiles(String sourceDirectory, String destinationDirectory) throws IOException {
        File sourceDir = new File(sourceDirectory);
        File destDir = new File(destinationDirectory);

        if (!sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new IllegalArgumentException("Source directory does not exist or is not a directory: " + sourceDirectory);
        }

        if (!destDir.exists()) {
            if (!destDir.mkdirs()) {
                throw new IOException("Failed to create destination directory: " + destinationDirectory);
            }
        } else if (!destDir.isDirectory()) {
            throw new IllegalArgumentException("Destination directory is not a directory: " + destinationDirectory);
        }

        File[] files = sourceDir.listFiles();
        return files;
    }

    private static boolean isFileContentStartsWithYSGP(File file) throws IOException {
        byte[] b = new byte[16];
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            inputStream.read(b, 0, 16);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
        return new String(b).contains("YSGP");
    }
}
