package com.mi.layoutinspector

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup

/**
 * create by niuxiaowei
 * date : 2021/7/29
 **/

@SuppressLint("ViewConstructor")
class InspectItemView constructor(
        context: Context,
        val inspectView: View,
        val inspectPage: InspectPage,
        var isSetClick4View: Boolean,
        var parentInspectItemView: InspectItemView?
) : android.support.v7.widget.AppCompatTextView(context) {

    private var childs: MutableList<InspectItemView>? = null

    init {
        parentInspectItemView?.addChild(this)
    }

    private var mPaint: Paint = Paint()
    private val COLOR_BORDER: Int by lazy { ContextCompat.getColor(context, R.color.li_color_fc2f68) }
    private val COLOR_CORNER: Int by lazy { ContextCompat.getColor(context, R.color.li_color_34b1f3) }
    private val COLOR_PADDING: Int by lazy { ContextCompat.getColor(context, R.color.li_color_03A9F4) }
    private val COLOR_BORDER_SELECT: Int by lazy { ContextCompat.getColor(context, R.color.li_gift_number_second) }

    private val STROKE_WIDTH: Float = 3.0f
    private val STROKE_WIDTH_SELECT = 15.0f

    private var isSelecte: Boolean = false

    init {
        mPaint = Paint()
        mPaint.color = COLOR_BORDER
        mPaint.strokeWidth = STROKE_WIDTH  //画笔粗细
        if (isSetClick4View) {
            setOnClickListener {
                if (inspectPage.curShowedView() == inspectView) {
                    inspectPage.hideViewAttributes()
                    setSelecte(false)
                } else {
                    showViewAttributes()
                }
            }
        }
    }

    fun addChild(child: InspectItemView) {
        if (childs == null) {
            childs = mutableListOf()
        }
        childs?.add(child)
    }

    fun getChilds(): MutableList<InspectItemView>? {
        return childs
    }

    fun hideViewAttributes() {
        inspectPage.hideViewAttributes()
        setSelecte(false)
    }

    fun showViewAttributes() {
        inspectPage.showViewAttributes(inspectView, this)
        setSelecte(true)

    }

    fun setSelecte(select: Boolean) {
        isSelecte = select
        invalidate()
    }

    /**
     * 画margin
     * @param canvas Canvas?
     * @param paint Paint
     */
    fun drawMargin(canvas: Canvas?, paint: Paint) {
        if (inspectView.layoutParams is ViewGroup.MarginLayoutParams) {
            //横线长度
            val horLlineLen = 30.0
            val arrowOffsetX = 5.0
            val arrowOffsetY = 8.0


            val marginLP = inspectView.layoutParams as ViewGroup.MarginLayoutParams
            /**
             * 画top margin, 画出的样子如下
             *
             *   *****
             *     *
             *   * * *
             *     *
             *     *
             */
            if (marginLP.topMargin > 0) {
                val arrowVerticesX = x + (width / 2).toFloat()
                val arrowVerticesY = y - marginLP.topMargin
                //画竖线
                canvas?.drawLine(x + (width / 2).toFloat(), y, arrowVerticesX, arrowVerticesY, paint)
                //画横线
                canvas?.drawLine((arrowVerticesX - horLlineLen / 2).toFloat(), arrowVerticesY, (arrowVerticesX + horLlineLen / 2).toFloat(), arrowVerticesY, paint)
                //画箭头左边
                canvas?.drawLine(arrowVerticesX, arrowVerticesY, (arrowVerticesX - arrowOffsetX).toFloat(), (arrowVerticesY + arrowOffsetY).toFloat(), paint)
                //画箭头右边
                canvas?.drawLine(arrowVerticesX, arrowVerticesY, (arrowVerticesX + arrowOffsetX).toFloat(), (arrowVerticesY + arrowOffsetY).toFloat(), paint)
            }
            /**
             * 画right margin, 画出的样子如下
             *
             *             *
             *          *  *
             *    **********
             *          *  *
             *             *
             */
            if (marginLP.rightMargin > 0) {
                val arrowVerticesX = x + (width).toFloat() + marginLP.rightMargin
                val arrowVerticesY = y + height / 2
                //画竖线
                canvas?.drawLine(arrowVerticesX, (arrowVerticesY - horLlineLen / 2).toFloat(), arrowVerticesX, (arrowVerticesY + horLlineLen / 2).toFloat(), paint)
                //画横线
                canvas?.drawLine(arrowVerticesX, arrowVerticesY, arrowVerticesX - marginLP.rightMargin, arrowVerticesY, paint)
                //画箭头左边
                canvas?.drawLine(arrowVerticesX, arrowVerticesY, (arrowVerticesX - arrowOffsetX).toFloat(), (arrowVerticesY - arrowOffsetY).toFloat(), paint)
                //画箭头右边
                canvas?.drawLine(arrowVerticesX, arrowVerticesY, (arrowVerticesX - arrowOffsetX).toFloat(), (arrowVerticesY + arrowOffsetY).toFloat(), paint)
            }
            /**
             * 画bottom margin, 画出的样子如下

             *
             *     *
             *     *
             *     *
             *   * * *
             *   *****
             */
            if (marginLP.bottomMargin > 0) {
                val arrowVerticesX = x + (width / 2).toFloat()
                val arrowVerticesY = y + height + marginLP.bottomMargin
                //画竖线
                canvas?.drawLine(arrowVerticesX, arrowVerticesY, arrowVerticesX, (arrowVerticesY - marginLP.bottomMargin), paint)
                //画横线
                canvas?.drawLine((arrowVerticesX - horLlineLen / 2).toFloat(), arrowVerticesY, (arrowVerticesX + horLlineLen / 2).toFloat(), arrowVerticesY, paint)
                //画箭头左边
                canvas?.drawLine(arrowVerticesX, arrowVerticesY, (arrowVerticesX - arrowOffsetX).toFloat(), (arrowVerticesY - arrowOffsetY).toFloat(), paint)
                //画箭头右边
                canvas?.drawLine(arrowVerticesX, arrowVerticesY, (arrowVerticesX + arrowOffsetX).toFloat(), (arrowVerticesY - arrowOffsetY).toFloat(), paint)
            }
            /**
             * 画left margin, 画出的样子如下
             *
             *   *
             *   * *
             *   **********
             *   * *
             *   *
             */
            if (marginLP.leftMargin > 0) {
                val arrowVerticesX = x - marginLP.leftMargin
                val arrowVerticesY = y + height / 2
                //画竖线
                canvas?.drawLine(arrowVerticesX, (arrowVerticesY - horLlineLen / 2).toFloat(), arrowVerticesX, ((arrowVerticesY + horLlineLen / 2).toFloat()), paint)
                //画横线
                canvas?.drawLine(arrowVerticesX, arrowVerticesY, (arrowVerticesX + marginLP.leftMargin), arrowVerticesY, paint)
                //画箭头左边
                canvas?.drawLine(arrowVerticesX, arrowVerticesY, (arrowVerticesX + arrowOffsetX).toFloat(), (arrowVerticesY - arrowOffsetY).toFloat(), paint)
                //画箭头右边
                canvas?.drawLine(arrowVerticesX, arrowVerticesY, (arrowVerticesX + arrowOffsetX).toFloat(), (arrowVerticesY + arrowOffsetY).toFloat(), paint)
            }
        }
    }

    override fun draw(canvas: Canvas?) {

        val width = this.width
        val height = this.height
        val LEFT = 0.0f
        val TOP = 0.0f

        //画四个边框
        mPaint.color = if (isSelecte) {
            COLOR_BORDER_SELECT
        } else {
            COLOR_BORDER
        }
        mPaint.strokeWidth = if (isSelecte) {
            STROKE_WIDTH_SELECT
        } else {
            STROKE_WIDTH
        }
        //画四个边
        canvas!!.drawLine(LEFT, TOP, (width).toFloat(), TOP, mPaint)
        canvas.drawLine((width).toFloat(), TOP, (width).toFloat(), height.toFloat(), mPaint)
        canvas.drawLine((width).toFloat(), height.toFloat(), LEFT, height.toFloat(), mPaint)
        canvas.drawLine(LEFT, height.toFloat(), LEFT, TOP, mPaint)
        //画四个角
        if (!isSelecte) {
            mPaint.color = COLOR_CORNER
            mPaint.strokeWidth = 5.0f //画笔粗细
            val CORNER_LINE_LEN = 15.0f
            //左上角
            canvas.drawLine(LEFT, TOP, (CORNER_LINE_LEN), TOP, mPaint)
            canvas.drawLine(LEFT, TOP, LEFT, CORNER_LINE_LEN, mPaint)

            //右上角
            canvas.drawLine(width.toFloat() - CORNER_LINE_LEN, TOP, width.toFloat(), TOP, mPaint)
            canvas.drawLine(width.toFloat(), TOP, width.toFloat(), CORNER_LINE_LEN, mPaint)

            //右下角
            canvas.drawLine(width.toFloat(), height.toFloat() - CORNER_LINE_LEN, width.toFloat(), height.toFloat(), mPaint)
            canvas.drawLine(width.toFloat() - CORNER_LINE_LEN, height.toFloat(), width.toFloat(), height.toFloat(), mPaint)

            //左下角
            canvas.drawLine(LEFT, height.toFloat(), CORNER_LINE_LEN, height.toFloat(), mPaint)
            canvas.drawLine(LEFT, height.toFloat(), LEFT, height.toFloat() - CORNER_LINE_LEN, mPaint)
        }

        //画padding
        mPaint.color = COLOR_PADDING
        if (inspectView.paddingTop > 0) {
            canvas.drawRect(LEFT, TOP, width.toFloat(), inspectView.paddingTop.toFloat(), mPaint)
        }
        if (inspectView.paddingBottom > 0) {
            canvas.drawRect(LEFT, height.toFloat() - inspectView.paddingBottom.toFloat(), width.toFloat(), height.toFloat(), mPaint)
        }
        if (inspectView.paddingLeft > 0) {
            canvas.drawRect(LEFT, TOP, inspectView.paddingLeft.toFloat(), height.toFloat(), mPaint)
        }
        if (inspectView.paddingRight > 0) {
            canvas.drawRect(width.toFloat() - inspectView.paddingRight, TOP, width.toFloat(), height.toFloat(), mPaint)
        }

        super.draw(canvas)
    }

}