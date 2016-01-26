package com.example.mydome.mediaPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mydome.R;
import com.example.mydome.R.string;

/**
 * 要有音量才能显示波形图
 * @author Administrator
 *
 */
public class AudioWaveform extends Activity {
	// 定义播放声音的MediaPlayer
    private MediaPlayer mPlayer;
    // 定义系统的频谱
    private Visualizer mVisualizer; 
    private LinearLayout mLayout;
//    private List<short> reverbNames = new ArrayList<short>();
    private List<string> reverbVals = new ArrayList<string>();
    private byte[] height={5,15,6,8,12,11,6,9,4,15,9,1,5,3,7,12,
    		15,11,8,3,7,7,10,13,9,10,14};
    private byte[] mData = new byte[25];
    private int j=0;
    private final Timer timer = new Timer();
    private TimerTask task=new TimerTask() {
		
		@Override
		public void run() {
			int k=j;
			for(int i=0;i<25;i++){
				mData[i]=height[k];
				k++;
				if(k==(height.length-1)){
					k=0;
				}
			}
			j++;
			if(j==(height.length-1)){
				j=0;
			}
			handler.sendEmptyMessage(1);
		}
	};
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mBaseVisualizerView.updateVisualizer(mData);
			Log.i("mBaseVisualizerView","刷新啦" );
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置音频流 - STREAM_MUSIC：音乐回放即媒体音量
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mLayout = new LinearLayout(this);//代码创建布局
        mLayout.setOrientation(LinearLayout.VERTICAL);//设置为线性布局-上下排列
        Button btn=new Button(this);
        Button btn1=new Button(this);
        btn.setText("播放效果1");
        btn1.setText("播放效果2");
        mLayout.addView(btn);
        mLayout.addView(btn1);
        setContentView(mLayout);//将布局添加到 Activity
        // 创建MediaPlayer对象,并添加音频.音频路径为  res/raw/psy.mp3
        mPlayer = MediaPlayer.create(AudioWaveform.this, R.raw.psy);
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        // 初始化示波器
		        setupVisualizer();
		        mPlayer.start();
			}
		});
        
        btn1.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				setupVisualizerFxAndUi();
				timer.schedule(task, 1000, 200);
				mPlayer.start();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mPlayer!=null){
			 // 释放所有对象
			mVisualizer.release();
			mPlayer.release();
	        mPlayer = null;
		}
	}


//	需要添加这两条权限：
//	<uses-permission android:name="android.permission.RECORD_AUDIO"/>
//    <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
	
	private void setupVisualizer() {
		// 创建MyVisualizerView组件，用于显示波形图
        final MyVisualizerView mVisualizerView =new MyVisualizerView(this);
        mVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            (int) (120f * getResources().getDisplayMetrics().density)));
        // 将MyVisualizerView组件添加到layout容器中
        mLayout.addView(mVisualizerView);
        // 以MediaPlayer的AudioSessionId创建Visualizer
        // 相当于设置Visualizer负责显示该MediaPlayer的音频数据
        mVisualizer = new Visualizer(mPlayer.getAudioSessionId());
        //设置需要转换的音乐内容长度，专业的说这就是采样，该采样值一般为2的指数倍，如64,128,256,512,1024。
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        // 为mVisualizer设置监听器
        /*
         * Visualizer.setDataCaptureListener(OnDataCaptureListener listener, 
         * 									int rate, 
         * 									boolean waveform,
         * 									 boolean fft
         *  
         *      listener，表监听函数，匿名内部类实现该接口，该接口需要实现两个函数   
                rate， 表示采样的周期，即隔多久采样一次，联系前文就是隔多久采样128个数据
                iswave，是波形信号
                isfft，是FFT信号，表示是获取波形信号还是频域信号
             
         */
        mVisualizer.setDataCaptureListener(
            new Visualizer.OnDataCaptureListener()
            {
                //这个回调应该采集的是快速傅里叶变换有关的数据
                @Override
                public void onFftDataCapture(Visualizer visualizer,
                    byte[] fft, int samplingRate)
                {
                }
                 //这个回调应该采集的是波形数据
                @Override
                public void onWaveFormDataCapture(Visualizer visualizer,
                    byte[] waveform, int samplingRate)
                {
                    // 用waveform波形数据更新mVisualizerView组件
                    mVisualizerView.updateVisualizer(waveform);
                }
            }, Visualizer.getMaxCaptureRate() / 2, true, false);
        mVisualizer.setEnabled(true);
	}
	
	
	/**
     * 生成一个VisualizerView对象，使音频频谱的波段能够反映到 VisualizerView上
     */
	private VisualizerView mBaseVisualizerView;
	private static final float VISUALIZER_HEIGHT_DIP = 150f;//频谱View高度
    private void setupVisualizerFxAndUi() {
        mBaseVisualizerView = new VisualizerView(this);

        mBaseVisualizerView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,//宽度
                (int) (VISUALIZER_HEIGHT_DIP * getResources().getDisplayMetrics().density)//高度
        ));
        //将频谱View添加到布局
        mLayout.addView(mBaseVisualizerView);
        //实例化Visualizer，参数SessionId可以通过MediaPlayer的对象获得
        Visualizer mVisualizer = new Visualizer(mPlayer.getAudioSessionId());
        //采样 - 参数内必须是2的位数 - 如64,128,256,512,1024
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        //设置允许波形表示，并且捕获它
        mBaseVisualizerView.setVisualizer(mVisualizer);
    }

	/**
     * 根据Visualizer传来的数据动态绘制波形效果，分别为：
     * 块状波形、柱状波形、曲线波形
     */
    private static class MyVisualizerView extends View
    {
        // bytes数组保存了波形抽样点的值
        private byte[] bytes;
        private float[] points;
        private Paint paint = new Paint();
        private Rect rect = new Rect();
        private byte type = 0;
        public MyVisualizerView(Context context)
        {
            super(context);
            bytes = null;
            // 设置画笔的属性
            paint.setStrokeWidth(1f);
            paint.setAntiAlias(true);//抗锯齿
            paint.setColor(Color.RED);//画笔颜色
            paint.setStyle(Paint.Style.FILL);
        }
 
        public void updateVisualizer(byte[] ftt)
        {
            bytes = ftt;
            // 通知该组件重绘自己。
            invalidate();
        }
         
        @Override
        public boolean onTouchEvent(MotionEvent me)
        {
            // 当用户触碰该组件时，切换波形类型
            if(me.getAction() != MotionEvent.ACTION_DOWN)
            {
                return false;
            }
            type ++;
            if(type >= 3)
            {
                type = 0;
            }
            return true;
        }
 
        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            if (bytes == null)
            {
                return;
            }
            // 绘制白色背景
            canvas.drawColor(Color.WHITE);          
            // 使用rect对象记录该组件的宽度和高度
            rect.set(0,0,getWidth(),getHeight());
            switch(type)
            {
                // -------绘制块状的波形图-------
                case 0: 
                    for (int i = 0; i < bytes.length - 1; i++)
                    {
                        float left = getWidth() * i / (bytes.length - 1);
                        // 根据波形值计算该矩形的高度        
                        float top = rect.height()-(byte)(bytes[i+1]+128)
                            * rect.height() / 128;
                        float right = left + 1;
                        float bottom = rect.height();
                        canvas.drawRect(left, top, right, bottom, paint);
                    }
                    break;
                // -------绘制柱状的波形图（每隔18个抽样点绘制一个矩形）-------
                case 1:
                    for (int i = 0; i < bytes.length - 1; i += 18)
                    {
                        float left = rect.width()*i/(bytes.length - 1);
                        // 根据波形值计算该矩形的高度
                        float top = rect.height()-(byte)(bytes[i+1]+128)
                            * rect.height() / 128;
                        float right = left + 6;
                        float bottom = rect.height();
                        canvas.drawRect(left, top, right, bottom, paint);
                    }
                    break;
                // -------绘制曲线波形图-------
                case 2:
                    // 如果point数组还未初始化
                    if (points == null || points.length < bytes.length * 4)
                    {
                        points = new float[bytes.length * 4];
                    }
                    for (int i = 0; i < bytes.length - 1; i++)
                    {
                        // 计算第i个点的x坐标
                        points[i * 4] = rect.width()*i/(bytes.length - 1);
                        // 根据bytes[i]的值（波形点的值）计算第i个点的y坐标
                        points[i * 4 + 1] = (rect.height() / 2)
                            + ((byte) (bytes[i] + 128)) * 128
                            / (rect.height() / 2);
                        // 计算第i+1个点的x坐标
                        points[i * 4 + 2] = rect.width() * (i + 1)
                            / (bytes.length - 1);
                        // 根据bytes[i+1]的值（波形点的值）计算第i+1个点的y坐标
                        points[i * 4 + 3] = (rect.height() / 2)
                            + ((byte) (bytes[i + 1] + 128)) * 128
                            / (rect.height() / 2);
                    }
                    // 绘制波形曲线
                    canvas.drawLines(points, paint);
                    break;
            }
        }
    }
    
    /**
     * 用小矩形来组成柱子
     * @author Administrator
     *
     */
    class VisualizerView extends View implements Visualizer.OnDataCaptureListener{

    	private static final int DN_W = 470;//view宽度与单个音频块占比 - 正常480 需微调
        private static final int DN_H = 180;//view高度与单个音频块占比
        private static final int DN_SL = 15;//单个音频块宽度
        private static final int DN_SW = 5;//单个音频块高度

        private int hgap = 0;
        private int vgap = 0;
        private int levelStep = 0;
        private float strokeWidth = 0;
        private float strokeLength = 0;
        
        protected final static int MAX_LEVEL = 15;//音量柱·音频块 - 最大个数

        protected final static int CYLINDER_NUM = 25;//音量柱 - 最大个数

        protected Visualizer mVisualizer = null;//频谱器

        protected Paint mPaint = null;//画笔

        protected byte[] mData = new byte[CYLINDER_NUM];//音量柱 数组

        boolean mDataEn = true;
//        private final Timer timer = new Timer();
//        private TimerTask task=new TimerTask() {
//			
//			@Override
//			public void run() {
//				int k=j;
//				for(int i=0;i<25;i++){
//					mData[i]=height[k];
//					k++;
//					if(k==(height.length-1)){
//						k=0;
//					}
//				}
//				j++;
//				if(j==(height.length-1)){
//					j=0;
//				}
//				postInvalidate();//刷新界面
//			}
//		};
        
		public VisualizerView(Context context) {
			super(context);
			mPaint = new Paint();//初始化画笔工具
	        mPaint.setAntiAlias(true);//抗锯齿
	        mPaint.setColor(Color.WHITE);//画笔颜色

//	        mPaint.setStrokeJoin(Join.ROUND); //频块圆角
//	        mPaint.setStrokeCap(Cap.ROUND); //频块圆角
		}
		
		//执行 Layout 操作
	    @Override
	    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
	        super.onLayout(changed, left, top, right, bottom);

	        float w, h, xr, yr;

	        w = right - left;
	        h = bottom - top;
	        xr = w / (float) DN_W;
	        yr = h / (float) DN_H;

	        strokeWidth = DN_SW * yr;
	        strokeLength = DN_SL * xr;
	        hgap = (int) ((w - strokeLength * CYLINDER_NUM) / (CYLINDER_NUM + 1));
	        vgap = (int) (h / (MAX_LEVEL + 2));

	        mPaint.setStrokeWidth(strokeWidth); //设置频谱块宽度
	    }

	    //绘制频谱块和倒影
	    protected void drawCylinder(Canvas canvas, float x, byte value) {
	        if (value <= 0) value = 1;//最少有一个频谱块

	        for (int i = 0; i < value; i++) { //每个能量柱绘制value个能量块
	            float y = (getHeight() - i * vgap - vgap) - 40;//计算y轴坐标

	            //绘制频谱块
	            mPaint.setColor(Color.WHITE);//画笔颜色
	            canvas.drawLine(x, y, (x + strokeLength), y, mPaint);//绘制频谱块

	            //绘制音量柱倒影
//	            if (i <= 6 && value > 0) {
//	                mPaint.setColor(Color.WHITE);//画笔颜色
//	                mPaint.setAlpha(100 - (100 / 6 * i));//倒影颜色
//	                canvas.drawLine(x, -y + 210, (x + strokeLength), -y + 210, mPaint);//绘制频谱块
//	            }
	        }
	    }
	    
	    public void updateVisualizer(byte[] ftt)
        {
	    	mData = ftt;
            // 通知该组件重绘自己。
            invalidate();
        }
	    @Override
	    public void onDraw(Canvas canvas) {
	        for (int i = 0; i < CYLINDER_NUM; i++) { //绘制25个能量柱
	            drawCylinder(canvas, strokeWidth / 2 + hgap + i * (hgap + strokeLength), mData[i]);
	        }
	    }

	    /**
	     * It sets the visualizer of the view. DO set the viaulizer to null when exit the program.
	     *
	     * @parma visualizer It is the visualizer to set.
	     */
	    public void setVisualizer(Visualizer visualizer) {
	        if (visualizer != null) {
	            if (!visualizer.getEnabled()) {
	                visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[0]);
	            }
	            levelStep = 128 / MAX_LEVEL;
	            visualizer.setDataCaptureListener(this, Visualizer.getMaxCaptureRate() / 2, false, true);

	        } else {

	            if (mVisualizer != null) {
	                mVisualizer.setEnabled(false);
	                mVisualizer.release();
	            }
	        }
	        mVisualizer = visualizer;
	    }

		@Override
		public void onWaveFormDataCapture(Visualizer visualizer,
				byte[] waveform, int samplingRate) {
			
		}
		
		@Override
		public void onFftDataCapture(Visualizer visualizer, byte[] fft,
				int samplingRate) {
			
			
//			byte[] model = new byte[fft.length / 2 + 1];
//	        if (mDataEn) {
//	            model[0] = (byte) Math.abs(fft[1]);
//	            int j = 1;
//	            for (int i = 2; i < fft.length; ) {
//	                model[j] = (byte) Math.hypot(fft[i], fft[i + 1]);
//	                i += 2;
//	                j++;
//	            }
//	        } else {
//	            for (int i = 0; i < CYLINDER_NUM; i++) {
//	                model[i] = 0;
//	            }
//	        }
//	        for (int i = 0; i < CYLINDER_NUM; i++) {
//	            final byte a = (byte) (Math.abs(model[CYLINDER_NUM - i]) / levelStep);
//
//	            final byte b = mData[i];
//	            if (a > b) {
//	                mData[i] = a;
//	            } else {
//	                if (b > 0) {
//	                    mData[i]--;
//	                }
//	            }
//	        }
//	        postInvalidate();//刷新界面
			
		}
    	
    }
}
