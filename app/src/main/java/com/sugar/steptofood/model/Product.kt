package com.sugar.steptofood.model

import android.arch.persistence.room.*
import com.google.gson.annotations.Expose

@Entity
data class Product(
        @PrimaryKey(autoGenerate = false)
        override var id: Int? = null,

        var name: String = "",

        @Ignore
        @Expose
        var weight: Int? = null,

        @Ignore
        @Expose
        var includedInSearch: Boolean = false
) : EntityDto {
    override fun toString(): String {
        return name
    }
}