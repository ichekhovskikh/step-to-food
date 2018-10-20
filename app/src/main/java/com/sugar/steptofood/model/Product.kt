package com.sugar.steptofood.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Product(
        @SerializedName("id")
        override var id: Int? = null,

        @SerializedName("name")
        var name: String = "",

        @SerializedName("foods")
        @Expose
        var foods: List<Food>? = null,

        @SerializedName("weight")
        @Expose
        var weight: Int? = null,

        @SerializedName("included_in_search")
        @Expose
        var includedInSearch: Boolean = false
) : Entity