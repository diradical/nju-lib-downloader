package object;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * 重命名之前版本程序下载的文件。
 * 之前版本命名不合理。
 *
 * @author padeoe
 * @Date: 2016/12/13
 */
public class FileRenamer {
    public static void main(String args[]) {
        renameSuffix("G:\\Book");
        renameSuffix("G:\\Book");
    }

    public static void renameZero(String rootDirPath) {
        Path root = Paths.get(rootDirPath);
        File rootDir = root.toFile();
        if (rootDir.isDirectory()) {
            File dirs[] = rootDir.listFiles();
            Arrays.asList(dirs).parallelStream().forEach(dir -> handleEndDir(dir));
        } else {
            System.out.println("根目录不是目录，终止");
        }
    }

    public static void handleEndDir(File dir) {
        if (dir.isDirectory()) {
            System.out.println("正在处理" + dir.getName());
            File files[] = dir.listFiles();
            for (File file : files) {
                String name = file.getName();
                if (name.endsWith("png")) {
                    String prefix = name.substring(0, name.indexOf('.'));
                    name = name.replaceAll(prefix, String.format("%04d", Integer.parseInt(prefix)));
                    try {
                        Files.move(file.toPath(), new File(dir.getPath() + "\\" + name).toPath());
                    } catch (IOException e) {
                        System.out.println(file.toString());
                    }
                }
            }
        } else {
            System.out.println(dir.getName() + "不是目录，跳过");
        }
    }

    public static void renameSuffix(String rootDirPath) {
        Path root = Paths.get(rootDirPath);
        File rootDir = root.toFile();
        if (rootDir.isDirectory()) {
            File dirs[] = rootDir.listFiles();
            Arrays.asList(dirs).parallelStream().forEach(dir -> imageEndDir(dir));
        } else {
            System.out.println("根目录不是目录，终止");
        }
    }

    public static void imageEndDir(File dir) {
        if (dir.isDirectory()) {
            System.out.println("正在处理" + dir.getName());
            File files[] = dir.listFiles();
            for (File file : files) {
                String name = file.getName();
                String prefix = name.substring(0, name.indexOf('.'));
                String trueSuffix = getImageSuffix(file);
                if ((name.endsWith("png") || name.endsWith("jpg")) && trueSuffix != null && !name.endsWith(trueSuffix)) {
                    name = prefix + "." + trueSuffix;
                    //  System.out.println("需要修改为"+name);
                    try {
                        Files.move(file.toPath(), new File(dir.getPath() + "\\" + name).toPath());
                    } catch (IOException e) {
                        System.out.println("修改出错" + file.toString());
                    }
                }
            }
        } else {
            System.out.println(dir.getName() + "不是目录，跳过");
        }
    }


    public static String getImageSuffix(File image) {
        FileInputStream fileInputStream = null;
        InputStream inputStream;
        try {
            fileInputStream = new FileInputStream(image);
            inputStream = fileInputStream;
            byte[] array = new byte[10];
            inputStream.read(array, 0, 10);
            if (array[6] == 'J' && array[7] == 'F' && array[8] == 'I' && array[9] == 'F') {
                inputStream.close();
                return "jpg";
            } else {
                inputStream.close();
                return "png";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}