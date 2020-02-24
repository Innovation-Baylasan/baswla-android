package org.baylasan.sudanmap

import org.junit.Test
import java.text.DateFormat
import java.text.SimpleDateFormat

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

    }

}
 fun main() {
     val dateString = "2020-02-24T19:33:51.000000Z"
     print(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateString))
 }
