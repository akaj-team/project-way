package vn.asiantech.way.invite

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import vn.asiantech.way.util.TestUtil
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
    @Before
    fun initTest() {
        MockitoAnnotations.initMocks(this)
        viewModel = ViewInviteViewModel("", groupRepository)
    }

    @Test
    fun `Given invites of user - When call get invites of user - then return right invite`() {
        /* Given */
        val inviteTest = TestObserver<Invite>()
        val invite = Invite("from", "to", "groupName", true)
        `when`(groupRepository.getInvite(TestUtil.any())).thenReturn(Observable.just(invite))

        /* When */
        viewModel.getInvitesOfUser().subscribe(inviteTest)

        /* Then */
        inviteTest.assertValue { it == invite }
    }

    @Test
    fun `Given a invite - When call remove invite user from group - then return progressbar show then hide`() {
        /* Given */
        val statusTest = TestObserver<Boolean>()
        val invite = Invite("from", "to", "groupName", true)
        `when`(groupRepository.deleteUserInvite(TestUtil.any(), TestUtil.any())).thenReturn(Single.just(true))

        /* When */
        viewModel.progressDialogObservable.subscribe(statusTest)
        viewModel.removeInviteUserFromGroup(invite).subscribe()

        /* Then */
        statusTest.assertValues(true, false)
    }

    @Test
    fun `Given a invite user - When call accept invite - then return progressbar show then hide`() {
        /* Given */
        val statusTest = TestObserver<Boolean>()
        val invite = Invite("from", "to", "groupName", true)
        `when`(groupRepository.acceptInvite(TestUtil.any(), TestUtil.any())).thenReturn(Single.just(true))

        /* When */
        viewModel.progressDialogObservable.subscribe(statusTest)
        viewModel.acceptInvite(invite).subscribe()

        /* Then */
        statusTest.assertValues(true, false)
    }
}
