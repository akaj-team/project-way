package vn.asiantech.way.ui.group

import android.content.Context
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import vn.asiantech.way.data.source.GroupRepository
import vn.asiantech.way.data.source.WayRepository
import vn.asiantech.way.extension.observeOnUiThread

/**
 * CreateGroupViewModel.
 *
 * @author at-ToanNguyen
 */
class CreateGroupViewModel(val context: Context) {
    private val wayRepository = WayRepository()

    internal fun createGroup(name: String): Observable<Boolean> {
        val result = PublishSubject.create<Boolean>()
        wayRepository.createGroup(name).observeOnUiThread().subscribe {
            GroupRepository().upGroupInfo(it)
                    .observeOnUiThread()
                    .subscribe({
                        result.onNext(it)
                    }, {
                        result.onError(Throwable(it))
                    })
        }
        return result
    }
}
