/*
package ga.winterhills.transly;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//public class DBHelper extends SQLiteOpenHelper {
//
//    public DBHelper(Context context) {
//        super(context, "myDB.db", null, 1);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("CREATE TABLE wordsTable (_id INTEGER PRIMARY KEY AUTOINCREMENT, COLUMN_WORD TEXT NOT NULL, COLUMN_TRANSLATION TEXT NOT NULL);");
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }
//}
class DBHelper extends SQLiteOpenHelper{

    // путь к базе данных вашего приложения
    private static String DB_PATH = "/data/data/ga.winterhills.transly/databases/";
    private static String DB_NAME = "eng_rus_db.db";
    private SQLiteDatabase myDataBase;
    private final Context mContext;


    DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
    }

    */
/**
 * Создает пустую базу данных и перезаписывает ее нашей собственной базой
 * <p>
 * Проверяет, существует ли уже эта база, чтобы не копировать каждый раз при запуске приложения
 *
 * @return true если существует, false если не существует
 * <p>
 * Копирует базу из папки assets заместо созданной локальной БД
 * Выполняется путем копирования потока байтов.
 * <p>
 * Проверяет, существует ли уже эта база, чтобы не копировать каждый раз при запуске приложения
 * @return true если существует, false если не существует
 * <p>
 * Копирует базу из папки assets заместо созданной локальной БД
 * Выполняется путем копирования потока байтов.
 * <p>
 * Проверяет, существует ли уже эта база, чтобы не копировать каждый раз при запуске приложения
 * @return true если существует, false если не существует
 * <p>
 * Копирует базу из папки assets заместо созданной локальной БД
 * Выполняется путем копирования потока байтов.
 *//*

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if(!dbExist){
            //вызывая этот метод создаем пустую базу, позже она будет перезаписана
            this.getReadableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    */
/**
 * Проверяет, существует ли уже эта база, чтобы не копировать каждый раз при запуске приложения
 * @return true если существует, false если не существует
 *//*

    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //база еще не существует
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null;
    }

    */
/**
 * Копирует базу из папки assets заместо созданной локальной БД
 * Выполняется путем копирования потока байтов.
 * *//*

    private void copyDataBase() throws IOException{
        //Открываем локальную БД как входящий поток
        InputStream myInput = mContext.getAssets().open(DB_NAME);

        //Путь ко вновь созданной БД
        String outFileName = DB_PATH + DB_NAME;

        //Открываем пустую базу данных как исходящий поток
        OutputStream myOutput = new FileOutputStream(outFileName);

        //перемещаем байты из входящего файла в исходящий
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //закрываем потоки
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        //открываем БД
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // Здесь можно добавить вспомогательные методы для доступа и получения данных из БД
    // вы можете возвращать курсоры через "return myDataBase.query(....)", это облегчит их использование
    // в создании адаптеров для ваших view
}*/
