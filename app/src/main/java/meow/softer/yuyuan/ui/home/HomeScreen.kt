package meow.softer.yuyuan.ui.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import meow.softer.yuyuan.R
import meow.softer.yuyuan.domain.BookInfo
import meow.softer.yuyuan.ui.navigation.Calendar
import meow.softer.yuyuan.ui.navigation.Inbox
import meow.softer.yuyuan.ui.navigation.Playground
import meow.softer.yuyuan.ui.navigation.Search
import meow.softer.yuyuan.ui.navigation.Setting
import meow.softer.yuyuan.ui.theme.primaryLight

@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    onRouteClick: (String) -> Unit
) {
    val mainUiState = mainViewModel.mainUiState.collectAsStateWithLifecycle()
    val homeUiState = when (mainUiState.value) {
        is MainUiState.NoData -> null
        is MainUiState.HasData -> (mainUiState.value as MainUiState.HasData).homeUiState
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
fun HomeScreenContentOld(
    uiState: HomeUiStateOld,
    onRouteClick: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Gray)
    ) {
        //create refs for the components
        val (header, body) = createRefs()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = 15.dp, end = 15.dp)
                .constrainAs(header) {
                    top.linkTo(parent.top)
                }
        ) {
            val calendarIcon: Painter = painterResource(id = R.drawable.ic_calendar)
            Image(
                painter = calendarIcon,
                contentDescription = null,
                modifier = Modifier
                    .size(25.dp)
                    .clickable { onRouteClick(Calendar.route) }
            )
            HomeSearchView(
                modifier = Modifier
                    .height(30.dp)
                    .width(100.dp)
                    .weight(1f)
                    .padding(start = 10.dp, end = 10.dp)
                    .clickable { onRouteClick(Search.route) }
                    .background(
                        color = Color.Gray,
                        shape = RoundedCornerShape(16.dp)
                    )

            ) {

            }
            Image(
                painter = painterResource(id = R.drawable.ic_message),
                contentDescription = null,
                modifier = Modifier
                    .size(25.dp)
                    .clickable { onRouteClick(Inbox.route) }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .constrainAs(body) {
                    top.linkTo(header.bottom)
                }
        ) {
            HomeMainCard(
                uiState = uiState,
                onButtonClick = onRouteClick
            )
            HomePracticeCard()
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
private fun BookCard(
    uiState: HomeUiStateOld
) {
    val bookName: String
    var learntWords = 0
    var totalWords = 1
    val daysLeft: Int
    val progress: String
    when (uiState) {
        is HomeUiStateOld.NoPlan -> {
            bookName = ""
            daysLeft = 0
            progress = ""
        }

        is HomeUiStateOld.HasPLan -> {
            bookName = uiState.planInfo.currentBook.bookName
            learntWords = uiState.planInfo.currentBook.learntWords
            totalWords = uiState.planInfo.currentBook.totalWords
            daysLeft = uiState.planInfo.daysLeft
            progress = "$learntWords / $totalWords"
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.baseline_book_60_purple),
            contentDescription = null,
            modifier = Modifier
        )

        ConstraintLayout(

            modifier = Modifier
                .fillMaxWidth()
        ) {
            val (bookNameElem, changeText, progressBar, progressText, daysText) = createRefs()
            Text(
                text = bookName,
                fontSize = 15.sp,
                modifier = Modifier
                    .constrainAs(bookNameElem) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )
            Text(
                text = stringResource(id = R.string.change_book),
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .constrainAs(changeText) {
                        top.linkTo(parent.top)
                        start.linkTo(bookNameElem.end)
                    }
            )
            LinearProgressIndicator(
                gapSize = (-1).dp,
                drawStopIndicator = {},
                progress = { (learntWords / totalWords).toFloat() },
                color = Color.Blue,
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
                text = progress,
                color = Color.LightGray,
                fontSize = 11.sp,
                modifier = Modifier
                    .constrainAs(progressText) {
                        start.linkTo(parent.start)
                        top.linkTo(progressBar.bottom)
                    }
            )
            Text(
                text = "$daysLeft days left",
                color = Color.LightGray,
                fontSize = 11.sp,
                modifier = Modifier
                    .constrainAs(daysText) {
                        top.linkTo(progressBar.bottom)
                        end.linkTo(parent.end)
                    }

            )
        }
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
private fun PlanCard(uiState: HomeUiStateOld, onButtonClick: (String) -> Unit) {
    val toLearn =
        when (uiState) {
            is HomeUiStateOld.NoPlan -> 0
            is HomeUiStateOld.HasPLan -> uiState.planInfo.toLearnAmount
        }
    val toReview =
        when (uiState) {
            is HomeUiStateOld.NoPlan -> 0
            is HomeUiStateOld.HasPLan -> uiState.planInfo.toReviewAmount
        }
    Column(
        modifier = Modifier
    ) {
        Text(
            text = stringResource(id = R.string.daily_plan),
            fontSize = 15.sp,
            modifier = Modifier
                .padding(top = 10.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(id = R.string.to_learn),
                    fontSize = 14.sp

                )
                Text(
                    text = "$toLearn",
                    fontSize = 40.sp,
                    modifier = Modifier
                        .padding(start = 5.dp)
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {

                Text(
                    text = stringResource(id = R.string.to_review),
                    fontSize = 14.sp

                )
                Text(
                    text = "$toReview",
                    fontSize = 40.sp,
                    modifier = Modifier
                        .padding(start = 5.dp)
                )
            }
        }
        Button(
            onClick = {
                onButtonClick(Playground.route)
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue
            ),
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth()

        ) {
            Text(
                text = stringResource(id = R.string.btn_start_to_learn),
                fontSize = 20.sp,
                color = Color.White,
                modifier = Modifier

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
private fun HomeMainCard(
    uiState: HomeUiStateOld,
    onButtonClick: (String) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 15.dp, end = 15.dp)
            .background(color = Color.White, shape = RoundedCornerShape(5.dp))
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(start = 10.dp, end = 10.dp)

        ) {
            BookCard(uiState = uiState)
            HorizontalDivider(
                color = Color.White,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()

            )
            PlanCard(
                uiState = uiState,
                onButtonClick = onButtonClick
            )
        }
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

@Composable
private fun PracticeItem(
    modifier: Modifier = Modifier,
    @DrawableRes imageId: Int,
    @StringRes textId: Int
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(color = Color.White, shape = RoundedCornerShape(5.dp))
            .padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)

    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = null,
            modifier = Modifier
                .size(35.dp)
        )
        Text(
            text = stringResource(id = textId),
            fontSize = 14.sp,
            color = Color.DarkGray
        )
    }
}


@Composable
private fun HomePracticeCard() {
    val icons = listOf(
        R.drawable.ic_train,
        R.drawable.ic_starred,
        R.drawable.ic_read,
        R.drawable.ic_listen
    )
    val texts = listOf(
        R.string.train,
        R.string.starred,
        R.string.read,
        R.string.listen
    )
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)

    ) {
        for (i in icons.indices) {
            PracticeItem(
                imageId = icons[i],
                textId = texts[i]
            )
        }
    }
}

@Composable
private fun HomeSearchView(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit
) {
    var query by remember {
        mutableStateOf("")
    }
    Row(modifier = modifier) {
        TextField(
            value = query,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            onValueChange = { query = it },
            placeholder = { Text("search...") },
            leadingIcon = {
                IconButton(onClick = { onSearch(query) }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_search_24),
                        contentDescription = ""
                    )
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch(query) }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .border(0.dp, Color.Transparent)
        )
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

