package com.wonddak.coinaverage.util

import android.content.Context
import android.net.Uri
import com.wonddak.coinaverage.room.AppDatabase
import com.wonddak.coinaverage.room.CoinDetail
import com.wonddak.coinaverage.room.CoinInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import java.io.BufferedReader
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.util.*


class DataManager(
    val context: Context
) {

    companion object {
        private var instance: DataManager? = null
        fun getInstance(context: Context): DataManager {
            if (instance == null) {
                instance = DataManager(context)
            }
            return instance!!
        }
    }

    private val db = AppDatabase.getInstance(context)

    fun export(
        fileUri: Uri,
        fail: () -> Unit = {},
        success: () -> Unit = {}
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            val element = withContext(Dispatchers.IO) {
                buildJsonObject {
                    db.dbDao().getAll().let {
                        putJsonArray("data") {
                            it.forEach {
                                addJsonObject {
                                    put("name", it.coinInfo.coinName)
                                    putJsonArray("detail") {
                                        it.coinDetailList.forEach {
                                            addJsonObject {
                                                put("count", it.coinCount)
                                                put("price", it.coinPrice)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            val bArr = element.toString().toByteArray()
            runCatching {
                context.contentResolver.openFileDescriptor(fileUri, "w")?.use { pfd ->
                    FileOutputStream(pfd.fileDescriptor).use { fos ->
                        fos.write(bArr)
                    }
                }
            }.onSuccess {
                success()
            }.onFailure {
                fail()
            }
        }
    }

    @Serializable
    data class SimpleData(
        @SerialName("data")
        val `data`: List<SimpleInfo>
    )

    @Serializable
    data class SimpleInfo(
        val name: String,
        val detail: List<SimpleDetail>
    )

    @Serializable
    data class SimpleDetail(
        val count: Float,
        val price: Float
    )

    fun import(
        fileUri: Uri,
        fail: () -> Unit = {},
        success: () -> Unit = {}
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            val st = StringBuilder()
            context.contentResolver.openInputStream(fileUri)?.use { fis ->
                BufferedReader(
                    InputStreamReader(Objects.requireNonNull(fis))
                ).use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        st.append(line)
                    }
                }
            }
            val jsonStr = st.toString()
            runCatching {
                Json.decodeFromString<SimpleData>(jsonStr).data
            }.onSuccess {
                runCatching {
                    withContext(Dispatchers.IO) {
                        it.forEach { info ->
                            var id = db.dbDao().insertCoinInfoData(CoinInfo(null, info.name)).toInt()
                            info.detail.forEach {
                                db.dbDao()
                                    .insertCoinDetailData(CoinDetail(null, id, it.price, it.count))
                            }
                        }
                    }
                }.onSuccess {
                    success()
                }.onFailure {
                    fail()
                }
            }.onFailure {
                fail()
            }
        }
    }

}