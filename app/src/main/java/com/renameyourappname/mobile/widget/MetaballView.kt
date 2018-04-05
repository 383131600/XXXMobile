package com.renameyourappname.mobile.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation

/**
 * 小球动画,可配合用于Loading
 * https://github.com/dodola/MetaballLoading
 */
class MetaballView : View {

    private val paint = Paint()
    private val handle_len_rate = 2f
    private val radius = 30f
    private val ITEM_COUNT = 6
    private val ITEM_DIVIDER = 60
    private val SCALE_RATE = 0.3f
    private var maxLength: Float = 0.toFloat()
    private val circlePaths = arrayListOf<Circle>()
    private var mInterpolatedTime: Float = 0.toFloat()
    private var wa: MoveAnimation? = null
    private var circle: Circle? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()

    }

    private inner class Circle {
        internal var center: FloatArray? = null
        internal var radius: Float = 0.toFloat()
    }

    fun setPaintMode(mode: Int) {
        paint.setStyle(if (mode == 0) Paint.Style.STROKE else Paint.Style.FILL)
        invalidate()
    }

    private fun init() {
        paint.setColor(-0xb24601)
        paint.setStyle(Paint.Style.FILL)
        paint.setAntiAlias(true)
        var circlePath = Circle()
        circlePath.center = floatArrayOf(radius + ITEM_DIVIDER, radius * (1f + SCALE_RATE))
        circlePath.radius = radius / 4 * 3
        circlePaths.add(circlePath)

        for (i in 1 until ITEM_COUNT) {
            circlePath = Circle()
            circlePath.center = floatArrayOf((radius * 2 + ITEM_DIVIDER) * i, radius * (1f + SCALE_RATE))
            circlePath.radius = radius
            circlePaths.add(circlePath)
        }
        maxLength = (radius * 2 + ITEM_DIVIDER) * ITEM_COUNT
    }

    private fun getVector(radians: Float, length: Float): FloatArray {
        val x = (Math.cos(radians.toDouble()) * length).toFloat()
        val y = (Math.sin(radians.toDouble()) * length).toFloat()
        return floatArrayOf(x, y)
    }

    private inner class MoveAnimation : Animation() {

        protected override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            super.applyTransformation(interpolatedTime, t)
            mInterpolatedTime = interpolatedTime
            invalidate()
        }
    }

    /**
     * @param canvas          画布
     * @param j
     * @param i
     * @param v               控制两个圆连接时候长度，间接控制连接线的粗细，该值为1的时候连接线为直线
     * @param handle_len_rate
     * @param maxDistance
     */
    private fun metaball(canvas: Canvas, j: Int, i: Int, v: Float, handle_len_rate: Float, maxDistance: Float) {
        val circle1 = circlePaths.get(i)
        val circle2 = circlePaths.get(j)

        val ball1 = RectF()
        ball1.left = circle1.center!![0] - circle1.radius
        ball1.top = circle1.center!![1] - circle1.radius
        ball1.right = ball1.left + circle1.radius * 2
        ball1.bottom = ball1.top + circle1.radius * 2

        val ball2 = RectF()
        ball2.left = circle2.center!![0] - circle2.radius
        ball2.top = circle2.center!![1] - circle2.radius
        ball2.right = ball2.left + circle2.radius * 2
        ball2.bottom = ball2.top + circle2.radius * 2

        val center1 = floatArrayOf(ball1.centerX(), ball1.centerY())
        val center2 = floatArrayOf(ball2.centerX(), ball2.centerY())
        val d = getDistance(center1, center2)

        var radius1 = ball1.width() / 2
        var radius2 = ball2.width() / 2
        val pi2 = (Math.PI / 2).toFloat()
        val u1: Float
        val u2: Float


        if (d > maxDistance) {
            //            canvas.drawCircle(ball1.centerX(), ball1.centerY(), circle1.radius, paint);
            canvas.drawCircle(ball2.centerX(), ball2.centerY(), circle2.radius, paint)
        } else {
            val scale2 = 1 + SCALE_RATE * (1 - d / maxDistance)
            val scale1 = 1 - SCALE_RATE * (1 - d / maxDistance)
            radius2 *= scale2
            //            radius1 *= scale1;
            //            canvas.drawCircle(ball1.centerX(), ball1.centerY(), radius1, paint);
            canvas.drawCircle(ball2.centerX(), ball2.centerY(), radius2, paint)

        }

        //        Log.d("Metaball_radius", "radius1:" + radius1 + ",radius2:" + radius2);
        if (radius1 == 0f || radius2 == 0f) {
            return
        }

        if (d > maxDistance || d <= Math.abs(radius1 - radius2)) {
            return
        } else if (d < radius1 + radius2) {
            u1 = Math.acos(((radius1 * radius1 + d * d - radius2 * radius2) / (2f * radius1 * d)).toDouble()).toFloat()
            u2 = Math.acos(((radius2 * radius2 + d * d - radius1 * radius1) / (2f * radius2 * d)).toDouble()).toFloat()
        } else {
            u1 = 0f
            u2 = 0f
        }
        //        Log.d("Metaball", "center2:" + Arrays.toString(center2) + ",center1:" + Arrays.toString(center1));
        val centermin = floatArrayOf(center2[0] - center1[0], center2[1] - center1[1])

        val angle1 = Math.atan2(centermin[1].toDouble(), centermin[0].toDouble()).toFloat()
        val angle2 = Math.acos(((radius1 - radius2) / d).toDouble()).toFloat()
        val angle1a = angle1 + u1 + (angle2 - u1) * v
        val angle1b = angle1 - u1 - (angle2 - u1) * v
        val angle2a = (angle1 + Math.PI - u2.toDouble() - (Math.PI - u2.toDouble() - angle2.toDouble()) * v).toFloat()
        val angle2b = (angle1 - Math.PI + u2.toDouble() + (Math.PI - u2.toDouble() - angle2.toDouble()) * v).toFloat()

        //        Log.d("Metaball", "angle1:" + angle1 + ",angle2:" + angle2 + ",angle1a:" + angle1a + ",angle1b:" + angle1b + ",angle2a:" + angle2a + ",angle2b:" + angle2b);


        val p1a1 = getVector(angle1a, radius1)
        val p1b1 = getVector(angle1b, radius1)
        val p2a1 = getVector(angle2a, radius2)
        val p2b1 = getVector(angle2b, radius2)

        val p1a = floatArrayOf(p1a1[0] + center1[0], p1a1[1] + center1[1])
        val p1b = floatArrayOf(p1b1[0] + center1[0], p1b1[1] + center1[1])
        val p2a = floatArrayOf(p2a1[0] + center2[0], p2a1[1] + center2[1])
        val p2b = floatArrayOf(p2b1[0] + center2[0], p2b1[1] + center2[1])


        //        Log.d("Metaball", "p1a:" + Arrays.toString(p1a) + ",p1b:" + Arrays.toString(p1b) + ",p2a:" + Arrays.toString(p2a) + ",p2b:" + Arrays.toString(p2b));

        val p1_p2 = floatArrayOf(p1a[0] - p2a[0], p1a[1] - p2a[1])

        val totalRadius = radius1 + radius2
        var d2 = Math.min(v * handle_len_rate, getLength(p1_p2) / totalRadius)
        d2 *= Math.min(1f, d * 2 / (radius1 + radius2))
        //        Log.d("Metaball", "d2:" + d2);
        radius1 *= d2
        radius2 *= d2

        val sp1 = getVector(angle1a - pi2, radius1)
        val sp2 = getVector(angle2a + pi2, radius2)
        val sp3 = getVector(angle2b - pi2, radius2)
        val sp4 = getVector(angle1b + pi2, radius1)
        //        Log.d("Metaball", "sp1:" + Arrays.toString(sp1) + ",sp2:" + Arrays.toString(sp2) + ",sp3:" + Arrays.toString(sp3) + ",sp4:" + Arrays.toString(sp4));


        val path1 = Path()
        path1.moveTo(p1a[0], p1a[1])
        path1.cubicTo(p1a[0] + sp1[0], p1a[1] + sp1[1], p2a[0] + sp2[0], p2a[1] + sp2[1], p2a[0], p2a[1])
        path1.lineTo(p2b[0], p2b[1])
        path1.cubicTo(p2b[0] + sp3[0], p2b[1] + sp3[1], p1b[0] + sp4[0], p1b[1] + sp4[1], p1b[0], p1b[1])
        path1.lineTo(p1a[0], p1a[1])
        path1.close()
        canvas.drawPath(path1, paint)

    }

    private fun getLength(b: FloatArray): Float {
        return Math.sqrt((b[0] * b[0] + b[1] * b[1]).toDouble()).toFloat()
    }

    private fun getDistance(b1: FloatArray, b2: FloatArray): Float {
        val x = b1[0] - b2[0]
        val y = b1[1] - b2[1]
        val d = x * x + y * y
        return Math.sqrt(d.toDouble()).toFloat()
    }


    //测试用
    //    @Override
    //    public boolean onTouchEvent(MotionEvent event) {
    //        switch (event.getAction()) {
    //            case MotionEvent.ACTION_DOWN:
    //                break;
    //            case MotionEvent.ACTION_MOVE:
    //                Circle circle = circlePaths.get(0);
    //                circle.center[0] = event.getX();
    //                circle.center[1] = event.getY();
    //                invalidate();
    //                break;
    //            case MotionEvent.ACTION_UP:
    //                break;
    //        }
    //
    //        return true;
    //    }

    protected override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        circle = circlePaths.get(0)
        circle!!.center!![0] = maxLength * mInterpolatedTime

        val ball1 = RectF()
        ball1.left = circle!!.center!![0] - circle!!.radius
        ball1.top = circle!!.center!![1] - circle!!.radius
        ball1.right = ball1.left + circle!!.radius * 2
        ball1.bottom = ball1.top + circle!!.radius * 2
        canvas.drawCircle(ball1.centerX(), ball1.centerY(), circle!!.radius, paint)


        var i = 1
        val l = circlePaths.size
        while (i < l) {
            metaball(canvas, i, 0, 0.6f, handle_len_rate, radius * 4f)
            i++
        }
    }

    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(resolveSizeAndState((ITEM_COUNT * (radius * 2 + ITEM_DIVIDER)).toInt(), widthMeasureSpec, 0),
                resolveSizeAndState((2f * radius * 1.4f).toInt(), heightMeasureSpec, 0))
    }


    private fun stopAnimation() {
        this.clearAnimation()
        postInvalidate()
    }

    private fun startAnimation() {
        wa = MoveAnimation()
        wa!!.duration = 2500
        wa!!.interpolator = AccelerateDecelerateInterpolator()
        wa!!.repeatCount = Animation.INFINITE
        wa!!.repeatMode = Animation.REVERSE
        startAnimation(wa)
    }

    protected override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)

        if (visibility == GONE || visibility == INVISIBLE) {
            stopAnimation()
        } else {
            startAnimation()
        }
    }

    protected override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    protected override fun onDetachedFromWindow() {
        stopAnimation()
        super.onDetachedFromWindow()
    }
}