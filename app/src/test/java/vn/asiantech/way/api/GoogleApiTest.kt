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
import vn.asiantech.way.RestClient
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
    fun `Given mock response -  When request getAddressLocation - Then return response mutablelist LocationAddress`() {
        /* Given */
        val test = TestObserver<Response<MutableList<LocationAddress>>>()
        server.addResponseBody("getAddressLocation.json")

        /* When */
        restClient.getAddressLocation("16.087190, 108.232773").subscribe(test)

        /* Then */
        val request = server.takeRequest()
        assertThat(request.method.toUpperCase(), `is`("GET"))
        assertThat(request.requestUrl.queryParameterNames(), hasItem("latlng"))
        assertThat(request.requestUrl.queryParameter("latlng"), `is`("16.087190, 108.232773"))

        test.assertValue {
            val item = it.results?.get(0)
            assertThat(it.results?.size, `is`(3))
            assertThat(item?.address, `is`("Nại Thịnh 5, Nại Hiên Đông, Sơn Trà, Đà Nẵng, Vietnam"))
            assertThat(item?.placeId, `is`("ChIJ8bYoOR8YQjERMiVjLDsw3Kg"))

            it.results!!.isNotEmpty()
        }
    }

    @Test
    fun `Given mock response -  When request getLocationDetail - Then ResultPlaceDetail`() {
        /* Given */
        val test = TestObserver<ResultPlaceDetail>()
        server.addResponseBody("getLocationDetail.json")

        /* When */
        restClient.getLocationDetail("ChIJ8bYoOR8YQjERMiVjLDsw3Kg").subscribe(test)

        /* Then */
        val request = server.takeRequest()
        assertThat(request.method.toUpperCase(), `is`("GET"))
        assertThat(request.requestUrl.queryParameterNames(), hasItem("placeid"))
        assertThat(request.requestUrl.queryParameter("placeid"), `is`("ChIJ8bYoOR8YQjERMiVjLDsw3Kg"))

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
