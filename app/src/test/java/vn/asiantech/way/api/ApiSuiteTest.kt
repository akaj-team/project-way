package vn.asiantech.way.api

import okhttp3.mockwebserver.MockWebServer
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Created by tien.hoang on 12/21/17.
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(
        SearchUserTest::class,
        GoogleApiTest::class,
        GetUserInfoTest::class,
        GetMemberListTest::class,
        AddUserToGroupTest::class,
        SearchGroupTest::class,
        RemoveUserFromGroupTest::class
)
class ApiSuiteTest {
    companion object {
        val server = MockWebServer()

        @BeforeClass
        fun beforeClass() {
            server.start()
        }

        @AfterClass
        fun afterClass() {
            server.shutdown()
        }
    }

}