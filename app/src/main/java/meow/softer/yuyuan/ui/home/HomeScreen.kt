package meow.softer.yuyuan.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import meow.softer.yuyuan.R
import meow.softer.yuyuan.domain.BookInfo
import meow.softer.yuyuan.ui.navigation.Playground
import meow.softer.yuyuan.ui.navigation.Setting
import meow.softer.yuyuan.ui.theme.primaryLight

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onRouteClick: (String) -> Unit
) {
    val mainUiState = homeViewModel.mainUiState.collectAsStateWithLifecycle()
    val homeUiState = when (mainUiState.value) {
        is HomeUiStateRaw.NoData -> null
        is HomeUiStateRaw.HasData -> (mainUiState.value as HomeUiStateRaw.HasData).homeUiState
    }
    //TODO: loading
    if (homeUiState != null) {
        HomeScreenContent(
            bookInfo = homeUiState.currentBook,
            onRouteClick = { onRouteClick(it) }
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
fun HomeScreenContent(
    bookInfo: BookInfo,
    onRouteClick: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeMainCardV1(
            bookInfo = bookInfo,
            onButtonClick = { onRouteClick(it) }
        )
        Spacer(
            modifier = Modifier.weight(1F)
        )
        Text(
            modifier = Modifier
                .clickable { onRouteClick(Setting.route) },
            text = "Change Book & Speed",
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold
        )
        Spacer(
            modifier = Modifier.height(100.dp)
        )

    }

}


@Composable
private fun BookCardV1(
    bookInfo: BookInfo,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_book_60_purple),
            contentDescription = null,
            modifier = Modifier,
            tint = MaterialTheme.colorScheme.primary
        )

        ConstraintLayout(

            modifier = Modifier
                .fillMaxWidth()
        ) {
            val (bookNameElem, progressText, progressBar) = createRefs()
            Text(
                text = bookInfo.bookName,
                fontSize = 15.sp,
                modifier = Modifier
                    .constrainAs(bookNameElem) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )
            LinearProgressIndicator(
                gapSize = (-1).dp,
                drawStopIndicator = {},
                progress = { bookInfo.learntWords.toFloat() / bookInfo.totalWords },
                color = primaryLight,
                trackColor = Color.Gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 4.dp)
                    .constrainAs(progressBar) {
                        start.linkTo(parent.start)
                        top.linkTo(bookNameElem.bottom)
                    }
            )
            Text(
                text = "${bookInfo.learntWords} / ${bookInfo.totalWords}",
                color = Color.LightGray,
                fontSize = 11.sp,
                modifier = Modifier
                    .constrainAs(progressText) {
                        start.linkTo(parent.start)
                        top.linkTo(progressBar.bottom)
                    }
            )

        }
    }
}


@Composable
private fun PlanCardV1(onStart: (String) -> Unit) {
    Button(
        onClick = {
            onStart(Playground.route)
        },
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(top = 5.dp)
            .fillMaxWidth()

    ) {
        Text(
            text = stringResource(id = R.string.btn_start_to_learn),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier

        )
    }
}


@Composable
private fun HomeMainCardV1(
    bookInfo: BookInfo,
    onButtonClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp, start = 15.dp, end = 15.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp)

        ) {
            BookCardV1(
                bookInfo = bookInfo
            )
            HorizontalDivider(
                color = Color.White,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()

            )
            PlanCardV1(
                onStart = onButtonClick
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun LinearProgressBarTest() {
    LinearProgressIndicator(
        gapSize = (-1).dp,
        drawStopIndicator = {},
        progress = { 0.48f },
        color = Color.Blue,
        trackColor = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp)

    )
}