package com.baixingkuaizu.live.android.widget.circularimage

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.baixingkuaizu.live.android.R
import androidx.core.content.withStyledAttributes

class CircularImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    private val path = Path()
    private var radius = 0f
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var borderWidth = 0f
    private var borderColor = Color.WHITE

    init {
        // 获取自定义属性
        context.withStyledAttributes(attrs, R.styleable.CircularImageView) {
            borderWidth = getDimension(R.styleable.CircularImageView_borderWidth, 0f)
            borderColor = getColor(R.styleable.CircularImageView_borderColor, Color.WHITE)
        }

        // 初始化边框画笔
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderWidth
        borderPaint.color = borderColor
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = (width.coerceAtMost(height) - borderWidth) / 2f
    }

    override fun onDraw(canvas: Canvas) {
        path.reset()
        path.addCircle(width / 2f, height / 2f, radius, Path.Direction.CW)
        canvas.clipPath(path)
        super.onDraw(canvas)

        // 绘制边框
        canvas.drawCircle(width / 2f, height / 2f, radius, borderPaint)
    }
}