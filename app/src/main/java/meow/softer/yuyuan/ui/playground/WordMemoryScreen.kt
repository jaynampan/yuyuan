package meow.softer.yuyuan.ui.playground

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import meow.softer.yuyuan.R
import meow.softer.yuyuan.data.local.entiity.Sentence
import meow.softer.yuyuan.data.local.entiity.Word

@Composable
fun WordMemoryScreen(
    playgroundViewModel: PlaygroundViewModel,
    onBack: () -> Unit
) {
    Column {
        HeadBar(
            onBack = {
                onBack()
            },
            onSearch = {},
            onStar = {
                playgroundViewModel.toggleStarred()
            },
            onDotsClick = {}
        )
        Body(
            statisticTexts = playgroundViewModel.getStatisticTexts(),
            word = playgroundViewModel.currentWord.collectAsStateWithLifecycle().value,
            sentence = playgroundViewModel.currentSentence.collectAsStateWithLifecycle().value,
            playWordSound = { playgroundViewModel.playWordSound() },
            playSentenceSound = { playgroundViewModel.playSentenceSound() }
        )
    }
}


@Composable
private fun HeadBar(
    onBack: () -> Unit, onSearch: () -> Unit, onStar: () -> Unit, onDotsClick: () -> Unit
) {
    Row(
        modifier = Modifier.padding(vertical = 5.dp)
    ) {
        IconButton(onClick = { onBack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null
            )
        }
        Spacer(
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = { onSearch() }) {
            Icon(
                imageVector = Icons.Outlined.Search, contentDescription = null
            )
        }
        IconButton(onClick = { onStar() }) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder, contentDescription = null
            )
        }
        IconButton(onClick = { onDotsClick() }) {
            Icon(
                imageVector = Icons.Filled.Settings, contentDescription = null
            )
        }
    }
}

@Composable
private fun Body(
    statisticTexts: List<String>,
    word: Word,
    sentence: Sentence,
    playWordSound: () -> Unit,
    playSentenceSound: () -> Unit
) {
    Column {
        StatisticBar(
            statisticTexts = statisticTexts
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
        MainCard(
            word = word,
            sentence = sentence,
            playWordSound = playWordSound,
            playSentenceSound = playSentenceSound,
        )
    }
}

@Composable
private fun MainCard(
    word: Word,
    sentence: Sentence,
    playWordSound: () -> Unit,
    playSentenceSound: () -> Unit
) {
    Column {
        WordArea(
            word = word,
            sentence = sentence,
            playWordSound = playWordSound,
            playSentenceSound = playSentenceSound,
        )
        ButtonArea(onNext = {})
    }
}

@Composable
private fun ButtonArea(
    onNext: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 36.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(), onClick = { onNext() }) {
            Text(
                text = "Next"
            )
        }
    }


}

@Composable
private fun WordArea(
    word: Word,
    sentence: Sentence,
    playWordSound: () -> Unit,
    playSentenceSound: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "learnt N days ago"
        )
        Text(
            text = word.character, fontWeight = FontWeight.Bold, fontSize = 24.sp
        )
        Row(
            modifier = Modifier, verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = word.pinyin, fontSize = 18.sp
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
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                tint = Color.Gray,
                imageVector = Icons.Default.Delete,
                contentDescription = null
            )
            Text(
                text = "Skip", color = Color.Gray, fontSize = 16.sp
            )
        }
        Column(
            modifier = Modifier
                .padding(4.dp)
                .background(
                    Color(255, 255, 255, 80), RoundedCornerShape(8.dp)
                )
                .padding(8.dp), horizontalAlignment = Alignment.End
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
            Text(
                text = sentence.sentence
            )
        }
    }
}

@Composable
private fun StatisticBar(
    statisticTexts: List<String>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                Color(255, 255, 255, 50), RoundedCornerShape(8.dp)
            )
            .padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatisticTab(title = "Review", text = statisticTexts[0])
        StatisticTab(title = "New", text = statisticTexts[1])
        StatisticTab(title = "Progress", text = statisticTexts[2])
    }
}

@Composable
private fun StatisticTab(
    title: String, text: String
) {
    Column(
        modifier = Modifier.padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title
        )
        Text(
            modifier = Modifier.padding(top = 4.dp), text = text
        )
    }
}
