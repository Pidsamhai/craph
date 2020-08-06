package com.psm.craph

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

class LineChart @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attributeSet, defStyle) {

    object DotStyle {
        const val FILL = 1
        const val STROKE = 2
    }

    private val exDataSet = listOf(
        LineDataSet(1f, 1f),
        LineDataSet(2f, 3f),
        LineDataSet(3f, 10f),
        LineDataSet(4f, 1f),
        LineDataSet(5f, 5f),
        LineDataSet(6f, 7f),
        LineDataSet(7f, 4f),
        LineDataSet(8f, 6f),
        LineDataSet(9f, 9f),
        LineDataSet(10f, 2f)
    )

    var dataSet: List<LineDataSet>? = null


    val typeArray = context.obtainStyledAttributes(attributeSet, R.styleable.LineChart)
    val gridSize = 12
    val padding = 10f
    val vGridPaint = Paint().apply {
        isAntiAlias = true
        color = typeArray.getColor(
            R.styleable.LineChart_craph_vGridColor,
            context.getColor(R.color.craph_vGridColor)
        )
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }
    val hGridPaint = Paint().apply {
        isAntiAlias = true
        color = typeArray.getColor(
            R.styleable.LineChart_craph_hGridColor,
            context.getColor(R.color.craph_hGridColor)
        )
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }
    val linePaint = Paint().apply {
        isAntiAlias = true
        color = typeArray.getColor(
            R.styleable.LineChart_craph_line_color,
            context.getColor(R.color.craph_line_color)
        )
        strokeWidth = typeArray.getDimension(
            R.styleable.LineChart_craph_line_width,
            context.resources.getDimension(R.dimen.craph_line_width)
        )
    }
    val axisYPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLUE
        strokeWidth = 3f
        textSize = 16F
    }
    val axisXPaint = Paint().apply {
        isAntiAlias = true
        color = Color.RED
        strokeWidth = 3f
        textSize = 16F
    }
    val dotPaint = Paint().apply {
        isAntiAlias = true
        style = when (typeArray.getInt(R.styleable.LineChart_dot_style, 1)) {
            DotStyle.FILL -> Paint.Style.FILL
            DotStyle.STROKE -> Paint.Style.STROKE
            else -> Paint.Style.STROKE
        }
        strokeWidth = 3f
    }
    val bgColor = typeArray.getColor(
        R.styleable.LineChart_craph_bgColor,
        context.getColor(R.color.craph_bgColor)
    )
    val dotRadius = typeArray.getDimension(
        R.styleable.LineChart_craph_dotRadius,
        context.resources.getDimension(R.dimen.craph_dotRadius)
    )
    val yMin = 10

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

//        val exDataSet = (0 until 10).map { _ ->
//            val rndX = Random.nextInt(0, 10)
//            val rndY = Random.nextInt(0, 10)
//            return@map LineDataSet(rndX.toFloat(), rndY.toFloat())
//        }


        val _dataSet: List<LineDataSet> = convertData(dataSet ?: exDataSet)


        canvas?.drawColor(bgColor)
        /**
         * Draw Y Axis
         */
        canvas?.drawLine(padding, padding, padding, height.toFloat() - padding, axisYPaint)
        canvas?.drawText("Y", 20F, 20f, axisYPaint)
        /**
         * Draw X Axis
         */
        canvas?.drawLine(
            10f,
            width.toFloat() - padding,
            width.toFloat() - padding,
            height.toFloat() - padding,
            axisXPaint
        )
        canvas?.drawText("X", width.toFloat() - 20, height.toFloat() - 20, axisXPaint)


        /**
         * Draw Point
         */
//        canvas?.drawLine(0f + padding, height - padding, width - padding, 0f, linePaint)
        canvas?.let {
            drawHorizontalGrid(canvas,_dataSet)
            drawVerticalGrid(canvas)
            drawConnectLine(canvas, _dataSet)
            drawPoint(it, _dataSet)
        }
    }

    fun drawPoint(canvas: Canvas, dataSet: List<LineDataSet>) {
        /**
         * Draw 0,0
         */
        canvas.drawCircle(0f + padding, height.toFloat() - padding, dotRadius, dotPaint)
        for (data in dataSet) {
            Log.d("TAG", "drawPoint: $data")
            canvas.drawCircle(data.x, data.y, dotRadius, dotPaint)
        }
    }

    fun drawConnectLine(canvas: Canvas, dataSet: List<LineDataSet>) {
        for (i in dataSet.indices) {
            if (i != dataSet.size - 1) {
                val current = dataSet[i]
                val next = dataSet[i + 1]
                if (i == 0) {
                    canvas.drawLine(
                        0f + padding,
                        height.toFloat() - padding,
                        current.x,
                        current.y,
                        linePaint
                    )
                }
                canvas.drawLine(current.x, current.y, next.x, next.y, linePaint)
            }
        }
    }

    fun drawVerticalGrid(canvas: Canvas) {
        var previousPoint = padding
        val space = (width) / gridSize
        for (i in 0 until gridSize) {
            val y =
                Log.d("TAG", "drawVerticalGrid: $previousPoint")
            canvas.drawLine(
                previousPoint,
                padding,
                previousPoint,
                height.toFloat() - padding,
                vGridPaint
            )
            previousPoint += space
        }
    }

    fun drawHorizontalGrid(canvas: Canvas, list: List<LineDataSet>) {
        for (i in 0 until list.size) {
            canvas.drawLine(
                padding,
                list[i].y,
                width - padding,
                list[i].y,
                hGridPaint
            )
        }
    }

    fun convertData(dataSet: List<LineDataSet>): List<LineDataSet> {
        val hspace = width / gridSize
        return dataSet.map { data ->
            val x = (hspace * data.x) + padding
            val y = (height) - (data.y * hspace) - padding
            Log.d("TAG", "convertData: $x $y")
            LineDataSet(x, y)
        }
    }

    data class LineDataSet(val x: Float, val y: Float)

}