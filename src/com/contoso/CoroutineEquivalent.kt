package com.contoso

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

private class CustomRecursiveAction(private val lo: Int, private val hi: Int) {

    suspend fun compute() {
        if (hi - lo < 0) return
        if (hi - lo + 1 > 4) {
            invokeSubtasks()
        } else {
            processing()
        }
    }

    private suspend fun invokeSubtasks() {
        val mid = (lo + hi) / 2
        val job1 = GlobalScope.launch { CustomRecursiveAction(lo, mid).compute() }
        val job2 = GlobalScope.launch { CustomRecursiveAction(mid + 1, hi).compute() }
        job1.join()
        job2.join()
    }

    private fun processing() {
        Utils.sleep((hi - lo + 1).toLong())
        println("This result - (" + lo + " - " + hi + ") - was processed by " + Thread.currentThread().name)
    }
}

class CoroutineEquivalent {

    @Test
    fun foo() {
        val start = System.currentTimeMillis()
        val len = 4000
        runBlocking {  CustomRecursiveAction(0, len - 1).compute() }
        println(System.currentTimeMillis() - start)
    }
}
