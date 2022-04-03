package com.wonddak.coinaverage.layout

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.wonddak.coinaverage.Font
import com.wonddak.coinaverage.R
import com.wonddak.coinaverage.core.Config
import com.wonddak.coinaverage.core.Const
import com.wonddak.coinaverage.room.AppDatabase
import com.wonddak.coinaverage.room.CoinDetail
import com.wonddak.coinaverage.ui.theme.MyGray
import com.wonddak.coinaverage.ui.theme.MyWhite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class MainActivity : ComponentActivity() {
    var mInterstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadAD()
        setContent {
            var title = remember { mutableStateOf(getString(R.string.app_name)) }

            BaseApp(title = title.value, bodyContent = {
                MainView()
            })
        }
        MobileAds.initialize(this) { }

    }


    private fun loadAD() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            getString(R.string.banner_ad_unit_id_for_test),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    mInterstitialAd = null
                }

                override fun onAdLoaded(p0: InterstitialAd) {
                    super.onAdLoaded(p0)
                    mInterstitialAd = p0
                    mInterstitialAd?.show(this@MainActivity)
                }
            })
    }
}

@Composable
fun BaseApp(
    title: String = "",
    bodyContent: @Composable () -> Unit
) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        color = MyGray,
                        fontFamily = Font.maplestoryfont,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                backgroundColor = MyWhite
            )
        },
        drawerContent = { Text(text = "drawerContent") },
        bottomBar = { AdvertView() }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            bodyContent()
        }
    }
}

@Composable
fun AdvertView(modifier: Modifier = Modifier) {
    val isInEditMode = LocalInspectionMode.current
    if (isInEditMode) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Red)
                .padding(horizontal = 2.dp, vertical = 6.dp),
            textAlign = TextAlign.Center,
            color = Color.White,
            text = "Advert Here",
        )
    } else {
        AndroidView(
            modifier = modifier.fillMaxWidth(),
            factory = { context ->
                AdView(context).apply {
                    adSize = AdSize.BANNER
                    adUnitId = context.getString(R.string.banner_ad_unit_id)
                    loadAd(AdRequest.Builder().build())
                }
            },
            update = {
                it.loadAd(AdRequest.Builder().build())
            }
        )

    }
}


@Preview
@Composable
fun PreviewMain() {
    MainView()
}

@Composable
fun MainView() {
    val context = LocalContext.current

    var avg by remember { mutableStateOf(0f) }
    var total by remember { mutableStateOf(0f) }
    var count by remember { mutableStateOf(0f) }

    val mConfig = Config(context)
    val format = mConfig.getString(Const.DECIMAL_FORMAT)
        ?: Const.DecimalFormat.TWO.value
    val dec = DecimalFormat(format)

    val db = AppDatabase.getInstance(context)
    val iddata = mConfig.getInt(Const.ID_DATA, 1)
    val coinData by db.dbDao().getCoinDetailById(iddata).observeAsState()

    Column(
        modifier = Modifier
            .background(MyGray)
            .fillMaxWidth()
            .fillMaxHeight(1f),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(7f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(20.dp, 10.dp)
                    .border(border = BorderStroke(2.dp, MyWhite))
            ) {
                val lineBoxModifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(7.dp, 5.dp)
                LineInMainBox(
                    modifier = lineBoxModifier,
                    text1 = "평균매수 단가 :",
                    text2 = String.format("%s 원", dec.format(avg))
                )
                LineInMainBox(
                    modifier = lineBoxModifier,
                    text1 = "총 매수 금액 : ",
                    text2 = String.format("%s 원", dec.format(total))
                )
                LineInMainBox(
                    modifier = lineBoxModifier,
                    text1 = "총 매수 량 :",
                    text2 = String.format("%s 원", dec.format(count))
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "각 항목별 짧게눌러 초기화/길게눌러 삭제 가능합니다.",
                color = MyWhite,
                fontFamily = Font.maplestoryfont,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(1.dp))


            Log.d("datasss", "" + coinData)
            if (coinData == null) {

            } else {
                LazyTemp(temp = coinData!!)
            }
        }
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.Bottom
        ) {
            Button(onClick = {
                GlobalScope.launch(Dispatchers.IO) {
                    db.dbDao().insertCoinDetailData(CoinDetail(null, iddata))
                }
            }) {

            }
        }


    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyTemp(temp: List<CoinDetail>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        LazyColumn(
        ) {
            itemsIndexed(temp) { index, item ->
                InputTextItem(index = index, price = item.coinPrice, count = item.coinCount, temp)
            }
        }
    }
}


@Composable
fun LineInMainBox(modifier: Modifier, text1: String, text2: String) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextInMainBox(modifier = Modifier.weight(1f), text = text1, textAlign = TextAlign.Left)
        TextInMainBox(modifier = Modifier.weight(1f), text = text2, textAlign = TextAlign.Right)
    }
}

@Composable
fun TextInMainBox(modifier: Modifier, text: String, textAlign: TextAlign) {
    Text(
        modifier = modifier,
        text = text,
        color = MyWhite,
        textAlign = textAlign,
        fontFamily = Font.maplestoryfont
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun InputTextItem(index: Int, price: Float, count: Float, coinData: List<CoinDetail>) {
    var prices by remember {
        mutableStateOf(price.toString())
    }
    var counts by remember {
        mutableStateOf(count.toString())
    }
    var total by remember {
        mutableStateOf(0f)
    }
    val context = LocalContext.current
    val mConfig = Config(context)
    val format = mConfig.getString(Const.DECIMAL_FORMAT)
        ?: Const.DecimalFormat.TWO.value
    val dec = DecimalFormat(format)

    val db = AppDatabase.getInstance(context)
    val iddata = mConfig.getInt(Const.ID_DATA, 1)

    Column(
        Modifier
            .padding(10.dp, 0.dp)
            .combinedClickable(
                onClick = {
                    prices = ""
                    counts = ""
                    total = 0f
                },
                onLongClick = {
                    if (coinData.size == 1) {
                        Toast
                            .makeText(context, "최소 하나는 존재해야합니다.", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast
                            .makeText(context, "${index}번 항목을 삭제 했습니다.", Toast.LENGTH_SHORT)
                            .show()
                        GlobalScope.launch(Dispatchers.IO) {
                            val data = db
                                .dbDao()
                                .getCoinDetailId(iddata)
                            db
                                .dbDao()
                                .deleteCoinDetailById(data[index])
                        }
                    }

                }
            )
            .background(MyWhite),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val modifier = Modifier
                .weight(1f)
                .wrapContentHeight()

            OutlinedTextField(
                label = {
                    Text(
                        text = "매수 가",
                        fontFamily = Font.maplestoryfont,
                        color = MyGray,
                        textAlign = TextAlign.Center
                    )
                },
                modifier = modifier.padding(5.dp, 0.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions {

                },
                singleLine = true,
                value = if (prices == "0") "" else prices.toString(),
                onValueChange = {
                    try {
                        Log.d("datasss", "" + it)
                        prices = when (it.toDoubleOrNull()) {
                            null -> prices
                            else -> it
                        }
                        if (it == "") {
                            prices = ""
                        }
                        Log.d("datassss", "" + prices)
                        total = prices.toFloat() * counts.toFloat()
                    } catch (e: NumberFormatException) {

                    }
                },
                textStyle = TextStyle(color = MyGray, fontFamily = Font.maplestoryfont),
                placeholder = {
                    Text(fontSize = 10.sp, text = "매수 가격을 입력해주세요")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MyGray,
                    unfocusedLabelColor = MyGray
                ),
            )
            OutlinedTextField(
                label = {
                    Text(
                        text = "매수 량",
                        fontFamily = Font.maplestoryfont,
                        color = MyGray,
                        textAlign = TextAlign.Center
                    )
                },
                modifier = modifier.padding(5.dp, 0.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions {

                },
                singleLine = true,
                value = if (counts == "0") "" else counts.toString(),
                onValueChange = {
                    try {
                        counts = when (it.toDoubleOrNull()) {
                            null -> counts
                            else -> it
                        }
                        if (it == "") {
                            counts = ""
                        }
                        total = prices.toFloat() * counts.toFloat()
                    } catch (e: NumberFormatException) {

                    }
                },
                textStyle = TextStyle(color = MyGray, fontFamily = Font.maplestoryfont),
                placeholder = {
                    Text(fontSize = 10.sp, text = "매수 량을 입력해주세요")
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MyGray,
                    unfocusedLabelColor = MyGray
                ),
            )

        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#${index + 1}",
                modifier = Modifier.weight(1f),
                fontFamily = Font.maplestoryfont,
                color = MyGray
            )
            Text(
                text = "총 가격",
                modifier = Modifier.weight(1f),
                fontFamily = Font.maplestoryfont,
                color = MyGray
            )
            Text(
                textAlign = TextAlign.Right,
                text = "${total}원",
                modifier = Modifier.weight(2f),
                fontFamily = Font.maplestoryfont,
                color = MyGray
            )

        }
    }


}



