package nonabili.nonabiliserver.common.util.error

import org.springframework.http.HttpStatus

enum class ErrorState(val status: HttpStatus = HttpStatus.OK, val message: String) {

    ERROR_FORMAT(HttpStatus.BAD_REQUEST, "It's test"),
    ACCESSTOKEN_HAS_EXPIRED(HttpStatus.BAD_REQUEST, "AccessToken has expired"),
    SERVER_UNAVAILABLE(HttpStatus.INTERNAL_SERVER_ERROR, "Server is unavailable"),
    UNAUTHENTICATED_PAYMENT(HttpStatus.BAD_REQUEST, "Payment is wrong"),

    ID_IS_ALREADY_USED(HttpStatus.BAD_REQUEST, "Id is already used"),
    NAME_IS_ALREADY_USED(HttpStatus.BAD_REQUEST, "Name is already used"),
    TELL_IS_ALREADY_USED(HttpStatus.BAD_REQUEST, "Tell is already used"),
    EMAIL_IS_ALREADY_USED(HttpStatus.BAD_REQUEST, "Email is already used"),

    DIFFERENT_USER(HttpStatus.FORBIDDEN, "User is not ower"),


    NOT_FOUND_ID(HttpStatus.NOT_FOUND, "Not found Id"),
    NOT_FOUND_IDX(HttpStatus.NOT_FOUND, "Not found Idx"),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "Not found user"),
    NOT_FOUND_FOLLOW(HttpStatus.NOT_FOUND, "Not Found Follow"),
    NOT_FOUND_ARTICLE(HttpStatus.NOT_FOUND, "Not found Article"),
    NOT_FOUND_LIKE(HttpStatus.NOT_FOUND, "Not found Like"),
    NOT_FOUND_REFRESHTOKEN(HttpStatus.NOT_FOUND, "Not found RefreshToken"),
    NOT_FOUDN_CHAT(HttpStatus.NOT_FOUND, "Not found chat"),
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "Not found order"),
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "Not found review"),
    NOT_FOUND_STATUS(HttpStatus.NOT_FOUND, "Not found status"),

    EMPTY_REQUEST(HttpStatus.BAD_REQUEST, "Request is empty"),
    CANT_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, "I don't know this error, maybe "),
    ILLEGAL_ARTICLE(HttpStatus.BAD_REQUEST, "Article is not legal"),
    ILLEGAL_RENTALTYPE(HttpStatus.BAD_REQUEST, "Rentaltype is not legal"),

    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "Wrong password"),
    WRONG_EMAILVERIFYCODE(HttpStatus.BAD_REQUEST, "Wrong emailVerifyCode"),
    WRONG_DESTINATION(HttpStatus.BAD_REQUEST, "Wrong destination"),

    NOT_VERIFED_EMAIL(HttpStatus.BAD_REQUEST, "Email not verifed"),
    NOT_VERIFED_TELL(HttpStatus.BAD_REQUEST, "Tell not verifed"),

    AREADY_FOLLOWED(HttpStatus.BAD_REQUEST, "Aready followed"),
    AREADY_LIKED(HttpStatus.BAD_REQUEST, "Aready liked")
}