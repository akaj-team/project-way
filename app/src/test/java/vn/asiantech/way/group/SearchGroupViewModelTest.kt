package vn.asiantech.way.group

import android.support.v7.util.DiffUtil
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
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.ui.group.search.SearchGroupViewModel
import vn.asiantech.way.util.ListUpdatesCallbackForDispatch
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
    fun `Given a right group name and empty list group - When call trigger search group - Then return list Group not empty`() {
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
            it.dispatchUpdatesTo(ListUpdatesCallbackForDispatch.callback(
                    onChanged = { _, _, _ -> },
                    onMoved = { _, _ -> },
                    onInserted = { position, count ->
                        Assert.assertThat(position, CoreMatchers.`is`(0))
                        Assert.assertThat(count, CoreMatchers.`is`(2))
                    },
                    onRemoved = { _, _ -> }
            ))
            true
        }

        Assert.assertThat(viewModel.groups.size, CoreMatchers.`is`(2))
        Assert.assertThat(viewModel.groups[0].id, CoreMatchers.`is`("id"))
        Assert.assertThat(viewModel.groups[0].token, CoreMatchers.`is`("token"))
    }

    @Test
    fun `Given a right group name and old list group - When call trigger search - Then return list Group not empty`() {
        /* Given */
        val updateListViewStatus1 = TestObserver<DiffUtil.DiffResult>()
        val groups = mutableListOf<Group>()
        viewModel.groups.add(Group("id", "name", "token", "ownerId", "", ""))
        viewModel.groups.add(Group("id1", "name1", "token1", "ownerId1", "", ""))
        viewModel.groups.add(Group("id3", "name3", "token3", "ownerId3", "", ""))
        groups.add(Group("id1", "name at id1", "token1", "ownerId1", "", ""))
        groups.add(Group("id2", "name2", "token2", "ownerId2", "", ""))
        groups.add(Group("id", "name", "token", "ownerId", "", ""))
        `when`(groupRepository.searchGroup(TestUtil.any())).thenReturn(Observable.just(groups))

        /* When */
        viewModel.updateAutocompleteList
                .subscribe(updateListViewStatus1)
        viewModel.eventAfterTextChanged("groupName")

        /* Then */
        updateListViewStatus1.assertValue {
            it.dispatchUpdatesTo(ListUpdatesCallbackForDispatch.callback(
                    onChanged = { position, count, _ ->
                        println("onChanged: at position:$position  with count: $count   ")
                        Assert.assertThat(position, CoreMatchers.`is`(0))
                        Assert.assertThat(count, CoreMatchers.`is`(1))
                    },
                    onMoved = { fromPosition, toPosition ->
                        println("onMoved: from position:$fromPosition  toPosition: $toPosition   ")
                        Assert.assertThat(fromPosition, CoreMatchers.`is`(2))
                        Assert.assertThat(toPosition, CoreMatchers.`is`(0))
                    },
                    onInserted = { position, count ->
                        println("onInserted: at position:$position  with count: $count   ")
                        Assert.assertThat(position, CoreMatchers.`is`(0))
                        Assert.assertThat(count, CoreMatchers.`is`(1))
                    },
                    onRemoved = { position, count ->
                        println("onRemoved: at position:$position  with count: $count   ")
                        Assert.assertThat(position, CoreMatchers.`is`(2))
                        Assert.assertThat(count, CoreMatchers.`is`(1))
                    }
            ))
            true
        }

        Assert.assertThat(viewModel.groups.size, CoreMatchers.`is`(3))
        Assert.assertThat(viewModel.groups[0].id, CoreMatchers.`is`("id1"))
        Assert.assertThat(viewModel.groups[0].name, CoreMatchers.`is`("name at id1"))
        Assert.assertThat(viewModel.groups[0].token, CoreMatchers.`is`("token1"))
        Assert.assertThat(viewModel.groups[0].ownerId, CoreMatchers.`is`("ownerId1"))
    }

    @Test
    fun `Given a wrong group name - When call trigger search group - Then return empty list Group`() {
        /* Given */
        val groups = mutableListOf<Group>()
        `when`(groupRepository.searchGroup(TestUtil.any())).thenReturn(Observable.just(groups))

        /* When */
        viewModel.eventAfterTextChanged("groupName")

        /* Then */
        Assert.assertThat(viewModel.groups.size, CoreMatchers.`is`(0))
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
