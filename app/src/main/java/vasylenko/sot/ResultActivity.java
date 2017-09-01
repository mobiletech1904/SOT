package vasylenko.sot;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.os.Environment;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import vasylenko.sot.validate.Validate;
import vasylenko.sot.worker.HtmlWorker;

/**
 * Активность отображения результов
 * @Created by Тёма on 23.06.2017.
 * @version 1.0
 */
public class ResultActivity extends BasicActivity implements View.OnClickListener {
    private final static CharSequence[] ITEMS_STATE_DIALOG = {"Save tabs", "Save tabs and send"};

    private Button saveSendButton;
    private ListView resultListView;
    private ArrayAdapter<String> resultAdapter;

    private EditText userInputEmail;

    private String pathToSave;
    private String currentHtmlFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        // Получаем айдишники контролов
        saveSendButton = (Button) findViewById(R.id.saveSendButton);
        resultListView = (ListView) findViewById(R.id.list);

        // Получаем список с открытыми табами и заносим в список
        Bundle bundleFromMainActivity = getIntent().getExtras();
        ArrayList<String> openTabsArray = bundleFromMainActivity.getStringArrayList("openTabsArray");

        // Создаем адаптер доступа к данным и устанавливаем для каждого элемента
        // чебокс с множественным выбором
        resultAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, openTabsArray);
        resultListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // Устанавливаем настроенный адаптер для списка отображения полученных вкладок
        resultListView.setAdapter(resultAdapter);

        // Устанавливаем все чебоксы отмеченными по-умолчанию
        setAllCheckboxState(true);

        // Вешаем слушатель на кнопку экспорта/отправки файла с вкладками
        saveSendButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Создаем диалоговое окно выбора способа сохранения
        AlertDialog.Builder chooseItemsBuilder = new AlertDialog.Builder(ResultActivity.this);
        chooseItemsBuilder.setTitle("Choose:");
        chooseItemsBuilder.setIcon(R.drawable.save);
        chooseItemsBuilder.setSingleChoiceItems(ITEMS_STATE_DIALOG, 0, null);

        chooseItemsBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        pathToSave = Environment.getExternalStorageDirectory() + File.separator + "SavedTabs";
                        currentHtmlFile = "/open_tabs_" + new SimpleDateFormat("dd.MM.yyyy.hh:mm:ss").format(new Date()) + ".html";

                        ListView dialogListView = ((AlertDialog) dialog).getListView();
                        int itemId = (int) dialogListView.getAdapter().getItemId(dialogListView.getCheckedItemPosition());

                        switch (itemId) {
                            case 0:
                                // Сохраняем файл
                                try {
                                    new HtmlWorker().saveResultHtml(pathToSave, currentHtmlFile, getCheckedOpenTabs());
                                    Toast.makeText(ResultActivity.this, "File was saved to the SavedTabs folder!", Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    Toast.makeText(ResultActivity.this, "Error saving file!", Toast.LENGTH_LONG).show();
                                }
                                break;

                            case 1:
                                // Получаем вид с файла prompt.xml, который применим для диалогового окна
                                LayoutInflater layoutInflater = LayoutInflater.from(ResultActivity.this);
                                View inputEmailView = layoutInflater.inflate(R.layout.prompt, null);

                                // Настраиваем отображение поля для ввода текста в открытом диалоге
                                userInputEmail = (EditText) inputEmailView.findViewById(R.id.input_text);

                                AlertDialog.Builder sendEmailBuilder = new AlertDialog.Builder(ResultActivity.this);
                                sendEmailBuilder.setCancelable(false)
                                        .setView(inputEmailView)
                                        .setPositiveButton("Send", null)
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });

                                AlertDialog sendEmailDialog = sendEmailBuilder.create();
                                sendEmailDialog.show();

                                Button sendEmailDialogButton = sendEmailDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                                sendEmailDialogButton.setOnClickListener(new CustomEmailListener(sendEmailDialog));
                                break;
                        }
                    }

                });

        chooseItemsBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        // Создаем диалог выбора варианта сохранения файла
        AlertDialog chooseItemsDialog = chooseItemsBuilder.create();
        chooseItemsDialog.show();
    }

    /**
     * Метод устанавливает все чебоксы элементов resultListView в состояние отмеченный/не отмеченный
     * @param state вариант состояния
     */
    private void setAllCheckboxState(boolean state){
        for (int i = 0; i < resultListView.getAdapter().getCount(); i++) {
            resultListView.setItemChecked(i, state);
        }
    }

    /**
     * Метод возвращает список отмеченных вкладок
     * @return список выбранных элементов
     */
    private ArrayList<String> getCheckedOpenTabs(){
        SparseBooleanArray checked = resultListView.getCheckedItemPositions();
        ArrayList<String> selectedItems = new ArrayList<String>();

        for (int i = 0; i < checked.size(); i++) {
            // Позиция элемента в адаптере
            int position = checked.keyAt(i);

            // Определяем выбранный элемент и заносим в список
            if (checked.valueAt(i)) selectedItems.add(resultAdapter.getItem(position));
        }

        return selectedItems;
    }

    /**
     * Метод отправки сохраненного файла вкладок на указанный емейл
     * @param pathToFile путь к файлу
     * @param sendTo емейл получателя
     * @param subjectEmail тема письма
     * @param bodyEmail тело письма
     * @throws NullPointerException если сохраненный файл не найден
     */
    private void sendEmail(String pathToFile, String sendTo, String subjectEmail, String bodyEmail)
            throws NullPointerException {
        Uri URI = Uri.parse("file://" + pathToFile);

        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[] { sendTo });
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                subjectEmail);

        if (URI != null) {
            emailIntent.putExtra(Intent.EXTRA_STREAM, URI);
        }

        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, bodyEmail);

        startActivity(Intent.createChooser(emailIntent,
                "Sending email..."));
    }

    /**
     * Внутренний класс, который определяет дополнительную логику валидации эмейла
     * в кастомном диалоге
     */
    private class CustomEmailListener implements View.OnClickListener {
        private final Dialog customDialog;

        public CustomEmailListener(Dialog customDialog) {
            this.customDialog = customDialog;
        }

        @Override
        public void onClick(View v) {
            final String inputEmail = String.valueOf(userInputEmail.getText()).trim();

            if(new Validate().isValidEmail(inputEmail)){
                // Сохраняем файл и отправляем его
                try {
                    new HtmlWorker().saveResultHtml(pathToSave, currentHtmlFile, getCheckedOpenTabs());

                    sendEmail(
                            pathToSave + currentHtmlFile,
                            inputEmail,
                            "Tabs from SaveOpenTabs",
                            "Hi! Save html file to computer and import to your Chrome."
                    );

                    Toast.makeText(ResultActivity.this, "File was saved to the SavedTabs folder!", Toast.LENGTH_LONG).show();
                    customDialog.dismiss();
                } catch (IOException e) {
                    Toast.makeText(ResultActivity.this, "Error saving file!", Toast.LENGTH_LONG).show();
                } catch (NullPointerException w) {
                    Toast.makeText(ResultActivity.this, "File not found error!", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(ResultActivity.this, "Invalid data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
