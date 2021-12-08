package za.co.botcoin.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest= Config.NONE)
abstract class BaseViewModelTest {

    protected val application = RuntimeEnvironment.getApplication()

    @ObsoleteCoroutinesApi
    protected val mainThreadSurrogate = newSingleThreadContext("UI Thread")

    @Rule
    @JvmField
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    val initRule: MockitoRule = MockitoJUnit.rule()

    @Before
    open fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        MockitoAnnotations.openMocks(this)
    }

    @After
    open fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }
}