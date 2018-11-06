package com.sugar.steptofood.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "user")
data class User(
        @DatabaseField(columnName = "id", id = true)
        @SerializedName("id")
        override var id: Int? = null,

        @DatabaseField(columnName = "name")
        @SerializedName("name")
        var name: String? = null,

        @DatabaseField(columnName = "avatar")
        @SerializedName("avatar")
        @Expose
        var avatar: String? = null,

        @SerializedName("token")
        @Expose
        val token: String? = null
) : Entity