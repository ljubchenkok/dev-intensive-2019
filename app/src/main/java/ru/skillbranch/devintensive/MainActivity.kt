package ru.skillbranch.devintensive


import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity(), View.OnClickListener {


    lateinit var benderObj: Bender
    lateinit var benderImage:ImageView
    lateinit var textTxt:TextView
    lateinit var messageEt:EditText
    lateinit var sendBtn:ImageView

    companion object{
        const val KEY_STATUS:String =  "KEY_STATUS"
        const val KEY_QUESTION:String =  "KEY_QUESTION"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("M_MainActivity","onCreate")
        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendBtn = iv_send
        val status:String = savedInstanceState?.getString(KEY_STATUS) ?: Bender.Status.NORMAL.name
        val question:String = savedInstanceState?.getString(KEY_QUESTION) ?: Bender.Question.NAME.name
        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))
        val(r,g,b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r,g,b), PorterDuff.Mode.MULTIPLY)
        textTxt.text = benderObj.askQuestion()
        sendBtn.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if(v==iv_send){
            val (phrase, color) = benderObj.listenAnswer(messageEt.text.toString().toLowerCase())
            messageEt.setText("")
            val(r,g,b) = color
            benderImage.setColorFilter(Color.rgb(r,g,b), PorterDuff.Mode.MULTIPLY)
            textTxt.text = phrase
        }

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(KEY_STATUS, benderObj.status.name)
        outState?.putString(KEY_QUESTION, benderObj.question.name)
        Log.d("M_MainActivity","onSaveInstanceState ${benderObj.status.name} ${benderObj.question.name}")


    }

    override fun onRestart() {
        super.onRestart()
        Log.d("M_MainActivity","onRestart")
    }

    override fun onStart() {
        super.onStart()
        Log.d("M_MainActivity","onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("M_MainActivity","onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("M_MainActivity","onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("M_MainActivity","onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("M_MainActivity","onDestroy")
    }

}
