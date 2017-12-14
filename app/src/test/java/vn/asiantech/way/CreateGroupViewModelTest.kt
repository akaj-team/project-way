package vn.asiantech.way

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
    fun `Given name and userId - When get name and userId - Then return true`() {
        /* Given */
        val name = "name"
        val userId = "userid"
        val test = TestObserver<Boolean>()

        /* When */
        `when`(groupRepository.createGroup(name, userId)).thenReturn(Single.just(true))

        /* Then */
        viewModel.createGroup(name, userId).subscribe(test)
        test.assertValue { it == true }
    }

    @Test
    fun `Given name and userId - When get name and userId - Then return false`() {
        /* Given */
        val name = ""
        val userId = ""
        val test = TestObserver<Boolean>()

        /* When */
        `when`(groupRepository.createGroup(name, userId)).thenReturn(Single.just(false))

        /* Then */
        viewModel.createGroup(name, userId).subscribe(test)
        test.assertValue { it == false }
    }

    @Test
    fun `Given valid groupName and userId - When get groupName and userId - Then return true`() {
        /* Given */
        val groupName = "Friends Group"
        val userId = "user 01"
        val test = TestObserver<Boolean>()

        /* When */
        `when`(groupRepository.createGroup(groupName, userId)).thenReturn(Single.just(true))

        /* Then */
        viewModel.createGroup(groupName, userId).subscribe(test)
        test.assertValue { it == true }
    }

    @Test
    fun `Given invalid groupName and userId - When get groupName and userId - Then return false`() {
        /* Given */
        val groupName = "@$%%"
        val userId = "5#@4"
        val test = TestObserver<Boolean>()

        /* When */
        `when`(groupRepository.createGroup(groupName, userId)).thenReturn(Single.just(false))

        /* Then */
        viewModel.createGroup(groupName, userId).subscribe(test)
        test.assertValue { it == false }
    }
}
