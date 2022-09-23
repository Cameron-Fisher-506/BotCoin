package za.co.botcoin.view

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest= Config.NONE)
abstract class BaseViewModelTest {
    protected val application = RuntimeEnvironment.getApplication()

    protected val unconfinedTestDispatcher = UnconfinedTestDispatcher()
}