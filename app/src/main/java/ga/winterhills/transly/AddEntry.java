/*
package ga.winterhills.transly;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.SimpleCursorAdapter;

import static ga.winterhills.transly.MainActivity.APP_PREFERENCES;
import static ga.winterhills.transly.MainActivity.APP_PREFERENCES_ENABLED_DICTIONARY;


class AddEntry extends AsyncTask<Void, Void, Void> {

    private DictionaryFragment dictionaryFragment;
    private String word, trans;
    private SQLiteDatabase db;
    private Cursor c;
    private SimpleCursorAdapter sca;
    private String dic_choice;
    private boolean flag;

    AddEntry( DictionaryFragment dictionaryFragment) {
        this.dictionaryFragment = dictionaryFragment;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        word = dictionaryFragment.addWord.getText().toString();
        trans = dictionaryFragment.addTrans.getText().toString();
        DataBase dbHelper = new DataBase(dictionaryFragment.getActivity());
        SharedPreferences mSettings = dictionaryFragment.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        dic_choice = mSettings.getString(APP_PREFERENCES_ENABLED_DICTIONARY, "");
        db = dbHelper.getWritableDatabase();
        flag = true;
    }

    @Override
    protected Void doInBackground(Void... params) {
        flag = false;
        ContentValues cv = new ContentValues();
        if (!(word.equalsIgnoreCase("") || trans.equalsIgnoreCase("") || word.startsWith(" ") || trans.startsWith(" "))) {
            cv.put("COLUMN_WORD", word);
            cv.put("COLUMN_TRANSLATION", trans);
            cv.put("WRONG", 0);
            cv.put("RIGHT", 0);
            db.insert(dic_choice, null, cv);
            c = db.rawQuery("SELECT * FROM " + dic_choice, null);
            String[] headers = new String[]{"COLUMN_WORD", "COLUMN_TRANSLATION"};
            sca = new SimpleCursorAdapter(dictionaryFragment.getActivity(), R.layout.dic_list_view,
                    c, headers,
                    new int[]{R.id.firstWord, R.id.secondWord}, 0);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        dictionaryFragment.addButton.setText(String.valueOf(flag));
        dictionaryFragment.lv.setAdapter(sca);
        dictionaryFragment.addWord.getText().clear();
        dictionaryFragment.addTrans.getText().clear();
        db.close();
        c.close();
    }
}
*/
