package com.cheng.xmpj;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.os.Build;
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

    private String TAG = "XXMMPPJJ";

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


            XLogger.e("!!!!!pkgname :"+event.getPackageName().toString() +"\nclazzName:"+clazzName);
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
            //?????

            List <AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText("权限管理");
            if (list != null && !list.isEmpty()
                    && !( (nodeInfo.findAccessibilityNodeInfosByText("应用")  ).isEmpty()
                    || (nodeInfo.findAccessibilityNodeInfosByText("权限" ) ).isEmpty() ) ) {

                List<AccessibilityNodeInfo> mList = nodeInfo.findAccessibilityNodeInfosByText("应用");
                if(mList != null && ! mList.isEmpty()){
                    AccessibilityNodeInfo node = mList.get(0);
                    if(! node.isSelected() && !node.isChecked()){

                        performTab(nodeInfo,
                                "应用",
                                "huawei_permission_step_1");
                    } else {

                        performMore(service,nodeInfo,
                                "XMPJ",
                                "huawei_permission_step_2");
                    }
                }
            }

            list = nodeInfo.findAccessibilityNodeInfosByText("信任此应用");
            Log.e(TAG,"list = "+list);
            if (list != null && !list.isEmpty()) {

                performSwitch(service, nodeInfo,
                        "信任此应用", true, true, "huawei_permission_step_trust");
                performSwitch(service, nodeInfo,
                           "启动", true, true, "huawei_permission_step_autoboot");
            }


//            if (event.getPackageName().equals("com.miui.securitycenter")) {
//                if(nodeInfo == null){
//                    return;
//                }
//                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(
//                    "XMPJ"
//                );
//                if(list != null && list.size() >0) {
//                    accessisbilityMap.put("isXMPJsetting",1);
//                    if ( !accessisbilityMap.containsKey("dh") ) {
//                        performMore(service, nodeInfo, "电话", "dh");
//                        return;
//                    }
//
//                    if ( !accessisbilityMap.containsKey("xfc") ) {
//                        performMore(service, nodeInfo, "显示悬浮窗", "xf");
//                        return;
//                    }
//                }
//            }

//            if (clazzName.equals("miui.app.AlertDialog") && accessisbilityMap.get("isXMPJsetting").equals(1) ) {
//                accessisbilityMap.put("isXMPJsetting",0);
//                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(
//                        "允许"
//                );
//                if(list !=null && list.size()>0)
//                    end = clickByText(nodeInfo, "允许");
//                list = nodeInfo.findAccessibilityNodeInfosByText(
//                        "允许"
//                );
//                if(list !=null && list.size()>0)
//                    end = clickByText(nodeInfo, "确定");
//            }
//            if (event.getPackageName().equals("com.huawei.systemmanager") ) {
//                if(nodeInfo == null){
//                    return;
//                }
//                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(
//                        "权限管理"
//                );
//                if(list != null && list.size() >0) {
//                    performMore(service, nodeInfo,"应用","HW_PJ_0");
////                    performMore(service, nodeInfo, "XMPJ", "HW_PJ_1");
//                    return;
//                }
//
//            }
            if (event.getPackageName().equals("com.android.packageinstaller")) {
                if (nodeInfo == null) {
                    return;
                }
                list = nodeInfo.findAccessibilityNodeInfosByText(
                        "应用权限");
                if (list != null && list.size() > 0) {

                    performMore(service, nodeInfo, "设置单项权限", "huawei_permission_auto_step_2");
                    return;
                }
            }
        }
    }
//
//    public void goes(AccessibilityNodeInfo node,int x){
//        XLogger.e(x+"--------AAAAXXXXXXX\n"+node+"\n");
//
//        if (node == null)
//            return;
//
//        int num = node.getChildCount();
//        AccessibilityNodeInfo childnode;
//        for(int i=0;i<num;i++){
//            childnode = node.getChild(i);
//            goes(childnode,x+1);
//        }
//        return;
//    }
//
//    private AccessibilityNodeInfo recycle(AccessibilityNodeInfo info) {
//
//        if (info!= null && info.getClassName() != null
//                && info.getClassName().equals("android.widget.ListView") ){
//
//            return info;
//        }
//        if (info == null || info.getChildCount() == 0) {
//            return null;
//        }
//        for (int i = 0; i < info.getChildCount(); i++) {
//            AccessibilityNodeInfo item = recycle(info.getChild(i));
//            if (item != null) {
//                return item;
//            }
//        }
//        return null;
//    }
//    private void performMoreKey(CxAccessService service, AccessibilityNodeInfo nodeInfo, String tagText, String key) {
//        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(tagText);
//        if (list == null || list.size() == 0) {
//            AccessibilityNodeInfo info = recycle(nodeInfo);
//            XLogger.e("list--size" + list.size());
//            if (info != null) {
//                info.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
//            }
//            return;
//        }
//        AccessibilityNodeInfo parent = list.get(0).getParent();
//        if (parent != null) {
//            //    TLog.d(TAG, "onAccessibilityEvent performMore= " + list.size() + ",isback=" + isGoback);
//            XLogger.e("accessibilityMap------" + accessisbilityMap);
//            if (!accessisbilityMap.containsKey(key)) {
//                accessisbilityMap.put(key, 1);
//                isGoback = false;
////                Toast.makeText(this, tagText+"---已开启", Toast.LENGTH_SHORT).show();
//                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
////            } else {
////                service.performGlobalAction(GLOBAL_ACTION_BACK);
////            }
//            }
//        }
//    }
//
//    private void performMore(CxAccessService service, AccessibilityNodeInfo nodeInfo, String tagText, String key) {
//        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(tagText);
//        if (list == null || list.size() == 0) {
//            AccessibilityNodeInfo info = recycle(nodeInfo);
//            XLogger.e("list--size"+list.size());
//            if(info != null) {
//                info.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
//            }
//            return;
//        }
//        AccessibilityNodeInfo parent = list.get(0).getParent();
//        if (parent != null) {
//        //    TLog.d(TAG, "onAccessibilityEvent performMore= " + list.size() + ",isback=" + isGoback);
//            XLogger.e("accessibilityMap------"+accessisbilityMap);
//            if (!accessisbilityMap.containsKey(key)) {
//                accessisbilityMap.put(key,1);
//                isGoback = false;
//                Toast.makeText(this, tagText+"---已开启", Toast.LENGTH_SHORT).show();
//                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//            } else {
//                service.performGlobalAction(GLOBAL_ACTION_BACK);
//            }
//        }
//    }


    private AccessibilityNodeInfo recycle(AccessibilityNodeInfo info){
        return recycle(info,"");
    }

    private AccessibilityNodeInfo recycle(AccessibilityNodeInfo info,String key) {
        if (info!= null && info.getClassName() != null
                && (info.getClassName().equals("android.widget.ListView")
                || info.getClassName().equals("android.widget.GridView")) ){

                return info;
        }
        if (info == null || info.getChildCount() == 0) {
            return null;
        }
        for (int i = 0; i < info.getChildCount(); i++) {
            AccessibilityNodeInfo item = recycle(info.getChild(i),key);
            if (item != null) {
                return item;
            }
        }
        return null;
    }
    private void performMore(AccessibilityService service,AccessibilityNodeInfo nodeInfo, String tagText,String key) {
        if (accessisbilityMap.containsKey(key)) {
            service.performGlobalAction(GLOBAL_ACTION_BACK);
            return;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(tagText);
        if (list == null || list.size() == 0) {
            AccessibilityNodeInfo info = recycle(nodeInfo,key);
            if(info != null) {
                info.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            }
            return;
        }
        AccessibilityNodeInfo parent = list.get(0).getParent();
        if (parent != null) {
            if (!accessisbilityMap.containsKey(key)) {
                if( parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)){
                    accessisbilityMap.put(key,1);
                }
            }
        }
    }

    private boolean performSwitch(AccessibilityService service,AccessibilityNodeInfo nodeInfo,String tagText,String key) {
        return performSwitch(service,nodeInfo,tagText,true,true,key);
    }

    private AccessibilityNodeInfo getSwitch(AccessibilityNodeInfo node){

        AccessibilityNodeInfo item= null;

        while(node!=null){
//            TLog.e(TAG,"getSwitch = "+node);
            item = getSwitchCycle(node);
            if (item == null)
                node = node.getParent();
            else
                return item;
        }
        return null;
    }

    private AccessibilityNodeInfo getSwitchCycle(AccessibilityNodeInfo node){
        if(node != null && ( node.getClassName().equals("android.widget.Switch")
                || node.getClassName().equals("android.widget.CheckBox")) ){
            return node;
        }

        if(node == null || node.getChildCount() == 0){
            return null;
        }

        for(int i=0;i < node.getChildCount(); i++){
//            TLog.e(TAG,"       getSwitchCycle = "+node.getChild(i));
            AccessibilityNodeInfo item = getSwitchCycle(node.getChild(i));
            if(item != null){
                return item;
            }
        }

        return null;
    }

    private boolean performSwitch(AccessibilityService service,AccessibilityNodeInfo nodeInfo,String tagText,boolean back,boolean target,String key) {
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(tagText);
        if (list != null && list.size() > 0 ) {
            if (!accessisbilityMap.containsKey(key)) {
                for(int i=0; i<list.size(); i++) {
                    AccessibilityNodeInfo parent = list.get(i).getParent();
                    if(! parent.isVisibleToUser()){
                        break;
                    }
                    try {
                        AccessibilityNodeInfo nodeSwitch = getSwitch(list.get(i));
                        if (parent != null && target != nodeSwitch.isChecked()) {
                            if (parent.performAction(AccessibilityNodeInfo.ACTION_CLICK)) {
                                accessisbilityMap.put(key, 1);
                            }
                        }
                    } catch (Exception e) {
                        if (parent != null && isEndable(parent) != target) {
                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            accessisbilityMap.put(key, 1);
                        }
                    }
                    break;
                }


                if (back) {
                    isGoback = back;
                    service.performGlobalAction(GLOBAL_ACTION_BACK);
                }
                return true;
            } else if (back) {
                isGoback = back;
                service.performGlobalAction(GLOBAL_ACTION_BACK);
            }
            return true;

        } else {
            AccessibilityNodeInfo info = recycle(nodeInfo);
            if (info!= null) {
                info.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            }
        }
        return false;
    }

    private boolean performTab(AccessibilityNodeInfo nodeInfo, String tagText, String key) {
        if (accessisbilityMap.containsKey(key)){
            return true;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(tagText);
        if (list == null || list.size() == 0) {
            return true;
        }
        if (!accessisbilityMap.containsKey(key)) {
            AccessibilityNodeInfo tagNode = list.get(0);
            while(!tagNode.isClickable()){
                tagNode = tagNode.getParent();
            }
            if(tagNode.performAction(AccessibilityNodeInfo.ACTION_CLICK)){
                accessisbilityMap.put(key,1);
                return true;
            }
        }
        return false;
    }



    private boolean isEndable(AccessibilityNodeInfo parent) {
        if (parent != null ) {
            int count = parent.getChildCount();
            for (int j = 0; j < count; j++) {
                AccessibilityNodeInfo info = parent.getChild(j);
                if (info.isChecked()
                        || !(info.findAccessibilityNodeInfosByText(
                        "已允许").isEmpty())) {
                    return true;
                }
            }
        }
        return false;
    }
}
