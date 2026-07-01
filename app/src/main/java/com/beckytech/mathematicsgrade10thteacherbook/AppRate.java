package com.beckytech.mathematicsgrade10thteacherbook;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.gms.tasks.Task;

public class AppRate {
    private static final int LAUNCHES_UNTIL_PROMPT = 3;

    public static void app_launched(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) return;

        SharedPreferences.Editor editor = prefs.edit();
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            showRateDialog(activity, editor);
        }
        editor.apply();
    }

    public static void showRateDialog(final Activity activity, final SharedPreferences.Editor editor) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_rate, null);
        
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        TextView statusTv = view.findViewById(R.id.statusTv);
        
        ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            switch ((int) rating) {
                case 1: statusTv.setText("Bad"); break;
                case 2: statusTv.setText("Not good"); break;
                case 3: statusTv.setText("Somewhat good"); break;
                case 4: statusTv.setText("Very good"); break;
                case 5: statusTv.setText("Excellent"); break;
                default: statusTv.setText(""); break;
            }
        });

        builder.setView(view)
                .setTitle("Rate Us")
                .setPositiveButton("Rate", (dialog, which) -> {
                    if (ratingBar.getRating() >= 4) {
                        requestInAppReview(activity);
                    }
                    if (editor != null) {
                        editor.putBoolean("dontshowagain", true);
                        editor.apply();
                    }
                })
                .setNegativeButton("Remind Later", (dialog, which) -> dialog.dismiss())
                .setNeutralButton("No Thanks", (dialog, which) -> {
                    if (editor != null) {
                        editor.putBoolean("dontshowagain", true);
                        editor.apply();
                    }
                });
        builder.show();
    }

    private static void requestInAppReview(Activity activity) {
        ReviewManager manager = ReviewManagerFactory.create(activity);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(activity, reviewInfo);
                flow.addOnCompleteListener(task2 -> {});
            }
        });
    }
}