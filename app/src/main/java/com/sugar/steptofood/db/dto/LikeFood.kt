package com.sugar.steptofood.db.dto

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "like_food")
data class LikeFood (
        @DatabaseField(generatedId = true)
        override var id: Int? = null,

        @DatabaseField(foreign = true)
        var food: Food? = null,

        @DatabaseField(foreign = true)
        var user: User? = null
) : Dto