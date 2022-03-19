package com.wonddak.coinaverage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.wonddak.coinaverage.core.Config
import com.wonddak.coinaverage.core.Const
import com.wonddak.coinaverage.ui.theme.MyGray
import com.wonddak.coinaverage.ui.theme.MyWhite
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
        content = { bodyContent() },
        bottomBar = { AdvertView() }
    )
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
    var avg by remember { mutableStateOf(0) }
    var total by remember { mutableStateOf(0) }
    var count by remember { mutableStateOf(0) }
    val format = Config(context = context).getStringConfig(Const.DECIMAL_FORMAT)
        ?: Const.DecimalFormat.TWO.value
    val dec = DecimalFormat(format)

    Column(
        modifier = Modifier
            .background(MyGray)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(20.dp, 10.dp)
                .border(border = BorderStroke(2.dp, MyWhite))
        ) {
            LineInMainBox(text1 = "평균매수 단가 :", text2 = String.format("%d 원", dec.format(avg)))
            LineInMainBox(text1 = "총 매수 금액 : ", text2 = String.format("%d 원", dec.format(total)))
            LineInMainBox(text1 = "총 매수 량 :", text2 = String.format("%d 원", dec.format(count)))
        }
        Text(text = "각 항목별 짧게눌러 초기화/길게눌러 삭제 가능합니다.")
    }
}

@Composable
fun LineInMainBox(text1: String, text2: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(7.dp, 5.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextInMainBox(text = text1, textAlign = TextAlign.Left)
        TextInMainBox(text = text2, textAlign = TextAlign.Right)
    }
}

@Composable
fun TextInMainBox(text: String, textAlign: TextAlign) {
    Text(
        modifier = Modifier
            .fillMaxWidth(0.5f),
        text = text,
        color = MyWhite,
        textAlign = textAlign,
        fontFamily = Font.maplestoryfont
    )
}



