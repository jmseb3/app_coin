package com.wonddak.coinaverage


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.wonddak.coinaverage.databinding.ActivityMainBinding
import com.wonddak.coinaverage.room.AppDatabase
import com.wonddak.coinaverage.room.CoinDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var mAdView: AdView
    private var mDrawerLayout: DrawerLayout? = null
    private var backKeyPressedTime: Long = 0

    var selectedPostion = -1
    var priceorcount = false

    var type = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mDrawerLayout = binding.drawerLayout

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
//                    type =2
//                    invalidateOptionsMenu()
                }
                R.id.nav_main -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.main_frag_area, MainFragment())
                        .commit()
//                    type =1
//                    invalidateOptionsMenu()

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
                R.id.nav_setting -> {
                    val itemshow = arrayOf("소수점 없음", "소수점 1개", "소수점 2개(기본)", "소수점 3개", "소수점 4개")
                    val items =
                        arrayOf("#,###", "#,###.0", "#,###.00", "#,###.000", "#,###.0000")

                    val builder = AlertDialog.Builder(this@MainActivity)
                    val prefs = getSharedPreferences("coindata", MODE_PRIVATE)
                    val editor = prefs.edit()

                    builder.setTitle("소수점 표시 방식 변경")
                    builder.setSingleChoiceItems(itemshow, 2) { dialog, which ->
                        editor.putString("dec", items[which])
                        editor.commit()
                        Toast.makeText(this@MainActivity, "표시형식을 변경했습니다.", Toast.LENGTH_SHORT)
                            .show()
                        dialog.dismiss()
                        finish()
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                    }
                    builder.show()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (type == 1) {
            menuInflater.inflate(R.menu.main_toolbar, menu)
        }
//        } else if (type == 2) {
//            menuInflater.inflate(R.menu.main_toolbar2, menu)
//        }
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






