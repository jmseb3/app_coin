package com.wonddak.coinaverage

object Const {

    enum class DecFormat(val dec: String) {
        Zero("#,##0"),
        One("#,##0.0"),
        Two("#,##0.00"),
        Three("#,##0.000"),
        Four("#,##0.0000"),
    }

    object Nav {
        const val Main = "main"
        const val List = "list"
        const val Chart = "chart"
        const val Setting = "setting"
    }
}