package trainning.udemy.sevenminutesworkout

import android.app.AlertDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_exercise.*
import java.text.SimpleDateFormat
import java.util.*

class ExerciseActivity : AppCompatActivity()
{
  private val LOGTAG : String by lazy { "in_class_${this::class.simpleName}" }

  companion object
  {
    private const val ONE_HUNDRED_MS : Long = 100
  }

  enum class TimerState
  {
    RUNNING, PAUSED
  }

  private val minSecFormatter : SimpleDateFormat = SimpleDateFormat("m:ss")
  private lateinit var exercisesList : ArrayList<Exercise>
  private lateinit var timer : ExerciseTimer
  private lateinit var lastSecondsSnackBar : Snackbar

  private var tts : TextToSpeech? = null
  private var soundPlayer : MediaPlayer? = null
  private var state : TimerState = TimerState.RUNNING
  private var exerciseId : Int = -1
  private var pauseOffset : Long = 0

  override fun onCreate(savedInstanceState : Bundle?)
  {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_exercise)

    exercisesList = Constants.initList()

    setSupportActionBar(tb_exercise_activity)
    supportActionBar?.apply {
      setDisplayHomeAsUpEnabled(true)
      tb_exercise_activity.setNavigationOnClickListener {
        AlertDialog.Builder(this@ExerciseActivity)
          .setTitle(R.string.exerciseActivity_back_dialog_title)
          .setCancelable(true)
          .setMessage(R.string.exerciseActivity_back_dialog_message)
          .setPositiveButton(R.string.yes) { dialog, _ ->
            dialog.dismiss()
            onBackPressed()
          }
          .setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
          }
          .create()
          .show()
      }
      // End of supportBar config
    }
    lastSecondsSnackBar = Snackbar.make(exerciseActivity_root, "", Snackbar.LENGTH_INDEFINITE)
    tts = TextToSpeech(this, Speaker())
    /**
     ** Uncomment to use media player
     **
    soundPlayer = MediaPlayer.create(this, R.raw.soundID)
    soundPlayer?.apply {
    try
    {
    isLooping = false
    start()
    } catch (e : Exception)
    {
    e.printStackTrace()
    }
    }
     **
     **/
    startNextExercise()
  }

  override fun onDestroy()
  {
    tts?.run {
      stop()
      shutdown()
    }
    soundPlayer?.run {
      stop()
    }
    super.onDestroy()
  }

  private fun startNextExercise()
  {
    // if it's null, we've reached the end of the list
    exercisesList.getOrNull(++exerciseId)?.run {
      tts?.speak("$name for ${time / 1000} seconds", TextToSpeech.QUEUE_FLUSH, null, "")
      tv_exerciseName.text = name
      iv_exercise.setImageResource(imageResourceId)
      pb_exercise.max = (time / 100).toInt()
      startTimer(time)
    } ?: run {
      tv_exerciseName.text = "Well Done !"
      iv_exercise.visibility = View.GONE
      tv_timer.text = "Finish"
      tts?.speak("Congratulations the session is over!", TextToSpeech.QUEUE_FLUSH, null, "")
      pb_exercise.setOnClickListener {
        Intent(this@ExerciseActivity, FinishActivity::class.java)
          .apply {
            startActivityForResult(this, MainActivity.ACTIVITY_FINISH)
          }
        this@ExerciseActivity.finish()
      }
    }
  }

  @Suppress("UNUSED_PARAMETER")
  fun toggleStartPause(view : View)
  {
    val currentExercise = exercisesList[exerciseId]
    when (state)
    {
      TimerState.RUNNING ->
      {
        tts?.speak("Pause", TextToSpeech.QUEUE_FLUSH, null, "")
        state = TimerState.PAUSED
        timer.cancel()
        lastSecondsSnackBar
          .setText("${currentExercise.name} : Timer paused !")
          .show()
      }
      TimerState.PAUSED  ->
      {
        tts?.speak("Resumed", TextToSpeech.QUEUE_FLUSH, null, "")
        state = TimerState.RUNNING
        startTimer(currentExercise.time - pauseOffset)
        lastSecondsSnackBar.dismiss()
      }
    }
  }

  private fun startTimer(millis : Long)
  {
    timer = ExerciseTimer(millis, ONE_HUNDRED_MS)
      .apply {
        start()
      }
  }

  /**
   ** Inner classes definition
   */
  private inner class ExerciseTimer(
    millisInFuture : Long,
    countDownInterval : Long
  ) : CountDownTimer(millisInFuture, countDownInterval)
  {
    private val LOGTAG : String by lazy { "in_class_${this::class.simpleName}" }
    override fun onFinish()
    {
      // this check is overkill, onFinish shouldn't be triggered while paused
      // as the countdown is cancelled and don't go through this method
      if (TimerState.PAUSED != state)
      {
        lastSecondsSnackBar.apply {
          if (isShown) dismiss()
        }
        if (exerciseId < exercisesList.size)
        {
          startNextExercise()
        }
      }
    }

    override fun onTick(millisUntilFinished : Long)
    {
      val currentExercise = exercisesList[exerciseId]
      when (state)
      {
        // this check is overkill as the timer is cancelled and destructed when paused
        // so it should never be ticking while paused
        TimerState.RUNNING ->
        {
          pauseOffset = currentExercise.time - millisUntilFinished
          val remaining = (millisUntilFinished / 100).toInt()
          val remainingSec = remaining / 10
          tv_timer.text = minSecFormatter.format(millisUntilFinished)
          pb_exercise.progress = remaining
          if (remaining / 10 < 10) // less than 10 sec remaining
          {
            lastSecondsSnackBar.apply {
              val exoName = exercisesList.getOrNull(exerciseId + 1)?.name ?: "the end"
              setText("$remainingSec before $exoName")
              if (!isShownOrQueued)
              {
                tts?.speak("$exoName in 10 seconds", TextToSpeech.QUEUE_FLUSH, null, "")
                show()
              }
            }
          }
        }
        TimerState.PAUSED  ->
        { // does nothing as this should never happen anyways
        }
      }
    }

    /**
     ** END OF ExerciseTimer inner Class
     */
  }

  private inner class Speaker : TextToSpeech.OnInitListener
  {
    private val LOGTAG : String by lazy { "in_class_${this::class.simpleName}" }
    override fun onInit(status : Int)
    {
      when (status)
      {
        TextToSpeech.SUCCESS ->
        {
          val locale = Locale.getDefault()
          Log.d(LOGTAG, "Using $locale to initialize TTS")
          when (tts?.setLanguage(locale))
          {
            TextToSpeech.LANG_MISSING_DATA  -> Log.e(LOGTAG, "Missing language data for $locale")
            TextToSpeech.LANG_NOT_SUPPORTED -> Log.e(LOGTAG, "Language $locale is not supported")
          }
        }
        else                 ->
        {
          Log.e(LOGTAG, "TTS initialization failed !")
        }
      }
    }

    /**
     ** END OF Speaker inner Class
     */
  }

  /**
   ** END OF ExerciseActivity Class
   */
}
