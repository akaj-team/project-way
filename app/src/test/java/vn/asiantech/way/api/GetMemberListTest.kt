package vn.asiantech.way.api

import com.hypertrack.lib.models.User
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.hasItems
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import vn.asiantech.way.HypertrackRestClient
import vn.asiantech.way.data.source.remote.hypertrackapi.HypertrackService
import vn.asiantech.way.data.source.remote.response.Response
import vn.asiantech.way.extension.addResponseBody

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 18/12/2017
 */
@Suppress("IllegalIdentifier")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class GetMemberListTest {
    private val server = MockWebServer()
    private lateinit var restClient: HypertrackService

    @Before
    fun initTest() {
        server.start()
        val baseUrl = server.url("/api/v1/")
        restClient = HypertrackRestClient.getClient(baseUrl, HypertrackService::class.java)
    }

    @After
    fun cleanUp() {
        server.shutdown()
    }

    @Test
    fun `Given any group id - When request getGroupMembers - Then return Response Object with results attribute is user mutable list`() {
        /* Given */
        val test = TestObserver<Response<MutableList<User>>>()
        server.addResponseBody("groupMemberList.json")

        /* When */
        restClient.getGroupMembers("groupId").subscribe(test)

        /* Then */
        val request = server.takeRequest()
        assertThat(request.method.toUpperCase(), CoreMatchers.`is`("GET"))
        assertThat(request.requestUrl.queryParameterNames(), hasItems("group_id"))
        assertThat(request.requestUrl.queryParameter("group_id"), `is`("groupId"))
        assertThat(request.getHeader("Authorization"), `is`("token sk_test_3b4f98fbf6b58eb9d6f710c98c7fcb7a52d2acb6"))

        test.assertValue {
            val firstUser = it.results!![0]
            assertThat(it.results?.size, `is`(2))
            assertThat(firstUser.id, `is`("0f3831a1-8131-4e70-be02-f6f85b1936f6"))
            assertThat(firstUser.name, `is`("mmmm"))
            assertThat(firstUser.groupId, `is`("e8a8e87e-1ac4-40ba-a1aa-47543ed1e5d1"))
            it.results!!.isNotEmpty()
        }
    }
}
