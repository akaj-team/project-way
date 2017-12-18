package vn.asiantech.way.search

import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import vn.asiantech.way.util.RxSchedulersOverrideRule
import vn.asiantech.way.util.TestUtil
import vn.asiantech.way.data.model.*
import vn.asiantech.way.data.source.LocalRepository
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.ui.search.SearchViewModel


/**
 * Created by y.nguyen on 11/28/17.
 */
@Suppress("IllegalIdentifier")
class SearchViewModelTest {
    @Mock
    private lateinit var wayRepository: WayRepository

    @Mock
    private lateinit var localRepository: LocalRepository

    @get:Rule
    val rule = RxSchedulersOverrideRule()

    private lateinit var viewModel: SearchViewModel

    @Before
    fun initTests() {
        MockitoAnnotations.initMocks(this)
        viewModel = SearchViewModel(wayRepository, localRepository)
    }

    @Test
    fun `Given place id - When call get location detail - Then return result location`() {
        /* Given */
        val resultPlace = ResultPlaceDetail(WayLocation("id", "placeId", "name", "formattedAddress"))
        val getLocationDetailTest = TestObserver<WayLocation>()
        `when`(wayRepository.getLocationDetail(TestUtil.any())).thenReturn(Observable.just(resultPlace))

        /* When */
        viewModel.getLocationDetail(resultPlace.result.placeId!!).subscribe(getLocationDetailTest)

        /* Then */
        getLocationDetailTest.assertValue { it == resultPlace.result }
    }

    @Test
    fun `Given input, language, sensor - When call trigger search location result - Then return list location`() {
        /* Given */
        val input = "input"
        val language = "vi"
        val sensor = false
        val autoCompleteResult = AutoCompleteResult(listOf(AutoCompleteLocation("description", StructuredFormatting(""),"1","placeId")))
        val wayLocations = listOf(WayLocation("1", "place", "name", "xxx"), WayLocation("12", "place", "name", "xxx"))
        val updateListViewStatus = TestObserver<DiffUtil.DiffResult>()
        `when`(wayRepository.searchLocations(input, language, sensor))
                .thenReturn(Observable.just(autoCompleteResult))

        `when`(localRepository.getSearchHistory())
                .thenReturn(wayLocations)

        /* When */
        viewModel.updateAutocompleteList.subscribe(updateListViewStatus)
        viewModel.searchLocations("input")

        /* Then */
        updateListViewStatus.assertValue {
            it.dispatchUpdatesTo(object : ListUpdateCallback{
                override fun onChanged(position: Int, count: Int, payload: Any?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onMoved(fromPosition: Int, toPosition: Int) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onInserted(position: Int, count: Int) {
                    Assert.assertThat(position, `is`(0))
                    Assert.assertThat(count, `is`(1))
                }

                override fun onRemoved(position: Int, count: Int) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
        true
        }
        Assert.assertThat(viewModel.locations.size, `is`(1))
    }

    @Test
    fun `Given input, language, sensor - When call trigger search location result - Then return list location1`() {
        /* Given */
        val input = "input"
        val language = "vi"
        val sensor = false
        val autoCompleteResult = AutoCompleteResult(listOf())
        val wayLocations = listOf(WayLocation("1", "place", "name", "xxx"), WayLocation("1", "place", "name", "xxx"))
        val updateListViewStatus = TestObserver<DiffUtil.DiffResult>()
        `when`(wayRepository.searchLocations(input, language, sensor))
                .thenReturn(Observable.just(autoCompleteResult))

        `when`(localRepository.getSearchHistory())
                .thenReturn(wayLocations)

        /* When */
        viewModel.updateAutocompleteList
                .observeOn(Schedulers.computation())
                .subscribe(updateListViewStatus)
        viewModel.searchLocations("")


        /* Then */
        updateListViewStatus.assertValue {
            it.dispatchUpdatesTo(object : ListUpdateCallback {
                override fun onChanged(position: Int, count: Int, payload: Any?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onMoved(fromPosition: Int, toPosition: Int) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onInserted(position: Int, count: Int) {
                    Assert.assertThat(position, `is`(0))
                    Assert.assertThat(count, `is`(2))
                }

                override fun onRemoved(position: Int, count: Int) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
            true
        }
        Assert.assertThat(viewModel.locations.size, `is`(2))
        Assert.assertThat(viewModel.locations[0].id, `is`("1"))
        Assert.assertThat(viewModel.locations[0].placeId, `is`("place"))
    }
}
