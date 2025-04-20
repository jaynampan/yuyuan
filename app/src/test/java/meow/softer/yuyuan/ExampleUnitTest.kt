package meow.softer.yuyuan

import meow.softer.yuyuan.utils.isToday
import org.junit.Test

import org.junit.Assert.*
import java.time.ZonedDateTime

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun zonedDateTime_isToday_test() {
        assertTrue(ZonedDateTime.now().isToday())
        assertTrue(ZonedDateTime.parse("2024-12-13T12:00:00+08:00[Asia/Shanghai]").isToday())
        assertTrue(ZonedDateTime.parse("2024-12-13T00:00:00+08:00[Asia/Shanghai]").isToday())
        assertTrue(ZonedDateTime.parse("2024-12-13T23:59:59+08:00[Asia/Shanghai]").isToday())

        assertFalse(ZonedDateTime.parse("2024-12-14T00:00:00+08:00[Asia/Shanghai]").isToday())
        assertFalse(ZonedDateTime.parse("2024-12-14T12:00:00+08:00[Asia/Shanghai]").isToday())
        assertFalse(ZonedDateTime.parse("2024-12-14T23:59:59+08:00[Asia/Shanghai]").isToday())

        assertFalse(ZonedDateTime.parse("2024-12-11T00:00:00+08:00[Asia/Shanghai]").isToday())
        assertFalse(ZonedDateTime.parse("2024-12-11T12:12:12+08:00[Asia/Shanghai]").isToday())
        assertFalse(ZonedDateTime.parse("2024-12-11T23:59:59+08:00[Asia/Shanghai]").isToday())

        val dhakaTime = ZonedDateTime.parse("2024-12-12T23:00:00+06:00[Asia/Dhaka]")
        println(dhakaTime.toLocalDate().toString())
        assertTrue(dhakaTime.isToday())
        assertTrue(ZonedDateTime.parse("2024-12-12T22:00:00+06:00[Asia/Dhaka]").isToday())
        assertFalse(ZonedDateTime.parse("2024-12-12T21:59:59+06:00[Asia/Dhaka]").isToday())

        assertTrue(ZonedDateTime.parse("2024-12-12T19:00:00-05:00[America/New_York]").isToday())
    }

    @Test
    fun zonedDateTime_nowTest(){
        println(ZonedDateTime.now())
    }

    @Test
    fun string_split(){
        val pinyin = "liǎng zhǐ xiǎo māo zài wán shuǎ"
        println(pinyin.split(" "))

        val sentence = "我很高兴认识你"
        val filteredList = sentence.filter { it in '\u4e00'..'\u9fff' }
        val result = filteredList.toList()
        println(result)

    }
}
