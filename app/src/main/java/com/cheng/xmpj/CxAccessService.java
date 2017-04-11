package com.cheng.xmpj;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.cheng.xmpj.XLogger.XLogger;

/**
 * Created by Junxiang.Cheng on 17-4-7.
 */
public class CxAccessService extends BaseAccessService {
    private boolean end;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        XLogger.v("eventType:" + eventType);

        switch (eventType) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                String clazzName = event.getClassName().toString();
                AccessibilityNodeInfo nodeInfo = event.getSource();
                XLogger.i( "悬浮窗：" + clazzName);
                if (clazzName.equals("com.miui.permcenter.permissions.AppPermissionsEditorActivity")) {
                    XLogger.i("Permissions"+end);
                    if (end) {
                        clickByText(nodeInfo, "XMPJ");
                    } else {
                        boolean access = clickByText(nodeInfo, "显示悬浮窗");
                        XLogger.i("access" + access);
                    }
                }
                if (clazzName.equals("miui.app.AlertDialog")) {
                    end = clickByText(nodeInfo, "允许");
                    XLogger.i( "getClick:" + end);
                }
        }
    }

}
