package com.rogergcc.schedulealarmworkmanager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * Created by rogergcc on 10/04/2020.
 * Copyright Ⓒ 2020 . All rights reserved.
 */
public class WorkManagerNotification extends Worker {

    public WorkManagerNotification(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public  static void saveNofification(long duration, Data data, String tag){
        OneTimeWorkRequest notificationWork = new OneTimeWorkRequest.Builder(WorkManagerNotification.class)
                                                .setInitialDelay(duration, TimeUnit.MILLISECONDS).addTag(tag)
                                                .setInputData(data).build();

        WorkManager instaWorkManager = WorkManager.getInstance();
        instaWorkManager.enqueue(notificationWork);
    }
    @NonNull
    @Override
    public Result doWork() {
        String title = getInputData().getString("title");
        String message= getInputData().getString("message");
        int id = (int) getInputData().getLong("idNotification",0);

        Context context = this.getApplicationContext();

        // Show Notification
        NotificationUtil notificationUtil = new NotificationUtil(context);
        if (title != null) {
            if (message != null) {
                notificationUtil.showNotification(title, message);
            }
        }

        return Result.success();
    }

}
