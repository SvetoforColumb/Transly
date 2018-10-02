package ga.winterhills.transly;

import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.Gravity;
import android.widget.Toast;

import java.util.Queue;
import java.util.Random;

import static ga.winterhills.transly.MainActivity.APP_PREFERENCES_ENABLED_DICTIONARY;

class ExerciseMaker extends AsyncTask<Queue<Exercise>, Void, Void> {

    private MainFragment mainFragment;
    private SQLiteDatabase db;
    private Cursor c;
    private boolean failFlag;
    private boolean isFirst;
    private Exercise tmp;
    private Queue<Exercise> _exercises;
    private String title, a;
    private int rowsNumber;
    private Random rand;


    ExerciseMaker(MainFragment mainFragment, boolean isFirst) {
        this.mainFragment = mainFragment;
        this.isFirst = isFirst;
    }

    private boolean exerciseChecker(String TVmain, String ButtonText) {
        c = db.rawQuery("SELECT _id FROM " + mainFragment.dic_choise + " WHERE COLUMN_WORD = '" + TVmain + "' AND COLUMN_TRANSLATION = '" + ButtonText + "'", null);
        if (c == null) {
            return false;
        } else if (!c.moveToFirst()) {
            //c.close();
            return false;
        }
        return true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DataBase dbHelper = new DataBase(mainFragment.getActivity());
        mainFragment.dic_choise = mainFragment.mSettings.getString(APP_PREFERENCES_ENABLED_DICTIONARY, null);
        db = dbHelper.getWritableDatabase();
        c = db.query(mainFragment.dic_choise, null, null, null, null, null, null);
    }

    @Override
    protected Void doInBackground(Queue<Exercise>... params) {
        rand = new Random();
        _exercises = params[0];
        tmp = new Exercise();
        String[] all = new String[4];
        rowsNumber = c.getCount();
        try {
            int randTVMainPlace = rand.nextInt(rowsNumber);
            c.moveToPosition(randTVMainPlace);
            title = c.getString(c.getColumnIndex("COLUMN_WORD"));
            //  a = " " + c.getString(c.getColumnIndex("WRONG")) + " " + c.getString(c.getColumnIndex("RIGHT"));
            int randButton1 = rand.nextInt(rowsNumber), randButton2 = rand.nextInt(rowsNumber), randButton3 = rand.nextInt(rowsNumber);
            while (randButton1 == randButton2 || randButton2 == randButton3 || randButton1 == randButton3 || randTVMainPlace == randButton1 || randTVMainPlace == randButton2 || randTVMainPlace == randButton3) {
                randButton1 = rand.nextInt(rowsNumber);
                randButton2 = rand.nextInt(rowsNumber);
                randButton3 = rand.nextInt(rowsNumber);
            }
            if (rowsNumber >= 4) {
                int randomButton = rand.nextInt(new int[]{0, 1, 2, 3}.length);
                switch (randomButton) {
                    case 0:
                        all[0] = c.getString(c.getColumnIndex("COLUMN_TRANSLATION"));
                        c.moveToPosition(randButton1);
                        all[1] = c.getString(c.getColumnIndex("COLUMN_TRANSLATION"));
                        c.moveToPosition(randButton2);
                        all[2] = c.getString(c.getColumnIndex("COLUMN_TRANSLATION"));
                        c.moveToPosition(randButton3);
                        all[3] = c.getString(c.getColumnIndex("COLUMN_TRANSLATION"));
                        break;
                    case 1:
                        all[1] = c.getString(c.getColumnIndex("COLUMN_TRANSLATION"));
                        c.moveToPosition(randButton1);
                        all[0] = c.getString(c.getColumnIndex("COLUMN_TRANSLATION"));
                        c.moveToPosition(randButton2);
                        all[2] = c.getString(c.getColumnIndex("COLUMN_TRANSLATION"));
                        c.moveToPosition(randButton3);
                        all[3] = c.getString(c.getColumnIndex("COLUMN_TRANSLATION"));
                        break;
                    case 2:
                        all[2] = c.getString(c.getColumnIndex("COLUMN_TRANSLATION"));
                        c.moveToPosition(randButton1);
                        all[1] = c.getString(c.getColumnIndex("COLUMN_TRANSLATION"));
                        c.moveToPosition(randButton2);
                        all[0] = c.getString(c.getColumnIndex("COLUMN_TRANSLATION"));
                        c.moveToPosition(randButton3);
                        all[3] = c.getString(c.getColumnIndex("COLUMN_TRANSLATION"));
                        break;
                    case 3:
                        all[3] = c.getString(c.getColumnIndex("COLUMN_TRANSLATION"));
                        c.moveToPosition(randButton1);
                        all[1] = c.getString(c.getColumnIndex("COLUMN_TRANSLATION"));
                        c.moveToPosition(randButton2);
                        all[2] = c.getString(c.getColumnIndex("COLUMN_TRANSLATION"));
                        c.moveToPosition(randButton3);
                        all[0] = c.getString(c.getColumnIndex("COLUMN_TRANSLATION"));
                        break;
                }

            } else {
                failFlag = true;
                return null;
            }

        } catch (CursorIndexOutOfBoundsException | IllegalArgumentException e) {
            failFlag = true;
        }

        tmp.setETVMain(title);
        tmp.setAns1(exerciseChecker(title, all[0]));
        tmp.setAns2(exerciseChecker(title, all[1]));
        tmp.setAns3(exerciseChecker(title, all[2]));
        tmp.setAns4(exerciseChecker(title, all[3]));
        tmp.setOne(all[0]);
        tmp.setTwo(all[1]);
        tmp.setThree(all[2]);
        tmp.setFour(all[3]);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (failFlag) {
            mainFragment.TVMain.setText(R.string.TRANSLY);
            Toast toast = Toast.makeText(mainFragment.getActivity(),
                    "Add more words in dictionary!",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 100);
            toast.show();
            return;
        }
        _exercises.add(tmp);
        if (isFirst) {
            mainFragment.onExerciseMakerFinished();
        }

    }
}
