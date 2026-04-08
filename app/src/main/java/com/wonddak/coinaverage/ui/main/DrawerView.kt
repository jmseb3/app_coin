package com.wonddak.coinaverage.ui.main

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wonddak.coinaverage.Const
import com.wonddak.coinaverage.R
import com.wonddak.coinaverage.noRippleClickable
import com.wonddak.coinaverage.ui.common.CommonText
import com.wonddak.coinaverage.ui.theme.MATCH2

@Composable
fun DrawerView(
    closeDrawer: () -> Unit,
    navigation: (String) -> Unit
) {
    val context = LocalContext.current
    val startActivity = { intent: Intent ->
        context.startActivity(intent)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .fillMaxHeight()
            .background(Color.White)
            .systemBarsPadding()
            .padding(10.dp)
    ) {
        BackHandler {
            closeDrawer()
        }
        CommonText(
            text = "코인 평단 계산 도우미",
            color = MATCH2,
            modifier = Modifier.padding(10.dp)
        )
        HorizontalDivider()
        NavigationDrawerItem(
            label = "메인",
            res = R.drawable.ic_baseline_home_24
        ) {
            navigation(Const.Nav.Main)
            closeDrawer()
        }
        NavigationDrawerItem(
            label = "저장 목록",
            res = R.drawable.ic_baseline_format_list_numbered_24
        ) {
            navigation(Const.Nav.List)
            closeDrawer()
        }
        NavigationDrawerItem(
            label = "손절 % 계산기",
            res = R.drawable.ic_baseline_bar_chart_24
        ) {
            navigation(Const.Nav.Chart)
            closeDrawer()
        }
        HorizontalDivider()
        NavigationDrawerItem(
            label = "설정",
            res = R.drawable.ic_baseline_settings_24
        ) {
            navigation(Const.Nav.Setting)
            closeDrawer()
        }
        HorizontalDivider()
        NavigationDrawerItem(
            label = "문의하기",
            res = R.drawable.ic_baseline_email_24
        ) {
            val email = Intent(Intent.ACTION_SEND).apply {
                type = "plain/text"
                val address = arrayOf<String>("jmseb2@gmail.com")
                putExtra(Intent.EXTRA_EMAIL, address)
                putExtra(Intent.EXTRA_SUBJECT, "<코인 평단 계산 도우미 관련 문의입니다.>")
                putExtra(Intent.EXTRA_TEXT, "내용:")
            }
            startActivity(email)

        }
        NavigationDrawerItem(
            label = "평가하기",
            res = R.drawable.ic_baseline_star_24
        ) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                data = Uri.parse("market://details?id=com.wonddak.coinaverage")
            }
            startActivity(intent)
        }
    }
}

@Composable
fun NavigationDrawerItem(
    label: String,
    @DrawableRes res: Int = 0,
    action: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .noRippleClickable {
                action()
            }
            .padding(horizontal = 10.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (res != 0) {
            Icon(
                painterResource(id = res),
                contentDescription = null,
                tint = MATCH2,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(15.dp))
        }
        CommonText(
            text = label,
            color = MATCH2
        )
    }
}