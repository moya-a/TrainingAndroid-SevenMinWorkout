package trainning.udemy.sevenminutesworkout

data class Exercise(
  var id : Int,
  var name : String,
  var time : Long = Constants.SECOND * 1,
  var imageResourceId : Int
)