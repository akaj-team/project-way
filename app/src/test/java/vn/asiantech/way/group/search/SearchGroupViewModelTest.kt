package vn.asiantech.way.group.search

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import vn.asiantech.way.TestUtil
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.ui.group.search.SearchGroupViewModel

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 14/12/2017.
 */
@Suppress("IllegalIdentifier")
class SearchGroupViewModelTest {

    @Mock
    private lateinit var groupRepository: GroupRepository
    private lateinit var viewModel: SearchGroupViewModel

    @Before
    fun initTest() {
        MockitoAnnotations.initMocks(this)
        val userId = ""
        viewModel = SearchGroupViewModel(groupRepository, userId)
    }

    @Test
    fun `Given a group name - When call trigger search group - Then return list Group`() {
        /* Given */
        val testGroup = TestObserver<List<Group>>()
        val groups = mutableListOf<Group>()
        val groupName = "AAA"
        val testInvite = TestObserver<Invite>()
        val invite = Invite("", "", "", false)

        /* When */
        `when`(groupRepository.getCurrentRequestOfUser(TestUtil.any())).thenReturn(Observable.just(invite))
        groupRepository.getCurrentRequestOfUser(TestUtil.any()).subscribe(testInvite)

        `when`(groupRepository.searchGroup(TestUtil.any())).thenReturn(Observable.just(groups))
        groupRepository.searchGroup(groupName).subscribe(testGroup)

        /* Then */
        viewModel.triggerSearchGroup().subscribe(testGroup)
        testGroup.assertValue { it == groups }
    }

    @Test
    fun `Given a wrong group - When call post request to group - Then return failure`() {
        /* Given */
        val test = TestObserver<Boolean>()
        val group = Group("", "", "", "", "", "")

        /* When */
        `when`(groupRepository.postRequestToGroup(TestUtil.any(), TestUtil.any())).thenReturn(Single.just(false))

        /* Then */
        viewModel.postRequestToGroup(group).subscribe(test)
        test.assertResult(false)
    }

    @Test
    fun `Given a right group - When call post request to group  - Then return success`() {
        /* Given */
        val test = TestObserver<Boolean>()
        val group = Group("03cf844c-beec-4066-9ff2-e5d5b7ed2fbf",
                "AAA",
                "gk_e1e3669ab26e063dd9f97a5e77a00a512c56446f",
                "543984f6-2642-4c24-97f8-79c92adf1630",
                "2017-12-07T09:32:01.691802Z",
                "2017-12-07T09:32:01.697798Z")

        /* When */
        `when`(groupRepository.postRequestToGroup(TestUtil.any(), TestUtil.any())).thenReturn(Single.just(true))

        /* Then */
        viewModel.postRequestToGroup(group).subscribe(test)
        test.assertValue(true)
    }


    @Test
    fun `Given progressDialog - When call post request to group - Then hide progressDialog`() {
        /* Given */
        val test = TestObserver<Boolean>()
        val group = Group("", "", "", "", "", "")

        /* When */
        `when`(groupRepository.postRequestToGroup(TestUtil.any(), TestUtil.any())).thenReturn(Single.just(false))

        /* Then */
        viewModel.progressDialogObservable.subscribe(test)
        viewModel.postRequestToGroup(group).subscribe()
        test.assertValues(true, false)
    }
}
