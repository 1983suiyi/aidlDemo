package com.suiyi.jnidemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import static java.lang.Thread.sleep;

public class aidlService extends Service {

    public aidlService() {

    }

    private static boolean     isStop = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IMyAidlInterface.Stub mBinder = new IMyAidlInterface.Stub() {
        @Override
        public String cmpString(String str1, String str2) throws RemoteException {
            return String.format("%s,%s", str1, str2);
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {

            String packageName = null;
            String[] packages = aidlService.this.getPackageManager().
                    getPackagesForUid(getCallingUid());
            if (packages != null && packages.length > 0) {
                packageName = packages[0];
            }
            Log.d("suiyi", "onTransact: " + packageName);
            return super.onTransact(code, data, reply, flags);
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("aidlService", "aidlService onCreate");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(2000);
                        Log.e("aidlService", "aidlService wait 2 seconds");
                        if (isStop) {
                            Log.e("aidlService", "aidlService stop");
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            isStop = intent.getBooleanExtra("is_stop", false);
            Log.e("suiyi", "isStop" +    isStop);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
