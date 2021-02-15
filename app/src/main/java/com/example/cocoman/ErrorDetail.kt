package com.example.cocoman

enum class ErrorDetail(detailCode: String) {
    INVALID_USER_ID("4000") {
        override fun getMessage() = "없는 아이디 입니다"
    },
    INVALID_PASSWORD("4001") {
        override fun getMessage() = "비밀번호가 잘못됐습니다"
    },
    NO_PARAMETER("4002") {
        override fun getMessage() = "필수 파라미터가 필요합니다"
    },
    INVALID_PARAMETER("4003") {
        override fun getMessage() = "필수 파라미터의 값이 잘못됐습니다"
    },
    NO_DATA("4004") {
        override fun getMessage() = "데이터가 없습니다"
    },
    INTERNAL_SERVER_ERROR("5000") {
        override fun getMessage() = "서버 오류 입니다"
    },
    SOCIAL_SERVER_ERROR("5001") {
        override fun getMessage() = "로그인 서버 오류입니다"
    },
    UNKNOWN_ERROR("-1") {
        override fun getMessage() = "알 수 없는 에러입니다"
    }
    ;

    abstract fun getMessage(): String

}