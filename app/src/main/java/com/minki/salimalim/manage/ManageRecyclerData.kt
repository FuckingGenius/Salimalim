package com.minki.salimalim.manage

import java.io.Serializable

data class ManageRecyclerData(var id : Int, var goodsName : String, var purchaseDate : String, var category : Int,
                              var quantity : Int, var volume : Int?, var usedTerm : Int) : Serializable {

}