package martin.grabarz.chat_2022

// import okhttp3.logging.HttpLoggingInterceptor.setLevel
// import okhttp3.OkHttpClient.Builder.addInterceptor
// import okhttp3.OkHttpClient.Builder.build
import android.support.test.runner.AndroidJUnit4
import android.support.test.InstrumentationRegistry
import com.google.gson.annotations.SerializedName
import com.example.chat_2022_eleves.Enseignant
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.example.chat_2022_eleves.APIClient
import retrofit2.converter.gson.GsonConverterFactory
import com.example.chat_2022_eleves.GlobalState
import android.widget.Toast
import kotlin.Throws
import android.net.ConnectivityManager
import android.net.NetworkInfo
import retrofit2.http.GET
import com.example.chat_2022_eleves.ListConversations
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android.widget.CheckBox
import android.content.SharedPreferences
import android.os.AsyncTask
import org.json.JSONObject
import org.json.JSONException
import com.example.chat_2022_eleves.Promo
import org.json.JSONArray
import android.os.Bundle
import com.example.chat_2022_eleves.R
import android.preference.PreferenceManager
import android.content.Intent
import com.example.chat_2022_eleves.SettingsActivity
import com.example.chat_2022_eleves.ChoixConvActivity
import com.example.chat_2022_eleves.LoginActivity.PostAsyncTask
import android.preference.PreferenceActivity
import android.widget.Spinner
import com.example.chat_2022_eleves.Conversation
import android.widget.ArrayAdapter
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView
import com.example.chat_2022_eleves.APIInterface
import com.example.chat_2022_eleves.ChoixConvActivity.MyCustomAdapter
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        Assert.assertEquals(4, (2 + 2).toLong())
    }
}