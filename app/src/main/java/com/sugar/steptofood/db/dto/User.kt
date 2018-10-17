package com.sugar.steptofood.db.dto

import com.j256.ormlite.dao.ForeignCollection
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.ForeignCollectionField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "user")
data class User(
        @DatabaseField(generatedId = true)
        override var id: Int? = null,

        @DatabaseField(canBeNull = false)
        var name: String = "",

        @DatabaseField
        var avatar: String = "",

        @ForeignCollectionField(eager = false)
        var likeFoods: ForeignCollection<LikeFood>? = null,

        @ForeignCollectionField(eager = false)
        var addedFoods: ForeignCollection<Food>? = null
) : Dto