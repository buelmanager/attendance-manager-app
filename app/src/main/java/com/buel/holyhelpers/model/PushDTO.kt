package com.buel.holyhelpers.model

data class PushDTO(
    var to: String? = null,
    var notification: Notification? = Notification(),
    var data: Data? = Data()

) {
    data class Notification(
            var body: String? = null,
            var title: String? = null
    )

    data class Data(
            var body: String? = null,
            var title: String? = null
    )
}
