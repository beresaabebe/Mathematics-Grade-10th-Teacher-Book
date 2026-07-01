package com.beckytech.mathematicsgrade10thteacherbook;

import android.app.Activity;
import android.content.IntentSender;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.gms.tasks.Task;

public class UpdateManager {
    private final AppUpdateManager appUpdateManager;
    private final Activity activity;
    private static final int UPDATE_REQUEST_CODE = 1001;

    public UpdateManager(Activity activity) {
        this.activity = activity;
        this.appUpdateManager = AppUpdateManagerFactory.create(activity);
    }

    public void checkForUpdates() {
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AppUpdateInfo appUpdateInfo = task.getResult();
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                AppUpdateType.IMMEDIATE,
                                activity,
                                UPDATE_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void onResume() {
        appUpdateManager.getAppUpdateInfo().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AppUpdateInfo appUpdateInfo = task.getResult();
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                AppUpdateType.IMMEDIATE,
                                activity,
                                UPDATE_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}