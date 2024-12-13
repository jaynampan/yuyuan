package meow.softer.yuyuan

import meow.softer.yuyuan.data.Result
import org.junit.Assert.fail

fun <T> testYuResult(
    arg: Result<T>,
    onSuccess: (T) -> Unit,
    onError: () -> Unit = {}
) {
    when (arg) {
        is Result.Success -> {
            val data = arg.data
            onSuccess(data)
        }

        is Result.Error -> {
            onError()
            fail(arg.exception.toString())
        }
    }
}