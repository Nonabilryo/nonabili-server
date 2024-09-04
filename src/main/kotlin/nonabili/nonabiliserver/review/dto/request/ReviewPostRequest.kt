package nonabili.nonabiliserver.review.dto.request

data class ReviewPostRequest(
    val rating: Float,
    val title: String,
    val description: String,
) {}