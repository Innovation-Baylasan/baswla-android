package org.baylasan.sudanmap

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import io.reactivex.Single
import org.baylasan.sudanmap.data.entity.model.Category
import org.baylasan.sudanmap.data.entity.model.Entity
import org.baylasan.sudanmap.data.entity.model.Location
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun <T> LiveData<T>.blockingObserve(): T? {
    var value: T? = null
    val latch = CountDownLatch(1)

    val observer = Observer<T> { t ->
        value = t
        latch.countDown()
    }

    observeForever(observer)

    latch.await(2, TimeUnit.SECONDS)
    return value
}

fun <T> LiveData<T>.testObserver() = TestObserver<T>().also {
    observeForever(it)
}

val emptyEntity = Entity("", Category(), "", "", 1, Location(), "", listOf(), "")

class TestObserver<T> : Observer<T>, MutableIterable<T> {

    private val observedValues = mutableListOf<T>()

    override fun onChanged(value: T) {
        observedValues.add(value)
    }

    override fun iterator(): MutableIterator<T> {
        return observedValues.iterator()
    }

}

fun <T> T.toSingle(): Single<T> {
    return Single.just(this)
}
