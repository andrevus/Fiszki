package fiszki.qm.eu.fiszki.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import fiszki.qm.eu.fiszki.AlarmReceiver;
import fiszki.qm.eu.fiszki.Alert;
import fiszki.qm.eu.fiszki.Flashcard;
import fiszki.qm.eu.fiszki.FlashcardsManagment;
import fiszki.qm.eu.fiszki.R;
import fiszki.qm.eu.fiszki.database.DBAdapter;
import fiszki.qm.eu.fiszki.database.DBStatus;
import fiszki.qm.eu.fiszki.Rules;

public class AddWordActivity extends AppCompatActivity {

    public Context context = this;
    public AlarmReceiver alarm;
    SettingsActivity settings = new SettingsActivity();
    DBAdapter myDb = new DBAdapter(this);
    DBStatus OpenDataBase = new DBStatus();
    Alert alert = new Alert();
    private EditText inputWord, inputTranslation;
    private Rules rules = new Rules();
    private Flashcard fiszka;
    private FlashcardsManagment flashcardsManagment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        inputWord = (EditText) findViewById(R.id.inputWord);
        inputTranslation = (EditText) findViewById(R.id.inputTranslation);
        fiszka = new Flashcard();
        flashcardsManagment = new FlashcardsManagment(context);
        alarm = new AlarmReceiver();

        OpenDataBase.openDB(myDb);
        inputWord.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        inputTranslation.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        inputWord.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        clickDone();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addword, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_new_word) {
            if (rules.addNewWord(inputWord, inputTranslation, this)) {
                flashcardsManagment.add(inputWord.getText().toString(), inputTranslation.getText().toString());
                if (flashcardsManagment.isFirst()) {
                    alarm.start(context, 15);
                    myDb.updateRow(settings.notificationPosition, 3);
                    myDb.updateRow(settings.notificationStatus, 1);
                    alert.buildAlert(
                            this.getString(R.string.alert_title_pass),
                            this.getString(R.string.alert_add_first_word_message),
                            this.getString(R.string.button_action_ok),
                            this);
                }
                inputWord.setText(null);
                inputTranslation.setText(null);
                inputWord.requestFocus();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickDone() {
        inputTranslation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (rules.addNewWord(inputWord, inputTranslation, AddWordActivity.this)) {
                        flashcardsManagment.add(inputWord.getText().toString(), inputTranslation.getText().toString());
                        if (flashcardsManagment.isFirst()) {
                            alarm.start(context, 15);
                            myDb.updateRow(settings.notificationPosition, 3);
                            myDb.updateRow(settings.notificationStatus, 1);
                            alert.buildAlert(
                                    AddWordActivity.this.getString(R.string.alert_title_pass),
                                    AddWordActivity.this.getString(R.string.alert_add_first_word_message),
                                    AddWordActivity.this.getString(R.string.button_action_ok),
                                    AddWordActivity.this);
                        }
                        inputWord.setText(null);
                        inputTranslation.setText(null);
                        inputWord.requestFocus();
                    }
                }
                return true;
            }
        });
    }

}
