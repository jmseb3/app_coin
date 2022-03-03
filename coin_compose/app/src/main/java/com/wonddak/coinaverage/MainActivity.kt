package com.wonddak.coinaverage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.wonddak.coinaverage.ui.theme.CoinaverageTheme
import com.wonddak.coinaverage.ui.theme.MyGray
import com.wonddak.coinaverage.ui.theme.MyWhite

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoinaverageTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    BaseApp(title = "제목", bodyContent = { Text(text = "dsddd") })
                }
            }
        }
    }
}
val maplestoryfont = FontFamily(
    Font(R.font.maplestory, FontWeight.Bold, FontStyle.Normal)
)

@Composable
fun BaseApp(title: String = "", bodyContent: @Composable () -> Unit) {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        color = MyGray,
                        fontFamily = maplestoryfont,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                backgroundColor = MyWhite
            )
        },
        drawerContent = { Text(text = "drawerContent") },
        content = { bodyContent() },
        bottomBar = {  }
    )
}
@Preview
@Composable
fun TextHello() {
    Text(text = "hello")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CoinaverageTheme {
        BaseApp(
            title = "코인평단 계산기",
            bodyContent = {
                TextHello()
            }
        )
    }
}
