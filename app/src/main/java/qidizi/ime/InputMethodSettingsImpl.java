package qidizi.ime;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;

import java.util.List;

class InputMethodSettingsImpl implements InputMethodSettingsInterface {
    private Preference mSubtypeEnablerPreference;
    private int mInputMethodSettingsCategoryTitleRes;
    private CharSequence mInputMethodSettingsCategoryTitle;
    private int mSubtypeEnablerTitleRes;
    private CharSequence mSubtypeEnablerTitle;
    private int mSubtypeEnablerIconRes;
    private Drawable mSubtypeEnablerIcon;
    private InputMethodManager mImm;
    private InputMethodInfo mImi;
    private Context mContext;

    /**
     * Initialize internal states of this object.
     * @param context the context for this application.
     * @param prefScreen a PreferenceScreen of PreferenceActivity or PreferenceFragment.
     * @return true if this application is an IME and has two or more subtypes, false otherwise.
     */
    public boolean init(final Context context, final PreferenceScreen prefScreen) {
        mContext = context;
        
        mImm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        mImi = getMyImi(context, mImm);
        if (mImi == null || mImi.getSubtypeCount() <= 1) {
        	//如果没有安装或是本包没有配置项目
            return false;
        }
        mSubtypeEnablerPreference = new Preference(context);
        //配置内部配置项目的点击处理
        mSubtypeEnablerPreference
                .setOnPreferenceClickListener(new OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        final CharSequence title = getSubtypeEnablerTitle(context);
                        final Intent intent =
                                new Intent(Settings.ACTION_INPUT_METHOD_SUBTYPE_SETTINGS);
                        intent.putExtra(Settings.EXTRA_INPUT_METHOD_ID, mImi.getId());
                        if (!TextUtils.isEmpty(title)) {
                            intent.putExtra(Intent.EXTRA_TITLE, title);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        
                        return true;
                    }
                });
                
        //把内部配置项目加入配置列表中
        prefScreen.addPreference(mSubtypeEnablerPreference);
        updateSubtypeEnabler();
        return true;
    }

    /*
     * 查看系统已经安装的输入法,如果包名跟自己相同,就认为是自己的,就返回输入信息
     */
    private static InputMethodInfo getMyImi(Context context, InputMethodManager imm) {
    	//获取安装所有输入法列表
        final List<InputMethodInfo> imis = imm.getInputMethodList();
        for (int i = 0; i < imis.size(); ++i) {
            final InputMethodInfo imi = imis.get(i);
            if (imis.get(i).getPackageName().equals(context.getPackageName())) {
                return imi;//某输入法包名跟自己相同就返回
            }
        }
        return null;
    }

    private static String getEnabledSubtypesLabel(
            Context context, InputMethodManager imm, InputMethodInfo imi) {
        if (context == null || imm == null || imi == null) return null;
        final List<InputMethodSubtype> subtypes = imm.getEnabledInputMethodSubtypeList(imi, true);
        final StringBuilder sb = new StringBuilder();
        final int N = subtypes.size();
        for (int i = 0; i < N; ++i) {
            final InputMethodSubtype subtype = subtypes.get(i);
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(subtype.getDisplayName(context, imi.getPackageName(),
                    imi.getServiceInfo().applicationInfo));
        }
        return sb.toString();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void setInputMethodSettingsCategoryTitle(int resId) {
        mInputMethodSettingsCategoryTitleRes = resId;
        updateSubtypeEnabler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInputMethodSettingsCategoryTitle(CharSequence title) {
        mInputMethodSettingsCategoryTitleRes = 0;
        mInputMethodSettingsCategoryTitle = title;
        updateSubtypeEnabler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSubtypeEnablerTitle(int resId) {
        mSubtypeEnablerTitleRes = resId;
        updateSubtypeEnabler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSubtypeEnablerTitle(CharSequence title) {
        mSubtypeEnablerTitleRes = 0;
        mSubtypeEnablerTitle = title;
        updateSubtypeEnabler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSubtypeEnablerIcon(int resId) {
        mSubtypeEnablerIconRes = resId;
        updateSubtypeEnabler();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSubtypeEnablerIcon(Drawable drawable) {
        mSubtypeEnablerIconRes = 0;
        mSubtypeEnablerIcon = drawable;
        updateSubtypeEnabler();
    }

    private CharSequence getSubtypeEnablerTitle(Context context) {
        if (mSubtypeEnablerTitleRes != 0) {
            return context.getString(mSubtypeEnablerTitleRes);
        } else {
            return mSubtypeEnablerTitle;
        }
    }

    public void updateSubtypeEnabler() {
        if (mSubtypeEnablerPreference != null) {
            if (mSubtypeEnablerTitleRes != 0) {
                mSubtypeEnablerPreference.setTitle(mSubtypeEnablerTitleRes);
            } else if (!TextUtils.isEmpty(mSubtypeEnablerTitle)) {
                mSubtypeEnablerPreference.setTitle(mSubtypeEnablerTitle);
            }
            final String summary = getEnabledSubtypesLabel(mContext, mImm, mImi);
            if (!TextUtils.isEmpty(summary)) {
                mSubtypeEnablerPreference.setSummary(summary);
            }
            if (mSubtypeEnablerIconRes != 0) {
                mSubtypeEnablerPreference.setIcon(mSubtypeEnablerIconRes);
            } else if (mSubtypeEnablerIcon != null) {
                mSubtypeEnablerPreference.setIcon(mSubtypeEnablerIcon);
            }            
        }
    }
}
