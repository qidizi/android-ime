package qidizi.ime;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Displays the IME preferences inside the input method setting.
 */
public class ImePreferences extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //修改top activity的标题bar成可以点击返回的功能
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    ;
    /*
     * 修改启动intent，让总是启动器设置activity
	 */

    @Override
    public Intent getIntent() {
        final Intent modIntent = new Intent(super.getIntent());
        modIntent.putExtra(EXTRA_SHOW_FRAGMENT, Settings.class.getName());
        modIntent.putExtra(EXTRA_NO_HEADERS, true);
        return modIntent;
    }

    /*
     * setings class exists
     */
    @Override
    protected boolean isValidFragment(final String fragmentName) {
        return Settings.class.getName().equals(fragmentName);
    }

    /*
     * 内部类
     */
    public static class Settings extends InputMethodSettingsFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setSubtypeEnablerTitle(R.string.select_language);
            //append自定义配置项目
            addPreferencesFromResource(R.xml.ime_preferences);
        }
    }
}
