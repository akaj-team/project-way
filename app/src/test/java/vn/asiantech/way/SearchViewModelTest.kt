package vn.asiantech.way

import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.SingleObserver
import io.reactivex.internal.operators.single.SingleObserveOn
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.SingleSubject
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import vn.asiantech.way.data.model.ResultPlaceDetail
import vn.asiantech.way.data.model.WayLocation
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
    private lateinit var assetDataRepository: LocalRepository

    private lateinit var viewModel: SearchViewModel

    @Before
    fun initTests() {
        MockitoAnnotations.initMocks(this)
        viewModel = SearchViewModel(wayRepository, assetDataRepository)
    }

    @Test
    fun `Given place id - When call get location detail - Then return result location`() {
        val resultPlace = ResultPlaceDetail(WayLocation("abcd", "bcdeee", "aaaaaaa", "aaaaaa"))

        val getLocationDetailTest = TestObserver<WayLocation>()
        `when`(wayRepository.getLocationDetail(resultPlace.result.placeId)).thenReturn(Observable.just(resultPlace))

        viewModel.getLocationDetail(resultPlace.result.placeId!!).subscribe(getLocationDetailTest)
        getLocationDetailTest.assertValue { it == resultPlace.result }
    }

    @Test
    fun `Given way location - When call get save search history - Then return result `
}

