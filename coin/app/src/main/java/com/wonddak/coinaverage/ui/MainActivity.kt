package com.wonddak.coinaverage.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
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
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.startUpdateFlowForResult
import com.wonddak.coinaverage.Const
import com.wonddak.coinaverage.R
import com.wonddak.coinaverage.room.AppDatabase
import com.wonddak.coinaverage.ui.dialog.NameDialog
import com.wonddak.coinaverage.ui.main.AdvertView
import com.wonddak.coinaverage.ui.main.BackOnPressedExitApp
import com.wonddak.coinaverage.ui.main.DrawerView
import com.wonddak.coinaverage.ui.main.TopAppBarView
import com.wonddak.coinaverage.ui.theme.MATCH2
import com.wonddak.coinaverage.ui.view.CoinListView
import com.wonddak.coinaverage.ui.view.GraphView
import com.wonddak.coinaverage.ui.view.MainView
import com.wonddak.coinaverage.ui.view.SettingView
import com.wonddak.coinaverage.util.AdManager
import com.wonddak.coinaverage.util.Config
import com.wonddak.coinaverage.viewmodel.CoinViewModel
import com.wonddak.coinaverage.viewmodel.CoinViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private var keep = true
    private lateinit var viewModel: CoinViewModel
    private lateinit var appUpdateManager: AppUpdateManager
    private val adManager by lazy { AdManager(this) }


    private fun initViewModel() {
        val config = Config.getInstance(this)
        val db = AppDatabase.getInstance(this)
        val factory = CoinViewModelFactory(db, config)
        viewModel = ViewModelProvider(this, factory)[CoinViewModel::class.java]
    }

    private val updateLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode != RESULT_OK) {
                AlertDialog.Builder(this)
                    .setPositiveButton("ok") { _, _ ->
                    }
                    .setMessage("업데이트가 취소되었습니다..")
                    .show()
            }
        }

    private fun initAppUpdater() {
        appUpdateManager = AppUpdateManagerFactory.create(this)

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        val listener = InstallStateUpdatedListener { state ->
            // (Optional) Provide a download progress bar.
            if (state.installStatus() == InstallStatus.DOWNLOADING) {
                val bytesDownloaded = state.bytesDownloaded()
                val totalBytesToDownload = state.totalBytesToDownload()
                // Show update progress bar.
            } else if (state.installStatus() == InstallStatus.DOWNLOADED) {
                // After the update is downloaded, show a notification
                // and request user confirmation to restart the app.
                popupSnackbarForCompleteUpdate()

            }
            // Log state or install the update.
        }
        appUpdateManager.registerListener(listener)

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    updateLauncher,
                    AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE)
                        .setAllowAssetPackDeletion(true)
                        .build()
                )
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { keep }

        initViewModel()
        initAppUpdater()
        adManager.initAd()

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
            MaterialTheme {
                // A surface container using the 'background' color from the theme

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MATCH2
                ) {
                    val scope = rememberCoroutineScope()
                    val drawerState = rememberDrawerState(DrawerValue.Closed)
                    val navController = rememberNavController()
                    val snackbarHostState = remember { SnackbarHostState() }


                    LaunchedEffect(viewModel.showUpdateNeed) {
                        if (viewModel.showUpdateNeed) {
                            val result = snackbarHostState.showSnackbar(
                                "An update has just been downloaded.",
                                "RESTART",
                                false,
                                SnackbarDuration.Indefinite
                            )
                            when(result) {
                                SnackbarResult.ActionPerformed -> {
                                    appUpdateManager.completeUpdate()
                                }
                                SnackbarResult.Dismissed -> {

                                }
                            }
                        }
                    }
                    var title by remember {
                        mutableStateOf("")
                    }
                    var showAddDialog by remember {
                        mutableStateOf(false)
                    }
                    val rewardTime by viewModel.reward.collectAsState()
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
                                        if (it != Const.Nav.Setting) {
                                            popUpTo(navController.graph.id) {
                                                inclusive = true
                                            }
                                        }
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
                                if (rewardTime == 0L || System.currentTimeMillis() >= rewardTime + 1000 * (60 * 60 * 24)) {
                                    AdvertView()
                                }
                                NavHost(
                                    navController = navController,
                                    startDestination = Const.Nav.Main
                                ) {
                                    composable(Const.Nav.Main) {
                                        BackOnPressedExitApp()
                                        MainView(viewModel = viewModel) {
                                            title = it
                                        }
                                    }
                                    composable(Const.Nav.List) {
                                        title = "내 코인 리스트"
                                        BackOnPressedExitApp()
                                        CoinListView(viewModel = viewModel) {
                                            navController.navigate(Const.Nav.Main)
                                        }
                                    }
                                    composable(Const.Nav.Chart) {
                                        title = "손절 % 계산기"
                                        BackOnPressedExitApp()
                                        GraphView()
                                    }
                                    composable(Const.Nav.Setting) {
                                        title = "설정"
                                        SettingView(
                                            viewModel = viewModel
                                        ) {
                                            adManager.showRewardAd {
                                                viewModel.setReward(System.currentTimeMillis())
                                            }
                                        }
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
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    // If an in-app update is already running, resume the update.
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        updateLauncher,
                        AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE)
                            .setAllowAssetPackDeletion(true)
                            .build()
                    )
                }else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate()
                }
            }
    }

    private fun popupSnackbarForCompleteUpdate() {
        viewModel.showUpdateNeed = true
    }

}