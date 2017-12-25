package vn.asiantech.way.api

import io.reactivex.observers.TestObserver
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import vn.asiantech.way.api.ApiSuiteTest.Companion.server
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.source.remote.hypertrackapi.HypertrackService
import vn.asiantech.way.extension.addResponseBody
import vn.asiantech.way.util.RestClient

/**
 * CreateGroupTest.
 *
 * @author at-ToanNguyen
 */
@Suppress("IllegalIdentifier")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class CreateGroupTest {

    companion object {
        private val restClient: HypertrackService by lazy {
            val baseUrl = ApiSuiteTest.server.url("/groups/")
            RestClient.getClient(baseUrl, HypertrackService::class.java)
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
        Assert.assertThat(request.method.toUpperCase(), `is`("POST"))
        Assert.assertThat(body, `is`("name=nameGroup"))

        test.assertValue {
            Assert.assertThat(it.id, `is`("f3ead2ae-dc0a-4a7e-85be-74ee51d9d70a"))
            Assert.assertThat(it.name, `is`("Southern group"))
            Assert.assertThat(it.token, `is`("gk_1409a26018fse12"))
            Assert.assertThat(it.createAt, `is`("2016-03-09T05:20:19.742229Z"))
            Assert.assertThat(it.modifiedAt, `is`("2016-03-09T05:20:19.742257Z"))
            true
        }
    }
}
