package vn.asiantech.way.group.info

import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import vn.asiantech.way.data.model.Group
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.ui.group.info.GroupInfoViewModel
import vn.asiantech.way.util.TestUtil

/**
 * GroupInfoViewModelTest
 * @author NgocTTN
 */
@Suppress("IllegalIdentifier")
class GroupInfoViewModelTest {
    @Mock
    private lateinit var groupRepository: GroupRepository
    private lateinit var viewModel: GroupInfoViewModel

    @Before
    fun initTest() {
        MockitoAnnotations.initMocks(this)
        viewModel = GroupInfoViewModel(groupRepository)
    }

    @Test
    fun `Given a group Id - When call get group info  - Then return a group`() {
        /* Given */
        val test = TestObserver<Group>()
        val groupId = "groupId"
        val group = Group(id = "id", name = "name", token = "token", ownerId = "ownerId",
                createAt = "createAt", modifiedAt = "modifiedAt")
        `when`(groupRepository.getGroupInfo(TestUtil.any())).thenReturn(Observable.just(group))

        /* When */
        viewModel.getGroupInfo(groupId).subscribe(test)

        /* Then */
        test.assertValue { it == group }
    }

    @Test
    fun `Given a group Id - When call get member list  - Then return list users `() {
        /* Given */
        val test = TestObserver<List<User>>()
        val groupId = "groupId"
        val users = mutableListOf<User>()
        `when`(groupRepository.getMemberList(TestUtil.any())).thenReturn(Single.just(users))

        /* When */
        viewModel.getMemberList(groupId).subscribe(test)

        /* Then */
        test.assertValue { it == users }
    }

    @Test
    fun `Given a userId - When call leave group - Then return an user `() {
        /* Given */
        val test = TestObserver<User>()
        val userId = "userId"
        val user = User()
        `when`(groupRepository.removeUserFromGroup(TestUtil.any())).thenReturn(Single.just(user))

        /* When */
        viewModel.leaveGroup(userId).subscribe(test)

        /* Then */
        test.assertValue { it == user }
    }

    @Test
    fun `Given a groupId and newOwnerId - When call change group owner  - Then return true `() {
        /* Given */
        val test =  TestObserver<Boolean>()
        val groupId = "groupId"
        val newOwnerId = "newOwnerId"
        `when`(groupRepository.changeGroupOwner(TestUtil.any(),TestUtil.any())).thenReturn(Single.just(true))

        /* When */
        viewModel.changeGroupOwner(groupId, newOwnerId).subscribe(test)

        /* Then */
        test.assertValue(true)
    }

    @Test
    fun `Given a groupId and newOwnerId - When call change group owner  - Then return false `() {
        /* Given */
        val test =  TestObserver<Boolean>()
        val groupId = "groupId"
        val newOwnerId = "newOwnerId"
        `when`(groupRepository.changeGroupOwner(TestUtil.any(),TestUtil.any())).thenReturn(Single.just(false))

        /* When */
        viewModel.changeGroupOwner(groupId, newOwnerId).subscribe(test)

        /* Then */
        test.assertValue(false)
    }
}
