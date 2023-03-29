/*
 * This is the source code of Telegram for Android v. 1.3.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2017.
 */

package com.mahdi.car.messenger;

import android.app.IntentService;
import android.content.Intent;

public class NotificationsService extends IntentService {

    public NotificationsService() {
        super("NotificationRepeat");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent == null) {
            return;
        }
        //final int currentAccount = intent.getIntExtra("currentAccount", UserConfig.selectedAccount);
        AndroidUtilities.run(new Runnable() {
            @Override
            public void run() {
                //NotificationsController.getInstance(currentAccount).repeatNotificationMaybe();
            }
        });
    }

}
