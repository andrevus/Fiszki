package fiszki.qm.eu.fiszki;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Random;

import fiszki.qm.eu.fiszki.database.DBAdapter;
import fiszki.qm.eu.fiszki.database.DBModel;
import fiszki.qm.eu.fiszki.database.DBStatus;

/**
 * Created by mBoiler on 31.01.2016.
 */
public class FlashcardsManagment {

    public Context context;
    public DBAdapter myDb;
    public DBStatus openDataBase;

    public FlashcardsManagment(Context context) {
        this.context = context;
        myDb = new DBAdapter(context);
        openDataBase = new DBStatus();
        openDataBase.openDB(myDb);
    }

    public ArrayList<Flashcard> getAll() {
        Cursor c = myDb.getAllRows();
        ArrayList<Flashcard> arrayList = new ArrayList<>();
        Flashcard data;
        if (c != null) {
            do {
                data = new Flashcard();
                data.setOrginalWord(c.getString(1));
                data.setTranslationWord(c.getString(2));
                arrayList.add(data);
            } while (c.moveToNext());
            c.close();
        }
        return arrayList;
    }

    public boolean existence(String name) {
        if (myDb.getRowValue(DBModel.KEY_WORD, name)) {
            return false;
        } else {
            return true;
        }
    }

    public int getCountAll() {
        return myDb.getAllRows().getCount();
    }

    public void add(String word, String translation) {
        myDb.insertRow(word, translation, 1);
    }

    public boolean isFirst(){
        if(myDb.getAllRows().getCount()==1){
            return true;
        }
        return false;
    }

    private Flashcard priorityAlgorytm() {
        final int[] points = {25, 20, 15, 10, 5};
        int[] totalPoints = new int[5];
        int[] section = new int[5];
        int drawn = 0;

        Flashcard fiszka = new Flashcard();

        for(int i=0; i<5; i++) {
            Cursor cardsPriority = myDb.getAllRowsPriority(i+1);
            int count = cardsPriority.getCount();
            totalPoints[i] = count * points[i];
            if(i <= 0) {
                section[i] = totalPoints[i];
            }else {
                section[i] = totalPoints[i] + section[i-1];
            }
        }
        Random rand = new Random();
        drawn = rand.nextInt(section[4]);
        drawn += 1;

        if(drawn <= section[0]) {
            Cursor c = myDb.getRandomRowWithpriority(1);
            fiszka.setOrginalWord(c.getString(1));
            fiszka.setTranslationWord(c.getString(2));
        } else if(drawn <= section[1]) {
            Cursor c = myDb.getRandomRowWithpriority(2);
            fiszka.setOrginalWord(c.getString(1));
            fiszka.setTranslationWord(c.getString(2));
        } else if(drawn <= section[2]) {
            Cursor c = myDb.getRandomRowWithpriority(3);
            fiszka.setOrginalWord(c.getString(1));
            fiszka.setTranslationWord(c.getString(2));
        } else if(drawn <= section[3]) {
            Cursor c = myDb.getRandomRowWithpriority(4);
            fiszka.setOrginalWord(c.getString(1));
            fiszka.setTranslationWord(c.getString(2));
        } else if(drawn <= section[4]+1) {
            Cursor c = myDb.getRandomRowWithpriority(5);
            fiszka.setOrginalWord(c.getString(1));
            fiszka.setTranslationWord(c.getString(2));
        }

        return fiszka;
    }

    public void cursorToFlashcard(Cursor cursor, Flashcard fiszka){
        fiszka.setId(cursor.getInt(0));
        fiszka.setOrginalWord(cursor.getString(1));
        fiszka.setTranslationWord(cursor.getString(2));
        fiszka.setPriority(cursor.getInt(3));
        fiszka.setCategory(cursor.getString(4));

    }
}
