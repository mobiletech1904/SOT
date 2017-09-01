package vasylenko.sot.access;

import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Класс для работы с правами доступа
 * @Created by Тёма on 24.06.2017.
 */
public class RootPermissionAccess {
    /**
     * Метод устанавливает значение прав доступа для определенного ресурса.
     * Используем библиотеку RootTools.jar
     * @param permission значение прав доступа, например 0777 для полного доступа к ресурсу
     * @param inputFile входной файл, которому предоставляется доступ
     * @throws java.util.concurrent.TimeoutException если превышен лимит времени изменения прав доступа
     * @throws com.stericson.RootShell.exceptions.RootDeniedException при отказе в изменении прав доступа
     * @throws java.io.IOException если возникли ошибки в процессе работы с файлом
     */
    public static void setRootPermission(String permission, File inputFile)
            throws TimeoutException, RootDeniedException, IOException {
        Command command = new Command(0, "chmod "+ permission + " " + inputFile.getAbsolutePath());
        RootTools.getShell(true).add(command);
    }
}
