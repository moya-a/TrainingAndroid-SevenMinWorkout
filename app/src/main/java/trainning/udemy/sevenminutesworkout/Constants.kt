package trainning.udemy.sevenminutesworkout

class Constants
{
  companion object
  {
    const val SECOND : Long = 1000
    private val exerciseList : ArrayList<Exercise> = ArrayList<Exercise>()

    fun initList() : ArrayList<Exercise>
    {
      Exercise(
        id = 0,
        name = "Abdominal Crunches",
        imageResourceId = R.drawable.ic_exo_abdominal_crunch
      ).apply {
        exerciseList.add(id, this)
      }
      Exercise(
        id = 1,
        name = "High Knee Running in Place",
        imageResourceId = R.drawable.ic_exo_high_knees_running_in_place
      ).apply {
        exerciseList.add(id, this)
      }
      Exercise(
        id = 2,
        name = "Jumping Jacks",
        imageResourceId = R.drawable.ic_exo_jumping_jacks
      ).apply {
        exerciseList.add(id, this)
      }
      Exercise(
        id = 3,
        name = "Lunge",
        imageResourceId = R.drawable.ic_exo_lunge
      ).apply {
        exerciseList.add(id, this)
      }
      Exercise(
        id = 4,
        name = "Plank",
        imageResourceId = R.drawable.ic_exo_plank
      ).apply {
        exerciseList.add(id, this)
      }
      Exercise(
        id = 5,
        name = "Push Ups",
        imageResourceId = R.drawable.ic_exo_push_up
      ).apply {
        exerciseList.add(id, this)
      }
      Exercise(
        id = 6,
        name = "Push Ups and Rotation",
        imageResourceId = R.drawable.ic_exo_push_up_and_rotation
      ).apply {
        exerciseList.add(id, this)
      }
      Exercise(
        id = 7,
        name = "Side Plank",
        imageResourceId = R.drawable.ic_exo_side_plank
      ).apply {
        exerciseList.add(id, this)
      }
      Exercise(
        id = 8,
        name = "Squat",
        imageResourceId = R.drawable.ic_exo_squat
      ).apply {
        exerciseList.add(id, this)
      }
      Exercise(
        id = 9,
        name = "Step Up onto Chair",
        imageResourceId = R.drawable.ic_exo_step_up_onto_chair
      ).apply {
        exerciseList.add(id, this)
      }
      Exercise(
        id = 10,
        name = "Triceps Dip on Chair",
        imageResourceId = R.drawable.ic_exo_triceps_dip_on_chair
      ).apply {
        exerciseList.add(id, this)
      }
      Exercise(
        id = 11,
        name = "Wall Sit",
        imageResourceId = R.drawable.ic_exo_wall_sit
      ).apply {
        exerciseList.add(id, this)
      }
      return exerciseList
    }
  }
}