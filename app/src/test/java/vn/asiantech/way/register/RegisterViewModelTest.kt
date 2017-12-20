package vn.asiantech.way.register

import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import com.hypertrack.lib.models.User
import com.hypertrack.lib.models.UserParams
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.SingleSubject
import org.hamcrest.Matchers.`is`
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
import vn.asiantech.way.util.TestUtil

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
        /* Given */
        val test = TestObserver<Boolean>()
        val param = UserParams()
        `when`(wayRepository.updateUser(TestUtil.any())).thenReturn(Observable.just(ResponseStatus(true)))

        /* When */
        viewModel.progressBarStatus.subscribe(test)
        viewModel.updateUser(param).subscribe()

        /* Then */
        test.assertValues(true, false)
    }

    @Test
    fun `Given progress bar - When call get user - Then progress bar should show then hide`() {
        /* Given */
        val test = TestObserver<Boolean>()
        `when`(wayRepository.getUser()).thenReturn(Single.just(User()))

        /* When */
        viewModel.progressBarStatus.subscribe(test)
        viewModel.getUser().subscribe()

        /* Then */
        test.assertValues(true, false)
    }

    @Test
    fun `Given an user - When get current user - Then return right user`() {
        /* Given */
        val user = User()
        val getUserTest = TestObserver<User>()
        `when`(wayRepository.getUser()).thenReturn(SingleSubject.just(user))

        /* When */
        viewModel.getUser().subscribe(getUserTest)

        /* Then */
        getUserTest.assertValue { it == user }
    }

    @Test
    fun `Given null old countries - When get countries - Then return right diff`() {
        /* Given */
        val countries = listOf(Country("84", "12345"), Country("12", "123456789"))
        val getDiffCountriesTest = TestObserver<DiffUtil.DiffResult>()
        `when`(assetDataRepository.getCountries()).thenReturn(Observable.just(countries))

        /* When */
        viewModel.getCountries().subscribe(getDiffCountriesTest)

        /* Then */
        getDiffCountriesTest.assertValue {
            it.dispatchUpdatesTo(object : ListUpdateCallback {
                override fun onChanged(position: Int, count: Int, payload: Any?) {
                    // No-op
                }

                override fun onMoved(fromPosition: Int, toPosition: Int) {
                    // No-op
                }

                override fun onInserted(position: Int, count: Int) {
                    Assert.assertThat(position, `is`(0))
                    Assert.assertThat(count, `is`(2))
                }

                override fun onRemoved(position: Int, count: Int) {
                    // No-op
                }
            })
            true
        }
        Assert.assertThat(viewModel.countries.size, `is`(2))
        Assert.assertThat(viewModel.countries[0].iso, `is`("84"))
        Assert.assertThat(viewModel.countries[0].tel, `is`("12345"))
        Assert.assertThat(viewModel.countries[1].iso, `is`("12"))
        Assert.assertThat(viewModel.countries[1].tel, `is`("123456789"))
    }

    @Test
    fun `Given old countries already value - When get countries - Then return right diff`() {
        /* Given */
        val newCountries = listOf(Country("12", "1234567"), Country("45", "012"))
        val getDiffCountriesTest = TestObserver<DiffUtil.DiffResult>()
        viewModel.countries.addAll(listOf(Country("84", "12345")))
        `when`(assetDataRepository.getCountries()).thenReturn(Observable.just(newCountries))

        /* When */
        viewModel.getCountries().subscribe(getDiffCountriesTest)

        /* Then */
        getDiffCountriesTest.assertValue {
            it.dispatchUpdatesTo(object : ListUpdateCallback {
                override fun onChanged(position: Int, count: Int, payload: Any?) {
                    // No-op
                }

                override fun onMoved(fromPosition: Int, toPosition: Int) {
                    // No-op
                }

                override fun onInserted(position: Int, count: Int) {
                    Assert.assertThat(position, `is`(0))
                    Assert.assertThat(count, `is`(2))
                }

                override fun onRemoved(position: Int, count: Int) {
                    Assert.assertThat(position, `is`(0))
                    Assert.assertThat(count, `is`(1))
                }
            })
            true
        }
        Assert.assertThat(viewModel.countries.size, `is`(2))
        Assert.assertThat(viewModel.countries[0].iso, `is`("12"))
        Assert.assertThat(viewModel.countries[0].tel, `is`("1234567"))
        Assert.assertThat(viewModel.countries[1].iso, `is`("45"))
        Assert.assertThat(viewModel.countries[1].tel, `is`("012"))
    }

    @Test
    fun `Given old  size bigger new countries - When get countries - Then return right diff`() {
        /* Given */
        val newCountries = listOf(Country("12", "1234567"), Country("45", "012"))
        val getDiffCountriesTest = TestObserver<DiffUtil.DiffResult>()
        viewModel.countries.addAll(listOf(Country("45", "012"), Country("84", "0976545507"),Country("12", "1234567")))
        `when`(assetDataRepository.getCountries()).thenReturn(Observable.just(newCountries))

        /* When */
        viewModel.getCountries().subscribe(getDiffCountriesTest)

        /* Then */
        getDiffCountriesTest.assertValue {
            it.dispatchUpdatesTo(object : ListUpdateCallback {
                override fun onChanged(position: Int, count: Int, payload: Any?) {
                    // No-op
                }

                override fun onMoved(fromPosition: Int, toPosition: Int) {
                    Assert.assertThat(fromPosition, `is`(1))
                    Assert.assertThat(toPosition, `is`(0))
                }

                override fun onInserted(position: Int, count: Int) {
                    // No-op
                }

                override fun onRemoved(position: Int, count: Int) {
                    Assert.assertThat(position, `is`(1))
                    Assert.assertThat(count, `is`(1))
                }
            })
            true
        }
        Assert.assertThat(viewModel.countries.size, `is`(2))
        Assert.assertThat(viewModel.countries[0].iso, `is`("12"))
        Assert.assertThat(viewModel.countries[0].tel, `is`("1234567"))
        Assert.assertThat(viewModel.countries[1].iso, `is`("45"))
        Assert.assertThat(viewModel.countries[1].tel, `is`("012"))
    }

    @Test
    fun `Given new countries similar id but difference value - When get countries - Then return right diff`() {
        /* Given */
        val newCountries = listOf(Country("12", "1234567"), Country("45", "012"))
        val getDiffCountriesTest = TestObserver<DiffUtil.DiffResult>()
        viewModel.countries.addAll(listOf(Country("12", "01234"), Country("84", "12345") , Country("01", "0976545507")))
        `when`(assetDataRepository.getCountries()).thenReturn(Observable.just(newCountries))

        /* When */
        viewModel.getCountries().subscribe(getDiffCountriesTest)

        /* Then */
        getDiffCountriesTest.assertValue {
            it.dispatchUpdatesTo(object : ListUpdateCallback {
                override fun onChanged(position: Int, count: Int, payload: Any?) {
                    Assert.assertThat(position, `is`(0))
                    Assert.assertThat(count, `is`(1))
                }

                override fun onMoved(fromPosition: Int, toPosition: Int) {
                    // No-op
                }

                override fun onInserted(position: Int, count: Int) {
                    Assert.assertThat(position, `is`(1))
                    Assert.assertThat(count, `is`(1))
                }

                override fun onRemoved(position: Int, count: Int) {
                    Assert.assertThat(position, `is`(1))
                    Assert.assertThat(count, `is`(2))
                }
            })
            true
        }
        Assert.assertThat(viewModel.countries.size, `is`(2))
        Assert.assertThat(viewModel.countries[0].iso, `is`("12"))
        Assert.assertThat(viewModel.countries[0].tel, `is`("1234567"))
        Assert.assertThat(viewModel.countries[1].iso, `is`("45"))
        Assert.assertThat(viewModel.countries[1].tel, `is`("012"))
    }

    @Test
    fun `Given an user params - When create new user - Then return true response status`() {
        /* Given */
        val test = TestObserver<ResponseStatus>()
        val params = UserParams()
        `when`(wayRepository.createUser(TestUtil.any())).thenReturn(Observable.just(ResponseStatus(true)))

        /* When */
        viewModel.createUser(params).subscribe(test)

        /* Then */
        test.assertValue { true }
    }

    @Test
    fun `Given an user params - When update user - Then return true response status`() {
        /* Given */
        val test = TestObserver<ResponseStatus>()
        val params = UserParams()
        `when`(wayRepository.updateUser(TestUtil.any())).thenReturn(Observable.just(ResponseStatus(true)))

        /* When */
        viewModel.updateUser(params).subscribe(test)

        /* Then */
        test.assertValue { true }
    }

    @Test
    fun `Given an information - When create new UserParam - Then return UserParams`() {
        /* Given */
        val name = "Name"
        val phone = "0123456789"
        val isoCode = "+84"
        val avatar = null

        /* When */
        val userParams = viewModel.generateUserInformation(name, phone, isoCode, avatar)

        /* Then */
        Assert.assertEquals(userParams.name, name)
        Assert.assertEquals(userParams.phone, isoCode.plus("/").plus(phone))
        Assert.assertEquals(userParams.photo, avatar)
        Assert.assertEquals(userParams.lookupId, phone)
    }
}
