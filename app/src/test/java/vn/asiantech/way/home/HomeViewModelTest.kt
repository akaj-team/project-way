package vn.asiantech.way.home

import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import com.google.android.gms.maps.model.LatLng
import io.reactivex.observers.TestObserver
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import vn.asiantech.way.data.model.TrackingInformation
import vn.asiantech.way.data.source.LocalRepository
import vn.asiantech.way.ui.home.HomeViewModel
import vn.asiantech.way.util.RxSchedulersOverrideRule

/**
 * HomeViewModelTest.
 *
 * @author at-ToanNguyen
 */
@Suppress("IllegalIdentifier")
class HomeViewModelTest {
    @Mock
    private lateinit var localRepository: LocalRepository

    @get:Rule
    val rule = RxSchedulersOverrideRule()

    private lateinit var viewModel: HomeViewModel

    @Before
    fun initTest() {
        MockitoAnnotations.initMocks(this)
        viewModel = HomeViewModel(localRepository)
    }

    @Test
    fun `Given a list tracking history - When get tracking history - Then return list tracking history`() {
        /* Given */
        val trackingHistory = mutableListOf(TrackingInformation("2016-03-09T05:20:19.742229", "Tracking", "xxxx", LatLng(423.0, 12.0)),
                TrackingInformation("2016-03-09T05:20:19.742229", "Stop", "xxxx", LatLng(32.0, 14.0)))
        val test = TestObserver<DiffUtil.DiffResult>()
        `when`(localRepository.getTrackingHistory()).thenReturn(trackingHistory)

        /* When */
        viewModel.updateHistoryTrackingList.subscribe(test)
        viewModel.getTrackingHistory()

        /* Then */
        test.assertValue {
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
        Assert.assertThat(viewModel.historyTrackingList.size, `is`(2))
        Assert.assertThat(viewModel.historyTrackingList[0].status, `is`("Tracking"))
    }
}
