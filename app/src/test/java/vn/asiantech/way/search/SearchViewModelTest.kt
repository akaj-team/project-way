package vn.asiantech.way.search

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.SingleSubject
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import vn.asiantech.way.TestUtil
import vn.asiantech.way.data.model.*
import vn.asiantech.way.data.source.LocalRepository
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.ui.search.SearchViewModel
import vn.asiantech.way.utils.AppConstants
import java.util.concurrent.TimeUnit

/**
 * Created by y.nguyen on 11/28/17.
 */
@Suppress("IllegalIdentifier")
class SearchViewModelTest {
    @Mock
    private lateinit var wayRepository: WayRepository

    @Mock
    private lateinit var localRepository: LocalRepository

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

        /* When */
        `when`(wayRepository.getLocationDetail(TestUtil.any())).thenReturn(Observable.just(resultPlace))

        /* Then */
        viewModel.getLocationDetail(resultPlace.result.placeId!!).subscribe(getLocationDetailTest)
        getLocationDetailTest.assertValue { it == resultPlace.result }
    }

    @Test
    fun `Given input,language, sensor - When call trigger search location result - Then return list location`() {
        /* Given */
        val input = "input"
        val language = "vi"
        val sensor = false
        val autoCompleteResult = AutoCompleteResult(listOf())
        val triggerSearchLocationResultTest = TestObserver<List<WayLocation>>()

        /* When */
        `when`(wayRepository.searchLocations(input, language, sensor))
                .thenReturn(Observable.just(autoCompleteResult))

        /* Then */
        wayRepository.searchLocations("input", "vi", false)
                .map { it.predictions }
                .flatMapIterable { list -> list }
                .map {
                    with(it) {
                        WayLocation(id, placeId, description, structuredFormatting.mainText)
                    }
                }
                .toList()
                .toObservable()
                .subscribe(triggerSearchLocationResultTest)
        viewModel.triggerSearchLocationResult(input, sensor).subscribe(triggerSearchLocationResultTest)
        triggerSearchLocationResultTest.assertValue { it == autoCompleteResult.predictions }
    }

}

