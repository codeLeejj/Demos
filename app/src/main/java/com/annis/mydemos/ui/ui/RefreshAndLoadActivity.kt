package com.annis.mydemos.ui.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.annis.model.produce.DateDateListGenerate
import com.annis.mydemos.R
import com.annis.mydemos.custom.refreshAndLoad.RefreshAndLoader
import com.annis.mydemos.custom.refreshAndLoad.RefreshListener

class RefreshAndLoadActivity : AppCompatActivity() {
    companion object {
        var generate: DateDateListGenerate = DateDateListGenerate()
    }

    var ral: RefreshAndLoader? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refresh_and_load)

        ral = findViewById(R.id.ral)
        ral?.setRefreshView(MyRefreshView(this))
        ral?.setRefreshListener {
            Toast.makeText(this, "刷新啦", Toast.LENGTH_SHORT).show()

            ral?.postDelayed({
                ral?.refreshComplete()
                data = DateDateListGenerate.createOrderDate(20)
                mAdapter.notifyDataSetChanged()
            }, 2_000)
        }
        setRecycleView()
    }

    lateinit var data: List<String>
    var rv: RecyclerView? = null
    lateinit var mAdapter: MyAdapter
    fun setRecycleView() {
        rv = findViewById<RecyclerView>(R.id.rv).apply {
            mAdapter = MyAdapter()
            adapter = mAdapter
        }

        data = DateDateListGenerate.createOrderDate(20)

        mAdapter.notifyDataSetChanged()

    }

    inner class MyAdapter : RecyclerView.Adapter<MyAdapter.MViewHolder>() {
        inner class MViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            var textView: TextView = v as TextView
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            return MViewHolder(v)
        }

        override fun onBindViewHolder(holder: MViewHolder, position: Int) {
            holder.textView.text = data[position]

            holder.textView.setOnClickListener {
                Toast.makeText(this@RefreshAndLoadActivity, "位置:$position", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        override fun getItemCount(): Int {
            return data.size
        }
    }
}