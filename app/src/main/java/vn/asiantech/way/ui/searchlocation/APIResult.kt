package vn.asiantech.way.ui.searchlocation

/**
 * APIResult.
 *
 * @author CuongCV
 */

class APIResult {
    var results: List<MyLocation>? = null

    constructor() {}

    constructor(myLocations: List<MyLocation>) {
        this.results = myLocations
    }
}
