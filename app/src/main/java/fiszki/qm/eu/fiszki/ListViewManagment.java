package fiszki.qm.eu.fiszki;

import android.content.Context;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by mBoiler on 30.01.2016.
 */
public class ListViewManagment {

    ListView listView;
    public ListViewManagment(ListView listView){
        this.listView = listView;
    }

    public void populate(Context context,ArrayList<Flashcard> arrayList){
        ItemAdapter adapter = new ItemAdapter(context,R.layout.item_layout,arrayList);
        listView.setAdapter(adapter);
    }
}
