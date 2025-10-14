package com.example.counterviews

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var counter = 0

        val mainView = findViewById<ConstraintLayout>(R.id.main)
        val counterValue = mainView.findViewById<TextView>(R.id.counter_value)
        val plusButton = mainView.findViewById<Button>(R.id.plus)
        val minusButton = mainView.findViewById<Button>(R.id.minus)

        fun animateCounter(view: TextView) {
            val scaleUp = ObjectAnimator.ofPropertyValuesHolder(
                view,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f)
            )
            scaleUp.duration = 150

            val scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                view,
                PropertyValuesHolder.ofFloat("scaleX", 1f),
                PropertyValuesHolder.ofFloat("scaleY", 1f)
            )
            scaleDown.startDelay = 150
            scaleDown.duration = 150

            scaleUp.start()
            scaleDown.start()
        }

        plusButton.setOnClickListener {
            counter++
            counterValue.text = counter.toString()
            animateCounter(counterValue)
        }

        minusButton.setOnClickListener {
            if (counter > 0) {
                counter--
                counterValue.text = counter.toString()
                animateCounter(counterValue)
            }
        }
    }
}
