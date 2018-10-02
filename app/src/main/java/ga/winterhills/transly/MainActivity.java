package ga.winterhills.transly;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;


public class MainActivity extends AppCompatActivity implements MainFragment.onDicButtonClickedListener,
        MainFragment.onSetButtonClickedListener,
        SettingsFragment.onButtonToLeftClickedListener,
        DictionaryFragment.onButtonToRightClickedListener,
        MainFragment.onWordDeletedListener,
        DictionaryFragment.onDictionaryChangedListener {

    public static final String APP_PREFERENCES = "settings";
    public static final String APP_PREFERENCES_ENABLED_DICTIONARY = "Dictionary";// = "EN_RU_DEFAULT";
    public static final String APP_PREFERENCES_ENABLED_STATS = "Statistics";// = true;
    public static final String APP_PREFERENCES_ENABLED_TONEXT = "To next";// =
    public static final String APP_PREFERENCES_ENABLED_VIBRATION = "Vibration";
    public static final String APP_PREFERENCES_ENABLED_APP_LANGUAGE = "Language";
    DictionaryFragment dictionaryFragment;
    MainFragment mainFragment;
    SettingsFragment settingsFragment;
    SectionsPageAdapter adapter;
    SharedPreferences mSettings;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dictionaryFragment = new DictionaryFragment();
        mainFragment = new MainFragment();
        settingsFragment = new SettingsFragment();

        // SectionsPageAdapter mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);
        mViewPager.setCurrentItem(1);


        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        boolean hasVisited = mSettings.getBoolean("hasVisited", false);
        if (!hasVisited) {
            SharedPreferences.Editor e = mSettings.edit();
            e.putBoolean(APP_PREFERENCES_ENABLED_STATS, true);
            e.putBoolean(APP_PREFERENCES_ENABLED_TONEXT, false);
            e.putBoolean(APP_PREFERENCES_ENABLED_VIBRATION, true);
            e.putString(APP_PREFERENCES_ENABLED_DICTIONARY, "EN_RU_DEFAULT");
            // выводим нужную активность

            e.putBoolean("hasVisited", true);
            e.apply(); // не забудьте подтвердить изменения
        }

//        String en_dic = mSettings.getString(APP_PREFERENCES_ENABLED_DICTIONARY, "null");
//        if (en_dic.equals("null")) {
//            SharedPreferences.Editor e = mSettings.edit();
//            e.putString(APP_PREFERENCES_ENABLED_DICTIONARY, "EN_RU_DEFAULT");
//            e.apply();
//        }
//        String en_lang = mSettings.getString(APP_PREFERENCES_ENABLED_DICTIONARY, "null");
//        // assert en_lang != null;
//        if (en_lang.equals("null")) {
//            SharedPreferences.Editor e = mSettings.edit();
//            e.putString(APP_PREFERENCES_ENABLED_APP_LANGUAGE, "Default");
//            e.apply();
//        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(dictionaryFragment, "dictionary");
        adapter.addFragment(mainFragment, "main");
        adapter.addFragment(settingsFragment, "settings");
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() != 1) mViewPager.setCurrentItem(1);
        else finish();
    }


    @Override
    public void dicButtonClicked() {
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void setButtonClicked() {
        mViewPager.setCurrentItem(2);
    }


    @Override
    public void ButtonToLeftClicked() {
        mViewPager.setCurrentItem(1);
    }

    @Override
    public void ButtonToRightClicked() {
        mViewPager.setCurrentItem(1);
    }

    @Override
    public void wordDeleted() {
        adapter.mFragmentList.get(0).onStart();
    }

    @Override
    public void DictionaryChanged() {
        adapter.mFragmentList.get(1).onStart();
    }
}

