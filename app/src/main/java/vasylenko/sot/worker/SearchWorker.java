package vasylenko.sot.worker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для работы с поиском ссылок
 * Created by Тёма on 22.06.2017.
 * @version 1.0
 */
public class SearchWorker {
    /** Шаблон для поиска соответствующего вида ссылок */
    private static final String REGEX = "[.]*(http(s?)://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";

    /**
     * Метод парсинга файла открытых вкладок
     * @param inputFile входной файл
     * @return tempOpenTabsArray список открытых вкладок
     * @throws IOException если возникла ошибка чтения файла
     */
    public ArrayList<String> getOpenTabs(File inputFile) throws IOException {
        ArrayList<String> tempOpenTabsArray = new ArrayList<String>();

        FileWorker fileWorker = new FileWorker();
        String tempString = fileWorker.getFileContent(inputFile);

        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(tempString);
        while(matcher.find()) tempOpenTabsArray.add(matcher.group().trim());

        return tempOpenTabsArray;
    }
}
