package joguin.game.step.createcharacter

import joguin.testutil.PropertyBasedSpec
import eu.timepit.refined.auto._
import joguin.Name
import joguin.earth.maincharacter.Age
import joguin.earth.maincharacter.Gender
import joguin.testutil.interpreter.CreateCharacterStepInterpreter
import joguin.testutil.interpreter.CreateCharacterStepInterpreter.CreateCharacterStepF
import joguin.testutil.interpreter.WriteMessageTrack
import org.scalatest.OptionValues._

final class CreateCharacterStep_Properties extends PropertyBasedSpec {
  private val askToCreateCharacter = "\nCreate your main character\n"

  private val askName = "\nName:\n"
  private val askGender = "\nGender - (F)emale, (M)ale, (O)ther:\n"
  private val askAge = "\nAge:\n"

  private def characterCreated(name: Name): String =
    s"\nWelcome, commander $name! Now, you must bring our forces\n" +
    "to the invaded cities and take them back from the Zorblaxians.\n" +
    "Destroy the Terraform Devices and save all life on Earth!\n"

  private val errorInvalidName = "Invalid name\n"
  private val errorInvalidGender = "Invalid gender\n"
  private val errorInvalidAge = "Invalid age. You must be at least 18 to defend Earth\n"

  property("displays messages to the player in the correct order") {
    import joguin.testutil.generator.Generators.name
    import joguin.testutil.generator.Generators.gender
    import joguin.testutil.generator.Generators.age

    forAll { (
      name: Name,
      gender: Gender,
      age: Age
    ) =>

      val expectedAnswers: Map[Int, String] = Map(
        0 -> name,
        1 -> gender.code,
        3 -> age.toString
      )

      val track = CreateCharacterStep[CreateCharacterStepF].start
        .foldMap(CreateCharacterStepInterpreter.build(expectedAnswers))
        .runS(WriteMessageTrack.empty)

      val actualMessages = track.map(_.indexedMessages).value
      actualMessages.get(0).value shouldBe askToCreateCharacter
      actualMessages.get(1).value shouldBe askName
      actualMessages.get(2).value shouldBe askGender
      actualMessages.get(3).value shouldBe askAge
      actualMessages.get(4).value shouldBe characterCreated(name)
    }
  }

}
