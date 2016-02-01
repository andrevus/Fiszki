package fiszki.qm.eu.fiszki.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import fiszki.qm.eu.fiszki.Flashcard;
import fiszki.qm.eu.fiszki.FlashcardsManagment;
import fiszki.qm.eu.fiszki.ListViewManagment;
import fiszki.qm.eu.fiszki.R;

public class MainActivity extends AppCompatActivity {

    private FlashcardsManagment flashcardsManagment;
    public ImageView emptyDBImage;
    public TextView emptyDBText;
    public Context context;
    public ListView listView;
    public ListViewManagment lvm;
    public Flashcard fiszka;
    public FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;
        fiszka = new Flashcard();
        listView = (ListView) findViewById(R.id.listView);
        lvm = new ListViewManagment(listView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        emptyDBImage = (ImageView) findViewById(R.id.emptyDBImage);
        emptyDBText = (TextView) findViewById(R.id.emptyDBText);
        emptyDBImage.setImageResource(R.drawable.emptydb);
        flashcardsManagment = new FlashcardsManagment(context);

        if(flashcardsManagment.getCountAll()>0){
            setEmptyDatabasePngVisible(false);
            lvm.populate(context, flashcardsManagment.getAll());
        }else{
            setEmptyDatabasePngVisible(true);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoAddNewWord = new Intent(context, AddWordActivity.class);
                startActivity(gotoAddNewWord);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(flashcardsManagment.getCountAll()>0){
            setEmptyDatabasePngVisible(false);
            lvm.populate(context,flashcardsManagment.getAll());
        }else{
            setEmptyDatabasePngVisible(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            Intent goSettings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(goSettings);
            finish();
        } else if (id == R.id.examMode) {
            Intent goLearningMode = new Intent(MainActivity.this, ExamModeActivity.class);
            startActivity(goLearningMode);
        } else if (id == R.id.learningMode) {
            Intent goLearningMode = new Intent(MainActivity.this, LearningModeActivity.class);
            startActivity(goLearningMode);
        }

        return super.onOptionsItemSelected(item);
    }

    public void setEmptyDatabasePngVisible(boolean on) {
        if (on) {
            emptyDBImage.setVisibility(View.VISIBLE);
            emptyDBText.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        } else {
            emptyDBImage.setVisibility(View.INVISIBLE);
            emptyDBText.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }
    }
}
