package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorRes
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.utils.Utils
import kotlin.math.min

class CircleImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ImageView(context, attrs) {

    private var borderColor: Int
    private var borderWidth: Int
    private var bitmapShader: Shader? = null
    private val shaderMatrix = Matrix()
    private val bitmapDrawBounds = RectF()
    private val borderBounds = RectF()
    private var bitmap: Bitmap? = null
    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    companion object {
        const val DEFAULT_BORDER_WIDTH = 2
        const val DEFAULT_BORDER_COLOR = Color.WHITE
        const val DEFAULT_WIDTH = 96
        const val DEFAULT_HEIGHT = 96
    }

    init {
        borderColor = DEFAULT_BORDER_COLOR
        borderWidth = DEFAULT_BORDER_WIDTH
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, 0, 0)
            borderColor =
                a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            borderWidth = a.getDimensionPixelSize(
                R.styleable.CircleImageView_cv_borderWidth,
                DEFAULT_BORDER_WIDTH
            )
            a.recycle()
        }
        with(borderPaint) {
            color = borderColor
            style = Paint.Style.STROKE
            strokeWidth = borderWidth.toFloat()
        }

    }

    fun getBorderWidth(): Int = borderWidth

    fun setBorderWidth(dp: Int) {
        borderWidth = dp
        borderPaint.strokeWidth = dp.toFloat()
        invalidate()
    }

    fun getBorderColor(): Int = borderColor

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
        borderPaint.color = borderColor
        invalidate()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = context.resources.getColor(colorId, context.theme)
        borderPaint.color = borderColor
        invalidate()
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val halfStrokeWidth = borderPaint.strokeWidth / 2f
        updateCircleDrawBounds(bitmapDrawBounds)
        borderBounds.set(bitmapDrawBounds)
        borderBounds.inset(halfStrokeWidth, halfStrokeWidth)
        updateBitmapSize()
    }


    override fun onDraw(canvas: Canvas) {
        setupBitmap()
        canvas.drawOval(bitmapDrawBounds, bitmapPaint)
        if (borderPaint.strokeWidth > 0f) {
            canvas.drawOval(borderBounds, borderPaint)
        }
    }


    private fun updateCircleDrawBounds(bounds: RectF) {
        val contentWidth = (width - paddingLeft - paddingRight).toFloat()
        val contentHeight = (height - paddingTop - paddingBottom).toFloat()
        var left = paddingLeft.toFloat()
        var top = paddingTop.toFloat()
        if (contentWidth > contentHeight) {
            left += (contentWidth - contentHeight) / 2f
        } else {
            top += (contentHeight - contentWidth) / 2f
        }
        val diameter = min(contentWidth, contentHeight)
        bounds.set(left, top, left + diameter, top + diameter)
    }

    private fun setupBitmap() {
        bitmap = getBitmapFromDrawable(drawable)
        if (bitmap == null) {
            return
        }
        bitmapShader = BitmapShader(bitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        bitmapPaint.shader = bitmapShader
        updateBitmapSize()
    }

    private fun updateBitmapSize() {
        if (bitmap == null) return
        val dx: Float
        val dy: Float
        val scale: Float
        if (bitmap!!.width < bitmap!!.height) {
            scale = bitmapDrawBounds.width() / bitmap!!.width.toFloat()
            dx = bitmapDrawBounds.left
            dy = bitmapDrawBounds.top - bitmap!!.height * scale / 2f + bitmapDrawBounds.width() / 2f
        } else {
            scale = bitmapDrawBounds.height() / bitmap!!.height.toFloat()
            dx = bitmapDrawBounds.left - bitmap!!.width * scale / 2f + bitmapDrawBounds.width() / 2f
            dy = bitmapDrawBounds.top
        }
        shaderMatrix.setScale(scale, scale)
        shaderMatrix.postTranslate(dx, dy)
        bitmapShader!!.setLocalMatrix(shaderMatrix)
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        return when (drawable) {
            null -> null
            is BitmapDrawable -> drawable.bitmap
            else -> {
                val width =
                    if (drawable.intrinsicWidth == -1) DEFAULT_WIDTH else drawable.intrinsicWidth
                val height =
                    if (drawable.intrinsicHeight == -1) DEFAULT_HEIGHT else drawable.intrinsicHeight
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap!!)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                return bitmap
            }
        }
    }

        fun setInitials(initials: String) {
            val color = Utils.getColorFromInitials(initials, context)
            val drawable = TextDrawable.builder()
                .buildRound(initials, color)
            setImageDrawable(drawable)


        }


    }