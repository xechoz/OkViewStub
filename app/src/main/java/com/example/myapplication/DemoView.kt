package com.example.myapplication

import android.content.Context
import android.util.AttributeSet

class DemoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatButton(context, attrs, defStyleAttr) {
    init {
        text = javaClass.simpleName
    }
}