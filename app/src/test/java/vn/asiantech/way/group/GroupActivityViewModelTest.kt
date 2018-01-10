package vn.asiantech.way.group

import com.hypertrack.lib.models.User
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.ui.group.GroupActivityViewModel

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by datbuit. on 10/01/2018.
 */
@Suppress("IllegalIdentifier")
class GroupActivityViewModelTest {
    @Mock
    private lateinit var groupRepository: GroupRepository
    @Mock
    private lateinit var wayRepository: WayRepository

    private lateinit var viewModel: GroupActivityViewModel

    @Before
    fun initTests() {
        MockitoAnnotations.initMocks(this)
        viewModel = GroupActivityViewModel(groupRepository, wayRepository)
    }


    @Test
    fun `Given an user object - When get user - Then return right user object`() {
        /* Given */
        val user = User()
        val test = TestObserver<User>()
        `when`(wayRepository.getUser()).thenReturn(Single.just(user))

        /* When */
        wayRepository.getUser().subscribe(test)

        /* Then */
        test.assertValue { it == user }
    }


    @Test
    fun `Given user id - When listen for group change - Then return non group id`() {
        /* Given */
        val groupId = ""
        val userID = "436762c6-9ad3-4519-b8a4-61d22113a98e"
        val test = TestObserver<String>()
        `when`(groupRepository.listenerForGroupChange(userID)).thenReturn(Observable.just(groupId))

        /* When */
        groupRepository.listenerForGroupChange(userID).subscribe(test)

        /* Then */
        test.assertValue { it == groupId }
    }

    @Test
    fun `Given user id - When listen for group change - Then return group id`() {
        /* Given */
        val groupId = "bc659f54-6c2c-45c8-bb64-1a62b0af8ea0"
        val userID = "bc659f54-6c2c-45c8-bb64-1a62b0af8ea0"
        val test = TestObserver<String>()
        `when`(groupRepository.listenerForGroupChange(userID)).thenReturn(Observable.just(groupId))

        /* When */
        groupRepository.listenerForGroupChange(userID).subscribe(test)

        /* Then */
        test.assertValue { it == groupId }
    }
}
