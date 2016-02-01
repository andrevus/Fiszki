package fiszki.qm.eu.fiszki.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import fiszki.qm.eu.fiszki.Alert;
import fiszki.qm.eu.fiszki.FlashcardsManagment;
import fiszki.qm.eu.fiszki.R;

public class CheckActivity extends AppCompatActivity {

    FlashcardsManagment flashcardsManagment;
    TextView word;
    EditText enteredWord;
    Context context;
    Alert alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        context = this;
        alert = new Alert();

        enteredWord = (EditText) findViewById(R.id.EnteredWord);
        word = (TextView) findViewById(R.id.textView3);
        flashcardsManagment = new FlashcardsManagment(context);

        if (flashcardsManagment.getCountAll() <= 0) {
            alert.emptyBase(context, getString(R.string.main_activity_empty_base_main_layout),
                    getString(R.string.alert_title_fail), getString(R.string.button_action_ok));
        }else{

        }

    }
}
