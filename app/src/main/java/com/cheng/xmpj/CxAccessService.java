package com.cheng.xmpj;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.cheng.xmpj.XLogger.XLogger;

import java.util.HashMap;
import java.util.List;

import static android.R.attr.id;
import static android.R.id.list_container;

/**
 * Created by Junxiang.Cheng on 17-4-7.
 */
public class CxAccessService extends BaseAccessService {
    private boolean end;
    private HashMap<String, Object> accessisbilityMap = new HashMap<String, Object>();
    private boolean isGoback;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        XLogger.v("eventType:" + eventType);
        String pkgname = event.getPackageName().toString();
        CxAccessService service = this;
        if(( eventType == AccessibilityEvent.TYPE_VIEW_SCROLLED ||
                eventType== AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED)
                && event.getPackageName() != null){
//        switch (eventType) {
//            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
            String clazzName = event.getClassName().toString();
            XLogger.e(clazzName);
//            AccessibilityNodeInfo nodeInfo = event.getSource();
            AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();

            XLogger.i("eventType:" + eventType);
            XLogger.i( "悬浮窗：" + clazzName);
            if (clazzName.equals("com.miui.permcenter.permissions.PermissionsEditorActivity")) {
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
            if(clazzName.equals(MainActivity.HUAWEI_APP_PERMISSION_ACTIVITY_NAME)){
                XLogger.e("nodeInfo----"+nodeInfo.getChildCount()+"");
                for(int i=0;i<nodeInfo.getChildCount();i++) {
                    XLogger.e("nodeInfo--"+i+"----" + nodeInfo.getChild(i).getViewIdResourceName());
                }
                if (nodeInfo == null) {
                    return;
                }

                goes(nodeInfo,0);
//                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("触宝电话");
//                XLogger.e("list---size"+list.size()+"");
//
//
//                if (list != null && list.size() > 0) {
//                    list = nodeInfo.findAccessibilityNodeInfosByText("设置单项权限");
//                    if (list != null && list.size() > 0) {
//                        performMore(service, nodeInfo,
//                                "触宝电话","power_step_1");
//                        return;
//                    }
//
//
//                }

            }
        }
    }

    public void goes(AccessibilityNodeInfo node,int x){
        XLogger.e(x+"--------AAAAXXXXXXX\n"+node+"\n");

        if (node == null)
            return;

        int num = node.getChildCount();
        AccessibilityNodeInfo childnode;
        for(int i=0;i<num;i++){
            childnode = node.getChild(i);
            goes(childnode,x+1);
        }
        return;
    }

    private AccessibilityNodeInfo recycle(AccessibilityNodeInfo info) {

        if (info!= null && info.getClassName() != null
                && info.getClassName().equals("android.widget.ListView") ){

            return info;
        }
        if (info == null || info.getChildCount() == 0) {
            return null;
        }
        for (int i = 0; i < info.getChildCount(); i++) {
            AccessibilityNodeInfo item = recycle(info.getChild(i));
            if (item != null) {
                return item;
            }
        }
        return null;
    }

    private void performMore(CxAccessService service, AccessibilityNodeInfo nodeInfo, String tagText, String key) {
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(tagText);
        if (list == null || list.size() == 0) {
            AccessibilityNodeInfo info = recycle(nodeInfo);
            XLogger.e("list--size"+list.size());
            if(info != null) {
                info.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            }
            return;
        }
        AccessibilityNodeInfo parent = list.get(0).getParent();
        if (parent != null) {
        //    TLog.d(TAG, "onAccessibilityEvent performMore= " + list.size() + ",isback=" + isGoback);
            if (!accessisbilityMap.containsKey(key)) {
                accessisbilityMap.put(key,1);
                isGoback = false;
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            } else {
                service.performGlobalAction(GLOBAL_ACTION_BACK);
            }
        }
    }

}
