package com.wonddak.coinaverage.room

import androidx.room.*

@Entity
data class CoinInfo(
    @PrimaryKey(autoGenerate = true) var coinId: Int?,
    var coinName: String,
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CoinInfo::class,
            parentColumns = arrayOf("coinId"),
            childColumns = arrayOf("coinId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CoinDetail(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val coinId: Int,
    var coinPrice: Float = 0.0f,
    var coinCount: Float = 0.0f


)

data class CoinInfoAndCoinDetail(
    @Embedded val coinInfo: CoinInfo,

    @Relation(
        parentColumn = "coinId",
        entityColumn = "coinId"
    )

    val coinDetailList: List<CoinDetail>
)
