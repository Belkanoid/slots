package com.template

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    private val noteApplication = NoteApplication()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //history
        findViewById<TextView>(R.id.history_button).setOnClickListener {
            val historyIntent = Intent(this, HistoryActivity::class.java)
            startActivity(historyIntent)
        }





        //play_Button
        findViewById<Button>(R.id.play_button).setOnClickListener{
            val coins = SharedPreferences.getCoinsAmount(this)

            if (coins <= 0) {
                val layout = applicationContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val popup = layout.inflate(R.layout.reward_popup, null)

                val height = LinearLayout.LayoutParams.WRAP_CONTENT
                val popupWindow = PopupWindow(popup, 1000, height, true)
                popupWindow.showAtLocation(findViewById<RelativeLayout>(R.id.deltaRelative), Gravity.CENTER, 0, 0);

                val newAmount = rand()
                popup.findViewById<TextView>(R.id.reward).text = newAmount.toString()


                popup.findViewById<Button>(R.id.claim_button).setOnClickListener{
                    SharedPreferences.setCoinsAmount(this, newAmount)
                    findViewById<TextView>(R.id.game_coins)
                        .text = getString(R.string.coins, newAmount.toString())
                    popupWindow.dismiss()
                }
            }
            else {
                val gameIntent = Intent(this, GameActivity::class.java)
                startActivity(gameIntent)
            }





        }
    }


    override fun onResume() {
        super.onResume()
        //coins
        findViewById<TextView>(R.id.game_coins)
            .text = getString(R.string.coins, SharedPreferences.getCoinsAmount(this).toString())
    }

    private fun rand() : Int{
        val rand = (Math.random()*1524).toInt() + 100
        if (rand <= 150)
            return rand()

        return rand
    }
}


