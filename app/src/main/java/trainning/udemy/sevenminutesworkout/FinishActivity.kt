package trainning.udemy.sevenminutesworkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class FinishActivity : AppCompatActivity()
{

  override fun onCreate(savedInstanceState : Bundle?)
  {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_finish)
  }

  @Suppress("UNUSED_PARAMETER")
  fun onFinish(view : View)
  {
    Intent(this, MainActivity::class.java).apply {
      addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      startActivity(this)
    }
    finish()
  }
}
