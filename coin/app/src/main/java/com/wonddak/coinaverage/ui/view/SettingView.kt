package com.wonddak.coinaverage.ui.view

import android.content.ClipDescription
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.wonddak.coinaverage.R
import com.wonddak.coinaverage.ui.dialog.FormatDialog
import com.wonddak.coinaverage.ui.theme.MATCH1
import com.wonddak.coinaverage.ui.theme.MATCH2
import com.wonddak.coinaverage.ui.theme.maple
import com.wonddak.coinaverage.util.DataManager
import com.wonddak.coinaverage.viewmodel.CoinViewModel

@Composable
fun SettingView(
    viewModel: CoinViewModel
) {

    Column {
        SectionOption(viewModel)
        SectionData()
    }
}

@Composable
private fun SectionOption(
    viewModel: CoinViewModel
) {
    val next by viewModel.next.collectAsState()
    val updateCheck = {
        viewModel.setNext(!next)
    }


    var showFormatDialog by remember {
        mutableStateOf(false)
    }
    val dismiss = {
        showFormatDialog = false
    }
    SectionView(section = "옵션") {
        SectionRow(
            painter = painterResource(id = R.drawable.baseline_text_format_24),
            text = "소수점 표시 형식 변경",
            description = "앱 내의 보여지는 소수점 자리수를 변경합니다."
        ) {
            showFormatDialog = true
        }
        Divider(color = MATCH1)
        SectionRow(
            painter = painterResource(id = R.drawable.baseline_filter_center_focus_24),
            text = "입력 후 다음 항목으로 이동",
            description = "항목 입력 후 커서 이동여부를 변경합니다.",
            otherContent = {
                Switch(
                    checked = next,
                    onCheckedChange = {
                        updateCheck()
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MATCH1,
                        checkedTrackColor = MATCH2
                    )
                )
            }
        ) {
            updateCheck()
        }
    }
    if (showFormatDialog) {
        FormatDialog(onDismissRequest = dismiss) { format ->
            viewModel.setDec(format)
            dismiss()
        }
    }
}

@Composable
private fun SectionData() {
    val context = LocalContext.current
    val dataManager = DataManager.getInstance(context)

    val exportLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val fileUri = result.data?.data
            if (fileUri == null) {
                Toast.makeText(context, "에러 발생! 올바른 위치를 지정해주세요.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                dataManager.export(
                    fileUri,
                    { Toast.makeText(context, "저장에 실패하엿습니다.", Toast.LENGTH_SHORT).show() }
                ) {
                    Toast.makeText(context, "성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    val importLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val fileUri = result.data?.data
            if (fileUri == null) {
                Toast.makeText(context, "에러 발생! 파일을 불러오는데 실패했습니다..", Toast.LENGTH_SHORT).show()
            } else {
                dataManager.import(
                    fileUri,
                    { Toast.makeText(context, "데이터를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show() }
                ) {
                    Toast.makeText(context, "성공적으로 불러왔습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    SectionView(section = "데이터") {
        SectionRow(
            painter = painterResource(id = R.drawable.baseline_cloud_upload_24),
            text = "내보내기",
            description = "현재 기기에 저장된 데이터를 파일로 내보냅니다."
        ) {
            Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
                putExtra(Intent.EXTRA_TITLE, "coinAvg_${System.currentTimeMillis()}.cData")
            }.let { intent ->
                exportLauncher.launch(intent)
            }
        }
        Divider(color = MATCH1)
        SectionRow(
            painter = painterResource(id = R.drawable.baseline_cloud_download_24),
            text = "불러오기",
            description = "파일을 불러와 현재 기기에 추가합니다."
        ) {
            Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }.let { intent ->
                importLauncher.launch(intent)
            }
        }
    }

}

@Composable
private fun SectionView(
    section: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = section,
            fontSize = TextUnit(20f, TextUnitType.Sp),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(10.dp),
            color = MATCH1,
            fontFamily = maple
        )
        Divider(color = MATCH1)
        Column(

        ) {
            content()

        }
        Divider(color = MATCH1)
    }
}

@Composable
fun SectionRow(
    painter: Painter,
    text: String,
    description: String? = null,
    otherContent: (@Composable () -> Unit)? = null,
    action: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MATCH1)
            .clickable {
                action()
            }
            .height(70.dp)
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            tint = MATCH2
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = text,
                color = MATCH2,
                fontSize = TextUnit(14f, TextUnitType.Sp),
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                fontFamily = maple
            )
            if (description != null) {
                Text(
                    text = description,
                    color = MATCH2,
                    fontSize = TextUnit(11f, TextUnitType.Sp),
                    maxLines = 2,
                    fontFamily = maple
                )
            }
        }
        if (otherContent != null) {
            Spacer(modifier = Modifier.weight(1f))
            otherContent()
        }
    }
}
