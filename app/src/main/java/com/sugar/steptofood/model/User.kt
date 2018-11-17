package com.sugar.steptofood.model

import android.arch.persistence.room.*
import com.google.gson.annotations.Expose

@Entity
data class User(
        @PrimaryKey(autoGenerate = false)
        override var id: Int? = null,

        var name: String? = null,

        @Expose
        var avatar: String? = null,

        @Ignore
        @Expose
        val token: String? = null
) : EntityDto