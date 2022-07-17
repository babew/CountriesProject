package lyubomir.babev.countries.project.views.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText

class CustomEditText: AppCompatEditText {

    interface DrawableListener {
        fun onClick(editText: AppCompatEditText)
    }

    var drawableListener: DrawableListener? = null

    private var drawableRight :Drawable? = null

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr)


    override fun setCompoundDrawables(left: Drawable?, top: Drawable?, right: Drawable?, bottom: Drawable?) {
        if (right != null)
            drawableRight = right

        super.setCompoundDrawables(left, top, right, bottom)
    }

    @Throws(Throwable::class)
    protected fun finalize() {
        drawableRight = null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (drawableListener != null && drawableRight != null && event.action == MotionEvent.ACTION_UP) {
            val bounds          = drawableRight!!.bounds
            val extraTapArea    = 25

            var x   = width - (event.x.toInt() + extraTapArea)
            var y   = event.y.toInt() - extraTapArea

            if (x <= 0)
                x += extraTapArea

            if (y <= 0)
                y = event.y.toInt()

            if (bounds.contains(x, y)) {
                drawableListener?.onClick(this)
                event.action = MotionEvent.ACTION_CANCEL
                return false
            }

            return super.onTouchEvent(event)
        }
        return super.onTouchEvent(event)
    }
}