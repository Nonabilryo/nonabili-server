package nonabili.nonabiliserver.common.util.error

class CustomError(val reason: ErrorState): RuntimeException(reason.message)