package vn.asiantech.way.api

import io.reactivex.observers.TestObserver
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.MatcherAssert.assertThat
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import vn.asiantech.way.api.ApiSuiteTest.Companion.server
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
class SearchGroupTest {
    companion object {
        private val restClient: HypertrackService by lazy {
            val baseUrl = ApiSuiteTest.server.url("/groups/")
            RestClient.getClient(baseUrl, HypertrackService::class.java)
        }
    }

    @Test
    fun `Given mock response - When search group with right name - Then return SearchGroupResult object contains list group`() {
        /* Given */
        val testSearchGroup = TestObserver<SearchGroupResult>()
        server.addResponseBody("searchGroupRightName.json")

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
    fun `Given mock response - When search group with wrong name - Then return SearchGroupResult object without group`() {
        val testSearchGroup1 = TestObserver<SearchGroupResult>()
        server.addResponseBody("searchGroupWrongName.json")

        /* When */
        restClient.searchGroup("").subscribe(testSearchGroup1)

        /* Then */
        val request = server.takeRequest()
        assertThat(request.method.toUpperCase(), `is`("GET"))
        assertThat(request.requestUrl.queryParameterNames(), hasItems("name"))
        assertThat(request.requestUrl.queryParameter("name"), `is`(""))

        testSearchGroup1.assertValue {
            assertThat(it.groups.size, `is`(0))
            it.groups.isEmpty()
        }
    }
}
