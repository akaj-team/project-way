package vn.asiantech.way.api

import io.reactivex.observers.TestObserver
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.hasItem
import org.junit.Assert.assertThat
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import vn.asiantech.way.api.ApiSuiteTest.Companion.server
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.source.remote.hypertrackapi.HypertrackService
import vn.asiantech.way.extension.addResponseBody
import vn.asiantech.way.util.RestClient

/**
 * GetGroupInfoTest.
 *
 * @author at-ToanNguyen
 */
@Suppress("IllegalIdentifier")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class GetGroupInfoTest {

    companion object {
        private val restClient: HypertrackService by lazy {
            val baseUrl = ApiSuiteTest.server.url("/groups/{groupId}/")
            RestClient.getClient(baseUrl, HypertrackService::class.java)
        }
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
}
