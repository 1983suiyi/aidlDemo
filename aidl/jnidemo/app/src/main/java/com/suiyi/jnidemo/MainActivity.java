package com.suiyi.jnidemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private int i = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ChoreographerCallBack.getInstance().start();
        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI("I am coming"));
        int[] arr = arrayFromJNI(10);
        for (int i = 0; i < arr.length; i++) {
            Log.e("suiyi", "arr:" + arr[i]);
        }

        Log.e("suiyi","cGetJavaInt:"+cGetJavaInt()+"");

        NativeLib.printHello();

        Intent intent = new Intent("com.suiyi.jnidemo.aidlService");
        intent.setPackage(getPackageName());
        intent.putExtra("is_stop",false);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }else
            startService(intent);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
        Button bt = (Button)findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String cmpstr = ibinder.cmpString("123","456");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,cmpstr,Toast.LENGTH_LONG).show();
                            Intent intent = new Intent("com.suiyi.jnidemo.aidlService");
                            intent.setPackage(getPackageName());
                            intent.putExtra("is_stop",true);
                            startService(intent);
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    static native void initIDs();


    public void printABC(){

        System.out.println("in java print ABC");

        //OnlyNativeFun.exec();
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI(String msg);

    public native int[] arrayFromJNI(int size);


    public native int cGetJavaInt();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        initIDs();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //OnlyNativeFun.jniOnUnload();

    }

    @Override
    protected void onDestroy() {

        unbindService(serviceConnection);

        super.onDestroy();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    private IMyAidlInterface ibinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ibinder = IMyAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

}
