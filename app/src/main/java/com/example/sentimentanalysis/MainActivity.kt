package com.example.sentimentanalysis

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(),TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var buttonSpeak: Button? = null
    private var editText: EditText? = null

    private var sadFood = arrayOf("piña colada", "mojito", "cubita", "vodka");
    private var sadWords = setOf("morir", "triste", "desamor", "ayuda", "despresion", "deprimido", "lagrimas")
    private var neutralFood = arrayOf("tortilla", "bolillo", "nachos con queso", "betabel");
    private var happyFood = arrayOf("Quesadillas de Huitlacoche", "mcnuggets", "empanadas", "perico", "pan dulce for your heart");
    private var happyWords = setOf("vivir", "feliz", "amor", "amar", "felicidad", "alegría", "abrazo")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonSpeak = this.button_speak
        editText = this.edittext_input

        buttonSpeak!!.isEnabled = false;
        tts = TextToSpeech(this, this)

        buttonSpeak!!.setOnClickListener { speakOut() }
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language specified is not supported!")
            } else {
                buttonSpeak!!.isEnabled = true
            }

        } else {
            Log.e("TTS", "Initilization Failed!")
        }

    }

    private fun speakOut() {
        val text = editText!!.text.toString()
       // tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
        recommendFoodOutLoud(text)
    }

    private fun recommendFoodOutLoud(input: CharSequence) {
        val parts = input.split(" ").toSet()
        val happyWordsSize = parts.intersect(happyWords).size;
        val sadWordsSize = parts.intersect(sadWords).size;
        if (happyWordsSize<sadWordsSize){
            // Sad stuff!
            tts!!.speak(getRandomElement(sadFood), TextToSpeech.QUEUE_FLUSH, null,"")
        } else if (happyWordsSize>sadWordsSize) {
            tts!!.speak(getRandomElement(happyFood), TextToSpeech.QUEUE_FLUSH, null,"")
        } else {
            tts!!.speak(getRandomElement(neutralFood), TextToSpeech.QUEUE_FLUSH, null,"")
        }
    }

    private fun getRandomElement(input: Array<String>): String {
        return "I recommend you get some " + input[Random().nextInt(input.size)]
    }

    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

}
