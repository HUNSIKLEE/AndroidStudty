import android.net.Uri

data class ProfileData(
    val name: String,
    val age: String,
    val email: String,
    var imageUri: String? = null,
    val img: Int = 0
)