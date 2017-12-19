package vn.asiantech.way.group.request

import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.ui.group.request.ShowRequestViewModel
import vn.asiantech.way.util.TestUtil

/**
 * ShowRequestViewModelTest
 * @author NgocTTN
 */
@Suppress("IllegalIdentifier")
class ShowRequestViewModelTest {

    @Mock
    private lateinit var wayRepository: WayRepository

    @Mock
    private lateinit var groupRepository: GroupRepository

    private lateinit var viewModel: ShowRequestViewModel

    @Before
    fun initTests() {
        MockitoAnnotations.initMocks(this)
        viewModel = ShowRequestViewModel(wayRepository, groupRepository)
    }

    @Test
    fun `Given a group Id - When call get requests of user - Then return an user`() {
        /* Given */
        val testUser = TestObserver<User>()
        val user = User()
        val groupId = "groupId"
        Mockito.`when`(groupRepository.getUserInfo(TestUtil.any())).thenReturn(Observable.just(user))

        /* When */
        groupRepository.getUserInfo(groupId).subscribe(testUser)

        /* Then */
        testUser.assertValue { it == user }
    }

    @Test
    fun `Given a group Id and user Id - When call add user to group - Then return an user`() {
        /* Given */
        val testUser = TestObserver<User>()
        val user = User()
        val groupId = "groupId"
        val userId = "userId"
        Mockito.`when`(wayRepository.addUserToGroup(TestUtil.any(), TestUtil.any())).thenReturn(Observable.just(user))

        /* When */
        viewModel.addUserToGroup(groupId, userId).subscribe(testUser)

        /* Then */
        testUser.assertValue { it == user }
    }

    @Test
    fun `Given a group Id and user Id - When call remove request in group - Then return true`() {
        /* Given */
        val test = TestObserver<Boolean>()
        val groupId = "groupId"
        val userId = "userId"
        Mockito.`when`(groupRepository.deleteGroupRequest(TestUtil.any(), TestUtil.any())).thenReturn(Single.just(true))

        /* When */
        viewModel.removeRequestInGroup(groupId, userId).subscribe(test)

        /* Then */
        test.assertValue(true)
    }


    @Test
    fun `Given a group Id and user Id - When call remove request in group - Then return false`() {
        /* Given */
        val test = TestObserver<Boolean>()
        val groupId = "groupId"
        val userId = "userId"
        Mockito.`when`(groupRepository.deleteGroupRequest(TestUtil.any(), TestUtil.any())).thenReturn(Single.just(false))

        /* When */
        viewModel.removeRequestInGroup(groupId, userId).subscribe(test)

        /* Then */
        test.assertValue(false)
    }

    @Test
    fun `Given progressDialog - When call add user to group - Then progress dialog should show then hide`() {
        /* Given */
        val test = TestObserver<Boolean>()
        val user = User()
        val groupId = "groupId"
        val userId = "userId"
        Mockito.`when`(wayRepository.addUserToGroup(TestUtil.any(), TestUtil.any())).thenReturn(Observable.just(user))

        /* When */
        viewModel.progressDialogObservable.subscribe(test)
        viewModel.addUserToGroup(groupId, userId).subscribe()

        /* Then */
        test.assertValues(true, false)
    }

    @Test
    fun `Given progressDialog  - When call remove request in group  - Then progress dialog should show then hide`() {
        /* Given */
        val test = TestObserver<Boolean>()
        val groupId = "groupId"
        val userId = "userId"
        Mockito.`when`(groupRepository.deleteGroupRequest(TestUtil.any(), TestUtil.any())).thenReturn(Single.just(true))

        /* When */
        viewModel.progressDialogObservable.subscribe(test)
        viewModel.removeRequestInGroup(groupId, userId).subscribe()

        /* Then */
        test.assertValues(true, false)
    }
}
