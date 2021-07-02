package com.annis.mydemos.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityStack {
    private static ActivityStack stack;

    private List<Activity> activityList;

    private ActivityStack() {
        activityList = new ArrayList<>();
    }

    public static ActivityStack getInstance() {
        if (stack == null) {
            stack = new ActivityStack();
        }
        return stack;
    }

    public void add(Activity activity) {
        activityList.add(activity);
    }

    public void remove(Activity activity) {
        activityList.remove(activity);
    }

    public Activity getTop() {
        if (activityList == null || activityList.size() == 0) {
            return null;
        }
        return activityList.get(activityList.size() - 1);
    }
}