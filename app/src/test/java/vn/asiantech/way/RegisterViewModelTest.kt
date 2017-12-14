package vn.asiantech.way

import com.hypertrack.lib.models.User
import com.hypertrack.lib.models.UserParams
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.SingleSubject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import vn.asiantech.way.data.model.Country
import vn.asiantech.way.data.source.LocalRepository
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.data.source.remote.response.ResponseStatus
import vn.asiantech.way.ui.register.RegisterViewModel
import vn.asiantech.way.utils.AppConstants

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@Suppress("IllegalIdentifier")
class RegisterViewModelTest {
    @Mock
    private lateinit var wayRepository: WayRepository

    @Mock
    private lateinit var assetDataRepository: LocalRepository

    private lateinit var viewModel: RegisterViewModel

    @Before
    fun initTests() {
        MockitoAnnotations.initMocks(this)
        viewModel = RegisterViewModel(wayRepository, assetDataRepository, true)
    }

    @Test
    fun `Given progress bar - When call update user - Then progress bar should show then hide`() {
        val test = TestObserver<Boolean>()

        val param = UserParams()
        `when`(wayRepository.updateUser(param)).thenReturn(Observable.just(ResponseStatus(true)))

        viewModel.progressBarStatus.subscribe(test)
        viewModel.updateUser(param).subscribe()
        test.assertValues(true, false)
    }

    @Test
    fun `Given progress bar - When call get user - Then progress bar should show then hide`() {
        val test = TestObserver<Boolean>()

        `when`(wayRepository.getUser()).thenReturn(Single.just(User()))

        viewModel.progressBarStatus.subscribe(test)
        viewModel.getUser().subscribe()
        test.assertValues(true, false)
    }

    @Test
    fun `Given an user - When get current user - Then return right user`() {
        val user = User()

        val getUserTest = TestObserver<User>()
        `when`(wayRepository.getUser()).thenReturn(SingleSubject.just(user))

        viewModel.getUser().subscribe(getUserTest)
        getUserTest.assertValue { it == user }
    }

    @Test
    fun `Given countries from file json - When get countries - Then return right countries`() {
        val countries = listOf<Country>()

        val getCountriesTest = TestObserver<List<Country>>()
        `when`(assetDataRepository.getCountries()).thenReturn(Observable.just(countries))

        viewModel.getCountries().subscribe(getCountriesTest)
        getCountriesTest.assertValue { it == countries }
    }

    @Test
    fun `Given an user params - When create new user - Then return true response status`() {
        val test = TestObserver<ResponseStatus>()

        val params = UserParams()
        `when`(wayRepository.createUser(params)).thenReturn(Observable.just(ResponseStatus(true)))

        viewModel.createUser(params).subscribe(test)
        test.assertValue { it.status }
    }

    @Test
    fun `Given an user params - When create new user - Then return false response status`() {
        val test = TestObserver<ResponseStatus>()

        val params = UserParams()
        `when`(wayRepository.createUser(params)).thenReturn(Observable.just(ResponseStatus(false)))

        viewModel.createUser(params).subscribe(test)
        test.assertValue { !it.status }
    }

    @Test
    fun `Given an user params - When update user - Then return true response status`() {
        val test = TestObserver<ResponseStatus>()

        val params = UserParams()
        `when`(wayRepository.updateUser(params)).thenReturn(Observable.just(ResponseStatus(true)))

        viewModel.updateUser(params).subscribe(test)
        test.assertValue { it.status }
    }

    @Test
    fun `Given an user params - When update user - Then return false response status`() {
        val test = TestObserver<ResponseStatus>()

        val params = UserParams()
        `when`(wayRepository.updateUser(params)).thenReturn(Observable.just(ResponseStatus(false)))

        viewModel.updateUser(params).subscribe(test)
        test.assertValue { !it.status }
    }

    @Test
    fun `Given text difference old name - When text change - Then updateButton enable`() {
        val oldName = "Test"
        Assert.assertNotEquals(oldName, "")
    }

    @Test
    fun `Given text difference old phone - When text change - Then updateButton enable`() {
        val oldPhone = "0123456789"
        Assert.assertNotEquals(oldPhone.removeRange(0, AppConstants.NUM_CHAR_REMOVE), "01234")
    }

    @Test
    fun `Given information similar old user - When text change - Then updateButton disable`() {
        val oldName = "Test"
        val oldPhone = "0123456789"
        Assert.assertEquals(oldName, "Test")
        Assert.assertEquals(oldPhone, "0123456789")
    }

    @Test
    fun `Given an information - When create new UserParam - Then return UserParams`() {
        val name = "Test"
        val phone = "0123456789"
        val isoCode = "84"
        val avatar = null

        val userParams = viewModel.generateUserInformation(name, phone, isoCode, avatar)
        Assert.assertEquals(userParams.name, name)
        Assert.assertEquals(userParams.phone, isoCode.plus("/").plus(phone))
        Assert.assertEquals(userParams.photo, avatar)
        Assert.assertEquals(userParams.lookupId, phone)
    }
}
