package com.cb.vmss.gcm.notification;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {
	 // give your server registration url here
    static final String SERVER_URL = "http://45.55.152.215/dev/sabjiatdoor/services/notification/register"; 
 
    // Google project id
    public  static final String SENDER_ID = "399583839789"; 
    										 
    /**
     * Tag used on log messages.
     */
    public static final String TAG = "SubjiAtDoorGCM";
 
    public static final String DISPLAY_MESSAGE_ACTION =
            "com.cb.vmss.gcm.DISPLAY_MESSAGE";
 
    public static final String EXTRA_MESSAGE = "message";
 
    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
