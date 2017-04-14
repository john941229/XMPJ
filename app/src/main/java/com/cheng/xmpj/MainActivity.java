package com.cheng.xmpj;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cheng.xmpj.XLogger.XLogger;

import java.lang.reflect.Method;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button mMiBtn, mAccessBtn;
    public static final String HUAWEI_PERMISSION_PACKAGE_NAME = "com.huawei.systemmanager";
    public static final String HUAWEI_APP_PERMISSION_ACTIVITY_NAME = "com.huawei.permissionmanager.ui.MainActivity";
    private int huaweiVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        XLogger.init(true);
        setContentView(R.layout.activity_main);
        huaweiVersion = getHuaweiSystemManagerVersion(MainActivity.this);

        mMiBtn = (Button) findViewById(R.id.button_mi);
        mMiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMiuiFloatWindowOpAllowed(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "以开启", Toast.LENGTH_SHORT).show();
                } else {
                    if("Xiaomi".equals(Build.MANUFACTURER))
                        openMiuiPermissionActivity(MainActivity.this);
                    if("HUAWEI".equals(Build.MANUFACTURER))
                        openHUAWEIpermissionActivity(MainActivity.this);
                }
            }
        });
        mAccessBtn = (Button) findViewById(R.id.button_access);
        mAccessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                MainActivity.this.startActivity(accessibleIntent);
            }
        });
    }

    /**
     * 经测试V5版本是有区别的
     *
     * @param context
     */
    public void openMiuiPermissionActivity(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        String property=getProperty();
        XLogger.e(property);
        intent.putExtra("property",property);
        if ("V5".equals(property)) {
            PackageInfo pInfo = null;
            try {
                pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("canking", "error");
            }
            intent.setClassName("com.miui.securitycenter", "com.miui.securitycenter.permission.AppPermissionsEditor");
            intent.putExtra("extra_package_uid", pInfo.applicationInfo.uid);

        } else if(
                "V6".equals(property)||
                        "V7".equals(property)
                ){
            intent.setClassName("com.miui.securitycenter",
                    "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
            intent.putExtra("extra_pkgname", context.getPackageName());
        } else if("V8".equals(property)) {
            PackageInfo pInfo = null;
            try {
                pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("canking", "error");
            }
            intent.setClassName("com.miui.securitycenter",
                    "com.miui.permcenter.permissions.PermissionsEditorActivity");
            intent.putExtra("extra_pkgname", context.getPackageName());

        }

        if (isActivityAvailable(context, intent)) {
            if (context instanceof Activity) {
                Activity a = (Activity) context;
                a.startActivityForResult(intent,2);
            }
        } else {
            Log.e("canking", "Intent is not available!");
        }
    }

    /**
    * 华为ROM*/
    public void openHUAWEIpermissionActivity(Context context){
        try {
            Intent sysIntent = new Intent();
            Intent guideIntent = null;
//            if (huaweiVersion == 2 || huaweiVersion == 3 || huaweiVersion == 4) {
//                sysIntent.setClassName(HUAWEI_PERMISSION_PACKAGE_NAME,
//                        HUAWEI_APP_PERMISSION_ACTIVITY_NAME);
//                context.startActivity(sysIntent);
//            }
//            else if (huaweiVersion == 6) {
//                sysIntent.setClassName(HUAWEI_PERMISSION_PACKAGE_NAME,
//                        HUAWEI_APP_PERMISSION_ACTIVITY_NAME);
//                context.startActivity(sysIntent);
//            }
            sysIntent.setClassName(HUAWEI_PERMISSION_PACKAGE_NAME,
                        HUAWEI_APP_PERMISSION_ACTIVITY_NAME);
                context.startActivity(sysIntent);
        }catch (ActivityNotFoundException e ){
            XLogger.e(e.toString());
        }catch (SecurityException e) {
            XLogger.e(e.toString());
        }
    }

    public static boolean isActivityAvailable(Context cxt, Intent intent) {
        PackageManager pm = cxt.getPackageManager();
        if (pm == null) {
            return false;
        }
        List<ResolveInfo> list = pm.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list != null && list.size() > 0;
    }

    public static String getProperty() {
        String property = "null";
        if (!"Xiaomi".equals(Build.MANUFACTURER) ) {
            return property;
        }
        try {
            Class<?> spClazz = Class.forName("android.os.SystemProperties");
            Method method = spClazz.getDeclaredMethod("get", String.class, String.class);
            property = (String) method.invoke(spClazz, "ro.miui.ui.version.name", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return property;

    }

    /**
     * 判断悬浮窗权限
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isMiuiFloatWindowOpAllowed(Context context) {
        final int version = Build.VERSION.SDK_INT;
        Log.d("version",version+"");
        if (version >= 19) {
            return checkOp(context, 24);  // AppOpsManager.OP_SYSTEM_ALERT_WINDOW
        } else {
            if ((context.getApplicationInfo().flags & 1 << 27) == 1 << 27) {
                return true;
            } else {
                return false;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;

        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {

                Class<?> spClazz = Class.forName(manager.getClass().getName());
                Method method = manager.getClass().getDeclaredMethod("checkOp", int.class, int.class, String.class);
                int property = (Integer) method.invoke(manager, op,
                        Binder.getCallingUid(), context.getPackageName());
                XLogger.e(AppOpsManager.MODE_ALLOWED + " invoke " + property);
                Log.d("property",property+"");
                if (AppOpsManager.MODE_ALLOWED == property) {  
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                XLogger.e(e.getMessage());
            }
        } else {
            XLogger.e("Below API 19 cannot invoke!");
        }
        return false;
    }

    public static int getHuaweiSystemManagerVersion(Context context) {
        int version = 0;
        int versionNum = 0;
        int thirdPartFirtDigit = 0;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(HUAWEI_PERMISSION_PACKAGE_NAME, 0);
            String versionStr = info.versionName;
            String versionTmp[] = versionStr.split("\\.");
            if (versionTmp.length >= 2) {
                if (Integer.parseInt(versionTmp[0]) == 5) {
                    versionNum = 500;
                } else if (Integer.parseInt(versionTmp[0]) == 4) {
                    versionNum = Integer.parseInt(versionTmp[0]+versionTmp[1]+versionTmp[2]);
                } else {
                    versionNum = Integer.parseInt(versionTmp[0] + versionTmp[1]);
                }

            }
            if (versionTmp.length >= 3) {
                thirdPartFirtDigit = Integer.valueOf(versionTmp[2].substring(0, 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (versionNum >= 330) {
            if (versionNum >= 500) {
                version = 6;
            } else if (versionNum >= 400) {
                version = 5;
            } else if (versionNum >= 331) {
                version = 4;
            } else {
                version = (thirdPartFirtDigit == 6 || thirdPartFirtDigit == 4 || thirdPartFirtDigit == 2 )? 3 : 2;
            }
        }  else if (versionNum != 0 ) {
            version = 1;
        }
        return version;
    }
}
