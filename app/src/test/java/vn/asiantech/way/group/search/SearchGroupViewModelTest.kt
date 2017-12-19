package vn.asiantech.way.group.search

import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.ui.group.search.SearchGroupViewModel
import vn.asiantech.way.util.RxSchedulersOverrideRule
import vn.asiantech.way.util.TestUtil

/**
 *  Copyright © 2017 AsianTech inc.
 *  Created by hoavot on 14/12/2017.
 */
@Suppress("IllegalIdentifier")
class SearchGroupViewModelTest {

    @Mock
    private lateinit var groupRepository: GroupRepository
    private lateinit var viewModel: SearchGroupViewModel

    @get:Rule
    val rule1 = RxSchedulersOverrideRule()

    @Before
    fun initTest() {
        MockitoAnnotations.initMocks(this)
        viewModel = SearchGroupViewModel(groupRepository, "")
    }

    @Test
    fun `Given a group name - When call trigger search group - Then return list Group`() {
        /* Given */
        val groups = mutableListOf<Group>()
        val updateListViewStatus1 = TestObserver<DiffUtil.DiffResult>()
        groups.add(Group("id", "name", "token", "ownerId", "", ""))
        groups.add(Group("id", "name", "token", "ownerId", "", ""))

        `when`(groupRepository.searchGroup(TestUtil.any())).thenReturn(Observable.just(groups))

        /* When */
        viewModel.updateAutocompleteList
                .subscribe(updateListViewStatus1)
        viewModel.eventAfterTextChanged("groupName")

        /* Then */
        updateListViewStatus1.assertValue {
            it.dispatchUpdatesTo(object : ListUpdateCallback {
                override fun onChanged(position: Int, count: Int, payload: Any?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onMoved(fromPosition: Int, toPosition: Int) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onInserted(position: Int, count: Int) {
                    Assert.assertThat(position, CoreMatchers.`is`(0))
                    Assert.assertThat(count, CoreMatchers.`is`(2))
                }

                override fun onRemoved(position: Int, count: Int) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
            true
        }

        Assert.assertThat(viewModel.groups.size, CoreMatchers.`is`(2))
        Assert.assertThat(viewModel.groups[0].id, CoreMatchers.`is`("id"))
        Assert.assertThat(viewModel.groups[0].token, CoreMatchers.`is`("token"))
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
