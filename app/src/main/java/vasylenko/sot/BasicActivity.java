package vasylenko.sot;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Класс базовой активности, который задает общее поведение для наследников
 * Created by Тёма on 22.06.2017.
 * @version 1.0
 */
public class BasicActivity extends AppCompatActivity {
    /** Создаем меню и задаем его элементы */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.exit_settings:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
