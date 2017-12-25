package vn.asiantech.way.api

import com.hypertrack.lib.models.User
import io.reactivex.observers.TestObserver
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
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
        MatcherAssert.assertThat(request.method.toUpperCase(), CoreMatchers.`is`("PATCH"))
        MatcherAssert.assertThat(request.requestUrl.pathSegments(), CoreMatchers.hasItems("userId"))
        MatcherAssert.assertThat(request.body.readUtf8(), CoreMatchers.`is`("{\"group_id\":\"groupId\"}"))

        testRemoveUserGroup.assertValue {
            MatcherAssert.assertThat(it.id, CoreMatchers.`is`("8f525af5-05fa-44a3-8c6e-d7a0180e259f"))
            MatcherAssert.assertThat(it.name, CoreMatchers.`is`("haiiiiaaa"))
            MatcherAssert.assertThat(it.lookupId, CoreMatchers.`is`("123456"))
            MatcherAssert.assertThat(it.photo, CoreMatchers.`is`("https://core-api-prod-assets.s3.amazonaws.com/uploads/photos/drivers/driver_0df3cd1e-82ce-44f5-918e-d41884a8892b_image_name"))
            true
        }
    }
}
