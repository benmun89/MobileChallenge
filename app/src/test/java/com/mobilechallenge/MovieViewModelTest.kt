import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mobilechallenge.core.data.repository.MovieLocalRepository
import com.mobilechallenge.core.domain.GetNowPlayingMoviesUseCase
import com.mobilechallenge.core.domain.GetPopularMoviesUseCase
import com.mobilechallenge.ui.main.MovieViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class MovieViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: MovieViewModel
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase = mock()
    private val getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase = mock()
    private val movieLocalRepository: MovieLocalRepository = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MovieViewModel(getPopularMoviesUseCase, getNowPlayingMoviesUseCase, movieLocalRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `toggleViewType should switch between grid and list view`() {
        val isGridViewObserver = mock<Observer<Boolean>>()
        viewModel.isGridView.observeForever(isGridViewObserver)

        assert(viewModel.isGridView.value == true)

        viewModel.toggleViewType()
        verify(isGridViewObserver, times(1)).onChanged(false)
        assert(viewModel.isGridView.value == false)

        viewModel.toggleViewType()
        verify(isGridViewObserver, times(2)).onChanged(true)
        assert(viewModel.isGridView.value == true)
    }
}
