package vasylenko.sot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.stericson.RootShell.exceptions.RootDeniedException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import vasylenko.sot.access.RootPermissionAccess;
import vasylenko.sot.worker.SearchWorker;

/**
 * Главная активность проекта.
 * @Created by Тёма on 22.06.2017.
 * @version 1.0
 */
public class MainActivity extends BasicActivity {
    /** Путь к файлу с открытыми вкладками */
    private static final String PATH_TO_FILE = "/data/user/0/com.android.chrome/app_tabs/0/tab_state0";

    /** Кнопка запуска процесса поиска открытых вкладок на устройстве */
    private Button searchTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchTabs = (Button) findViewById(R.id.searchTabs);
    }

    /**
     * Метод выполнения процесса поиска открытых вкладок
     * @param view входная вьюшка по-умолчанию
     */
    public void startSearchTabs(View view) {
        // TODO: для лучшей динамичности приложения, необходимо дописать поиск файла в каталоге Хрома
        // Получаем файл для доступа на устройстве
        File tabsStateFile = new File(PATH_TO_FILE);

        // Проверяем существование файла
        // Если файл существует, устанавливаем права доступа и возвращаем рутированный файл
        if (tabsStateFile.exists()) {
            try {
                RootPermissionAccess.setRootPermission("0777", tabsStateFile);
                Toast.makeText(this, "Access granted successful!", Toast.LENGTH_LONG).show();

                // Храним список ссылок открытых табов
                ArrayList<String> openTabsArray = new SearchWorker().getOpenTabs(tabsStateFile);

                if(openTabsArray.size()>1) {
                    // "Подключаем" активити вывода полученных вкладок
                    Intent intent = new Intent(getApplicationContext(),
                            ResultActivity.class);
                    intent.addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK
                    );

                    // bundle обьект для хранения открытых вкладок
                    Bundle bundleOpenTabs = new Bundle();
                    bundleOpenTabs.putStringArrayList("openTabsArray", openTabsArray);

                    // Добавляем bundle обьект в intent для передачи в другой активити
                    intent.putExtras(bundleOpenTabs);

                    // Запускаем второй активити и передаем туда обьект со списком открытых вкладок
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "Count of tabs don't >1!", Toast.LENGTH_LONG).show();
                }
            } catch (TimeoutException e) {
                Toast.makeText(this, "Timeout error!", Toast.LENGTH_LONG).show();
            } catch (RootDeniedException e) {
                Toast.makeText(this, "Access error!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(this, "IO Error!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "File not found!", Toast.LENGTH_LONG).show();
        }

    }

}
