package meow.softer.yuyuan.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import meow.softer.yuyuan.R
import meow.softer.yuyuan.data.local.entiity.Book
import meow.softer.yuyuan.ui.theme.onPrimaryContainerLight
import meow.softer.yuyuan.ui.theme.primaryContainerLightMediumContrast
import meow.softer.yuyuan.ui.theme.primaryLight
import meow.softer.yuyuan.ui.theme.tertiaryContainerLightHighContrast
import meow.softer.yuyuan.ui.theme.tertiaryDark
import meow.softer.yuyuan.ui.theme.tertiaryLight
import meow.softer.yuyuan.utils.debug

@Composable
fun SettingScreen(
    onBack: () -> Unit,
    viewModel: SettingViewModel
) {
    val uiState = viewModel.settingUiStates.collectAsStateWithLifecycle()
    val settingUiState = when (uiState.value) {
        is SettingUiStateRaw.NoData -> null
        is SettingUiStateRaw.HasData -> {
            val result = (uiState.value as SettingUiStateRaw.HasData).settingUiState
            debug("speed = ${result.currentSpeed}")
            result
        }
    }
    //todo:Loading
    if (settingUiState != null) {
        SettingScreenContent(
            onBack = { onBack() },
            onBookChosen = { viewModel.onBookChosen(it) },
            onRawSpeedChange = { viewModel.onRawSpeedChange(it) },
            currentSpeed = settingUiState.currentSpeed,
            currentBookId = settingUiState.currentBookId,
            bookList = settingUiState.bookList,
            onRawSpeedChosen = {viewModel.onRawSpeedChosen()}
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {

            Text("Loading.....")
        }
    }

}

@Composable
fun SettingScreenContent(
    onBack: () -> Unit,
    onBookChosen: (Int) -> Unit,
    onRawSpeedChange: (Float) -> Unit,
    onRawSpeedChosen: () -> Unit,
    currentSpeed: Float,
    currentBookId: Int,
    bookList: List<Book>
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Header
        Header(onBack)
        // Body
        SpeedChooser(
            currentSpeed,
            onSliderPositionChange = { onRawSpeedChange(it) },
            onRawSpeedChosen = { onRawSpeedChosen() },
        )
        BookChooser(bookList, currentBookId, onBookChosen)
    }


}

@Composable
private fun BookChooser(
    bookList: List<Book>,
    currentBookId: Int,
    onBookChosen: (Int) -> Unit
) {
    Text("Choose Book")
    val tintList = listOf(
        primaryLight,
        tertiaryLight,
        tertiaryDark,
        tertiaryContainerLightHighContrast,
        onPrimaryContainerLight,
        primaryContainerLightMediumContrast,
    )
    var selectedBook by remember { mutableIntStateOf(currentBookId) }
    LazyColumn(
        modifier = Modifier
            .padding(top = 10.dp)
    ) {

        items(bookList) { book ->
            val color = if (book.id == selectedBook) {
                MaterialTheme.colorScheme.surfaceContainerLow
            } else {
                MaterialTheme.colorScheme.surfaceDim
            }
            Row(
                modifier = Modifier
                    .clickable {
                        selectedBook = book.id
                        onBookChosen(book.id)
                    }
                    .padding(2.dp)
                    .fillMaxWidth()
                    .background(
                        color = color,
                        shape = MaterialTheme.shapes.small
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_book_60_purple),
                    contentDescription = null,
                    tint = tintList[book.id - 1] //todo: temp
                )
                Text(
                    text = book.bookTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }


    }
}

@Composable
private fun SpeedChooser(
    currentSpeed: Float,
    onSliderPositionChange: (Float) -> Unit,
    onRawSpeedChosen: () -> Unit
) {
    Text("Choose Speed")
    SpeedPicker(
        modifier = Modifier
            .padding(horizontal = 5.dp),
        currentSpeed = currentSpeed,
        onSliderPositionChange = { onSliderPositionChange(it) },
        onRawSpeedChosen = { onRawSpeedChosen() },
    )
    Spacer(
        modifier = Modifier
            .height(20.dp)
    )
}

@Composable
private fun Header(onBack: () -> Unit) {
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
fun SpeedPicker(
    modifier: Modifier = Modifier,
    currentSpeed: Float = 1f,
    onSliderPositionChange: (Float) -> Unit,
    onRawSpeedChosen: () -> Unit
) {
    val rawSpeed = currentSpeed * 10
    var sliderPosition by remember { mutableFloatStateOf(rawSpeed) }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                onSliderPositionChange(sliderPosition)
            },
            onValueChangeFinished = { onRawSpeedChosen() },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.outlineVariant,
            ),
            steps = 7,
            valueRange = 4f..12f
        )
        Text(
            text = ((sliderPosition + 0.1).toInt() / 10f).toString() + "x",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 18.sp,
            fontStyle = FontStyle.Italic
        )
    }
}
