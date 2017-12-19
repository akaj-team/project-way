package vn.asiantech.way.api

import com.hypertrack.lib.models.User
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.hasItems
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertThat
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
        val userId = "a7f13570-5e5e-4651-a58d-985bfcfcedf7"

        /* Given */
        val test = TestObserver<User>()
        server.addResponseBody("getUserInfo.json")

        /* When */
        restClient.getUserInfo(userId).subscribe(test)

        /* Then */
        val request = server.takeRequest()
        Assert.assertThat(request.method.toUpperCase(), `is`("GET"))
        Assert.assertThat(request.requestUrl.pathSegments(), hasItems("users", "{userId}"))

        test.assertValue {
            assertThat(it.id, `is`("a7f13570-5e5e-4651-a58d-985bfcfcedf7"))
            assertThat(it.groupId, `is`("5bb1367e-8941-4874-8897-0bb84f154bfa"))
            assertThat(it.name, `is`("rim"))
            true
        }
    }

}
