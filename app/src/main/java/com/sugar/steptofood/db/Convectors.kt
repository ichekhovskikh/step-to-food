package com.sugar.steptofood.db

import android.arch.persistence.room.TypeConverter
import com.sugar.steptofood.model.fullinfo.FullProductInfo

object Convectors {

    class BooleanConverters {

        @TypeConverter
        fun toBoolean(value: String?): Boolean = value
                ?.split(".")
                .let { columns ->
                    columns!![COLUMN_FUNCTION] == "toBoolean()" && columns[COLUMN_VALUE].toInt() != 0
                }

        companion object {
            private const val COLUMN_VALUE = 0
            private const val COLUMN_FUNCTION = 1
        }
    }

    class ProductConverters {

        @TypeConverter
        fun toFullProductInfoList(value: String?): List<FullProductInfo>? = value
                ?.split(";")
                ?.asSequence()
                ?.map { row -> row.split(",") }
                ?.map { columns ->
                    FullProductInfo(
                            id = columns[COLUMN_ID].toIntOrNull(),
                            name = columns[COLUMN_NAME],
                            weight = columns[COLUMN_WEIGHT].toIntOrNull())
                }
                ?.toList() ?: emptyList()

        companion object {
            private const val COLUMN_ID = 0
            private const val COLUMN_NAME = 1
            private const val COLUMN_WEIGHT = 2
        }
    }
}