package com.mahdi.car.share;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import com.mahdi.car.App;
import com.mahdi.car.library.autolinklibrary.AutoLinkMode;

public class AutoLinkText {

    private static volatile AutoLinkText Instance = null;

    public static AutoLinkText getInstance() {

        AutoLinkText localInstance = Instance;
        if (localInstance == null) {
            synchronized (AutoLinkText.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new AutoLinkText();
                }
            }
        }
        return localInstance;
    }


    public void click(AutoLinkMode autoLinkMode, String matchedText, AutoLinkTextDelegate delegate) {

        String text = matchedText;
        text = text.replace(" ", "");
        text = text.replace("\n", "");
        text = text.replace("\r", "");
        text = text.replace("<br/>", "");

        if (autoLinkMode.equals(AutoLinkMode.MODE_MENTION)) {

            text = text.replace("@", "");
            delegate.mention(text);

        } else if (autoLinkMode.equals(AutoLinkMode.MODE_HASHTAG)) {

            text = text.replace("#", "");
            delegate.hashtag(text);

        } else if (autoLinkMode.equals(AutoLinkMode.MODE_URL)) {

            try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(text));
                App.getContext().startActivity(i);
            } catch (ActivityNotFoundException e) {

            }

        } else if (autoLinkMode.equals(AutoLinkMode.MODE_EMAIL)) {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{text});

            try {
                App.getContext().startActivity(Intent.createChooser(intent, "How to click mail?"));
            } catch (ActivityNotFoundException ex) {
                //do something else
            }

        } else if (autoLinkMode.equals(AutoLinkMode.MODE_PHONE)) {

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + text));
            App.getContext().startActivity(intent);

        }
    }

}
