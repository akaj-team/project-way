package vn.asiantech.way.api

import com.hypertrack.lib.models.User
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import vn.asiantech.way.data.model.BodyAddUserToGroup
import vn.asiantech.way.data.model.SearchGroupResult
import vn.asiantech.way.data.source.remote.hypertrackapi.HypertrackService
import vn.asiantech.way.extension.addResponseBody
import vn.asiantech.way.util.RestClient

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by at-hoavo on 18/12/2017.
 */
@Suppress("IllegalIdentifier")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class HypertrackApiTest {
    private val server = MockWebServer()
    private lateinit var restClient: HypertrackService

    @Before
    fun initTests() {
        server.start()
        val baseUrl = server.url("/api/v1/")
        restClient = RestClient.getClient(baseUrl, HypertrackService::class.java)
    }

    @After
    fun cleanUp() {
        server.shutdown()
    }

    @Test
    fun `Given mock response - When search group - Then return SearchGroupResult object`() {
        /* Given */
        val testSearchGroup = TestObserver<SearchGroupResult>()
        server.addResponseBody("searchGroup.json")

        /* When */
        restClient.searchGroup("nameGroup").subscribe(testSearchGroup)

        /* Then */
        val request = server.takeRequest()
        assertThat(request.method.toUpperCase(), `is`("GET"))
        assertThat(request.requestUrl.queryParameterNames(), hasItems("name"))
        assertThat(request.requestUrl.queryParameter("name"), `is`("nameGroup"))

        testSearchGroup.assertValue {
            val item = it.groups[0]
            assertThat(it.groups.size, `is`(2))
            assertThat(item.id, `is`("83892b6f-4d4e-4ead-bcdd-d95a24571506"))
            assertThat(item.name, `is`("Group Cua Cuong"))
            assertThat(item.createAt, `is`("2017-11-09T04:02:01.769935Z"))
            assertThat(item.modifiedAt, `is`("2017-11-09T04:02:01.784838Z"))
            assertThat(item.token, `is`("gk_f1a6f81c400dc67bacfd547dfd66aa16b08f8191"))
            it.groups.isNotEmpty()
        }
    }


    @Test
    fun `Given mock response - When remove user from group - Then return user object`() {
        /* Given */
        val testRemoveUserGroup = TestObserver<User>()
        server.addResponseBody("removeUserFromGroup.json")

        /* When */
        restClient.removeUserFromGroup("userId", BodyAddUserToGroup("groupId")).subscribe(testRemoveUserGroup)

        /* Then */
        val request = server.takeRequest()
        print(request.body.toString())
        assertThat(request.method.toUpperCase(), `is`("PATCH"))
        assertThat(request.requestUrl.pathSegments(), hasItems("userId"))
        assertThat(request.body.readUtf8(), `is`("{\"group_id\":\"groupId\"}"))

        testRemoveUserGroup.assertValue {
            assertThat(it.id, `is`("8f525af5-05fa-44a3-8c6e-d7a0180e259f"))
            assertThat(it.name, `is`("haiiiiaaa"))
            assertThat(it.lookupId,`is`("123456"))
            assertThat(it.photo,`is`("https://core-api-prod-assets.s3.amazonaws.com/uploads/photos/drivers/driver_0df3cd1e-82ce-44f5-918e-d41884a8892b_image_name"))
            true
        }
    }
}
