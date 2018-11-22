package com.sugar.steptofood.model.fullinfo

import android.arch.persistence.room.*
import com.google.gson.annotations.Expose
import com.sugar.steptofood.model.dto.*

@Entity
data class FullProductInfo(
        @ColumnInfo(name = "product_id") override var id: Int? = null,
        @ColumnInfo(name = "product_name") var name: String = "",
        @Expose var weight: Int? = null,
        @Expose var includedInSearch: Boolean = false
) : EntityDto {
    override fun toString(): String {
        return name
    }
}