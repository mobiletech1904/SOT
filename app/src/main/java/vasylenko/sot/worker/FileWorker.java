package vasylenko.sot.worker;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Класс для работы с файлами
 * Created by Тёма on 24.06.2017.
 * @version 1.0
 */
public class FileWorker {
    /**
     * Метот читает файл и записывает содержимое в строку
     * @param inputFile входной файл
     * @throws java.io.IOException если возникли ошибки в процессе работы с файлом
     * @return возвращает содержимое файла в виде строки
     */
    public String getFileContent(File inputFile)throws IOException{
        String pathToFile = inputFile.getAbsolutePath();

        FileInputStream fileInputStream = new FileInputStream(pathToFile);
        byte[] bytes = new byte[fileInputStream.available()];
        fileInputStream.read(bytes);
        String textFromFile = new String (bytes);

        return textFromFile;
    }

    /**
     * Метод создания папки в указанной директории, если ее не существует
     * @param path путь к директории
     */
    public static void createNewFile(String path){
        File newFolder = new File(path);

        if (!newFolder.exists()) newFolder.mkdir();
    }
}
