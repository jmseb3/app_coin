package com.wonddak.coinaverage.core

class Const {
    companion object {
        const val DECIMAL_FORMAT = "decimal_format"
        const val ID_DATA = "iddata"
    }

    enum class DecimalFormat(val value: String, val index: Int) {
        NONE("#,###", 0),
        ONE("#,###.0", 1),
        TWO("#,###.00", 2),
        THREE("#,###.000", 3),
        FOUR("#,###.000", 4),
    }


}