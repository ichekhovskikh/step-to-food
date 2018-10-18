package com.sugar.steptofood.model

import com.j256.ormlite.dao.ForeignCollection
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.field.ForeignCollectionField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "product")
data class Product(
        @DatabaseField(generatedId = true)
        override var id: Int? = null,

        @DatabaseField(canBeNull = false)
        var name: String = "",

        @ForeignCollectionField(eager = false)
        var foods: ForeignCollection<ProductFood>? = null
) : Entity