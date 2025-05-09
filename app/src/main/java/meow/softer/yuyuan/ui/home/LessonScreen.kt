package meow.softer.yuyuan.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import meow.softer.yuyuan.R


@Composable
fun LearnFragmentLayout() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.lesson_title),
            fontSize = 20.sp,
            modifier = Modifier
                .padding(top = 20.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {

        }
    }
}
