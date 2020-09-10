package com.colin.go

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView

/**
 * create by colin
 * 2020/9/1
 */
class TestView : AppCompatImageView {

    private val mPathPaint = Paint()
        .also {
            it.style = Paint.Style.STROKE
            it.strokeWidth = 50f
            it.isAntiAlias = true
            it.strokeCap = Paint.Cap.ROUND
            it.color = Color.WHITE
        }
    private val mPathList = mutableListOf<Path>()

    private val mPaint = Paint()
    private val mode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    private lateinit var mPathBitmap: Bitmap
    private lateinit var mPathCanvas: Canvas

    constructor(context: Context) : super(context)

    constructor(context: Context, attr: AttributeSet) : super(context, attr)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mPathBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mPathCanvas = Canvas(mPathBitmap)
    }

    override fun onDraw(canvas: Canvas) {
        if (mPathList.isEmpty())
            return
        val count = canvas.saveLayer(null, null)
        super.onDraw(canvas)
        //像path清空
        mPathCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        mPathList.forEach {
            mPathCanvas.drawPath(it, mPathPaint)
        }
        mPaint.xfermode = mode
        canvas.drawBitmap(mPathBitmap, 0f, 0f, mPaint)
        mPaint.xfermode = null
        canvas.restoreToCount(count)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled)
            return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Path().also {
                    it.moveTo(event.x, event.y)
                    mPathList.add(it)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                mPathList.last().lineTo(event.x, event.y)
            }
        }
        invalidate()
        return true
    }

    fun getMSKPath() = mPathList

    fun hasMSK() = mPathList.isNotEmpty()

    fun reset() {
        mPathList.clear()
    }
}