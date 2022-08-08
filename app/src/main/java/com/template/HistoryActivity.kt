package com.template

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView


    private val historyViewModel : HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);



        recyclerView = findViewById<RecyclerView>(R.id.history_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        historyViewModel.historyLiveData.observe(this, Observer { histories ->
            histories?.let {
                recyclerView.adapter = HistoryAdapter(histories)
            }
        })
    }


    private inner class HistoryHolder(view : View)
        :RecyclerView.ViewHolder(view) {

        val combaRes : TextView = itemView.findViewById(R.id.comba_res)
        val comba : TextView = itemView.findViewById(R.id.comba)
        val combaCoins : TextView = itemView.findViewById(R.id.comba_coins)

        fun bind(history : History) {

            if (history.combaMoney < 0) {
                combaRes.text = "LOSE"
                combaRes.setTextColor(resources.getColor(R.color.lose))
            }
            else {
                combaRes.text = "WIN"
                combaRes.setTextColor(resources.getColor(R.color.win))
            }

            combaCoins.text = getString(R.string.your_bet, history.combaMoney.toString())
            val list : List<String> = history.comba.split(",")
            val combaList = "${getEmojiByUnicode(list[0].toInt())}${getEmojiByUnicode(list[1].toInt())}${getEmojiByUnicode(list[2].toInt())}"
            comba.text = "${getEmojiByUnicode(list[0].toInt())}${getEmojiByUnicode(list[1].toInt())}${getEmojiByUnicode(list[2].toInt())}"
        }

    }

    fun getEmojiByUnicode(unicode: Int): String {
        return String(Character.toChars(unicode))
    }

    private inner class HistoryAdapter(private val histories : List<History>)
        :RecyclerView.Adapter<HistoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
            val view = layoutInflater.inflate(R.layout.fragment_history_item, parent, false)
            return HistoryHolder(view)
        }

        override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
            holder.bind(histories[position])
        }

        override fun getItemCount() = histories.size

    }




}