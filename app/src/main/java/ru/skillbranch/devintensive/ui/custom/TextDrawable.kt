package ru.skillbranch.devintensive.ui.custom


import android.graphics.*
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape



class TextDrawable private constructor(builder: Builder) : ShapeDrawable(builder.shape) {

    private val textPaint: Paint
    private val text: String?
    private val color: Int
    private val shape: OvalShape?
    private val height: Int
    private val width: Int
    private val fontSize: Int
    private val radius: Float


    init {
        shape = builder.shape
        height = builder.height
        width = builder.width
        radius = builder.radius
        text = if (builder.toUpperCase) builder.text!!.toUpperCase() else builder.text
        color = builder.color
        fontSize = builder.fontSize
        textPaint = Paint()
        textPaint.color = builder.textColor
        textPaint.isAntiAlias = true
        textPaint.isFakeBoldText = builder.isBold
        textPaint.style = Paint.Style.FILL
        textPaint.typeface = builder.font
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.strokeWidth = builder.borderThickness.toFloat()
        val paint = paint
        paint.color = color
    }


    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val r = bounds

        val count = canvas.save()
        canvas.translate(r.left.toFloat(), r.top.toFloat())
        val width = if (this.width < 0) r.width() else this.width
        val height = if (this.height < 0) r.height() else this.height
        val fontSize = if (this.fontSize < 0) Math.min(width, height) / 2 else this.fontSize
        textPaint.textSize = fontSize.toFloat()
        canvas.drawText(
            text!!,
            (width / 2).toFloat(),
            height / 2 - (textPaint.descent() + textPaint.ascent()) / 2,
            textPaint
        )
        canvas.restoreToCount(count)
    }

    override fun getIntrinsicWidth(): Int {
        return width
    }

    override fun getIntrinsicHeight(): Int {
        return height
    }

    class Builder() : IConfigBuilder, IShapeBuilder, IBuilder {

        var text: String? = null
        var color: Int = 0
        var borderThickness: Int = 0
        var width: Int = 0
        var height: Int = 0
        var font: Typeface? = null
        var shape: OvalShape? = null
        var textColor: Int = 0
        var fontSize: Int = 0
        var isBold: Boolean = false
        var toUpperCase: Boolean = false
        var radius: Float = 0.toFloat()

        init {
            text = ""
            color = Color.GRAY
            textColor = Color.WHITE
            borderThickness = 0
            width = -1
            height = -1
            shape = OvalShape()
            font = Typeface.create("sans-serif-light", Typeface.NORMAL)
            fontSize = -1
            isBold = false
            toUpperCase = false
        }

        override fun width(width: Int): IConfigBuilder {
            this.width = width
            return this
        }

        override fun height(height: Int): IConfigBuilder {
            this.height = height
            return this
        }

        override fun textColor(color: Int): IConfigBuilder {
            this.textColor = color
            return this
        }


        override fun useFont(font: Typeface): IConfigBuilder {
            this.font = font
            return this
        }

        override fun fontSize(size: Int): IConfigBuilder {
            this.fontSize = size
            return this
        }

        override fun bold(): IConfigBuilder {
            this.isBold = true
            return this
        }


        override fun beginConfig(): IConfigBuilder {
            return this
        }

        override fun endConfig(): IShapeBuilder {
            return this
        }

        override fun round(): IBuilder {
            this.shape = OvalShape()
            return this
        }


        override fun buildRound(text: String, color: Int): TextDrawable {
            round()
            return build(text, color)
        }

        override fun build(text: String, color: Int): TextDrawable {
            this.color = color
            this.text = text
            return TextDrawable(this)
        }
    }

    interface IConfigBuilder {
        fun width(width: Int): IConfigBuilder
        fun height(height: Int): IConfigBuilder
        fun textColor(color: Int): IConfigBuilder
        fun useFont(font: Typeface): IConfigBuilder
        fun fontSize(size: Int): IConfigBuilder
        fun bold(): IConfigBuilder
        fun endConfig(): IShapeBuilder
    }

    interface IBuilder {

        fun build(text: String, color: Int): TextDrawable
    }

    interface IShapeBuilder {

        fun beginConfig(): IConfigBuilder
        fun round(): IBuilder
        fun buildRound(text: String, color: Int): TextDrawable
    }

    companion object {

        fun builder(): IShapeBuilder {
            return Builder()
        }
    }
}