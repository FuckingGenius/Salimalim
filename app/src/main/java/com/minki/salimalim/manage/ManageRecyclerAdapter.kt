package com.minki.salimalim.manage

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.minki.salimalim.R
import com.minki.salimalim.system.CommonActivity
import kotlinx.android.synthetic.main.manage_recycler.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ManageRecyclerAdapter (private val data : ArrayList<ManageRecyclerData>, private val category: MutableMap<Int,String> ) : RecyclerView.Adapter<ManageRecyclerAdapter.ViewHolder>(), Filterable {

    val filteredData = ArrayList<ManageRecyclerData>()
    init{
        filteredData.addAll(data)
    }
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var view: View = v
        fun bind(item: ManageRecyclerData) {
            var resource = R.drawable.household
            when (item.category) {
                1 -> resource = R.drawable.grocery
                2 -> resource = R.drawable.restroom
                3 -> resource = R.drawable.household
                4 -> resource = R.drawable.kitchen
            }
            view.ManageIcon.setImageResource(resource)
            Log.v("호호","이게 먼저야? ${CommonActivity.goods}" )
            view.ManageGoodsName.text = CommonActivity.goods[item.goodsName-1].goodsName
            view.ManageGoodsCategory.text = category.get(item.category)
            view.ManagePurchasedDate.text = item.purchaseDate + " ~"
            view.ManageGoodsQuantity.text = item.quantity.toString() + "개"
            if (item.volume == null || item.volume == 0)
                view.ManageVolume.text = ""
            else
                view.ManageVolume.text = item.volume.toString() + "ml(g)"
            var daysLeft = (item.usedTerm * item.quantity) - CommonActivity().getTime(item.purchaseDate)
            if (daysLeft < 0)
                view.ManageDaysLeft.text = Math.abs(daysLeft).toString() + "일 경과"
            else
                view.ManageDaysLeft.text = daysLeft.toString() + "일 남음"
            if (daysLeft > 10)
                view.ManageLayout.setBackgroundColor(Color.parseColor("#E1E0E0"))
            else
                view.ManageLayout.setBackgroundColor(Color.parseColor("#FAAEAE")) // 기존 : #EF8282
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflateView = LayoutInflater.from(parent.context).inflate(R.layout.manage_recycler, parent, false)
        return ViewHolder(inflateView)
    }

    interface OnItemClickListener{
        fun onItemClick(v : View, item:ManageRecyclerData)
    }

    private lateinit var itemClickListener : OnItemClickListener

    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredData[position]
        holder.view.ManageLayout.setOnClickListener {
            itemClickListener.onItemClick(it, item)
        }
        holder.apply { bind(item) }
    }

    var itemFilter = ItemFilter()

    inner class ItemFilter : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filterString = charSequence.toString()
            val results = FilterResults()

            //검색이 필요없을 경우를 위해 원본 배열을 복제
            val filteredList = ArrayList<ManageRecyclerData>()
            if(filterString == " "){
                results.values = data
                results.count = data.size

                return results
            }else{
                for(i in data)
                    if(category.get(i.category) == filterString){
                        Log.v("찾았다","$filterString 인 $i 를")
                        filteredList.add(i)
                    }

                results.values = filteredList
                results.count = filteredList.size
                Log.v("왔다1",results.values.toString())
                return results
            }
        }

        override fun publishResults(p0: CharSequence?, result: FilterResults) {
            filteredData.clear()
            Log.v("왔다2", "${result.values}")
            filteredData.addAll(result.values as ArrayList<ManageRecyclerData>)
            Log.v("왔다2", "$filteredData")
            notifyDataSetChanged()
        }
    }

    override fun getFilter(): Filter {
        return itemFilter
    }
}