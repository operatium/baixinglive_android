package com.baixingkuaizu.live.android.widget.loading;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;
import kotlin.math.sqrt
import androidx.core.graphics.toColorInt
import androidx.core.graphics.withTranslation

class Baixing_LoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mBaixing_earthPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    }
    private val mBaixing_moonPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    }
    private val mBaixing_earthRadius = 30f // 地球大小增大一倍
    private val mBaixing_moonRadius = 12f
    private var mBaixing_rotationAngle = 0f
    private var mBaixing_earthRotationAngle = 0f // 地球自转角度
    private var mBaixing_animator: ValueAnimator? = null
    private val mBaixing_ellipseMajorAxis = 60f
    private val mBaixing_ellipseMinorAxis = 30f
    private val mBaixing_verticalOffset = 0f
    private var isMovingRight = false // 月亮移动方向

    private val mBaixing_shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = "#000000".toColorInt()
        alpha = 80 // 调整阴影透明度
        maskFilter = BlurMaskFilter(5f, BlurMaskFilter.Blur.NORMAL) // 模糊阴影
    }

    private val mBaixing_backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    
    private val moonPath = Path()
    private val smallMoonPath = Path()
    private val finalMoonPath = Path()
    private val earthGradient: LinearGradient by lazy {
        LinearGradient(
            0f, -mBaixing_earthRadius,
            0f, mBaixing_earthRadius,
            "#ADD8E6".toColorInt(), // 浅蓝
            "#00008B".toColorInt(), // 深蓝
            Shader.TileMode.CLAMP
        )
    }
    private val moonLightColor = "#E0E0E0".toColorInt() // 浅灰色
    private val moonGoldColor = "#FFD700".toColorInt() // 金黄色

    init {
        setupAnimation()
        setupBackgroundGradient()
    }

    private fun setupBackgroundGradient() {
        val lightColor = Color.argb(178, 0, 0, 0) // 70% 透明黑色
        val darkColor = Color.argb(78, 40, 0, 10)
        val centerX = width / 2f
        val centerY = height / 2f + mBaixing_verticalOffset
        val gradient = RadialGradient(
            centerX,
            centerY,
            mBaixing_ellipseMajorAxis + mBaixing_moonRadius,
            lightColor,
            darkColor,
            Shader.TileMode.CLAMP
        )
        mBaixing_backgroundPaint.shader = gradient
    }

    private fun setupAnimation() {
        mBaixing_animator = ValueAnimator.ofFloat(0f, 360f).apply {
            duration = 800
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener { animation ->
                mBaixing_rotationAngle = animation.animatedValue as Float
                mBaixing_earthRotationAngle = (mBaixing_earthRotationAngle + 1) % 360 // 更新地球自转角度
                isMovingRight = mBaixing_rotationAngle in 0f..180f
                invalidate()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = width / 2f
        val centerY = height / 2f + mBaixing_verticalOffset

        canvas.drawCircle(centerX, centerY, mBaixing_ellipseMajorAxis + mBaixing_moonRadius, mBaixing_backgroundPaint)
        canvas.drawCircle(centerX, centerY + mBaixing_earthRadius * 0.2f, mBaixing_earthRadius, mBaixing_shadowPaint)
        canvas.withTranslation(centerX, centerY) {
            mBaixing_earthPaint.shader = earthGradient
            val angle = Math.toRadians(mBaixing_rotationAngle.toDouble())
            val moonX = mBaixing_ellipseMajorAxis * Math.cos(angle).toFloat()
            val moonY = mBaixing_ellipseMinorAxis * Math.sin(angle).toFloat()
            val distance = sqrt(moonX * moonX + moonY * moonY)
            // 使用径向渐变实现浅灰到金黄色
            val moonGradient = RadialGradient(
                moonX, moonY, mBaixing_moonRadius,
                moonLightColor, moonGoldColor,
                Shader.TileMode.CLAMP
            )
            mBaixing_moonPaint.shader = moonGradient
            if (isMovingRight) {
                drawCircle(0f, 0f, mBaixing_earthRadius, mBaixing_earthPaint)
                moonPath.reset()
                smallMoonPath.reset()
                finalMoonPath.reset()
                moonPath.addCircle(moonX, moonY, mBaixing_moonRadius, Path.Direction.CW)
                smallMoonPath.addCircle(
                    moonX + mBaixing_moonRadius * 0.8f,
                    moonY,
                    mBaixing_moonRadius * 0.9f,
                    Path.Direction.CW
                )
                if (distance <= mBaixing_earthRadius + mBaixing_moonRadius) {
                    val saveCount =
                        saveLayer(-width / 2f, -height / 2f, width / 2f, height / 2f, null)
                    finalMoonPath.op(moonPath, smallMoonPath, Path.Op.DIFFERENCE)
                    drawPath(finalMoonPath, mBaixing_moonPaint)
                    mBaixing_moonPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
                    drawCircle(0f, 0f, mBaixing_earthRadius, mBaixing_moonPaint)
                    mBaixing_moonPaint.xfermode = null
                    restoreToCount(saveCount)
                } else {
                    finalMoonPath.op(moonPath, smallMoonPath, Path.Op.DIFFERENCE)
                    drawPath(finalMoonPath, mBaixing_moonPaint)
                }
            } else {
                moonPath.reset()
                smallMoonPath.reset()
                finalMoonPath.reset()
                moonPath.addCircle(moonX, moonY, mBaixing_moonRadius, Path.Direction.CW)
                smallMoonPath.addCircle(
                    moonX + mBaixing_moonRadius * 0.8f,
                    moonY,
                    mBaixing_moonRadius * 0.9f,
                    Path.Direction.CW
                )
                finalMoonPath.op(moonPath, smallMoonPath, Path.Op.DIFFERENCE)
                drawPath(finalMoonPath, mBaixing_moonPaint)
                if (distance <= mBaixing_earthRadius + mBaixing_moonRadius) {
                    val saveCount =
                        saveLayer(-width / 2f, -height / 2f, width / 2f, height / 2f, null)
                    mBaixing_earthPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
                    drawCircle(0f, 0f, mBaixing_earthRadius, mBaixing_earthPaint)
                    mBaixing_earthPaint.xfermode = null
                    restoreToCount(saveCount)
                }
                drawCircle(0f, 0f, mBaixing_earthRadius, mBaixing_earthPaint)
            }
        }
    }

    fun baixing_startAnimation() {
        mBaixing_animator?.start()
    }

    fun baixing_stopAnimation() {
        mBaixing_animator?.cancel()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        baixing_startAnimation()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        baixing_stopAnimation()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setupBackgroundGradient()
    }
}