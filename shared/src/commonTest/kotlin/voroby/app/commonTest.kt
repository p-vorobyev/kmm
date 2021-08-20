package voroby.app

import kotlin.test.Test
import kotlin.test.assertTrue

class CommonGreetingTest {

    @Test
    fun testExample() {
        assertTrue(Greeting().greeting().contains("Guess what is it platform?"), "Check 'Guess what is it platform?' is mentioned")
    }
}