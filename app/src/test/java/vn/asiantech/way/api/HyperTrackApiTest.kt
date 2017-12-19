package vn.asiantech.way.api

import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.hasItem
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.source.remote.hypertrackapi.HypertrackService
import vn.asiantech.way.extension.addResponseBody
import vn.asiantech.way.util.RestClient

/**
 * HyperTrackApiTest.
 *
 * @author at-ToanNguyen
 */
@Suppress("IllegalIdentifier")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class HyperTrackApiTest {
    private val server = MockWebServer()
    private lateinit var restClient: HypertrackService

    @Before
    fun initTest() {
        server.start()
        val baseUrl = server.url("/api/v1/")
        restClient = RestClient.getClient(baseUrl, HypertrackService::class.java)
    }

    @After
    fun cleanUp() {
        server.shutdown()
    }

    @Test
    fun `Given mock response - When request getGroupInfo - Then return group`() {
        /* Given */
        val test = TestObserver<Group>()
        server.addResponseBody("groupInfo.json")

        /* When */
        restClient.getGroupInfo("id").subscribe(test)

        /* Then */
        val request = server.takeRequest()
        assertThat(request.method.toUpperCase(), `is`("GET"))
        assertThat(request.requestUrl.pathSegments(), hasItem("id"))

        test.assertValue {
            assertThat(it.id, `is`("83892b6f-4d4e-4ead-bcdd-d95a24571506"))
            assertThat(it.name, `is`("Group Cua Cuong"))
            assertThat(it.createAt, `is`("2017-11-09T04:02:01.769935Z"))
            assertThat(it.modifiedAt, `is`("2017-11-09T04:02:01.784838Z"))
            true
        }
    }

    @Test
    fun `Given mock response  - When request createGroup - Then return group`() {
        /* Given */
        val test = TestObserver<Group>()
        server.addResponseBody("createGroup.json")

        /* When */
        restClient.createGroup("nameGroup").subscribe(test)

        /* Then */
        val request = server.takeRequest()
        val body = request.body.readUtf8()
        assertThat(request.method.toUpperCase(), `is`("POST"))
        assertThat(body, `is`("name=nameGroup"))

        test.assertValue {
            assertThat(it.id, `is`("f3ead2ae-dc0a-4a7e-85be-74ee51d9d70a"))
            assertThat(it.name, `is`("Southern group"))
            assertThat(it.token, `is`("gk_1409a26018fse12"))
            assertThat(it.createAt, `is`("2016-03-09T05:20:19.742229Z"))
            assertThat(it.modifiedAt, `is`("2016-03-09T05:20:19.742257Z"))
            true
        }
    }
}
