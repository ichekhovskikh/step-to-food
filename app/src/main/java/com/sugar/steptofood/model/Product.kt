package com.sugar.steptofood.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "product")
data class Product(
        @DatabaseField(columnName = "id", id = true)
        @SerializedName("id")
        override var id: Int? = null,

        @DatabaseField(columnName = "name", canBeNull = false)
        @SerializedName("name")
        var name: String = "",

        @DatabaseField(columnName = "weight", canBeNull = false)
        @SerializedName("weight")
        @Expose
        var weight: Int? = null,

        @SerializedName("foods")
        @Expose
        var foods: List<Food>? = mutableListOf(),

        @SerializedName("included_in_search")
        @Expose
        var includedInSearch: Boolean = false
) : Entity