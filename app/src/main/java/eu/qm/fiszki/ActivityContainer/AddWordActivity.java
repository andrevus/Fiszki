package eu.qm.fiszki.ActivityContainer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import eu.qm.fiszki.DataBaseContainer.DBAdapter;
import eu.qm.fiszki.DataBaseContainer.DBModel;
import eu.qm.fiszki.DataBaseContainer.DBStatus;
import eu.qm.fiszki.R;


public class AddWordActivity extends AppCompatActivity {

    EditText inputWord, inputTranslation;
    DBAdapter myDb = new DBAdapter(this);
    DBStatus OpenDataBase = new DBStatus();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        inputWord = (EditText) findViewById(R.id.inputWord);
        inputWord.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        inputWord.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        inputTranslation = (EditText) findViewById(R.id.inputTranslation);
        inputTranslation.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        OpenDataBase.openDB(myDb);
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
            if (myDb.getRowValue(DBModel.KEY_WORD, inputWord.getText().toString()) == true){
                Toast.makeText(getApplicationContext(), "dana fiszka już istnieje", Toast.LENGTH_LONG).show();
                inputWord.setText(null);
                inputTranslation.setText(null);
            }
            else if (!TextUtils.isEmpty(inputWord.getText().toString()) && !TextUtils.isEmpty(inputTranslation.getText().toString())) {
                myDb.insertRow(inputWord.getText().toString(), inputTranslation.getText().toString());
                Toast.makeText(getApplicationContext(), "Dodano rekord.", Toast.LENGTH_LONG).show();
                inputWord.setText(null);
                inputTranslation.setText(null);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Pola nie mogą być puste.", Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}