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
        val groupName = ""
        val invite = Invite("", "", "", true)

        /* When */
        `when`(groupRepository.getCurrentRequestOfUser(TestUtil.any())).thenReturn(Observable.just(invite))
        `when`(groupRepository.searchGroup(TestUtil.any())).thenReturn(Observable.just(groups))

        /* Then */
        groupRepository.searchGroup(groupName).subscribe(testGroup)
        viewModel.triggerSearchGroup().subscribe(testGroup)
        testGroup.assertValue { it == groups }
    }

    @Test
    fun `Given a group - When call post request to group - Then return success`() {
        /* Given */
        val test = TestObserver<Boolean>()
        val group = Group("", "", "", "", "", "")

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
