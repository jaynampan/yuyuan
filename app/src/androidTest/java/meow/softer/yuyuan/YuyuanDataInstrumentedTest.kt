package meow.softer.yuyuan

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.test.runTest
import meow.softer.yuyuan.data.repository.book.BookRepository
import meow.softer.yuyuan.data.local.YuyuanDatabase
import meow.softer.yuyuan.data.local.entiity.WordStatus
import meow.softer.yuyuan.data.repository.sentence.SentenceRepository
import meow.softer.yuyuan.data.repository.word.WordRepository
import meow.softer.yuyuan.data.repository.wordstatus.WordStatusRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.ZonedDateTime

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class YuyuanDataInstrumentedTest {
    private lateinit var testContext: Context
    private lateinit var testDatabase: YuyuanDatabase


    @Before
    fun setup() {
        testContext = InstrumentationRegistry.getInstrumentation().targetContext
        testDatabase = YuyuanDatabase.getDatabase(testContext)
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        assertEquals("meow.softer.yuyuan", testContext.packageName)
    }

    @Test
    fun yuyuanDatabaseTest() {
        // book 1
        var allWords = testDatabase.wordDao().getByBookId(1)
        assertEquals(520, allWords.size)
        println(allWords[1])
        println(allWords[260])
        println(allWords[519])

        // book 6
        allWords = testDatabase.wordDao().getByBookId(6)
        assertEquals(1312, allWords.size)
        println(allWords[1])
        println(allWords[260])
        println(allWords[1311])
    }

    /**
     * Use runTest to launch suspend functions in coroutine
     */
    @Test
    fun bookRepository_test() = runTest {
        val bookRepository = BookRepository(testDatabase.bookDao())
        testYuResult(
            arg = bookRepository.getAllBooks(),
            onSuccess = { data ->
                assertEquals(6, data.size)
                println(data[0])
                println(data[5])
            }
        )
        testYuResult(
            arg = bookRepository.getBookById(1),
            onSuccess = { data ->
                println(data)
            }
        )
    }

    @Test
    fun wordRepository_test() = runTest {
        val wordRepository = WordRepository(testDatabase.wordDao())
        testYuResult(
            arg = wordRepository.getAllWords(),
            onSuccess = { data ->
                assertEquals(6100, data.size)
                println(data[1568])
            }
        )
        testYuResult(
            arg = wordRepository.getWordById(1257),
            onSuccess = { data ->
                println(data)
            }
        )
        testYuResult(
            arg = wordRepository.getWordCountByBookId(1),
            onSuccess = { data ->
                assertEquals(520, data)
            }
        )
        testYuResult(
            arg = wordRepository.getWordsByIds(listOf(1, 122, 1890, 5120)),
            onSuccess = { data ->
                assertEquals(4, data.size)
                data.forEach { println(it) }
            }
        )
    }

    @Test
    fun wordStatusRepository_test() = runTest {
        val wordStatusRepository = WordStatusRepository(testDatabase.wordStatusDao())
        testYuResult(
            arg = wordStatusRepository.getAllWordStatus(),
            onSuccess = { data ->
                assertNotNull(data)
                assertEquals(6100, data.size)
            }
        )
        testYuResult(
            arg = wordStatusRepository.getWordStatusByUser(),
            onSuccess = { data ->
                assertNotNull(data)
                assertEquals(6100, data.size)
            }
        )
        testYuResult(
            arg = wordStatusRepository.getWordStatusByWordId(155),
            onSuccess = { data ->
                assertNotNull(data)
                println(data)
            }
        )
        testYuResult(
            arg = wordStatusRepository.getWordStatusById(id = 125),
            onSuccess = { data ->
                assertNotNull(data)
                println(data)
            }
        )
        testYuResult(
            arg = wordStatusRepository.getLearntList(),
            onSuccess = { data ->
                assertNotNull(data)
                assertEquals(0, data.size)
            }
        )
        testYuResult(
            arg = wordStatusRepository.getStarredList(),
            onSuccess = { data ->
                assertNotNull(data)
                assertEquals(0, data.size)
            }
        )
        testYuResult(
            arg = wordStatusRepository.getLearntListByDate(ZonedDateTime.now()),
            onSuccess = { data ->
                assertNotNull(data)
                assertEquals(0, data.size)
            }
        )
        testYuResult(
            arg = wordStatusRepository.getLearntListByInterval(
                ZonedDateTime.now().minusDays(7),
                ZonedDateTime.now()
            ),
            onSuccess = { data ->
                assertNotNull(data)
                assertEquals(0, data.size)
            }
        )

        // make status id = 15 starred
        testYuResult(
            arg = wordStatusRepository.toggleStarred(
                WordStatus(
                    id = 15,
                    wordId = 168,
                    userId = 1
                )
            ),
            onSuccess = {}
        )
        testYuResult(
            arg = wordStatusRepository.getStarredList(),
            onSuccess = { data ->
                assertNotNull(data)
                assertEquals(1, data.size)
                assertEquals(15, data[0].id)
            }
        )

        // set wordStatus id = 190
        testYuResult(
            arg = wordStatusRepository.setLearntByStatusId(190),
            onSuccess = {}
        )
        testYuResult(
            arg = wordStatusRepository.getLearntList(),
            onSuccess = { data ->
                assertNotNull(data)
                assertEquals(1, data.size)
                assertEquals(190, data[0].id)
            }
        )

    }

    @Test
    fun sentenceRepository_test() = runTest {
        val sentenceRepository = SentenceRepository(testDatabase.sentenceDao())
        testYuResult(
            arg = sentenceRepository.getAllSentences(),
            onSuccess = { data ->
                assertNotNull(data)
                assertEquals(6100, data.size)
            }
        )
        testYuResult(
            arg = sentenceRepository.getSentenceById(129),
            onSuccess = { data ->
                assertNotNull(data)
                assertEquals(129, data.id)
                assertEquals(129, data.wordId)
                assertEquals("这是新的一年。", data.sentence)
                assertEquals("This is a new year.", data.translation)
                assertEquals("zhè shì xīn de yī nián", data.sentencePinyin)
                assertEquals("年_s.mp3", data.sentenceAudioFile)

                println(data.createdAt)
                assertTrue("2024-12-11" in data.createdAt.toString())
            }
        )

        val word = testDatabase.wordDao().getById(518)
        testYuResult(
            arg = sentenceRepository.getSentenceByWord(word),
            onSuccess = { data ->
                assertNotNull(data)
                assertEquals(518, data.id)
                assertEquals(518, data.wordId)
                assertEquals("请给我一张纸。", data.sentence)
            }
        )
        testYuResult(
            arg = sentenceRepository.getSentenceByWordId(850),
            onSuccess = { data ->
                assertNotNull(data)
                assertEquals(850, data.id)
                assertEquals(850, data.wordId)
                assertEquals("他穿着一条牛仔裤。", data.sentence)
            }
        )
        val words = testDatabase.wordDao().getByBookId(1)
        testYuResult(
            arg = sentenceRepository.getSentenceByWordIds(words.map { it.id }),
            onSuccess = { data ->
                assertNotNull(data)
                assertEquals(520, data.size)
                assertNotNull(data[120].sentence)
                assertNotNull(data[120].sentenceAudioFile)
                assertEquals(data[120].id, data[120].wordId)
            }
        )
        val ids = listOf<Int>(1, 15, 79)
        testYuResult(
            arg = sentenceRepository.getSentencesByIds(ids),
            onSuccess = { data ->
                assertNotNull(data)
                assertEquals(ids, data.map { it.id })
                assertNotNull(data[0].sentence)
            }
        )
    }
}