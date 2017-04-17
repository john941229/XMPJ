package com.cheng.xmpj;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

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

            String clazzName = event.getClassName().toString();
            AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();


            XLogger.e("!!!!!pkgname :"+event.getPackageName().toString());
            /*
            * 辅助功能自动退出-有未知bug
            * */
//            if(event.getPackageName().equals("com.android.settings")){
//                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(
//                        "Accessibility");
//                if (list != null && list.size() > 0) {
//                    String txt = "XXMMPPJJ";
//                    list = nodeInfo.findAccessibilityNodeInfosByText(txt);
//                    if (list != null && list.size() > 0) {
//                        service.performGlobalAction(GLOBAL_ACTION_BACK);
//                        return;
//                    }
//                }
//                String txt = "XXMMPPJJ";
//                list = nodeInfo.findAccessibilityNodeInfosByText(txt);
//                if (list != null && list.size() > 0) {
//                    service.performGlobalAction(GLOBAL_ACTION_BACK);
//                    return;
//                }
//            }

            if (event.getPackageName().equals("com.miui.securitycenter")) {
                if(nodeInfo == null){
                    return;
                }
                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(
                    "XMPJ"
                );
                if(list != null && list.size() >0) {
                    accessisbilityMap.put("isXMPJsetting",1);
                    if ( !accessisbilityMap.containsKey("dh") ) {
                        performMore(service, nodeInfo, "电话", "dh");
                        return;
                    }

                    if ( !accessisbilityMap.containsKey("xfc") ) {
                        performMore(service, nodeInfo, "显示悬浮窗", "xf");
                        return;
                    }

                }
            }
            if (clazzName.equals("miui.app.AlertDialog") && accessisbilityMap.get("isXMPJsetting").equals(1) ) {
                accessisbilityMap.put("isXMPJsetting",0);
                end = clickByText(nodeInfo, "允许");
            }
            if (event.getPackageName().equals("com.huawei.systemmanager")) {
                if(nodeInfo == null){
                    return;
                }
                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(
                        "权限管理"
                );
                if(list != null && list.size() >0) {
                    performMore(service, nodeInfo, "XMPJ", "HW_PJ_1");
                    return;
                }

            }
            if (event.getPackageName().equals("com.android.packageinstaller")) {
                if(nodeInfo == null){
                    return;
                }
                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(
                        "应用权限"
                );
                if(list != null && list.size() >0) {
                    performMore(service, nodeInfo, "设置单项权限", "HW_PJ_2");
                    return;
                }
            }
            if (event.getPackageName().equals("com.huawei.systemmanager")) {
                if(nodeInfo == null){
                    return;
                }
                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(
                        "XMPJ"
                );
                if(list != null && list.size() >0) {
                    performMore(service, nodeInfo, "信任此应用", "HW_PJ_3");
                    return;
                }
            }
//            if(clazzName.equals(MainActivity.HUAWEI_APP_PERMISSION_ACTIVITY_NAME)){
//                XLogger.e("nodeInfo----"+nodeInfo.getChildCount()+"");
//                for(int i=0;i<nodeInfo.getChildCount();i++) {
//                    XLogger.e("nodeInfo--"+i+"----" + nodeInfo.getChild(i).getViewIdResourceName());
//                }
//                if (nodeInfo == null) {
//                    return;
//                }
//
//                goes(nodeInfo,0);
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

//            }
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
            XLogger.e("accessibilityMap------"+accessisbilityMap);
            if (!accessisbilityMap.containsKey(key)) {
                accessisbilityMap.put(key,1);
                isGoback = false;
                Toast.makeText(this, tagText+"---已开启", Toast.LENGTH_SHORT).show();
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            } else {
                service.performGlobalAction(GLOBAL_ACTION_BACK);
            }
        }
    }

}
