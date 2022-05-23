package com.minki.salimalim.manage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minki.salimalim.R
import kotlinx.android.synthetic.main.manage_recycler.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ManageRecyclerAdapter (private val data : ArrayList<ManageRecyclerData> ) : RecyclerView.Adapter<ManageRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var view: View = v
        fun bind(item: ManageRecyclerData) {
            view.ManageIcon.setImageResource(R.drawable.ic_launcher_foreground)
            view.ManageGoodsName.text = item.goodsName
            view.ManageGoodsCategory.text = item.category
            view.ManagePurchasedDate.text = item.purchaseDate + " ~"
            view.ManageGoodsQuantity.text = item.quantity.toString()
            if (item.volume == null)
                view.ManageVolume.text = "        "
            else
                view.ManageVolume.text = item.volume.toString() + "ml(g)"
            var daysLeft =  (item.usedTerm * item.quantity) - getTime(item.purchaseDate)
            if(daysLeft < 0)
                view.ManageDaysLeft.text = Math.abs(daysLeft).toString() + "일 경과"
            else
                view.ManageDaysLeft.text = daysLeft.toString() + "일 남음"
        }
    }

    fun getTime(time : String) : Int {
        val t = System.currentTimeMillis()
        val date = Date(t)
        val format2 = SimpleDateFormat("yyyy-MM-dd")
        val now = format2.parse(format2.format(date))
        val purchased = format2.parse(time)
        return ((now.time - purchased.time) / 60000 / 1440).toInt()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflateView = LayoutInflater.from(parent.context).inflate(R.layout.manage_recycler, parent, false)
        return ViewHolder(inflateView)
    }

    interface OnItemClickListener{
        fun onItemClick(v : View, position: Int)
    }

    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.view.ManageLayout.setOnClickListener {
            itemClickListener.onItemClick(it, position)
        }
        holder.apply { bind(item) }
    }
}