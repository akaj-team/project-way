package vn.asiantech.way.ui.test.recent

import vn.asiantech.way.R

/**
 * Copyright © 2017 Asian Tech Co., Ltd.
 * Created by cuongcaov on 21/12/2017
 */
class RecentSearchFragmentViewModel {
    internal val recentSearches = mutableListOf<Any>()

    init {
        recentSearches.add(HeaderModel(R.string.text_search_recent_header))
        recentSearches.add(RecentModel("Lưu Diệc Phi", "Thần tiên tỷ tỷ", "http://sohanews.sohacdn.com/2017/2-1492136563590.jpg"))
        recentSearches.add(RecentModel("Lưu Diệc Phi", "Thần tiên tỷ tỷ", "http://sohanews.sohacdn.com/2017/2-1492136563590.jpg"))
        recentSearches.add(RecentModel("Lưu Diệc Phi", "Thần tiên tỷ t", "http://sohanews.sohacdn.com/2017/2-1492136563590.jpg"))
        recentSearches.add(HeaderModel(R.string.text_search_popular_header))
        recentSearches.add(PopularModel("Lưu Diệc Phi", "http://sohanews.sohacdn.com/2017/2-1492136563590.jpg"))
        recentSearches.add(PopularModel("Lưu Diệc Phi", "http://sohanews.sohacdn.com/2017/2-1492136563590.jpg"))
        recentSearches.add(PopularModel("Lưu Diệc Phi", "http://sohanews.sohacdn.com/2017/2-1492136563590.jpg"))
    }
}
