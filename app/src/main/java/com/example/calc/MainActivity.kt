package com.example.calc

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.util.Size
import android.util.TypedValue.COMPLEX_UNIT_SP
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var slectedButton: Button? = null
    var lastResult = ""
    var isActionJustSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState != null) {
            textView.text = savedInstanceState.getString("value").toString()
            setupAction(savedInstanceState.getString("action").toString())
            println(savedInstanceState.getString("action").toString())
        } else {
            textView.text = "0"
        }
    }

    fun setupAction(action: String) {
        var button: Button? = null
        when (action) {
            "+" -> button = buttonPlus
            "–" -> button = buttonMinus
            "x" -> button = buttonMult
            "/" -> button = buttonDevide
        }

        if (button != null) {
            this.slectedButton = button
            this.slectedButton?.let { setSelected(it) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("value", textView.text.toString())
        outState.putString("action", this.slectedButton?.text.toString())
    }

    fun buttonAction(view: View) {
        val button = view as Button

        when (button.text) {
            "+", "–", "x", "/" -> makeAction(button)
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" -> {numberButtonAction(button)}
            "Clear" -> {clearAction()}
            "." -> {dotAction()}
            "=" -> {resultAction()}
            else -> { return }
        }
    }

    fun clearAction() {
        textView.text = "0"
        this.lastResult = ""
        this.slectedButton?.let { setDiselected(it) }
        this.slectedButton = null
    }

    fun resultAction() {

        if (this.slectedButton == null) {
            return
        }

        if (this.isActionJustSelected) {
            this.isActionJustSelected = false
            this.slectedButton?.let { this.setDiselected(it) }
            this.slectedButton = null
            this.lastResult = ""
            return
        }
        result()
        this.slectedButton?.let { this.setDiselected(it) }
        this.slectedButton = null
        this.lastResult = ""
        this.isActionJustSelected = true
    }

    fun result() {
        val firstParam: Double? = lastResult.toFloatOrNull()?.toDouble()
        val secondParam: Double? = textView.text.toString().toFloatOrNull()?.toDouble()
        var result = 0.0
        if ((firstParam != null) && (secondParam != null)) {
            when (this.slectedButton?.text) {
                "+" -> {result = firstParam + secondParam}
                "–" -> {result = firstParam - secondParam}
                "x" -> {result = firstParam * secondParam}
                "/" -> {result = firstParam / secondParam}
            }
            if (!result.isNaN()) {
                textView.text = result.toString()
            } else {
                textView.text = "Error"
            }
        }
    }

    fun makeAction(button: Button) {
        if ((this.lastResult != "") && (!isActionJustSelected)) {
            result()
        }
        this.isActionJustSelected = true
        this.slectedButton?.let { setDiselected(it) }
        this.slectedButton = button
        if (textView.text != "0") {
            this.lastResult = textView.text as String
        }
        setSelected(button)
    }

    fun dotAction() {
        if (!textView.text.contains(".")) {
            textView.text = textView.text.toString() + "."
        }
    }

    fun numberButtonAction(button: Button) {
        if (this.isActionJustSelected) {
            textView.text = ""
        }
        this.isActionJustSelected = false
        if (textView.text.length < 9) {
            if (textView.text == "0") {
                textView.text = button.text
            } else {
                textView.text = textView.text.toString() + button.text
            }
        }
    }

    fun setSelected(button: Button) {
        button.setTextColor(Color.WHITE)
    }

    fun setDiselected(button: Button) {
        button.setTextColor(Color.BLACK)
    }
}