package com.sugar.steptofood.model.dto

import android.arch.persistence.room.*

@Entity(indices = [Index(value = ["name"], unique = true)])
data class Product(
        @PrimaryKey(autoGenerate = false)
        override var id: Int? = null,

        var name: String = ""
) : EntityDto