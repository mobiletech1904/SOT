package vasylenko.sot.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для работы с валидацией
 * Created by Тёма on 25.06.2017.
 */
public class Validate {
    /** Шаблон емейла для сравнения с входящей строкой  */
    final static String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    // String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

    /**
     * @param email входящая строка для проверки с шаблоном емейла
     * @return булевый результат сравнения входящей строки с шаблоном емейла
     */
    public boolean isValidEmail(String email) {
        boolean isValid = false;

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        if (matcher.matches()) isValid = true;

        return isValid;
    }

}
