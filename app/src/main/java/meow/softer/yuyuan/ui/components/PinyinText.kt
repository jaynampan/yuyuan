package meow.softer.yuyuan.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PinyinText(
    pinyin: List<String>,
    chinese: List<String>,
    size: TextUnit = TextUnit.Unspecified,
) {
    if (pinyin.size == chinese.size) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            pinyin.forEachIndexed { idx, item ->
                Column(
                    modifier = Modifier
                        .padding(horizontal = 1.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item,
                        fontSize = size * 0.6,
                        lineHeight =  size * 0.6
                    )
                    Text(
                        text = chinese[idx],
                        fontSize = size
                    )
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PinyinTextPreview() {
    PinyinText(
        pinyin = listOf("wo", "shi", "lin", "da"),
        chinese = listOf("我", "是", "林", "达"),
        size = 20.sp
    )
}