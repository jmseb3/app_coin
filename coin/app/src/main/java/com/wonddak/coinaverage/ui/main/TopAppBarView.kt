package com.wonddak.coinaverage.ui.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.wonddak.coinaverage.R
import com.wonddak.coinaverage.ui.common.CommonText
import com.wonddak.coinaverage.ui.theme.MATCH1
import com.wonddak.coinaverage.ui.theme.MATCH2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarView(
    title :String,
    openDrawer :() -> Unit,
    addDialog :() -> Unit
) {
    TopAppBar(
        title = {
            CommonText(
                text = title,
                color = MATCH2
            )
        },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_menu_24),
                    contentDescription = null,
                    tint = MATCH2
                )
            }
        },
        actions = {
            AnimatedVisibility (title != "설정") {
                IconButton(onClick = addDialog) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_open_in_new_24),
                        contentDescription = null,
                        tint = MATCH2
                    )
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MATCH1
        )
    )
}