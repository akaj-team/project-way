package vn.asiantech.way.group

import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.ui.group.invite.InviteViewModel
import vn.asiantech.way.util.RxSchedulersOverrideRule
import vn.asiantech.way.util.TestUtil

/**
 * Invite View Model Test
 * Created by haolek on 14/12/2017.
 */
@Suppress("IllegalIdentifier")
class InviteViewModelTest {
    @Mock
    private lateinit var groupRepository: GroupRepository

    private lateinit var viewModel: InviteViewModel

    @get:Rule
    val rule = RxSchedulersOverrideRule()

    @Before
    fun initTests() {
        MockitoAnnotations.initMocks(this)
        viewModel = InviteViewModel(groupRepository)
    }

    @Test
    fun `Given a name - When call trigger search list user - Then return list user`() {
        /* Given */
        val name = "hoa"
        val users = mutableListOf<User>(User(), User())
        val updateUserListObservable = TestObserver<DiffUtil.DiffResult>()
        `when`(groupRepository.searchUser(TestUtil.any())).thenReturn(Observable.just(users))

        /* When */
        viewModel.updateAutocompleteList.subscribe(updateUserListObservable)
        viewModel.searchListUser(name)

        /* Then */
        updateUserListObservable.assertValue {
            it.dispatchUpdatesTo(object : ListUpdateCallback {
                override fun onChanged(position: Int, count: Int, payload: Any?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onMoved(fromPosition: Int, toPosition: Int) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onInserted(position: Int, count: Int) {
                    assertThat(count, `is`(2))
                    assertThat(position, `is`(0))
                }

                override fun onRemoved(position: Int, count: Int) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
            true
        }
        Assert.assertThat(viewModel.users.size, `is`(2))
    }
}
