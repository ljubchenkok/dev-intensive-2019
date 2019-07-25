package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import ru.skillbranch.devintensive.R

class CircleImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ImageView(context, attrs) {

    private var borderWidth: Int
    private var borderColor: Int
    private var bitmapShader: Shader? = null
    private val shaderMatrix: Matrix
    private val bitmapDrawBounds: RectF
    private val borderBounds: RectF
    private var bitmap: Bitmap? = null
    private val bitmapPaint: Paint
    private val borderPaint: Paint
    private val pressedPaint: Paint
    private val initialized: Boolean

    companion object {
        const val DEFAULT_BORDER_WIDTH = 2
        const val DEFAULT_BORDER_COLOR = Color.WHITE
    }

    init {
        borderColor = DEFAULT_BORDER_COLOR
        borderWidth = DEFAULT_BORDER_WIDTH

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, 0, 0)

            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, Color.TRANSPARENT)
            borderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_cv_borderWidth, 0)
            a.recycle()
        }

        shaderMatrix = Matrix()
        bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        borderBounds = RectF()
        bitmapDrawBounds = RectF()
        borderPaint.color = borderColor
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderWidth.toFloat()
        pressedPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        pressedPaint.style = Paint.Style.FILL
        initialized = true
        setupBitmap()
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
        drawBitmap(canvas)
        drawBorder(canvas)
    }


    private fun drawBorder(canvas: Canvas) {
        if (borderPaint.strokeWidth > 0f) {
            canvas.drawOval(borderBounds, borderPaint)
        }
    }

    private fun drawBitmap(canvas: Canvas) {
        canvas.drawOval(bitmapDrawBounds, bitmapPaint)
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
        val diameter = Math.min(contentWidth, contentHeight)
        bounds.set(left, top, left + diameter, top + diameter)
    }

    private fun setupBitmap() {
        if (!initialized) {
            return
        }
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
        if (drawable == null) {
            return null
        }
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }



}