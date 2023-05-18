import android.net.Uri

data class ProfileData(
    val name: String,
    val age: String,
    val email: String,
    val img: Int,
    var imageUri: Uri? = null // Add this property to hold the image URI
)