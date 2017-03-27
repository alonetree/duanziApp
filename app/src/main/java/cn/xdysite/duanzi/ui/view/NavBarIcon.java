package cn.xdysite.duanzi.ui.view;

//http://m.blog.csdn.net/article/details?id=50472485
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import cn.xdysite.duanzi.R;

/**
 * Created by Administrator on 2017/3/19.
 */

public class NavBarIcon extends View {

    private int mColor = 0x8A8A8A;    //字体颜色
    private Bitmap mIconBitmap;       //原始icon
    private String mText = "段子";    //默认文字
    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
            12, getResources().getDisplayMetrics());      //默认字体大小
    private Canvas mCanvas;         //用于绘制渐变色画布
    private Bitmap mBitmap;         //用于存储渐变色View
    private Paint mPaint;           //绘制Icon画笔
    private volatile int mAlpha;    //控制渐变色透明度,1最大
    private Rect mIconRect;         //描述Icon绘制位置
    private Rect mTextBound;        //描述文字的大小
    private TextPaint mTextPaint;   //描述绘制文字画笔
    private int mid;                //每个View指定一个id
    private float textX;            //View中文字的位置
    private float textY;
    private int mTargetColor;       //罩层颜色

    public NavBarIcon(Context context) {
        this(context, null);
    }

    public NavBarIcon(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NavBarIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性值
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NavBarIcon);
        int n = typedArray.getIndexCount();
        for (int i=0; i<n; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.NavBarIcon_color :
                    mColor = typedArray.getColor(attr, mColor);
                    break;
                case R.styleable.NavBarIcon_icon :
                    BitmapDrawable db = (BitmapDrawable) typedArray.getDrawable(attr);
                    if (db != null)
                        mIconBitmap = db.getBitmap();
                    break;
                case R.styleable.NavBarIcon_text :
                    mText = typedArray.getString(attr);
                    break;
                case R.styleable.NavBarIcon_text_size :
                    mTextSize = typedArray.getDimension(attr, mTextSize);
                    break;
                default:break;
            }
        }

        typedArray.recycle();

        //绘图工具初始化,有的工具不初始化是因为使用懒加载
        mTextPaint = new TextPaint();
        mTextPaint.setFilterBitmap(true);
        mTextPaint.setAntiAlias(true);
        mIconRect = new Rect();
        mTextBound = new Rect();

        //获取罩层颜色
        mTargetColor = ContextCompat.getColor(getContext(), R.color.green);
        //设置文字画笔粗细
        mTextPaint.setTextSize(mTextSize);
        //设置文字画笔颜色
        mTextPaint.setColor(mColor);
        //获取包含文字的最小矩形
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);

        /****设置Icon画笔颜色****/
        mPaint = new Paint();
        mPaint.setColor(mTargetColor);
        //设置抗锯齿 /*http://blog.csdn.net/lovexieyuan520/article/details/50732023*/
        mPaint.setAntiAlias(true);
        mPaint.setFilterBitmap(true);
        //设置防抖动
        mPaint.setDither(true);
        mCanvas = new Canvas();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NavBarIcon(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 设置自定义View中Icon和Text的大小
     * @param widthMeasureSpec  宽度测量
     * @param heightMeasureSpec 高度测量
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int iconWidth = Math.min(getMeasuredWidth()-getPaddingStart()-getPaddingEnd(),
                getMeasuredHeight()-getPaddingTop()-getPaddingBottom()-mTextBound.height());
        int left = (getMeasuredWidth() -iconWidth)/2;
        int top = (getMeasuredHeight() - mTextBound.height() - iconWidth)/2;
        //计算Icon应该出现的位置
        mIconRect.set(left, top, left+iconWidth, top+iconWidth);
        textX = (getMeasuredWidth()-mTextBound.width())/2;
        textY = mIconRect.bottom+mTextBound.height()+4;
    }

    /**
     * 重写onDraw方法,绘制自定义View
     * @see #drawSourcView(Canvas) 绘制原图标,未点击时的样子
     * @see #drawTargetView(Canvas)  //绘制点击后图标的效果,这个会遮盖前面绘制的结果,注意是遮盖
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("onDraw", "id = " + mid + " mAlpha ="+mAlpha);
        drawSourcView(canvas);
        drawTargetView(canvas);
    }

    /**
     * 绘制原图,包括icon和文字
     * @param canvas 画布
     */
    private void drawSourcView(Canvas canvas) {
        canvas.drawARGB(0, 255, 255, 255);
        //绘制icon
        canvas.drawBitmap(mIconBitmap, null, mIconRect,null);
        //绘制文字
        canvas.drawText(mText, textX, textY, mTextPaint);
    }

    /**
     * 在内存中绘制可变色的图标,使用到混杂模式(在画布上绘制两张图,然后求它们的交集)
     * @param canvas 画布
     */
    private void drawTargetView(Canvas canvas) {
        //这里不能在mIconBitmap上直接进行绘制,因为其是不可变对象
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        //创建画布
        mCanvas.setBitmap(mBitmap);
        //设置透明度
        mPaint.setAlpha(mAlpha);
        //绘制第一张图(Dst),矩形,纯绿色
        mCanvas.drawRect(mIconRect, mPaint);
        //将画笔调为混杂模式
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        //绘制第二张图(Src),临摹mIconBitmap这张图
        mCanvas.drawBitmap(mIconBitmap,null,mIconRect,mPaint);
        //关闭混杂模式
        mPaint.setXfermode(null);
        //设置绘制字体画笔的颜色
        mTextPaint.setColor(mTargetColor);
        mTextPaint.setAlpha(mAlpha);
        mCanvas.drawText(mText, textX, textY, mTextPaint);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(255);
        mBitmap.recycle();
    }

    /**
     * 修改上层View的透明度
     * @param ratio 透明度比率
     */
    public void setmAlphaRatio (float ratio) {
        mAlpha = (int)(253*ratio);
        invalidateView();
    }

    /**
     * 重绘View对象,只有onDraw方法会被调用
     */
    private void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper())
            invalidate();
        else
            postInvalidate();
    }

    public void setMid (int _mid) {
        mid = _mid;
    }

    public int getMid() {
        return mid;
    }
}
