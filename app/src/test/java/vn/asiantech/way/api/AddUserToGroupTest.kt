package vn.asiantech.way.api

import com.hypertrack.lib.models.User
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import vn.asiantech.way.HypertrackRestClient
import vn.asiantech.way.data.model.BodyAddUserToGroup
import vn.asiantech.way.data.source.remote.hypertrackapi.HypertrackService
import vn.asiantech.way.extension.addResponseBody

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 18/12/2017
 */
@Suppress("IllegalIdentifier")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class AddUserToGroupTest {
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
    fun `Given any group id and any user id - When call addUserToGroup - Then return User object`() {
        /* Given */
        val test = TestObserver<User>()
        server.addResponseBody("addUserToGroupResult.json")

        /* When */
        restClient.addUserToGroup("userId", BodyAddUserToGroup("groupId")).subscribe(test)

        /* Then */
        val request = server.takeRequest()
        assertThat(request.method.toUpperCase(), CoreMatchers.`is`("PATCH"))
        assertThat(request.getHeader("Authorization"), `is`("token sk_test_3b4f98fbf6b58eb9d6f710c98c7fcb7a52d2acb6"))

        test.assertValue {
            assertThat(it.id, `is`("0f3831a1-8131-4e70-be02-f6f85b1936f6"))
            assertThat(it.groupId, `is`("e8a8e87e-1ac4-40ba-a1aa-47543ed1e5d1"))
            it.id.isNotEmpty()
        }
    }
}
