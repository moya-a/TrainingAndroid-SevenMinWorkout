package trainning.udemy.sevenminutesworkout

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity()
{
  private val LOGTAG : String by lazy { "in_class_${this::class.simpleName}" }

  companion object
  {
    const val ACTIVITY_EXERCISE : Int = 0
    const val ACTIVITY_FINISH : Int = 1
  }

  override fun onCreate(savedInstanceState : Bundle?)
  {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  @Suppress("UNUSED_PARAMETER")
  fun startExercise(view : View)
  {
    Intent(this, ExerciseActivity::class.java).apply {
      startActivityForResult(this, ACTIVITY_EXERCISE)
    }
  }

  override fun onActivityResult(requestCode : Int, resultCode : Int, data : Intent?)
  {
    super.onActivityResult(requestCode, resultCode, data)
    when (requestCode)
    {
      ACTIVITY_EXERCISE -> onExerciseActivityResult(resultCode, data)
      ACTIVITY_FINISH   -> onFinishActivityResult(resultCode, data)
      else              -> Log.d(LOGTAG, "Unknown Activity finished")
    }
  }

  @Suppress("UNUSED_PARAMETER")
  private fun onFinishActivityResult(resultCode : Int, data : Intent?)
  {
    when (resultCode)
    {
      Activity.RESULT_OK       -> Log.d(LOGTAG, "FinishActivity OK")
      Activity.RESULT_CANCELED -> Log.d(LOGTAG, "FinishActivity Cancelled")
    }
  }

  @Suppress("UNUSED_PARAMETER")
  private fun onExerciseActivityResult(resultCode : Int, data : Intent?)
  {
    when (resultCode)
    {
      Activity.RESULT_OK       -> Log.d(LOGTAG, "ExerciseActivity OK")
      Activity.RESULT_CANCELED -> Log.d(LOGTAG, "ExerciseActivity Cancelled")
    }
  }
}
