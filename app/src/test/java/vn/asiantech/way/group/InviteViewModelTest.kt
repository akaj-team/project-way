package vn.asiantech.way.group

import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import vn.asiantech.way.TestUtil
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.ui.group.invite.InviteViewModel

/**
 * Invite View Model Test
 * Created by haolek on 14/12/2017.
 */
@Suppress("IllegalIdentifier")
class InviteViewModelTest {
    @Mock
    private lateinit var groupRepository: GroupRepository

    private lateinit var viewModel: InviteViewModel

    @Before
    fun initTests() {
        MockitoAnnotations.initMocks(this)
        viewModel = InviteViewModel(groupRepository)
    }

    @Test
    fun `Given a name - When call trigger search list user - Then return list user`() {
        /* Given */
        val name = ""
        val users = mutableListOf<User>()
        val test = TestObserver<List<User>>()
        `when`(groupRepository.searchUser(TestUtil.any())).thenReturn(Observable.just(users))

        /* When */
        groupRepository.searchUser(name).subscribe(test)
        viewModel.triggerSearchListUser().subscribe(test)

        /* Then */
        test.assertValue { it == users }
    }
}
