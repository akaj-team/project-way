package vn.asiantech.way.group.info

import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.ui.group.info.GroupInfoViewModel

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 20/12/2017
 */
@Suppress("IllegalIdentifier")
class GroupInfoViewModelTest {

    @Mock
    private lateinit var groupRepository: GroupRepository
    private lateinit var groupInfoViewModel: GroupInfoViewModel

    @Before
    fun initTest() {
        MockitoAnnotations.initMocks(this)
        groupInfoViewModel = GroupInfoViewModel(groupRepository)
    }


    @Test
    fun `Given any group id - When call getMemberList - Then return list user`() {
        /* Given */
        val users = mutableListOf(User(), User())
        val updateMemberListObservable = TestObserver<DiffUtil.DiffResult>()
        `when`(groupRepository.getMemberList("groupId")).thenReturn(Single.just(users))

        /* When */
        groupInfoViewModel.updateMemberList.subscribe(updateMemberListObservable)
        groupInfoViewModel.getMemberList("groupId")

        /* Then */
        updateMemberListObservable.assertValue {
            it.dispatchUpdatesTo(object : ListUpdateCallback {
                override fun onChanged(position: Int, count: Int, payload: Any?) {
                    // No-op
                }

                override fun onMoved(fromPosition: Int, toPosition: Int) {
                    // No-op
                }

                override fun onInserted(position: Int, count: Int) {
                    assertThat(count, `is`(2))
                    assertThat(groupInfoViewModel.users[position], `is`(users[position]))
                }

                override fun onRemoved(position: Int, count: Int) {
                    // No-op
                }
            })
            true
        }
        Assert.assertThat(groupInfoViewModel.users.size, `is`(2))
        Assert.assertThat(groupInfoViewModel.users[0], `is`(users[0]))
    }

    @Test
    fun `Given any group id - When call getGroupInfo - Then return group object`() {
        /* Given */
        val test = TestObserver<Group>()
        val group = Group("groupId", "name", "token", "ownerId", "creatAt", "modifiedAt")
        `when`(groupRepository.getGroupInfo("groupId")).thenReturn(Observable.just(group))

        /* When */
        groupInfoViewModel.getGroupInfo("groupId").subscribe(test)

        /* Then */
        test.assertValue { it == group }
    }

    @Test
    fun `Given any user id - When call leaveGroup - Then return user object`() {
        /* Given */
        val test = TestObserver<User>()
        val user = User()
        `when`(groupRepository.removeUserFromGroup("userId")).thenReturn(Single.just(user))

        /* When */
        groupInfoViewModel.leaveGroup("userId").subscribe(test)

        /* Then */
        test.assertValue { it == user }
    }

    @Test
    fun `Given any group id and any user id - When call changeGroupOwner - Then return true`() {
        /* Given */
        val test = TestObserver<Boolean>()
        `when`(groupRepository.changeGroupOwner("groupId", "userId")).thenReturn(Single.just(true))

        /* When */
        groupInfoViewModel.changeGroupOwner("groupId", "userId").subscribe(test)

        /* Then */
        test.assertValue { it }
    }

    @Test
    fun `Given any group id and any user id - When call changeGroupOwner - Then return false`() {
        /* Given */
        val test = TestObserver<Boolean>()
        `when`(groupRepository.changeGroupOwner("groupId", "userId")).thenReturn(Single.just(false))

        /* When */
        groupInfoViewModel.changeGroupOwner("groupId", "userId").subscribe(test)

        /* Then */
        test.assertValue { !it }
    }
}
