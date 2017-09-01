package vasylenko.sot.worker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.util.ArrayList;

/**
 * Класс для работы с HTML-файлами
 * Created by Тёма on 24.06.2017.
 * @version 1.0
 */
public class HtmlWorker {
    /**
     * Метод создает результирующий HTML шаблон из полученного списка вкладок
     * @param inputArrayList входной список открытых вкладок
     * @return templateHtml готовый результирующий HTML шаблон
     */
    private String createHtml(ArrayList<String> inputArrayList){
        long nowUnixDate = System.currentTimeMillis()/1000;

        StringBuilder templateHtml = new StringBuilder();
        templateHtml.append("<!DOCTYPE NETSCAPE-Bookmark-file-1>\n");
        templateHtml.append("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">\n");
        templateHtml.append("<TITLE>Bookmarks</TITLE>\n");
        templateHtml.append("<H1>Bookmarks</H1>\n");
        templateHtml.append("<DL><p>");
        templateHtml.append("<DT><H3 ADD_DATE=\""+nowUnixDate+"\" LAST_MODIFIED=\""+nowUnixDate+"\">Open tabs from <b>Save Open Tabs</b></H3>");
        templateHtml.append("<DL><p>");

        for(int i=1; i<=inputArrayList.size(); i++) {
            templateHtml.append("<DT><A HREF=\""+inputArrayList.get(i-1)+"\" ADD_DATE=\""+nowUnixDate+"\">URL "+i+"</A></DT>");
        }

        templateHtml.append("</p></DL>");
        templateHtml.append("</DT>");
        templateHtml.append("</p></DL>");

        return String.valueOf(templateHtml);
    }

    /**
     * Метод сохраняет готовый шаблон из полученного списка вкладок в HTML файл
     * @param pathToDirectory путь к директории сохранения
     * @param fileName имя файла для мохранения
     * @param inputArrayList входной список с вкладками
     * @throws IOException если возникла ошибка в процессе сохранения файла
     */
    public void saveResultHtml(String pathToDirectory, String fileName, ArrayList<String> inputArrayList)
            throws IOException {
        FileWorker.createNewFile(pathToDirectory);

        FileOutputStream fileOutputStream = new FileOutputStream (new File(pathToDirectory
                + fileName), false);

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        outputStreamWriter.write(createHtml(inputArrayList));
        outputStreamWriter.flush();
        outputStreamWriter.close();

        fileOutputStream.close();
    }
}
