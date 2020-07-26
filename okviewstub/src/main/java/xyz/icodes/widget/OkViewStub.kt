package xyz.icodes.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import xyz.icodes.R

/**
 * @author xechoz
 * Date：2020-07-26
 * Email: xechoz.gmail@gmail.com
 * Description: 改进版的 ViewStub. 增加了 xml 布局预览，xml 指定view 类, 代码设置 View 的功能
 *
 * Usage: 有两种使用方式
 *  + 在布局里 设置 layout 或者 layout_class, 在需要显示的时候再调用 无参的 inflate()
 *  + inflate(your view): 直接设置显示的内容
 *
 *  @see inflate 填充内容。可多次调用
 *  @see isInflated 是否已填充过内容
 *  @see contentView 填充的内容
 */
class OkViewStub @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private companion object {
        private const val TAG = "PlaceHolderView"
    }

    private var targetClass: String? = ""
    private var layoutId: Int = 0
    private val boundRect: Rect by lazy {
        Rect()
    }

    private val paint: Paint by lazy {
        Paint()
    }

    var isInflated = false
        private set
    var contentView: View? = null
        private set

    init {
        context.obtainStyledAttributes(attrs, R.styleable.OkViewStub, defStyleAttr, 0).apply {
            layoutId = getResourceId(R.styleable.OkViewStub_layout, 0)
            targetClass = getString(R.styleable.OkViewStub_layout_class)

            if (isInEditMode) {
                paint.style = Paint.Style.STROKE
                paint.color = Color.BLACK
                paint.strokeWidth = 1f
                paint.pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
            }

            recycle()
        }

        if (isInEditMode) {
            setWillNotDraw(false)
        } else {
            setWillNotDraw(true)
        }
    }

    private fun createTargetView(layoutResId: Int, clazz: String?, attrs: AttributeSet): View? {
        return if (layoutResId != 0) {
            createTargetView(layoutId)
        } else if (!clazz.isNullOrEmpty()) {
            createTargetView(clazz, attrs)
        } else null
    }

    private fun createTargetView(layoutResId: Int): View? {
        return LayoutInflater.from(context).inflate(layoutResId, null, false)
    }

    private fun createTargetView(clazz: String, attrs: AttributeSet): View? {
        return when {
            clazz.startsWith(".") -> {
                LayoutInflater.from(context).createView(context, clazz, context.packageName, attrs)
            }
            clazz.contains(".") -> {
                LayoutInflater.from(context).createView(context, clazz, "", attrs)
            }
            else -> {
                LayoutInflater.from(context).createView(context, clazz, "android.widget.", attrs)
                    ?: LayoutInflater.from(context)
                        .createView(context, clazz, "android.view.", attrs)
            }
        }
    }

    /**
     * 使用 xml 中指定的 layout 或者 layout_class 填充内容
     */
    fun inflate() {
        if (contentView != null) {
            Log.w(TAG, "inflate: content is inflated")
            return
        }

        attrs?.let {
            createTargetView(layoutId, targetClass, attrs)?.let { inflate(it) }
        }
    }

    /**
     * 整个流程是参考 {@see ViewStub}
     * 设置填充的view, 将会执行以下逻辑
     * 1. 把 stub 或者 当前的View 的 layout param 会设置给 content
     * 2. 把 stub 或者 当前的View 从 parent 移除，对应的位置设置为 content
     * @param content 内容
     */
    fun inflate(content: View) {
        if (isInflated) {
            Log.i(TAG, "inflate: replace content view")
        } else {
            Log.i(TAG, "inflate: replace stub view")
        }

        isInflated = true
        val parent = this.parent ?: contentView?.parent

        if (parent is ViewGroup) {
            replaceSelfWithView(contentView ?: this, content, parent)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (isInEditMode) {
            inflate()
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (isInEditMode) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        } else {
            // super.onMeasure(widthMeasureSpec, heightMeasureSpec) // do nothing
            setMeasuredDimension(0, 0)
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun draw(canvas: Canvas) {
        if (isInEditMode) {
            super.draw(canvas)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (isInEditMode) {
            super.onDraw(canvas)
            boundRect.set(1, 1, measuredWidth - 1, measuredHeight - 1)
            canvas.drawRect(boundRect, paint)
        }
    }

    private fun replaceSelfWithView(from: View, to: View, parent: ViewGroup) {
        this.contentView = to
        to.id = id
        val index = parent.indexOfChild(from)
        parent.removeViewInLayout(from) // remove holder

        (to.parent as? ViewGroup)?.removeView(to)

        val layoutParams = layoutParams
        if (layoutParams != null) {
            parent.addView(to, index, layoutParams)
        } else {
            parent.addView(to, index)
        }
    }
}
