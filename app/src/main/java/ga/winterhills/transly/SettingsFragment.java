package ga.winterhills.transly;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import static ga.winterhills.transly.MainActivity.APP_PREFERENCES;
import static ga.winterhills.transly.MainActivity.APP_PREFERENCES_ENABLED_STATS;
import static ga.winterhills.transly.MainActivity.APP_PREFERENCES_ENABLED_TONEXT;
import static ga.winterhills.transly.MainActivity.APP_PREFERENCES_ENABLED_VIBRATION;

public class SettingsFragment extends Fragment {

    onButtonToLeftClickedListener buttonToLeftClickedListener;
    SharedPreferences mSettings;
    Switch vibe_switch;
    Switch stat_switch;
    Switch tonext_switch;
    private Button button_back;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            buttonToLeftClickedListener = (onButtonToLeftClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onDictionaryChangedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        button_back = view.findViewById(R.id.button_to_left);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonToLeftClickedListener.ButtonToLeftClicked();
            }
        });
        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        vibe_switch = view.findViewById(R.id.switch_vibe);
        vibe_switch.setChecked(mSettings.getBoolean(APP_PREFERENCES_ENABLED_VIBRATION, false));
        vibe_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    SharedPreferences.Editor e = mSettings.edit();
                    e.putBoolean(APP_PREFERENCES_ENABLED_VIBRATION, true);
                    e.apply();
                } else {
                    SharedPreferences.Editor e = mSettings.edit();
                    e.putBoolean(APP_PREFERENCES_ENABLED_VIBRATION, false);
                    e.apply();
                }
            }
        });
        stat_switch = view.findViewById(R.id.switch_stat);
        stat_switch.setChecked(mSettings.getBoolean(APP_PREFERENCES_ENABLED_STATS, false));
        stat_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    SharedPreferences.Editor e = mSettings.edit();
                    e.putBoolean(APP_PREFERENCES_ENABLED_STATS, true);
                    e.apply();
                } else {
                    SharedPreferences.Editor e = mSettings.edit();
                    e.putBoolean(APP_PREFERENCES_ENABLED_STATS, false);
                    e.apply();
                }
            }
        });
        tonext_switch = view.findViewById(R.id.switch_tonext);
        tonext_switch.setChecked(mSettings.getBoolean(APP_PREFERENCES_ENABLED_TONEXT, false));
        tonext_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    SharedPreferences.Editor e = mSettings.edit();
                    e.putBoolean(APP_PREFERENCES_ENABLED_TONEXT, true);
                    e.apply();
                } else {
                    SharedPreferences.Editor e = mSettings.edit();
                    e.putBoolean(APP_PREFERENCES_ENABLED_TONEXT, false);
                    e.apply();
                }
            }
        });

        return view;
    }

    interface onButtonToLeftClickedListener {
        void ButtonToLeftClicked();
    }
}
