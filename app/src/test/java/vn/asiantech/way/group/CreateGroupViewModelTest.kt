package vn.asiantech.way.group

import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.ui.group.create.CreateGroupViewModel

/**
 *
 * Created by haolek on 13/12/2017.
 */
@Suppress("IllegalIdentifier")
class CreateGroupViewModelTest {
    @Mock
    private lateinit var groupRepository: GroupRepository

    private lateinit var viewModel: CreateGroupViewModel

    @Before
    fun initTests() {
        MockitoAnnotations.initMocks(this)
        viewModel = CreateGroupViewModel(groupRepository)
    }

    @Test
    fun `Given valid groupName and valid userId - When create group - Then return true`() {
        /* Given */
        val groupName = "name"
        val userId = "userid"
        val test = TestObserver<Boolean>()
        `when`(groupRepository.createGroup(groupName, userId)).thenReturn(Single.just(true))

        /* When */
        viewModel.createGroup(groupName, userId).subscribe(test)

        /* Then */
        test.assertValue { it }
    }

    @Test
    fun `Given valid groupName and invalid userId - When create group - Then return false`() {
        /* Given */
        val groupName = "name"
        val userId = ""
        val test = TestObserver<Boolean>()
        `when`(groupRepository.createGroup(groupName, userId)).thenReturn(Single.just(false))

        /* When */
        viewModel.createGroup(groupName, userId).subscribe(test)

        /* Then */
        test.assertValue { it == false }
    }

    @Test
    fun `Given invalid groupName and valid userId - When create group - Then return false`() {
        /* Given */
        val groupName = ""
        val userId = "user 01"
        val test = TestObserver<Boolean>()
        `when`(groupRepository.createGroup(groupName, userId)).thenReturn(Single.just(false))

        /* When */
        viewModel.createGroup(groupName, userId).subscribe(test)

        /* Then */
        test.assertValue { it == false }
    }

    @Test
    fun `Given invalid groupName and invalid userId - When create group - Then return false`() {
        /* Given */
        val groupName = ""
        val userId = ""
        val test = TestObserver<Boolean>()
        `when`(groupRepository.createGroup(groupName, userId)).thenReturn(Single.just(false))

        /* When */
        viewModel.createGroup(groupName, userId).subscribe(test)

        /* Then */
        test.assertValue { it == false }
    }
}
