package com.mahdi.car.setting;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;

import com.mahdi.car.core.BaseFragment;
import com.mahdi.car.core.RootView;
import com.mahdi.car.service.ServiceManager;
import com.mahdi.car.setting.cell.ProfileTextEditCell;
import com.mahdi.car.share.Button;
import com.mahdi.car.share.component.ui.LayoutHelper;

public class EditNameFragment extends BaseFragment {

    private ProfileTextEditCell editCell;
    private Button buttonSave;

    private SharedPreferences mirrorPreferences;
    private SharedPreferences.Editor mirrorPreferencesEdit;

    private Delegate delegate;
    private String username;

    EditNameFragment(String username) {
        this.username = username;
    }

    @Override
    public View createView(Context context) {
        loadingShow = false;
        config.hasServerData = false;

        super.createView(context);

        mirrorPreferences = context.getSharedPreferences("mirror", 0); // 0 - for private mode
        mirrorPreferencesEdit = mirrorPreferences.edit();

        toolbar.setName("Edit Name");

        editCell = new ProfileTextEditCell(context);
        editCell.setName(username);

        buttonSave = new Button(context);
        buttonSave.setTitle("Save");
        buttonSave.setDelegate((Button.Delegate) () -> {
            if (editCell.getText().length() > 2) {
                delegate.edit(editCell.getText());
            }
        });

        swipe.addView(editCell, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, 50, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 40, 40, 40, 0));
        swipe.addView(buttonSave, LayoutHelper.createFrame(200, 40, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 150, 0, 0));

        return contentView;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (ServiceManager.instance().webSocketIsOpen()) {
        } else {
            RootView.instance().floatViewParent.hide();
        }
    }

    public interface Delegate {
        void edit(String username);
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }
}




