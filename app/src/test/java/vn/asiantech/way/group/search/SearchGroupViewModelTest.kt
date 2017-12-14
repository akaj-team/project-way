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
        viewModel = SearchGroupViewModel(groupRepository, "")
    }

    @Test
    fun `Given a group name - When call trigger search group - Then return list Group`() {
        /* Given */
        val testGroup = TestObserver<List<Group>>()
        val groups = mutableListOf<Group>()
        val groupName = "groupName"
        val invite = Invite("from", "to", "groupName", true)
        `when`(groupRepository.getCurrentRequestOfUser(TestUtil.any())).thenReturn(Observable.just(invite))
        `when`(groupRepository.searchGroup(TestUtil.any())).thenReturn(Observable.just(groups))


        /* When */
        groupRepository.searchGroup(groupName).subscribe(testGroup)
        viewModel.triggerSearchGroup().subscribe(testGroup)

        /* Then */
        testGroup.assertValue { it == groups }
    }

    @Test
    fun `Given a group - When call post request to group - Then return true`() {
        /* Given */
        val test = TestObserver<Boolean>()
        val group = Group("id", "name", "token", "ownerId", "createAt", "modifiedAt")
        `when`(groupRepository.postRequestToGroup(TestUtil.any(), TestUtil.any())).thenReturn(Single.just(true))

        /* When */
        viewModel.postRequestToGroup(group).subscribe(test)

        /* Then */
        test.assertValue(true)
    }

    @Test
    fun `Given progressDialog - When call post request to group - Then progress dialog should show then hide`() {
        /* Given */
        val test = TestObserver<Boolean>()
        val group = Group("id", "name", "token", "ownerId", "createAt", "modifiedAt")
        `when`(groupRepository.postRequestToGroup(TestUtil.any(), TestUtil.any())).thenReturn(Single.just(true))

        /* When */
        viewModel.progressDialogObservable.subscribe(test)
        viewModel.postRequestToGroup(group).subscribe()

        /* Then */
        test.assertValues(true, false)
    }
}
