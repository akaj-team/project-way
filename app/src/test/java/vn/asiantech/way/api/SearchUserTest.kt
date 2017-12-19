package vn.asiantech.way.api

import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.hasItems
import org.junit.*
import org.junit.runners.MethodSorters
import vn.asiantech.way.data.model.UserListResult
import vn.asiantech.way.data.source.remote.hypertrackapi.HypertrackService
import vn.asiantech.way.extension.addResponseBody
import vn.asiantech.way.util.RestClient

/**
 * Search User Test
 * Created by haolek on 18/12/2017.
 */
@Suppress("IllegalIdentifier")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class SearchUserTest {
    private val server = MockWebServer()
    private lateinit var restClient: HypertrackService

    @Before
    fun initTests() {
        server.start()
        val baseUrl = server.url("/users/")
        restClient = RestClient.getClient(baseUrl, HypertrackService::class.java)
    }

    @After
    fun afterTests() {
        server.shutdown()
    }

    @Test
    fun `Given mock response - When request searchUser - Then return UserListResult object`() {
        /* Given */
        val test = TestObserver<UserListResult>()
        server.addResponseBody("searchUser.json")

        /* When */
        restClient.searchUser("rim").subscribe(test)

        /* Then */
        val request = server.takeRequest()
        Assert.assertThat(request.method.toUpperCase(), `is`("GET"))
        Assert.assertThat(request.requestUrl.queryParameterNames(), hasItems("name"))
        Assert.assertThat(request.requestUrl.queryParameter("name"), `is`("rim"))

        test.assertValue {
            val item = it.users[0]
            Assert.assertThat(it.users.size, `is`(1))
            Assert.assertThat(item.id, `is`("a7f13570-5e5e-4651-a58d-985bfcfcedf7"))
            Assert.assertThat(item.name, `is`("rim"))
            it.users.isNotEmpty()
        }
    }
}
