package vn.asiantech.way.api

import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import vn.asiantech.way.util.RestClient
import vn.asiantech.way.data.model.AutoCompleteResult
import vn.asiantech.way.data.model.LocationAddress
import vn.asiantech.way.data.model.ResultPlaceDetail
import vn.asiantech.way.data.source.remote.googleapi.ApiService
import vn.asiantech.way.data.source.remote.response.Response
import vn.asiantech.way.extension.addResponseBody

/**
 *
 * Created by tien.hoang on 12/14/17.
 */
@Suppress("IllegalIdentifier")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class GoogleApiTest {
    private val server = MockWebServer()
    private lateinit var restClient: ApiService

    @Before
    fun initTests() {
        server.start()
        val baseUrl = server.url("/maps/api/")
        restClient = RestClient.getClient(baseUrl, ApiService::class.java)
    }

    @After
    fun cleanUp() {
        server.shutdown()
    }

    @Test
    fun `Given mock response - When request searchLocation - Then return autocomplete object`() {
        /* Given */
        val test = TestObserver<AutoCompleteResult>()
        server.addResponseBody("searchLocation.json")

        /* When */
        restClient.searchLocations("keyWord").subscribe(test)

        /* Then */
        val request = server.takeRequest()
        assertThat(request.method.toUpperCase(), `is`("GET"))
        assertThat(request.requestUrl.queryParameterNames(), hasItems("input", "key", "language"))
        assertThat(request.requestUrl.queryParameter("input"), `is`("keyWord"))

        test.assertValue {
            val item = it.predictions[0]
            assertThat(it.predictions.size, `is`(2))
            assertThat(item.id, `is`("4ff79643b4436ddb162cfa77da29cd1b17d083ac"))
            assertThat(item.description, `is`("xxx, Port Loko, Phía Bắc, Si-ê-ra Lê-ôn"))
            assertThat(item.placeId, `is`("ChIJDxMGRSaWBA8Ra3bExYjY4NQ"))

            it.predictions.isNotEmpty()
        }
    }

    @Test
    fun `Given mock response -  When request getAddressLocation - Then return response mutable list LocationAddress`() {
        /* Given */
        val test = TestObserver<Response<MutableList<LocationAddress>>>()
        val latLng = "16.087190, 108.232773"
        server.addResponseBody("getAddressLocation.json")

        /* When */
        restClient.getAddressLocation(latLng).subscribe(test)

        /* Then */
        val request = server.takeRequest()
        assertThat(request.method.toUpperCase(), `is`("GET"))
        assertThat(request.requestUrl.queryParameterNames(), hasItem("latlng"))
        assertThat(request.requestUrl.queryParameter("latlng"), `is`(latLng))

        test.assertValue {
            assertThat(it.results?.size, `is`(3))
            val item = it.results?.get(0)
            assertThat(item?.address, `is`("Nại Thịnh 5, Nại Hiên Đông, Sơn Trà, Đà Nẵng, Vietnam"))
            assertThat(item?.placeId, `is`("ChIJ8bYoOR8YQjERMiVjLDsw3Kg"))

            it.results!!.isNotEmpty()
        }
    }

    @Test
    fun `Given mock response -  When request getLocationDetail - Then return ResultPlaceDetail`() {
        /* Given */
        val test = TestObserver<ResultPlaceDetail>()
        val placeId = "ChIJ8bYoOR8YQjERMiVjLDsw3Kg"
        server.addResponseBody("getLocationDetail.json")

        /* When */
        restClient.getLocationDetail(placeId).subscribe(test)

        /* Then */
        val request = server.takeRequest()
        assertThat(request.method.toUpperCase(), `is`("GET"))
        assertThat(request.requestUrl.queryParameterNames(), hasItem("placeid"))
        assertThat(request.requestUrl.queryParameter("placeid"), `is`(placeId))

        test.assertValue {
            val item = it.result
            assertThat(item.formatAddress, `is`("Nại Thịnh 5, Nại Hiên Đông, Sơn Trà, Đà Nẵng, Vietnam"))
            assertThat(item.id, `is`("b4e0c93e867aae145f8e107b6a080293c8382239"))
            assertThat(item.name, `is`("Nại Thịnh 5"))
            assertThat(item.placeId, `is`("ChIJ8bYoOR8YQjERMiVjLDsw3Kg"))

            true
        }
    }
}
