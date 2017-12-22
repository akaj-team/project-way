package vn.asiantech.way.share

import android.content.Context
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.hypertrack.lib.models.HyperTrackLocation
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import vn.asiantech.way.data.model.*
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.ui.share.ShareViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 14/12/2017.
 */
@Suppress("IllegalIdentifier")
class ShareViewModelTest {
    @Mock
    private lateinit var wayRepository: WayRepository
    @Mock
    private lateinit var context: Context
    private lateinit var viewModel: ShareViewModel

    @Before
    fun initTests() {
        MockitoAnnotations.initMocks(this)
        viewModel = ShareViewModel(wayRepository)
    }

    @Test
    fun `Given start point and destination point - When get during time and distance ETA - Then return the ETA time and distance between two point`() {
        /* Given */
        val test = TestObserver<List<Row>>()
        val startPoint = "16.0790101,108.2361203"
        val destinationPoint = "16.0803501,108.2345213"

        val result = ResultDistance(listOf(), listOf(), listOf(Row(listOf(Element(ElementInfo("a", 12), ElementInfo("b", 21))))))
        `when`(wayRepository.getLocationDistance(startPoint, destinationPoint)).thenReturn(Observable.just(result))

        /* When */
        viewModel.getLocationDistance(startPoint, destinationPoint).subscribe(test)

        /* Then */
        test.assertValue { it.size == result.rows.size }
        test.assertValue { it[0].elements[0].distance.text == result.rows[0].elements[0].distance.text }
        test.assertValue { it[0].elements[0].distance.value == result.rows[0].elements[0].distance.value }
        test.assertValue { it[0].elements[0].duration.text == result.rows[0].elements[0].duration.text }
        test.assertValue { it[0].elements[0].duration.value == result.rows[0].elements[0].duration.value }
    }

    @Test
    fun `Given date time calendar - When get current time - Then return formatted string`() {
        /* Given */
        val test = TestObserver<String>()
        val calendar = Calendar.getInstance().time
        val simple = SimpleDateFormat("KK:mm a', Th'MM dd", Locale.ENGLISH)

        /* When */
        viewModel.getCalendarDateTime().subscribe(test)

        /* Then */
        test.assertValue { it == simple.format(calendar) }
    }

    @Test
    fun `Given a latLng point - When compare two point - Then return compare result`() {
        /* Given */
        val resultTest = TestObserver<Boolean>()
        val currentLatLng = LatLng(1.123456, 1.123456)
        val destinationLatLng = LatLng(1.12311, 1.12311)

        /* When */
        viewModel.compareLocation(currentLatLng, destinationLatLng).subscribe(resultTest)

        /* Then */
        resultTest.assertValue { it }
    }

    @Test
    fun `Given two point - When calculator the degree for rotation - Then return the degree`() {
        /* Given */
        val resultTest = TestObserver<Float>()
        val startLatLng = LatLng(16.0804405, 108.238518)
        val endLatLng = LatLng(16.0804405, 108.238518)
        val startLatLng1 = LatLng(16.0804405, 108.238518)
        val endLatLng1 = LatLng(16.0804426, 108.2385165)

        /* When */
        viewModel.getAngleMarker(startLatLng, endLatLng).subscribe(resultTest)
        viewModel.getAngleMarker(startLatLng1, endLatLng1).subscribe(resultTest)

        /* Then */
        resultTest.assertValueAt(0, 0.0f)
        resultTest.assertValueAt(1, 325.53668f)
    }

    @Test
    fun `Given enabled GPS - When get current location - Then return your current location`() {
        /* Given */
        val location = Location("point")
        val resultTest = TestObserver<Location>()
        `when`(wayRepository.getCurrentLocation(context)).thenReturn(Single.just(location))

        /* When */
        viewModel.getCurrentLocation(context).subscribe(resultTest)

        /* Then */
        resultTest.assertValue { it == location }
    }

    @Test
    fun `Given tracking url - When get tracking url - Then return right tracking url`() {
        /* Given */
        val test = TestObserver<String>()
        val url = "ahahahahah"
        `when`(wayRepository.getTrackingURL()).thenReturn(Single.just(url))

        /* When */
        viewModel.getTrackingURL().subscribe(test)

        /* Then */
        test.assertValue { it == url }
    }

    @Test
    fun `Given HyperTrack object - When current location with hyper track - Then return right HyperTrack object`() {
        /* Given */
        val test = TestObserver<HyperTrackLocation>()
        val hyperTrackLocation = HyperTrackLocation(Location("point"))
        `when`(wayRepository.getCurrentLocationHyperTrack()).thenReturn(Single.just(hyperTrackLocation))

        /* When */
        viewModel.getCurrentLocationHyperTrack().subscribe(test)

        /* Then */
        test.assertValue { it == hyperTrackLocation }
        test.assertResult(hyperTrackLocation)
    }


    @Test
    fun `Given location LatLng - When get location name - Then return right name of the location`() {
        /* Given */
        val test = TestObserver<String>()
        val locationName = "Da Nang"
        val latLng = LatLng(16.123, 108.111)
        `when`(wayRepository.getLocationName(context, latLng)).thenReturn(Single.just(locationName))

        /* When */
        viewModel.getLocationName(context, latLng).subscribe(test)

        /* Then */
        test.assertValue { it == locationName }
    }

    @Test
    fun `Given time - When get time during status - Then formatted string`() {
        /* Given */
        val test = TestObserver<String>()
        val time = 3600L
        val timeConverse = "1hour"

        /* When */
        viewModel.getTimeDuringStatus(time).subscribe(test)

        /* Then */
        test.assertValue { it == timeConverse }
    }


    @Test
    fun `Given two LatLng point - When get distance per second - Then return right value`() {
        /* Given */
        val test = TestObserver<List<Float>>()
        val startPoint = LatLng(1.123, 2.123)
        val endPoint = LatLng(1.133, 2.133)
        val result = listOf(0.0f, 2.0f)

        /* When */
        viewModel.getDistancePerSecond(startPoint, endPoint).subscribe(test)

        /* Then */
        test.assertValue { it.size == result.size }
        test.assertValue { it[0] == result[0] }
    }


    @Test
    fun `Given url google road api - When get google road api - Then return right ResultRoad object`() {
        /* Given */
        val test = TestObserver<List<LocationRoad>>()
        val url = "abc.com"
        val resultRoad = ResultRoad(listOf(LocationRoad(Point(1.123, 2.123), "AALSKSK"),
                LocationRoad(Point(2.123, 3.123), "JDKSOALSD")))
        `when`(wayRepository.getListLocation(url)).thenReturn(Observable.just(resultRoad))
        /* When */
        viewModel.getListLocationWhenReOpen(url).subscribe(test)

        /* Then */
        test.assertValue { it == resultRoad.locationRoads }
        test.assertValue { it[0].point == resultRoad.locationRoads[0].point }
        test.assertValue { it[0].placeId == resultRoad.locationRoads[0].placeId }
    }
}
