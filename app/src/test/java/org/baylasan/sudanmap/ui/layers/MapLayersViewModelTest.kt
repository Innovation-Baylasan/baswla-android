package org.baylasan.sudanmap.ui.layers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.baylasan.sudanmap.domain.category.FetchCategoriesUseCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class MapLayersViewModelTest {
    private lateinit var fetchCategoriesUseCase: FetchCategoriesUseCase
    private lateinit var viewModel: MapLayersViewModel
    lateinit var lifecycleOwner: LifecycleOwner
    private lateinit var lifecycleRegistry: LifecycleRegistry
    private lateinit var observer: Observer<MapLayersEvent>
    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        lifecycleOwner = mock(verboseLogging = true)
        observer = mock(verboseLogging = true)
        lifecycleRegistry = LifecycleRegistry(lifecycleOwner)
        fetchCategoriesUseCase = mock(verboseLogging = true)
        viewModel = MapLayersViewModel(fetchCategoriesUseCase)
    }

    @Test
    fun testWhenDataLoaded() {
        whenever(fetchCategoriesUseCase.execute()).thenReturn(Single.just(listOf()))
        viewModel.loadCategories()
        viewModel.events.observeForever {
            print(it)
        }
        Thread.sleep(10000L)
    }
}