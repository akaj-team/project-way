package vn.asiantech.way.api

import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.hasItems
import org.junit.*
import org.junit.runners.MethodSorters
import vn.asiantech.way.data.model.ResultRoad
import vn.asiantech.way.data.source.remote.googleapi.ApiService
import vn.asiantech.way.extension.addResponseBody
import vn.asiantech.way.util.RestClient

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 18/12/2017.
 */
@Suppress("IllegalIdentifier")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class GoogleSnapToRoadApiTest {
    private val server = MockWebServer()
    private lateinit var restClient: ApiService

    @Before
    fun initTests() {
        server.start()
        val baseUrl = server.url("")
        restClient = RestClient.getClient(baseUrl, ApiService::class.java)
    }

    @After
    fun cleanUp() {
        server.shutdown()
    }

    @Test
    fun `Given mock an url - When request google road api - Then return resultRoad object`() {
        /* Given */
        val test = TestObserver<ResultRoad>()
        val url = "roads.googleapis.com/v1/snapToRoads?path=16.0798071,108.2364393|16.0803531,108.2354526&interpolate=true&key=AIzaSyCZc4PAEpeVC18QnS5fPBt5hk3EFMbFjj8"
        server.addResponseBody("googleSnapToRoad.json")

        /* When */
        restClient.getListLocation(url).subscribe(test)

        /* Then */
        val request = server.takeRequest()
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
