package com.sugar.steptofood.model

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import com.sugar.steptofood.utils.FoodType

@DatabaseTable(tableName = "user_food")
data class UserFood (
        @DatabaseField(generatedId = true)
        override var id: Int? = null,

        @DatabaseField(foreign = true)
        var food: Food? = null,

        @DatabaseField(foreign = true)
        var user: User? = null,

        @DatabaseField
        var type: String = FoodType.LIKE.toString()
) : Entity