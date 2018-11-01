package com.suiyi.jnidemo;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.Choreographer;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class ChoreographerCallBack implements Choreographer.FrameCallback {
    /**
     * Called when a new display frame is being rendered.
     * <p>
     * This method provides the time in nanoseconds when the frame started being rendered.
     * The frame time provides a stable time base for synchronizing animations
     * and drawing.  It should be used instead of {@link SystemClock#uptimeMillis()}
     * or {@link System#nanoTime()} for animations and drawing in the UI.  Using the frame
     * time helps to reduce inter-frame jitter because the frame time is fixed at the time
     * the frame was scheduled to start, regardless of when the animations or drawing
     * callback actually runs.  All callbacks that run as part of rendering a frame will
     * observe the same frame time so using the frame time also helps to synchronize effects
     * that are performed by different callbacks.
     * </p><p>
     * Please note that the framework already takes care to process animations and
     * drawing using the frame time as a stable time base.  Most applications should
     * not need to use the frame time information directly.
     * </p>
     *
     * @param frameTimeNanos The time in nanoseconds when the frame started being rendered,
     *                       in the {@link System#nanoTime()} timebase.  Divide this value by {@code 1000000}
     *                       to convert it to the {@link SystemClock#uptimeMillis()} time base.
     */
    @Override
    public void doFrame(long frameTimeNanos) {
        if(lastFrameTimeNanos==0){
            lastFrameTimeNanos=frameTimeNanos;
            Choreographer.getInstance().postFrameCallback(this);
            return;
        }
        currentFrameTimeNanos=frameTimeNanos;
        float value=(currentFrameTimeNanos-lastFrameTimeNanos)/1000000.0f;

        final int skipFrameCount = skipFrameCount(lastFrameTimeNanos, currentFrameTimeNanos, deviceRefreshRateMs);
        Log.e(TAG,"两次绘制时间间隔value="+value+"  frameTimeNanos="+frameTimeNanos+"  currentFrameTimeNanos="+currentFrameTimeNanos+"  skipFrameCount="+skipFrameCount+"");
        lastFrameTimeNanos=currentFrameTimeNanos;
        Choreographer.getInstance().postFrameCallback(this);
    }


    public static  ChoreographerCallBack sInstance;

    private String TAG="SMFrameCallback";

    public static final float deviceRefreshRateMs=16.6f;

    public static  long lastFrameTimeNanos=0;//纳秒为单位

    public static  long currentFrameTimeNanos=0;

    public void start() {
        Choreographer.getInstance().postFrameCallback(ChoreographerCallBack.getInstance());
    }

    public static ChoreographerCallBack getInstance() {
        if (sInstance == null) {
            sInstance = new ChoreographerCallBack();
        }
        return sInstance;
    }




    /**
     *
     *计算跳过多少帧
     * @param start
     * @param end
     * @param devicefreshRate
     * @return
     */
    private  int skipFrameCount(long start,long end,float devicefreshRate){
        int count =0;
        long diffNs=end-start;
        long diffMs = Math.round(diffNs / 1000000.0f);
        long dev=Math.round(devicefreshRate);
        if(diffMs>dev){
            long skipCount=diffMs/dev;
            count=(int)skipCount;
        }
        return  count;
    }

}
