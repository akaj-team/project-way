package vn.asiantech.way

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import vn.asiantech.way.data.model.Invite
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.ui.group.showinvite.ViewInviteViewModel

/**
 * ViewInviteViewModelTest.
 *
 * @author at-ToanNguyen
 */
@Suppress("IllegalIdentifier")
class ViewInviteViewModelTest {
    @Mock
    private lateinit var groupRepository: GroupRepository
    private lateinit var viewModel: ViewInviteViewModel
    private lateinit var userId: String
    @Before
    fun initTest() {
        userId = ""
        MockitoAnnotations.initMocks(this)
        viewModel = ViewInviteViewModel(userId, groupRepository)
    }

    @Test
    fun `Given invites of user - When call get invites of user - then return right invite`() {
        val inviteTest = TestObserver<Invite>()
        val invite = Invite("", "", "", true)
        `when`(groupRepository.getInvite(TestUtil.any())).thenReturn(Observable.just(invite))
        viewModel.getInvitesOfUser().subscribe(inviteTest)
        inviteTest.assertValue { it == invite }
    }

    @Test
    fun `Given remove invite user - When call remove invite user from group - then return progressbar show then hide`() {
        val statusTest = TestObserver<Boolean>()
        val invite = Invite("", "", "", true)
        `when`(groupRepository.deleteUserInvite(TestUtil.any(), TestUtil.any())).thenReturn(Single.just(true))
        viewModel.progressDialogObservable.subscribe(statusTest)
        viewModel.removeInviteUserFromGroup(invite).subscribe()
        statusTest.assertValues(true, false)
    }

    @Test
    fun `Given accept invite user - When call accept invite - then return progressbar show then hide`() {
        val statusTest = TestObserver<Boolean>()
        val invite = Invite("", "", "", true)
        `when`(groupRepository.acceptInvite(TestUtil.any(), TestUtil.any())).thenReturn(Single.just(true))
        viewModel.progressDialogObservable.subscribe(statusTest)
        viewModel.acceptInvite(invite).subscribe()
        statusTest.assertValues(true, false)
    }
}