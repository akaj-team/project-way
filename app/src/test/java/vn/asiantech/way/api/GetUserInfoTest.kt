package vn.asiantech.way.api

import com.hypertrack.lib.models.User
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.hasItems
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import vn.asiantech.way.data.source.remote.hypertrackapi.HypertrackService
import vn.asiantech.way.extension.addResponseBody
import vn.asiantech.way.util.RestClient

/**
 * Get User Info Test
 * Created by haolek on 18/12/2017.
 */
@Suppress("IllegalIdentifier")
class GetUserInfoTest {
    private val server = MockWebServer()
    private lateinit var restClient: HypertrackService

    @Before
    fun initTests() {
        server.start()
        val baseUrl = server.url("/users/{userId}/")
        restClient = RestClient.getClient(baseUrl, HypertrackService::class.java)
    }

    @After
    fun afterTests() {
        server.shutdown()
    }

    @Test
    fun `Given mock response - When request getUserInfo - Then return user object`() {
        /* Given */
        val test = TestObserver<User>()
        server.addResponseBody("getUserInfo.json")

        /* When */
        restClient.getUserInfo("a7f13570-5e5e-4651-a58d-985bfcfcedf7").subscribe(test)

        /* Then */
        val request = server.takeRequest()
        Assert.assertThat(request.method.toUpperCase(), `is`("GET"))
        Assert.assertThat(request.requestUrl.pathSegments(), hasItems("users", "{userId}"))

        test.assertValue { it.id == "a7f13570-5e5e-4651-a58d-985bfcfcedf7" }
        test.assertValue { it.groupId == "5bb1367e-8941-4874-8897-0bb84f154bfa" }
        test.assertValue { it.name == "rim" }
    }
}