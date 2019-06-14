package ga.winterhills.transly;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import static ga.winterhills.transly.MainActivity.APP_PREFERENCES;
import static ga.winterhills.transly.MainActivity.APP_PREFERENCES_ENABLED_DICTIONARY;


public class DictionaryFragment extends Fragment {

    EditText addWord;
    EditText addTrans;
    ListView listView;
    Button addButton;
    Button button_back;
    Spinner dic_spinner;
    SharedPreferences mSettings;
    String dic_choice;
    String item;
    String sql_name;
    onDictionaryChangedListener dictionaryChangedListener;
    onButtonToRightClickedListener buttonToRightClickedListener;
    SQLiteDatabase db;
    Cursor c;
    SimpleCursorAdapter simpleCursorAdapter;
    DataBase dbHelper;
    DictionaryFragment dictionaryFragment;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dictionaryChangedListener = (onDictionaryChangedListener) activity;
            buttonToRightClickedListener = (onButtonToRightClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onDictionaryChangedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        dic_choice = mSettings.getString(APP_PREFERENCES_ENABLED_DICTIONARY, "");
        dbHelper = new DataBase(getActivity());
        db = dbHelper.getWritableDatabase();
        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        dic_choice = mSettings.getString(APP_PREFERENCES_ENABLED_DICTIONARY, "");
        c = db.rawQuery("SELECT * FROM " + dic_choice, null);
        String[] headers = new String[]{"COLUMN_WORD", "COLUMN_TRANSLATION"};
        simpleCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.dic_list_view,
                c, headers,
                new int[]{R.id.firstWord, R.id.secondWord}, 0);


        //db.close();
        //c.close();

        //ListMaker listMakerTask = new ListMaker(this);
        //listMakerTask.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void addEntry() {
        AddEntryMaker addEntryTask = new AddEntryMaker(this);
        addEntryTask.execute();
        dictionaryChangedListener.DictionaryChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);

        addWord = view.findViewById(R.id.firstLine);
        addTrans = view.findViewById(R.id.secondLine);
        listView = view.findViewById(R.id.listView);

        addButton = view.findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEntry();
            }
        });
        dic_spinner = view.findViewById(R.id.dic_spinner);
        final DictionaryFragment l = this;
        SpinnerMaker spinnerMakerTask = new SpinnerMaker(l);
        spinnerMakerTask.execute();
        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = (String) parent.getItemAtPosition(position);

                // dictionaryChangedListener.DictionaryChanged();
                onResume();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        dic_spinner.setOnItemSelectedListener(itemSelectedListener);
        button_back = view.findViewById(R.id.button_to_right);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonToRightClickedListener.ButtonToRightClicked();
            }
        });
        listView = view.findViewById(R.id.listView);
        listView.setAdapter(simpleCursorAdapter);
//        ListMaker listMaker = new ListMaker(this);
//        listMaker.execute();
        return view;
    }

    interface onButtonToRightClickedListener {
        void ButtonToRightClicked();
    }

    interface onDictionaryChangedListener {
        void DictionaryChanged();
    }

    class ListMaker extends AsyncTask<Void, Void, Void> {

        SQLiteDatabase db;
        Cursor c;
        SimpleCursorAdapter simpleCursorAdapter;
        DataBase dbHelper;
        DictionaryFragment dictionaryFragment;
        String dic_choice;
        SharedPreferences mSettings;

        ListMaker(DictionaryFragment dictionaryFragment) {
            this.dictionaryFragment = dictionaryFragment;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected Void doInBackground(Void... params) {
            dbHelper = new DataBase(getActivity());
            db = dbHelper.getWritableDatabase();
            mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            dic_choice = mSettings.getString(APP_PREFERENCES_ENABLED_DICTIONARY, "");
            c = db.rawQuery("SELECT * FROM " + dic_choice, null);
            String[] headers = new String[]{"COLUMN_WORD", "COLUMN_TRANSLATION"};
            simpleCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.dic_list_view,
                    c, headers,
                    new int[]{R.id.firstWord, R.id.secondWord}, 0);

            db.close();
            c.close();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dictionaryFragment.listView.setAdapter(simpleCursorAdapter);
        }
    }

    class AddEntryMaker extends AsyncTask<Void, Void, Void> {

        SQLiteDatabase db;
        DataBase dbHelper;
        Cursor c;
        ContentValues contentValues;
        DictionaryFragment dictionaryFragment;
        SimpleCursorAdapter simpleCursorAdapter;
        String word, trans;

        AddEntryMaker(DictionaryFragment dictionaryFragment) {
            this.dictionaryFragment = dictionaryFragment;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            word = dictionaryFragment.addWord.getText().toString();
            trans = dictionaryFragment.addTrans.getText().toString();

        }

        @Override
        protected Void doInBackground(Void... params) {
            contentValues = new ContentValues();
            if (!(word.equalsIgnoreCase("") || trans.equalsIgnoreCase("") || word.startsWith(" ") || trans.startsWith(" "))) {
                db = dbHelper.getWritableDatabase();
                contentValues.put("COLUMN_WORD", word);
                contentValues.put("COLUMN_TRANSLATION", trans);
                db.insert(dic_choice, null, contentValues);
                c = db.rawQuery("SELECT * FROM " + dic_choice, null);
                String[] headers = new String[]{"COLUMN_WORD", "COLUMN_TRANSLATION"};
                simpleCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.dic_list_view,
                        c, headers,
                        new int[]{R.id.firstWord, R.id.secondWord}, 0);
            }
            c.close();
            db.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            listView.setAdapter(simpleCursorAdapter);
            if (!(word.equalsIgnoreCase("") || trans.equalsIgnoreCase("") || word.startsWith(" ") || trans.startsWith(" "))) {
                dictionaryFragment.addWord.getText().clear();
                dictionaryFragment.addTrans.getText().clear();
            }
        }

    }

    class SpinnerMaker extends AsyncTask<Void, Void, Void> {

        DictionaryFragment dictionaryFragment;
        SQLiteDatabase db;
        DataBase dbHelper;
        Cursor c;
        SharedPreferences mSettings;
        String dic_choice;
        ArrayAdapter<String> adapter;
        int selection;

        SpinnerMaker(DictionaryFragment dictionaryFragment) {
            this.dictionaryFragment = dictionaryFragment;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected Void doInBackground(Void... params) {
            mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            dic_choice = mSettings.getString(APP_PREFERENCES_ENABLED_DICTIONARY, null);
            dbHelper = new DataBase(getActivity());
            db = dbHelper.getReadableDatabase();
            c = db.rawQuery("SELECT * FROM LIST_OF_TABLES", null);
            c.moveToFirst();
            List<String> all_dic = new ArrayList<>();
            do {
                all_dic.add(c.getString(c.getColumnIndex("SHOW_NAME")));
            } while (c.moveToNext());
            adapter = new ArrayAdapter<>(getActivity(), R.layout.dic_spinner, R.id.point, all_dic);
            String db_choise;
            c.moveToFirst();
            do {
                db_choise = c.getString(c.getColumnIndex("SQL_NAME"));
                if (dic_choice.equals(db_choise)) break;
                selection++;
            } while (c.moveToNext());
            c.close();
            db.close();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dictionaryFragment.dic_spinner.setAdapter(adapter);
            dic_spinner.setSelection(selection);
        }
    }

    class DicChagedMaker extends AsyncTask<Void, Void, Void> {

        DictionaryFragment dictionaryFragment;
        Cursor c;
        SQLiteDatabase db;
        DataBase dbHelper;
        String chosen_item;

        DicChagedMaker(DictionaryFragment dictionaryFragment) {
            this.dictionaryFragment = dictionaryFragment;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            chosen_item = dictionaryFragment.item;
        }


        @Override
        protected Void doInBackground(Void... params) {
            dbHelper = new DataBase(getActivity());
            db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM LIST_OF_TABLES", null);
            dictionaryFragment.sql_name = null;
            String compare_str;
            do {
                compare_str = c.getString(c.getColumnIndex("SHOW_NAME"));
                if (compare_str.equals(chosen_item)) {
                    dictionaryFragment.sql_name = c.getString(c.getColumnIndex("SQL_NAME"));
                    break;
                }
            } while (c.moveToNext());
            SharedPreferences.Editor e = mSettings.edit();
            e.putString(APP_PREFERENCES_ENABLED_DICTIONARY, dictionaryFragment.sql_name);
            e.apply();
            c.close();
            db.close();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dictionaryFragment.dic_choice = dictionaryFragment.sql_name;
        }
    }
}


