package meow.softer.yuyuan.ui.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import meow.softer.yuyuan.R
import meow.softer.yuyuan.domain.HanziInfo
import meow.softer.yuyuan.ui.components.PinyinText
import meow.softer.yuyuan.utils.debug

@Composable
fun WordMemoryScreen(
    currentHanzi: HanziInfo,
    onBack: () -> Unit,
    playWordSound: () -> Unit,
    playSentenceSound: () -> Unit,
    onNext: () -> Unit,
    onWrite: () -> Unit
) {
    Column {
        HeadBarV1(
            onBack = {
                onBack()
            }
        )
        BodyV1(
            currentHanzi = currentHanzi,
            playWordSound = { playWordSound() },
            playSentenceSound = { playSentenceSound() },
            onNext = { onNext() },
            onWrite = { onWrite() }
        )
    }
}


@Composable
internal fun HeadBarV1(
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier.padding(top = 20.dp, bottom = 5.dp)
    ) {
        IconButton(onClick = { onBack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null
            )
        }
        Spacer(
            modifier = Modifier.weight(1f)
        )
    }
}


@Composable
private fun BodyV1(
    currentHanzi: HanziInfo,
    playWordSound: () -> Unit,
    playSentenceSound: () -> Unit,
    onNext: () -> Unit,
    onWrite: () -> Unit
) {
    MainCardV1(
        currentHanzi = currentHanzi,
        playWordSound = playWordSound,
        playSentenceSound = playSentenceSound,
        onNext = { onNext() },
        onWrite = { onWrite() }
    )

}

@Composable
private fun MainCardV1(
    currentHanzi: HanziInfo,
    playWordSound: () -> Unit,
    playSentenceSound: () -> Unit,
    onNext: () -> Unit,
    onWrite: () -> Unit
) {
    Column {
        WordAreaV1(
            hanziInfo = currentHanzi,
            playWordSound = playWordSound,
            playSentenceSound = playSentenceSound,
            onWrite = { onWrite() }
        )
        ButtonArea(onNextClick = { onNext() })
    }
}

@Composable
internal fun ButtonArea(
    onNextClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 36.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(), onClick = { onNextClick() }) {
            Text(
                text = "Next"
            )
        }
    }


}

@Composable
private fun WordAreaV1(
    hanziInfo: HanziInfo,
    playWordSound: () -> Unit,
    playSentenceSound: () -> Unit,
    onWrite: () -> Unit
) {
    LaunchedEffect(hanziInfo) {
        delay(100)
        playWordSound()
        delay(1000)
        playSentenceSound()
    }
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Character
        Text(
            text = hanziInfo.word.character, fontWeight = FontWeight.Bold, fontSize = 32.sp
        )
        Row(
            modifier = Modifier, verticalAlignment = Alignment.CenterVertically
        ) {
            // Pinyin
            Text(
                text = hanziInfo.word.pinyin, fontSize = 18.sp
            )
            IconButton(
                onClick = { playWordSound() }
            ) {
                Icon(
                    tint = Color.Blue,
                    painter = painterResource(R.drawable.ic_sound_blue),
                    contentDescription = null
                )
            }
        }
        Text(
            text = hanziInfo.word.meaning,
            fontSize = 18.sp,
            fontStyle = FontStyle.Italic,
            color = Color.DarkGray,
            textDecoration = TextDecoration.Underline
        )
        // sentence
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 30.dp)
                .background(
                    Color(255, 255, 255, 80), RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = { playSentenceSound() }
            ) {
                Icon(
                    tint = Color.Blue,
                    painter = painterResource(R.drawable.ic_sound_blue),
                    contentDescription = null
                )
            }
            PinyinText(
                pinyin = hanziInfo.sentence.pinyin.split(" "),
                chinese = splitChinese(hanziInfo.sentence.content),
                size = 20.sp
            )
            Text(
                text = hanziInfo.sentence.translation,
                fontSize = 18.sp
            )
        }
        if (hanziInfo.word.charJsonFile != null) {
            // writing display
            WebPage(
                hanziInfo.word.character
            ) {
                debug("OnWriteClicked")
                onWrite()
            }
        }
    }
}

fun splitChinese(input: String): List<String> {
    return input.filter { it in '\u4e00'..'\u9fff' }
        .map { it -> it.toString() }
        .toList()
}