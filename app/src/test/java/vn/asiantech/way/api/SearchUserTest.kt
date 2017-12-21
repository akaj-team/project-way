package vn.asiantech.way.api

import io.reactivex.observers.TestObserver
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.hasItems
import org.junit.Assert
import org.junit.FixMethodOrder
import org.junit.Test
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

    companion object {
        private val restClient: HypertrackService by lazy {
            val baseUrl = ApiSuiteTest.server.url("/users/")
            RestClient.getClient(baseUrl, HypertrackService::class.java)
        }
    }

    @Test
    fun `Given mock response - When request searchUser - Then return UserListResult object`() {
        val name = "rim"

        /* Given */
        val test = TestObserver<UserListResult>()
        ApiSuiteTest.server.addResponseBody("searchUser.json")

        /* When */
        restClient.searchUser(name).subscribe(test)

        /* Then */
        val request = ApiSuiteTest.server.takeRequest()
        Assert.assertThat(request.method.toUpperCase(), `is`("GET"))
        Assert.assertThat(request.requestUrl.queryParameterNames(), hasItems("name"))
        Assert.assertThat(request.requestUrl.queryParameter("name"), `is`("rim"))

        test.assertValue {
            val item = it.users[0]
            Assert.assertThat(it.users.size, `is`(1))
            Assert.assertThat(item.id, `is`("a7f13570-5e5e-4651-a58d-985bfcfcedf7"))
            Assert.assertThat(item.name, `is`(name))
            it.users.isNotEmpty()
        }
    }
}
