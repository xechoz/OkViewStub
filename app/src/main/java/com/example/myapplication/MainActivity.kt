package com.example.myapplication

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stubA.inflate()
        stubB.inflate()
        stubC.inflate()

        // first content view
        var image = ImageView(this)
        image.setImageResource(R.mipmap.ic_launcher_round)
        stubD.inflate(image)

        image.postDelayed({
            // we can replace content view multiple times
            image = ImageView(this)
            image.setBackgroundResource(R.drawable.ic_launcher_background)
            image.setImageResource(R.drawable.ic_launcher_foreground)
            stubD.inflate(image)
        }, 2000)
    }
}