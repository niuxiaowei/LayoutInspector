package com.mi.layoutinspector.inspector

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import com.mi.layoutinspector.LayoutInspector
import com.mi.layoutinspector.LayoutInspector.getContext
import com.mi.layoutinspector.R

/**
 * create by niuxiaowei
 * date : 2021/7/29
 * 它对应界面中的一个具体的view（该view既可以是View也可以是ViewGroup）
 * 用来绘制 inspectedView 的宽高，位置，padding  margin信息的view，自定义了绘制流程
 * @param inspectedView 被检测的view（界面上的具体view）
 **/

@SuppressLint("ViewConstructor")
class ViewInspector constructor(
        context: Context,
        private val inspectedView: View,
        val pageInspector: PageInspector,
        var isSetClick4View: Boolean,
        private val parent: IViewInspector?
) : android.support.v7.widget.AppCompatTextView(context), IViewInspector {

    private var children: MutableList<IViewInspector>? = null

    init {
        parent?.addChild(this)

    }

    private var mPaint: Paint = Paint()


    companion object {
        private const val STROKE_WIDTH = 3.0f
        private const val STROKE_WIDTH_SELECT = 18.0f
        private val COLOR_BORDER: Int = ContextCompat.getColor(getContext(), R.color.li_color_fc2f68)
        private val COLOR_CORNER: Int by lazy { ContextCompat.getColor(getContext(), R.color.li_color_34b1f3) }
        private val COLOR_PADDING: Int by lazy { ContextCompat.getColor(getContext(), R.color.li_color_03A9F4) }
        private val COLOR_BORDER_SELECT: Int by lazy { ContextCompat.getColor(getContext(), R.color.li_gift_number_second) }
    }


    private var isSelecte: Boolean = false

    //InspectItemView是否超出了屏幕
    var isOutOfScreen: Boolean = false

    // InspectItemView大小是否是小于等于0
    var sizeIsZero: Boolean = false
        get() = width * height <= 0


    init {
        mPaint = Paint()
        mPaint.color = COLOR_BORDER
        mPaint.strokeWidth = STROKE_WIDTH  //画笔粗细
        if (isSetClick4View) {
            setOnClickListener {
                if (pageInspector.curInspectedView() == inspectedView) {
                    pageInspector.hideViewInfosPopupWindow()
                    setSelecte(false)
                } else {
                    showViewInfosPopupWindow()
                }
            }
        }
    }

    override fun addChild(child: IViewInspector) {
        if (children == null) {
            children = mutableListOf()
        }
        children?.add(child)
    }

    override fun childs(): MutableList<IViewInspector>? {
        return children
    }

    override fun hideViewInfosPopupWindown() {
        pageInspector.hideViewInfosPopupWindow()
        setSelecte(false)
    }

    override fun showViewInfosPopupWindow() {
        pageInspector.showViewInfosPopupWindow(inspectedView, this)
        setSelecte(true)

    }

    override fun setClickable(clickable: Boolean) {
        super.setClickable(clickable)
    }

    override fun parent(): IViewInspector? {
        return parent
    }

    override fun inspectedView(): View {
        return inspectedView
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
    internal fun drawMargin(canvas: Canvas?, paint: Paint) {
        if (sizeIsZero) {
            return
        }
        if (inspectedView.layoutParams is ViewGroup.MarginLayoutParams) {
            //横线长度
            val horLlineLen = 30.0
            val arrowOffsetX = 5.0
            val arrowOffsetY = 8.0


            val marginLP = inspectedView.layoutParams as ViewGroup.MarginLayoutParams
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
        if (sizeIsZero) {
            return
        }
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
        if (LayoutInspector.isShowViewPadding) {
            mPaint.color = COLOR_PADDING
            if (inspectedView.paddingTop > 0) {
                canvas.drawRect(LEFT, TOP, width.toFloat(), inspectedView.paddingTop.toFloat(), mPaint)
            }
            if (inspectedView.paddingBottom > 0) {
                canvas.drawRect(LEFT, height.toFloat() - inspectedView.paddingBottom.toFloat(), width.toFloat(), height.toFloat(), mPaint)
            }
            if (inspectedView.paddingLeft > 0) {
                canvas.drawRect(LEFT, TOP, inspectedView.paddingLeft.toFloat(), height.toFloat(), mPaint)
            }
            if (inspectedView.paddingRight > 0) {
                canvas.drawRect(width.toFloat() - inspectedView.paddingRight, TOP, width.toFloat(), height.toFloat(), mPaint)
            }
        }

        super.draw(canvas)
    }

}