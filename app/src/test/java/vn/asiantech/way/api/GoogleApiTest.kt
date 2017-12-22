package vn.asiantech.way.api

import io.reactivex.observers.TestObserver
import org.hamcrest.CoreMatchers.*
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import vn.asiantech.way.data.model.*
import vn.asiantech.way.data.source.remote.googleapi.ApiService
import vn.asiantech.way.data.source.remote.response.Response
import vn.asiantech.way.extension.addResponseBody
import vn.asiantech.way.util.RestClient

/**
 *
 * Created by tien.hoang on 12/14/17.
 */
@Suppress("IllegalIdentifier")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class GoogleApiTest {

    companion object {
        private val restClient: ApiService by lazy {
            val baseUrl = ApiSuiteTest.server.url("/maps/api/")
            RestClient.getClient(baseUrl, ApiService::class.java)
        }
    }

    @Test
    fun `Given mock response - When request searchLocation - Then return autocomplete object`() {
        /* Given */
        val test = TestObserver<AutoCompleteResult>()
        ApiSuiteTest.server.addResponseBody("searchLocation.json")

        /* When */
        restClient.searchLocations("keyWord").subscribe(test)

        /* Then */
        val request = ApiSuiteTest.server.takeRequest()
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
        ApiSuiteTest.server.addResponseBody("getAddressLocation.json")

        /* When */
        restClient.getAddressLocation(latLng).subscribe(test)

        /* Then */
        val request = ApiSuiteTest.server.takeRequest()
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
        ApiSuiteTest.server.addResponseBody("getLocationDetail.json")

        /* When */
        restClient.getLocationDetail(placeId).subscribe(test)

        /* Then */
        val request = ApiSuiteTest.server.takeRequest()
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

    @Test
    fun `Given mock two lat lng point - When request google distance matrix api - Then return resultDistance object`() {
        /* Given */
        val test = TestObserver<ResultDistance>()
        val origin = "16.083833,108.243501"
        val destination = "16.084153,108.242214"
        ApiSuiteTest.server.addResponseBody("locationDistance.json")

        /* When */
        restClient.getLocationDistance(origin, destination).subscribe(test)

        /* Then */
        val request = ApiSuiteTest.server.takeRequest()
        Assert.assertThat(request.method.toUpperCase(), `is`("GET"))
        Assert.assertThat(request.requestUrl.queryParameterNames(), hasItems("units", "origins", "destinations"))
        Assert.assertThat(request.requestUrl.queryParameter("origins"), `is`("16.083833,108.243501"))
        Assert.assertThat(request.requestUrl.queryParameter("destinations"), `is`("16.084153,108.242214"))

        test.assertValue {
            Assert.assertThat(it.rows.size, `is`(1))
            val item = it.rows[0].elements[0]
            Assert.assertThat(item.distance.text, `is`("0.4 km"))
            Assert.assertThat(item.distance.value, `is`(351))
            Assert.assertThat(item.duration.text, `is`("1 min"))
            Assert.assertThat(item.duration.value, `is`(76))

            it.rows.isNotEmpty()
        }
    }

    @Test
    fun `Given mock an url - When request google road api - Then return resultRoad object`() {
        /* Given */
        val test = TestObserver<ResultRoad>()
        val url = "roads.googleapis.com/v1/snapToRoads?path=16.0798071,108.2364393|16.0803531,108.2354526&interpolate=true&key=AIzaSyCZc4PAEpeVC18QnS5fPBt5hk3EFMbFjj8"
        ApiSuiteTest.server.addResponseBody("googleSnapToRoad.json")

        /* When */
        restClient.getListLocation(url).subscribe(test)

        /* Then */
        val request = ApiSuiteTest.server.takeRequest()
        Assert.assertThat(request.method.toUpperCase(), `is`("GET"))
        Assert.assertThat(request.requestUrl.queryParameterNames(), hasItems("path", "interpolate", "key"))
        Assert.assertThat(request.requestUrl.queryParameter("path"), `is`("16.0798071,108.2364393|16.0803531,108.2354526"))
        Assert.assertThat(request.requestUrl.queryParameter("interpolate"), `is`("true"))
        Assert.assertThat(request.requestUrl.queryParameter("key"), `is`("AIzaSyCZc4PAEpeVC18QnS5fPBt5hk3EFMbFjj8"))

        test.assertValue {
            Assert.assertThat(it.locationRoads.size, `is`(9))
            Assert.assertThat(it.locationRoads[0].point.latitude, `is`(16.079881331167627))
            Assert.assertThat(it.locationRoads[0].point.longitude, `is`(108.23634932975393))
            Assert.assertThat(it.locationRoads[0].placeId, `is`("ChIJVXmLIycYQjERpY4bM9aTUG8"))
            Assert.assertThat(it.locationRoads[8].point.latitude, `is`(16.080934072276641))
            Assert.assertThat(it.locationRoads[8].point.longitude, `is`(108.23596334365941))
            Assert.assertThat(it.locationRoads[8].placeId, `is`("ChIJnXA9wiAYQjERQ1CTfr7tnSE"))

            it.locationRoads.isNotEmpty()
        }
    }
}
