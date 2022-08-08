package com.template

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.shawnlin.numberpicker.NumberPicker


class GameActivity : AppCompatActivity() {
    private var flag : Boolean = false

    private val historyViewModel : HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    private lateinit var slot1 : NumberPicker
    private lateinit var slot2 : NumberPicker
    private lateinit var slot3 : NumberPicker
    private lateinit var betButton: Button
    private lateinit var currentBet : TextView
    private lateinit var spinButton: Button
    private lateinit var coins : TextView
    private lateinit var menuButton : TextView

    val emojis = mutableListOf(0x1F34C, 0x1F349, 0x1F347, 0x1F353, 0x1F352, 0x1F48E, 0x1F4B5, 0x1F3B0)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        findView()
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //banan, water, grap, straw, cherry, gem, money, slot

        slot1.formatter = NumberPicker.Formatter { value ->
            getEmojiByUnicode(emojis.shuffled()[value])
        }
        slot2.formatter = NumberPicker.Formatter { value ->
            getEmojiByUnicode(emojis.shuffled()[value])
        }
        slot3.formatter = NumberPicker.Formatter { value ->
            getEmojiByUnicode(emojis.shuffled()[value])
        }
        spinButton.setOnClickListener {

            val coins = SharedPreferences.getCoinsAmount(this)

            if (coins <= 0) {
                val layout = applicationContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val popup = layout.inflate(R.layout.reward_popup, null)

                val height = LinearLayout.LayoutParams.WRAP_CONTENT
                val popupWindow = PopupWindow(popup, 1000, height, true)
                popupWindow.showAtLocation(findViewById<RelativeLayout>(R.id.parentRelative), Gravity.CENTER, 0, 0);

                val newAmount = rand() + rand()*123
                popup.findViewById<TextView>(R.id.reward).text = newAmount.toString()


                popup.findViewById<Button>(R.id.claim_button).setOnClickListener{
                    SharedPreferences.setCoinsAmount(this, newAmount)
                    findViewById<TextView>(R.id.game_coins)
                        .text = getString(R.string.coins, newAmount.toString())
                    popupWindow.dismiss()
                }
            }
            else {
                slot1.spin(rand(), 5000)
                Thread.sleep(100)
                slot2.spin(rand(), 4650)
                Thread.sleep(100)
                slot3.spin(rand(), 4300)
                spinButton.isEnabled = false
                menuButton.isEnabled = false
                betButton.isEnabled = false
            }



        }
        menuButton.setOnClickListener {
            this.finish()
        }
        betButton.setOnClickListener{

            val layout = applicationContext.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val popup = layout.inflate(R.layout.popup_window, null)

            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            val popupWindow = PopupWindow(popup, 1000, height, true)
            popupWindow.showAtLocation(findViewById<RelativeLayout>(R.id.parentRelative), Gravity.CENTER, 0, 0);

            val editText = popup.findViewById<EditText>(R.id.place_bet)
            val confirmButton = popup.findViewById<Button>(R.id.confirm_button)
            val cancelButton = popup.findViewById<Button>(R.id.cancel_button)
            var bet = ""
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    bet = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })


            confirmButton.setOnClickListener{
                if (bet != "") {
                    currentBet.text = getString(R.string.current_bet, bet)
                    flag = true
                }
                popupWindow.dismiss()
            }
            cancelButton.setOnClickListener{
                popupWindow.dismiss()
            }

        }



    }



    private fun NumberPicker.spin(rand : Int, time : Long) {

       object : CountDownTimer(time, 100)  {
            override fun onTick(currentTime: Long) {
                smoothScroll(true, rand)
            }
            override fun onFinish() {
                cancel()
                if(time == 5000L) {
                    checkWin()
                }
            }

        }.start()

    }



    private fun checkWin(){
        val currentCoins = SharedPreferences.getCoinsAmount(this)
        var text = ""
        var income = (rand()*67)%2000
        if (rand()%2 == 0) {
            text = "YOU WON $income!!!"
        }
        else {
            if(flag) {
                income = try {
                    Integer.parseInt(currentBet.text.toString().substring(14))
                } catch (e : Exception) {
                    income
                }
            }
            income *=-1
            text = "YOU LOST $income"
        }
        SharedPreferences.setCoinsAmount(this, currentCoins + income)

        historyViewModel.addHistory(History(combaMoney = income, comba = "${emojis[slot1.value]},${emojis[slot2.value]},${emojis[slot3.value]}"))
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        coins.text = getString(R.string.coins, SharedPreferences.getCoinsAmount(this).toString())
        spinButton.isEnabled = true
        menuButton.isEnabled = true
        betButton.isEnabled = true
    }

    private fun rand() : Int{
        val rand = (Math.random()*15).toInt() + 3
        if (rand <= 5)
            return rand()

        return rand
    }

    fun getEmojiByUnicode(unicode: Int): String {
        return String(Character.toChars(unicode))
    }

    private fun findView() {
        slot1 = findViewById(R.id.slot_1)
        slot2 = findViewById(R.id.slot_2)
        slot3 = findViewById(R.id.slot_3)

        slot1.maxValue = 7
        slot1.minValue = 0
        slot2.maxValue = 7
        slot2.minValue = 0
        slot3.maxValue = 7
        slot3.minValue = 0


        betButton = findViewById(R.id.bet_button)
        currentBet = findViewById(R.id.current_bet)
        spinButton = findViewById(R.id.spin_button)
        coins = findViewById(R.id.game_coins)
        menuButton = findViewById(R.id.menu_button)
        coins.text = getString(R.string.coins, SharedPreferences.getCoinsAmount(this).toString())


        val slots : LinearLayout = findViewById(R.id.slots)
        for (i in 0 until slots.childCount) {
            val child: View = slots.getChildAt(i)
            child.isEnabled = false
        }
    }




}



