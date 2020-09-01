package com.colin.go.bean

/**
 * create by colin
 * 2020/8/19
 */
data class UIResult<T>(private val status: Int = statusLoading) {

    var errorCode: Int = 0

    var errorMsg: String = ""

    var content: T? = null

    constructor(errorCode: Int, errorMsg: String) : this(statusError) {
        this.errorCode = errorCode
        this.errorMsg = errorMsg
    }

    constructor(content: T?) : this(statusContent) {
        this.content = content
    }


    fun isLoading() = status == statusLoading

    fun isContentState(): Boolean {
        return (status == statusContent) && (content != null)
    }

    fun isError() = status == statusError || content == null

    companion object {

        private const val statusLoading = 1
        private const val statusError = 2
        private const val statusContent = 3

        fun <T> loading(): UIResult<T> {
            return UIResult(statusLoading)
        }

        fun <T> error(code: Int, msg: String): UIResult<T> {
            return UIResult(code, msg)
        }

        fun <T> content(content: T): UIResult<T> {
            return UIResult(content)
        }
    }
}