package com.colin.go.fragments

import android.graphics.*
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.colin.go.BaseFragment
import com.colin.go.R
import com.colin.go.databinding.CompassFragmentBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.math.max

class CompassFragment : BaseFragment<CompassFragmentBinding>() {

    override fun getContentLayoutId(): Int {
        return R.layout.compass_fragment
    }

    override fun initView(contentView: View) {

        binding.btnOk.setOnClickListener {
            applyMSK()
        }
    }

    private fun applyMSK() {
        val hasMSK = binding.mskView.hasMSK()
        if (!hasMSK)
            return
        val originPic = ResourcesCompat
            .getDrawable(resources, R.drawable.test2, null)
        val mskPic = ResourcesCompat
            .getDrawable(resources, R.drawable.test, null)
        if (originPic == null || mskPic == null)
            return
        originPic.setBounds(0, 0, originPic.intrinsicWidth, originPic.intrinsicHeight)
        mskPic.setBounds(0, 0, mskPic.intrinsicWidth, mskPic.intrinsicHeight)

        val scaleX = originPic.intrinsicWidth.toFloat() / binding.mskView.width.toFloat()
        val scaleY = originPic.intrinsicHeight.toFloat() / binding.mskView.height.toFloat()
        val matrix = Matrix()
        matrix.postScale(scaleX, scaleY)
        val mskPath = binding.mskView.getMSKPath()

        val resultBitmap = Bitmap.createBitmap(
            originPic.intrinsicWidth,
            originPic.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val mskBitmap = Bitmap.createBitmap(
            originPic.intrinsicWidth,
            originPic.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val mskPathBitmap = Bitmap.createBitmap(
            originPic.intrinsicWidth,
            originPic.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val resultCanvas = Canvas(resultBitmap)
        val mskCanvas = Canvas(mskBitmap)
        val mskPathCanvas = Canvas(mskPathBitmap)
        val mskPathPaint = Paint()
            .also {
                it.style = Paint.Style.STROKE
                it.strokeWidth = (50f * max(scaleX, scaleY))
                it.isAntiAlias = true
                it.strokeCap = Paint.Cap.ROUND
                it.color = Color.WHITE
            }
        val mskPaint = Paint().also {
            it.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        }

        originPic.draw(resultCanvas)
        val count = mskCanvas.saveLayer(null, null)
        mskPic.draw(mskCanvas)
        mskPathCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        mskPath.forEach {
            it.transform(matrix)
            mskPathCanvas.drawPath(it, mskPathPaint)
        }
        mskCanvas.drawBitmap(mskPathBitmap, 0f, 0f, mskPaint)
        mskCanvas.restoreToCount(count)
        resultCanvas.drawBitmap(mskBitmap, 0f, 0f, Paint())

        binding.ivResult.setImageBitmap(resultBitmap)
        binding.mskView.reset()

        GlobalScope.launch {
            val result = test()
            logInfo(result)
        }
    }

    private suspend fun test(): String {
        return suspendCancellableCoroutine {
            it.invokeOnCancellation {
                logInfo("cancel")
            }

            Thread {
                Thread.sleep(1500)
                it.resume("hello this is value")
            }.start()
        }
    }
}