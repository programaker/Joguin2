package joguin.game.step.createcharacter

import eu.timepit.refined.auto._
import eu.timepit.refined.refineV
import joguin.Name
import joguin.earth.maincharacter.Age
import joguin.earth.maincharacter.Gender
import joguin.earth.maincharacter.MainCharacter
import joguin.game.progress.Index
import joguin.game.progress.IndexR
import joguin.game.progress.allInvasionsDefeated
import joguin.game.progress.isInvasionDefeated
import joguin.game.step.Explore
import joguin.testutil.PropertyBasedSpec
import joguin.testutil.generator.AgeGenerators
import joguin.testutil.generator.Generators
import joguin.testutil.generator.Tag
import joguin.testutil.interpreter.createcharacter._
import joguin.testutil.interpreter._
import org.scalacheck.Arbitrary
import org.scalatest.Inside._
import org.scalatest.Inspectors
import org.scalatest.OptionValues._

@SuppressWarnings(Array("org.wartremover.warts.NonUnitStatements"))
final class CreateCharacterStep_Properties extends PropertyBasedSpec {

  property("displays messages to the player in the correct order") {
    import joguin.testutil.generator.Generators.age
    import joguin.testutil.generator.Generators.gender
    import joguin.testutil.generator.Generators.name

    forAll {
      (
        name: Name,
        gender: Gender,
        age: Age
      ) =>
        val answers: Map[String, List[String]] = Map(
          askName   -> List(name),
          askGender -> List(gender.code),
          askAge    -> List(age.toString)
        )

        val actualMessages = new CreateCharacterStep[CreateCharacterStepF].play
          .foldMap(createCharacterInterpreter)
          .runS(WriteMessageTrack.of(answers))
          .map(_.indexedMessages)
          .value

        actualMessages.get(0).value shouldBe askToCreateCharacter
        actualMessages.get(1).value shouldBe askName
        actualMessages.get(2).value shouldBe askGender
        actualMessages.get(3).value shouldBe askAge
        actualMessages.get(4).value shouldBe characterCreated(name)
    }
  }

  property("goes to the Explore step, starting the GameProgress with the created MainCharacter") {
    import joguin.testutil.generator.Generators.age
    import joguin.testutil.generator.Generators.gender
    import joguin.testutil.generator.Generators.name

    forAll {
      (
        name: Name,
        gender: Gender,
        age: Age
      ) =>
        //MainCharacter is created with 0 experience - never fought aliens before
        val expectedMainChar = MainCharacter(name, gender, age, experience = 0)

        val answers: Map[String, List[String]] = Map(
          askName   -> List(name),
          askGender -> List(gender.code),
          askAge    -> List(age.toString)
        )

        val nextStep = new CreateCharacterStep[CreateCharacterStepF].play
          .foldMap(createCharacterInterpreter)
          .runA(WriteMessageTrack.of(answers))

        inside(nextStep.value) {
          case Explore(progress) =>
            progress.mainCharacter shouldBe expectedMainChar

            //GameProgress starts with all cities invaded
            val indexes = (1 to progress.invasions.size).map(refineV[IndexR](_).getOrElse(1: Index))
            allInvasionsDefeated(progress) shouldBe false
            Inspectors.forAll(indexes)(idx => isInvasionDefeated(progress, idx) shouldBe false)
        }
    }
  }

  property("repeats a question to the player until receives a valid answer, informing the error") {
    import joguin.testutil.generator.Generators.age
    import joguin.testutil.generator.Generators.gender
    import joguin.testutil.generator.Generators.invalidName
    import joguin.testutil.generator.Generators.name
    import joguin.testutil.generator.Generators.arbitraryTag

    implicit val a1: Arbitrary[Tag[1, Int]] = arbitraryTag(Generators.genSmallInt)
    implicit val a2: Arbitrary[Tag[2, Int]] = arbitraryTag(AgeGenerators.genInvalidAge)

    forAll {
      (
        name: Name,
        gender: Gender,
        age: Age,
        repetitions: Tag[1, Int],
        invalidName: String,
        invalidAge: Tag[2, Int]
      ) =>
        val names = List.fill(repetitions)(invalidName) ++ List(name.value)
        val genders = List.fill(repetitions)(invalidName) ++ List(gender.code)
        val ages = List.fill(repetitions)(invalidAge.toString) ++ List(age.toString)

        val answers = Map(
          askName   -> names,
          askGender -> genders,
          askAge    -> ages
        )

        val actualMessages = new CreateCharacterStep[CreateCharacterStepF].play
          .foldMap(createCharacterInterpreter)
          .runS(WriteMessageTrack.of(answers))
          .map(_.indexedMessages)
          .value

        val n: Int = repetitions

        actualMessages.count { case (_, msg) => msg === askName } shouldBe (n + 1)
        actualMessages.count { case (_, msg) => msg === errorInvalidName } shouldBe n

        actualMessages.count { case (_, msg) => msg === askGender } shouldBe (n + 1)
        actualMessages.count { case (_, msg) => msg === errorInvalidGender } shouldBe n

        actualMessages.count { case (_, msg) => msg === askAge } shouldBe (n + 1)
        actualMessages.count { case (_, msg) => msg === errorInvalidAge } shouldBe n
    }
  }

  private val createCharacterInterpreter = createCharacterStepTestInterpreter()

  private val askToCreateCharacter = "\nCreate your main character\n"

  private val askName = "\nName:\n"
  private val askGender = "\nGender - (F)emale, (M)ale, (O)ther:\n"
  private val askAge = "\nAge:\n"

  private val errorInvalidName = "Invalid name\n"
  private val errorInvalidGender = "Invalid gender\n"
  private val errorInvalidAge = "Invalid age. You must be at least 18 to defend Earth\n"

  private def characterCreated(name: Name): String =
    s"\nWelcome, commander $name! Now, you must bring our forces\n" +
      "to the invaded cities and take them back from the Zorblaxians.\n" +
      "Destroy the Terraform Devices and save all life on Earth!\n"

}
