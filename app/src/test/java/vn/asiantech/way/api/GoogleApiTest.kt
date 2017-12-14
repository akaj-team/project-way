package vn.asiantech.way.api

import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.hasItems
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import vn.asiantech.way.RestClient
import vn.asiantech.way.data.model.AutoCompleteResult
import vn.asiantech.way.data.source.remote.googleapi.ApiService
import vn.asiantech.way.extension.addResponseBody


/**
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
            assertThat(it.predictions.size, `is`(3))
            assertThat(item.id, `is`("4ff79643b4436ddb162cfa77da29cd1b17d083ac"))
            assertThat(item.description, `is`("xxx, Port Loko, Phía Bắc, Si-ê-ra Lê-ôn"))
            assertThat(item.placeId, `is`("ChIJDxMGRSaWBA8Ra3bExYjY4NQ"))

            it.predictions.isNotEmpty()
        }

    }
}