package com.wonddak.coinaverage.ui


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.wonddak.coinaverage.R
import com.wonddak.coinaverage.databinding.ActivityMainBinding
import com.wonddak.coinaverage.ui.fragment.CoinInfoFragment
import com.wonddak.coinaverage.ui.fragment.GraphFragment
import com.wonddak.coinaverage.ui.fragment.ListFragment
import com.wonddak.coinaverage.ui.fragment.MainFragment


class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    lateinit var binding: ActivityMainBinding
    private lateinit var mAdView: AdView
    private var mDrawerLayout: DrawerLayout? = null
    private var backKeyPressedTime: Long = 0
    private lateinit var appUpdateManager: AppUpdateManager


    var nowPosition = -1
    var priceOrCount = false

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "dec" || key == "next") {
            Log.d("data","변경감지")
            this.recreate()
            overridePendingTransition(0,0)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mDrawerLayout = binding.drawerLayout

        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.registerOnSharedPreferenceChangeListener(this)

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


        setSupportActionBar(binding.toolbarMain)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_frag_area, MainFragment())
            .commit()


        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            mDrawerLayout!!.closeDrawers()
            when (menuItem.itemId) {

                R.id.nav_rate -> {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    intent.data = Uri.parse("market://details?id=com.wonddak.coinaverage")
                    startActivity(intent)
                }
                R.id.nav_list -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_frag_area, ListFragment())
                        .commit()
                }
                R.id.nav_main -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_frag_area, MainFragment())
                        .commit()
                }
                R.id.nav_graph -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_frag_area, GraphFragment())
                        .commit()
                }
                R.id.nav_info -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_frag_area, CoinInfoFragment())
                        .commit()
                }
                R.id.nav_mail -> {
                    val email = Intent(Intent.ACTION_SEND)
                    email.type = "plain/text"
                    val address = arrayOf<String>("jmseb2@gmail.com")
                    email.putExtra(Intent.EXTRA_EMAIL, address)
                    email.putExtra(Intent.EXTRA_SUBJECT, "<코인 평단 계산 도우미 관련 문의입니다.>")
                    email.putExtra(Intent.EXTRA_TEXT, "내용:")
                    startActivity(email)
                }
                R.id.nav_setting ->{
                    val intent = Intent(this,SettingsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                }
            }
            true
        }

        MobileAds.initialize(this) {}
        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


    }

    fun hideKeyboard(editText: EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    fun showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    override fun onPause() {
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this)

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
                MaterialAlertDialogBuilder(this)
                    .setPositiveButton("ok") { _, _ ->
                    }
                    .setMessage("업데이트가 취소되었습니다..")
                    .show()
            } else {

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout!!.openDrawer(GravityCompat.START)

            }
            R.id.action_new -> {
                Dialog(this, this, supportFragmentManager).newGameStart(1, null)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        if (mDrawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout!!.closeDrawers()
        } else if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }


    }


}






