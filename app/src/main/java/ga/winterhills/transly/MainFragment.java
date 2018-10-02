package ga.winterhills.transly;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Queue;

import static ga.winterhills.transly.MainActivity.APP_PREFERENCES;
import static ga.winterhills.transly.MainActivity.APP_PREFERENCES_ENABLED_DICTIONARY;
import static ga.winterhills.transly.MainActivity.APP_PREFERENCES_ENABLED_TONEXT;
import static ga.winterhills.transly.MainActivity.APP_PREFERENCES_ENABLED_VIBRATION;


public class MainFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener, ExerciseMakerFinished {


    TextView TVMain, one_t, two_t, three_t, four_t;
    Button one, two, three, four, dic_but, set_but;
    ImageView trash_img, tribles;
    View.DragShadowBuilder TVMainShadow;
    boolean ans1, ans2, ans3, ans4;
    String dic_choise;
    SharedPreferences mSettings;
    Queue<Exercise> exercises;
    ExerciseMaker exerciseMakerTask1;
    onWordDeletedListener wordDeletedListener;
    onDicButtonClickedListener dicButtonClickedListener;
    onSetButtonClickedListener setButtonClickedListener;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        onStart();
    }

    @Override
    public void onExerciseMakerFinished() {
        showExercise();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            wordDeletedListener = (onWordDeletedListener) activity;
            dicButtonClickedListener = (onDicButtonClickedListener) activity;
            setButtonClickedListener = (onSetButtonClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        exercises = new LinkedList<>();
        super.onCreate(savedInstanceState);
        //     dbHelper = new DataBase(getActivity());

        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        // mSettings = getActivity().getSharedPreferences(SharedPrefHelper.DATA_WEATHER_NOW, 0);
        mSettings.registerOnSharedPreferenceChangeListener(this);


    }

    @Override
    public void onStart() {
        super.onStart();
        exercises.clear();
        dic_choise = mSettings.getString(APP_PREFERENCES_ENABLED_DICTIONARY, null);
        one_t.setText(R.string.WHEN);
        two_t.setText(R.string.BEAUTY);
        three_t.setText(R.string.BRINGS);
        four_t.setText(R.string.KNOWLEDGE);
        exerciseMakerTask1 = new ExerciseMaker(this, true);
        exerciseMakerTask1.execute(exercises);
        ExerciseMaker exerciseMakerTask2 = new ExerciseMaker(this, false);
        exerciseMakerTask2.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exercises);
        ExerciseMaker exerciseMakerTask3 = new ExerciseMaker(this, false);
        exerciseMakerTask3.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exercises);
        ExerciseMaker exerciseMakerTask4 = new ExerciseMaker(this, false);
        exerciseMakerTask4.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, exercises);
        dic_choise = mSettings.getString(APP_PREFERENCES_ENABLED_DICTIONARY, null);
    }

    public void showExercise() {
        one.setBackgroundResource(R.drawable.button);
        two.setBackgroundResource(R.drawable.button);
        three.setBackgroundResource(R.drawable.button);
        four.setBackgroundResource(R.drawable.button);
        Exercise tmp = exercises.poll();
        TVMain.setText(tmp.getETVMain());
        one_t.setText(tmp.getOne());
        two_t.setText(tmp.getTwo());
        three_t.setText(tmp.getThree());
        four_t.setText(tmp.getFour());
        ans1 = tmp.isAns1();
        ans2 = tmp.isAns2();
        ans3 = tmp.isAns3();
        ans4 = tmp.isAns4();
        Animation fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
        one.setBackgroundResource(R.drawable.button);
        two.setBackgroundResource(R.drawable.button);
        three.setBackgroundResource(R.drawable.button);
        four.setBackgroundResource(R.drawable.button);
        one_t.startAnimation(fadeIn);
        two_t.startAnimation(fadeIn);
        three_t.startAnimation(fadeIn);
        four_t.startAnimation(fadeIn);
        one.startAnimation(fadeIn);
        two.startAnimation(fadeIn);
        three.startAnimation(fadeIn);
        four.startAnimation(fadeIn);
        TVMain.startAnimation(fadeIn);
        tribles.startAnimation(fadeIn);
        one.setVisibility(View.VISIBLE);
        two.setVisibility(View.VISIBLE);
        three.setVisibility(View.VISIBLE);
        four.setVisibility(View.VISIBLE);
        TVMain.setVisibility(View.VISIBLE);
        tribles.setVisibility(View.VISIBLE);
    }

    public void makeExercise() {
        ExerciseMaker exerciseMakerTask = new ExerciseMaker(this, false);
        exerciseMakerTask.execute(exercises);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        final android.os.Handler handler = new android.os.Handler();
//        Runnable maker_call = new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        };
//        handler.postDelayed(maker_call, 500);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        TVMain = view.findViewById(R.id.TVMain);
        TVMain.setSelected(true);
        tribles = view.findViewById(R.id.tribles);
        trash_img = view.findViewById(R.id.delete);
        one_t = (ScrollingTextView) view.findViewById(R.id.one_t);
        two_t = (ScrollingTextView) view.findViewById(R.id.two_t);
        three_t = (ScrollingTextView) view.findViewById(R.id.three_t);
        four_t = (ScrollingTextView) view.findViewById(R.id.four_t);
        TVMain.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (TVMain.getText() != "TRANSLY") {
                    Animation fadeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);
                    Animation fadeOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);
                    one.startAnimation(fadeOutAnimation);
                    one.setVisibility(View.INVISIBLE);
                    two.startAnimation(fadeOutAnimation);
                    two.setVisibility(View.INVISIBLE);
                    three.startAnimation(fadeOutAnimation);
                    three.setVisibility(View.INVISIBLE);
                    four.startAnimation(fadeOutAnimation);
                    four.setVisibility(View.INVISIBLE);
                    one_t.startAnimation(fadeOutAnimation);
                    one_t.setVisibility(View.INVISIBLE);
                    two_t.startAnimation(fadeOutAnimation);
                    two_t.setVisibility(View.INVISIBLE);
                    three_t.startAnimation(fadeOutAnimation);
                    three_t.setVisibility(View.INVISIBLE);
                    four_t.startAnimation(fadeOutAnimation);
                    four_t.setVisibility(View.INVISIBLE);
                    trash_img.startAnimation(fadeInAnimation);
                    trash_img.setVisibility(View.VISIBLE);
                    ClipData data = ClipData.newPlainText("", "");
                    TVMainShadow = new View.DragShadowBuilder(TVMain);
//                    TVMainShadow.onDrawShadow(null);
                    v.startDrag(data, TVMainShadow, v, 0);
                }
                return true;
            }
        });

        View.OnDragListener dragListener = new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int dragEvent = event.getAction();
                Animation fadeInAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fadein);
                Animation fadeOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.fadeout);
                switch (dragEvent) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        one.startAnimation(fadeInAnimation);
                        one.setVisibility(View.VISIBLE);
                        two.startAnimation(fadeInAnimation);
                        two.setVisibility(View.VISIBLE);
                        three.startAnimation(fadeInAnimation);
                        three.setVisibility(View.VISIBLE);
                        four.startAnimation(fadeInAnimation);
                        four.setVisibility(View.VISIBLE);
                        //TVMain.startAnimation(fadeOutAnimation);
                        trash_img.startAnimation(fadeOutAnimation);
                        trash_img.setVisibility(View.INVISIBLE);
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        View view = (View) event.getLocalState();
                        if (view.getId() == R.id.delete) {
                            trash_img.setVisibility(View.INVISIBLE);
                        }
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:

                        break;
                    case DragEvent.ACTION_DROP:
                        try {
                            DeleteWord deleteWordTask = new DeleteWord();
                            deleteWordTask.execute(String.valueOf(TVMain.getText()));
                            wordDeletedListener.wordDeleted();
                        } catch (IllegalArgumentException | CursorIndexOutOfBoundsException e) {
                            TVMain.setText(R.string.TRANSLY);
                        }
                        one.startAnimation(fadeInAnimation);
                        one.setVisibility(View.VISIBLE);
                        two.startAnimation(fadeInAnimation);
                        two.setVisibility(View.VISIBLE);
                        three.startAnimation(fadeInAnimation);
                        three.setVisibility(View.VISIBLE);
                        four.startAnimation(fadeInAnimation);
                        four.setVisibility(View.VISIBLE);
                        one_t.startAnimation(fadeInAnimation);
                        one_t.setVisibility(View.VISIBLE);
                        two_t.startAnimation(fadeInAnimation);
                        two_t.setVisibility(View.VISIBLE);
                        three_t.startAnimation(fadeInAnimation);
                        three_t.setVisibility(View.VISIBLE);
                        four_t.startAnimation(fadeInAnimation);
                        four_t.setVisibility(View.VISIBLE);
                        //TVMain.startAnimation(fadeOutAnimation);
                        trash_img.startAnimation(fadeOutAnimation);
                        trash_img.setVisibility(View.INVISIBLE);
                        makeExercise();
                        return true;
                }
                return true;
            }
        };

        trash_img.setOnDragListener(dragListener);

        one = view.findViewById(R.id.one);
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ans1) {
                    one.setBackgroundResource(R.drawable.green_button);
                    makeExercise();
                    AddRight addRightTask = new AddRight();
                    addRightTask.execute(String.valueOf(TVMain.getText()));
                    final android.os.Handler handler = new android.os.Handler();
                    Runnable maker_call = new Runnable() {
                        @Override
                        public void run() {
                            showExercise();
                        }
                    };
                    handler.postDelayed(maker_call, 100);
                } else {
                    one.setBackgroundResource(R.drawable.red_button);
                    AddMistake addMistakeTask = new AddMistake();
                    addMistakeTask.execute(String.valueOf(TVMain.getText()));
                    Vibrator vibrator;
                    if (mSettings.getBoolean(APP_PREFERENCES_ENABLED_VIBRATION, false)) {
                        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(50);
                    }
                    if (mSettings.getBoolean(APP_PREFERENCES_ENABLED_TONEXT, false)) {
                        final android.os.Handler handler = new android.os.Handler();
                        Runnable maker_call = new Runnable() {
                            @Override
                            public void run() {
                                showExercise();
                                makeExercise();
                            }
                        };
                        handler.postDelayed(maker_call, 50);
                    }
                }
            }
        });
        two = view.findViewById(R.id.two);
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ans2) {
                    two.setBackgroundResource(R.drawable.green_button);
                    makeExercise();
                    AddRight addRightTask = new AddRight();
                    addRightTask.execute(String.valueOf(TVMain.getText()));
                    final android.os.Handler handler = new android.os.Handler();
                    Runnable maker_call = new Runnable() {
                        @Override
                        public void run() {
                            showExercise();
                        }
                    };
                    handler.postDelayed(maker_call, 100);
                } else {
                    two.setBackgroundResource(R.drawable.red_button);
                    AddMistake addMistakeTask = new AddMistake();
                    addMistakeTask.execute(String.valueOf(TVMain.getText()));
                    Vibrator vibrator;
                    if (mSettings.getBoolean(APP_PREFERENCES_ENABLED_VIBRATION, false)) {
                        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(50);
                    }
                    if (mSettings.getBoolean(APP_PREFERENCES_ENABLED_TONEXT, false)) {
                        final android.os.Handler handler = new android.os.Handler();
                        Runnable maker_call = new Runnable() {
                            @Override
                            public void run() {
                                showExercise();
                                makeExercise();
                            }
                        };
                        handler.postDelayed(maker_call, 50);
                    }
                }
            }
        });
        three = view.findViewById(R.id.three);
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ans3) {
                    three.setBackgroundResource(R.drawable.green_button);
                    makeExercise();
                    AddRight addRightTask = new AddRight();
                    addRightTask.execute(String.valueOf(TVMain.getText()));
                    final android.os.Handler handler = new android.os.Handler();
                    Runnable maker_call = new Runnable() {
                        @Override
                        public void run() {
                            showExercise();
                        }
                    };
                    handler.postDelayed(maker_call, 100);
                } else {
                    three.setBackgroundResource(R.drawable.red_button);
                    AddMistake addMistakeTask = new AddMistake();
                    addMistakeTask.execute(String.valueOf(TVMain.getText()));
                    Vibrator vibrator;
                    if (mSettings.getBoolean(APP_PREFERENCES_ENABLED_VIBRATION, false)) {
                        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(50);
                    }
                    if (mSettings.getBoolean(APP_PREFERENCES_ENABLED_TONEXT, false)) {
                        final android.os.Handler handler = new android.os.Handler();
                        Runnable maker_call = new Runnable() {
                            @Override
                            public void run() {
                                showExercise();
                                makeExercise();

                            }
                        };
                        handler.postDelayed(maker_call, 50);
                    }
                }
            }
        });
        four = view.findViewById(R.id.four);
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ans4) {
                    four.setBackgroundResource(R.drawable.green_button);
                    makeExercise();
                    AddRight addRightTask = new AddRight();
                    addRightTask.execute(String.valueOf(TVMain.getText()));
                    final android.os.Handler handler = new android.os.Handler();
                    Runnable maker_call = new Runnable() {
                        @Override
                        public void run() {
                            showExercise();
                        }
                    };
                    handler.postDelayed(maker_call, 100);
                } else {
                    four.setBackgroundResource(R.drawable.red_button);
                    AddMistake addMistakeTask = new AddMistake();
                    addMistakeTask.execute(String.valueOf(TVMain.getText()));
                    Vibrator vibrator;
                    if (mSettings.getBoolean(APP_PREFERENCES_ENABLED_VIBRATION, false)) {
                        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(50);
                    }
                    if (mSettings.getBoolean(APP_PREFERENCES_ENABLED_TONEXT, false)) {
                        final android.os.Handler handler = new android.os.Handler();
                        Runnable maker_call = new Runnable() {
                            @Override
                            public void run() {
                                showExercise();
                                makeExercise();
                            }
                        };
                        handler.postDelayed(maker_call, 50);
                    }
                }
            }
        });
        dic_but = view.findViewById(R.id.dicbut);
        dic_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dicButtonClickedListener.dicButtonClicked();
            }
        });
        set_but = view.findViewById(R.id.setbut);
        set_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonClickedListener.setButtonClicked();
            }
        });

        return view;

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    interface onWordDeletedListener {
        void wordDeleted();
    }


    interface onDicButtonClickedListener {
        void dicButtonClicked();
    }


    interface onSetButtonClickedListener {
        void setButtonClicked();
    }

    class DeleteWord extends AsyncTask<String, Void, Void> {

        String TVMain_val;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // TVMain_val = String.valueOf(TVMain.getText());
        }

        @Override
        protected Void doInBackground(String... params) {
            SharedPreferences mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            DataBase dbHelper = new DataBase(getActivity());
            String dic_choice = mSettings.getString(APP_PREFERENCES_ENABLED_DICTIONARY, null);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(dic_choice, "COLUMN_WORD = ?", new String[]{params[0]});
            return null;
        }
    }

    class AddMistake extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            SharedPreferences mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            DataBase dbHelper = new DataBase(getActivity());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String dic_choice = mSettings.getString(APP_PREFERENCES_ENABLED_DICTIONARY, null);
//            Cursor c = db.rawQuery("SELECT * FROM " + dic_choice, null );
            db.execSQL("UPDATE " + dic_choice + " SET WRONG = WRONG + 1 WHERE COLUMN_WORD = '" + params[0] + "'");
            return null;
        }
    }

    class AddRight extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            SharedPreferences mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            DataBase dbHelper = new DataBase(getActivity());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String dic_choice = mSettings.getString(APP_PREFERENCES_ENABLED_DICTIONARY, null);
            //           Cursor c = db.rawQuery("SELECT * FROM " + dic_choice, null );
            db.execSQL("UPDATE " + dic_choice + " SET RIGHT = RIGHT + 1 WHERE COLUMN_WORD = '" + params[0] + "'");
            return null;
        }
    }
}
