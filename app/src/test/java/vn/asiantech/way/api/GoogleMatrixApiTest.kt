package vn.asiantech.way.api

import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.hasItems
import org.junit.*
import org.junit.runners.MethodSorters
import vn.asiantech.way.data.model.ResultDistance
import vn.asiantech.way.data.source.remote.googleapi.ApiService
import vn.asiantech.way.extension.addResponseBody
import vn.asiantech.way.util.RestClient

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 18/12/2017.
 */
@Suppress("IllegalIdentifier")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class GoogleMatrixApiTest {
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
    fun `Given mock two lat lng point - When request google distance matrix api - Then return resultDistance object`() {
        /* Given */
        val test = TestObserver<ResultDistance>()
        server.addResponseBody("locationDistance.json")

        /* When */
        restClient.getLocationDistance("16.083833,108.243501", "16.084153,108.242214").subscribe(test)

        /* Then */
        val request = server.takeRequest()
        Assert.assertThat(request.method.toUpperCase(), `is`("GET"))
        Assert.assertThat(request.requestUrl.queryParameterNames(), hasItems("units", "origins", "destinations"))
        Assert.assertThat(request.requestUrl.queryParameter("origins"), `is`("16.083833,108.243501"))
        Assert.assertThat(request.requestUrl.queryParameter("destinations"), `is`("16.084153,108.242214"))

        test.assertValue {
            val item = it.rows[0].elements[0]
            Assert.assertThat(it.rows.size, `is`(1))
            Assert.assertThat(item.distance.text, `is`("0.4 km"))
            Assert.assertThat(item.distance.value, `is`(351))
            Assert.assertThat(item.duration.text, `is`("1 min"))
            Assert.assertThat(item.duration.value, `is`(76))

            it.rows.isNotEmpty()
        }
    }
}
