package com.yod.common.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.StateListDrawable
import android.graphics.drawable.shapes.RectShape
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

/**
 * 点击效果 ImageView
 * Created by tangJ on 2017/8/3
 */
class TouchHImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr) {

  var alphaDrawable: Drawable? = null
  val mRect = Rect()

  val defColor = Color.BLACK

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    alphaDrawable?.apply {
      bounds = mRect
      draw(canvas)
    }
  }

  override fun drawableStateChanged() {
    super.drawableStateChanged()

    if (alphaDrawable == null) {
      createDrawable()
    }
    alphaDrawable?.apply {
      if (isStateful) state = drawableState
    }
    invalidate()
  }

  private fun createDrawable() {

    val shapeDrawable = ShapeDrawable(RectShape())
    val paint = shapeDrawable.paint
    paint.color = defColor
    paint.alpha = 40
    val stateDrawable = StateListDrawable()
    stateDrawable.addState(
        intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled), shapeDrawable)
    alphaDrawable = stateDrawable
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    mRect.set(0, 0, w, h)
  }
}