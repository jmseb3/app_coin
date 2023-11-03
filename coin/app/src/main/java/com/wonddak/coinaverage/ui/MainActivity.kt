package com.wonddak.coinaverage.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.wonddak.coinaverage.Const
import com.wonddak.coinaverage.room.AppDatabase
import com.wonddak.coinaverage.ui.dialog.NameDialog
import com.wonddak.coinaverage.ui.main.AdvertView
import com.wonddak.coinaverage.ui.main.DrawerView
import com.wonddak.coinaverage.ui.main.TopAppBarView
import com.wonddak.coinaverage.ui.theme.MATCH2
import com.wonddak.coinaverage.ui.view.CoinListView
import com.wonddak.coinaverage.ui.view.GraphView
import com.wonddak.coinaverage.ui.view.MainView
import com.wonddak.coinaverage.ui.view.SettingView
import com.wonddak.coinaverage.util.Config
import com.wonddak.coinaverage.viewmodel.CoinViewModel
import com.wonddak.coinaverage.viewmodel.CoinViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var keep = true
    private lateinit var viewModel: CoinViewModel
    private lateinit var appUpdateManager: AppUpdateManager

    private var backKeyPressedTime: Long = 0
    private var adRequest: AdRequest? = null
    private var rewardedAd: RewardedAd? = null

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis()
                Toast.makeText(this@MainActivity, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
                return
            }
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                finish();
            }
        }
    }

    private fun initViewModel() {
        val config = Config.getInstance(this)
        val db = AppDatabase.getInstance(this)
        val factory = CoinViewModelFactory(db, config)
        viewModel = ViewModelProvider(this, factory)[CoinViewModel::class.java]
    }

    private fun initAppUpdater() {
        appUpdateManager = AppUpdateManagerFactory.create(this)

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    this,
                    700
                )
            }

        }
    }

    private fun initAd() {
        val TAG = "JWH"
        adRequest = AdRequest.Builder().build()
        MobileAds.initialize(this) {}
        RewardedAd.load(
            this,
            "ca-app-pub-3940256099942544/5224354917",
            adRequest!!,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("JWH", adError?.toString().toString())
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d("JWH", "Ad was loaded.")
                    rewardedAd = ad
                    rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdClicked() {
                            // Called when a click is recorded for an ad.
                            Log.d(TAG, "Ad was clicked.")
                        }

                        override fun onAdDismissedFullScreenContent() {
                            // Called when ad is dismissed.
                            // Set the ad reference to null so you don't show the ad a second time.
                            Log.d(TAG, "Ad dismissed fullscreen content.")
                            rewardedAd = null
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            // Called when ad fails to show.
                            Log.e(TAG, "Ad failed to show fullscreen content.")
                            rewardedAd = null
                        }

                        override fun onAdImpression() {
                            // Called when an impression is recorded for an ad.
                            Log.d(TAG, "Ad recorded an impression.")
                        }

                        override fun onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                            Log.d(TAG, "Ad showed fullscreen content.")
                        }
                    }

                }
            })
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { keep }

        this.onBackPressedDispatcher.addCallback(this, callback)

        initViewModel()
        initAppUpdater()
        initAd()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.id.collect {
                    if (it >= 0) {
                        keep = false
                    }
                }
            }
        }

        setContent {
            MaterialTheme() {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MATCH2
                ) {
                    val scope = rememberCoroutineScope()
                    val drawerState = rememberDrawerState(DrawerValue.Closed)
                    val navController = rememberNavController()
                    val snackbarHostState = remember { SnackbarHostState() }


                    val nowInfo by viewModel.nowInfo.collectAsState()
                    val dec by viewModel.dec.collectAsState()
                    val next by viewModel.next.collectAsState()

                    var title by remember {
                        mutableStateOf("")
                    }
                    var showAddDialog by remember {
                        mutableStateOf(false)
                    }
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            DrawerView(
                                closeDrawer = {
                                    scope.launch {
                                        drawerState.close()
                                    }
                                },
                                navigation = {
                                    navController.navigate(it) {
                                        launchSingleTop = true
                                    }
                                })
                        }
                    ) {
                        Scaffold(
                            snackbarHost = { SnackbarHost(snackbarHostState) },
                            topBar = {
                                TopAppBarView(
                                    title = title,
                                    openDrawer = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }
                                ) {
                                    showAddDialog = true
                                }
                            },
                        ) { padding ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(padding)
                                    .background(MATCH2)
                            ) {
                                AdvertView()
                                NavHost(
                                    navController = navController,
                                    startDestination = Const.Nav.Main
                                ) {
                                    composable(Const.Nav.Main) {
                                        MainView(viewModel = viewModel) {
                                            title = it
                                        }
                                    }
                                    composable(Const.Nav.List) {
                                        title = "내 코인 리스트"
                                        CoinListView(viewModel = viewModel) {
                                            navController.navigate(Const.Nav.Main)
                                        }
                                    }
                                    composable(Const.Nav.Chart) {
                                        title = "손절 % 계산기"
                                        GraphView()
                                    }
                                    composable(Const.Nav.Setting) {
                                        title = "설정"
                                        SettingView(
                                            viewModel = viewModel
                                        ) { showRewardAd() }
                                    }
                                }
                            }
                        }
                    }

                    if (showAddDialog) {
                        NameDialog(
                            coinInfoAndCoinDetail = null,
                            onDismissRequest = { showAddDialog = false },
                        ) { name ->
                            viewModel.insertCoin(name)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() ==
                    UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.FLEXIBLE,
                        this,
                        700
                    )

                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 700) {
            if (resultCode != RESULT_OK) {
                AlertDialog.Builder(this)
                    .setPositiveButton("ok") { _, _ ->
                    }
                    .setMessage("업데이트가 취소되었습니다..")
                    .show()
            } else {

            }
        }
    }

    private fun showRewardAd() {
        rewardedAd?.let { ad ->
            ad.show(
                this@MainActivity,
                OnUserEarnedRewardListener { rewardItem ->
                    // Handle the reward.
                    val rewardAmount = rewardItem.amount
                    val rewardType = rewardItem.type
                    Log.d("JWH", "User earned the reward.")
                })
        } ?: run {
            Log.d("JWH", "The rewarded ad wasn't ready yet.")
        }
    }
}