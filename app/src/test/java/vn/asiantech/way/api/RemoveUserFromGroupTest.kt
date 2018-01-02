package vn.asiantech.way.api

import com.hypertrack.lib.models.User
import io.reactivex.observers.TestObserver
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.MatcherAssert.assertThat
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import vn.asiantech.way.api.ApiSuiteTest.Companion.server
import vn.asiantech.way.data.model.BodyAddUserToGroup
import vn.asiantech.way.data.source.remote.hypertrackapi.HypertrackService
import vn.asiantech.way.extension.addResponseBody
import vn.asiantech.way.util.RestClient

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by at-hoavo on 22/12/2017.
 */
@Suppress("IllegalIdentifier")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RemoveUserFromGroupTest {
    companion object {
        private val restClient: HypertrackService by lazy {
            val baseUrl = ApiSuiteTest.server.url("/users/")
            RestClient.getClient(baseUrl, HypertrackService::class.java)
        }
    }

    @Test
    fun `Given mock response - When remove user from group with valid userId - Then return user object`() {
        /* Given */
        val testRemoveUserGroup = TestObserver<User>()
        server.addResponseBody("removeUserFromGroup.json")

        /* When */
        restClient.removeUserFromGroup("userId", BodyAddUserToGroup("groupId")).subscribe(testRemoveUserGroup)

        /* Then */
        val request = server.takeRequest()
        assertThat(request.method.toUpperCase(), `is`("PATCH"))
        assertThat(request.requestUrl.pathSegments(), hasItems("userId"))
        assertThat(request.body.readUtf8(), `is`("{\"group_id\":\"groupId\"}"))

        testRemoveUserGroup.assertValue {
            assertThat(it.id, `is`("8f525af5-05fa-44a3-8c6e-d7a0180e259f"))
            assertThat(it.name, `is`("haiiiiaaa"))
            assertThat(it.lookupId, `is`("123456"))
            assertThat(it.photo, `is`("https://core-api-prod-assets.s3.amazonaws.com/uploads/photos/drivers/driver_0df3cd1e-82ce-44f5-918e-d41884a8892b_image_name"))
            true
        }
    }
}
