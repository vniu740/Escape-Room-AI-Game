package nz.ac.auckland.se206.gpt;

/** Utility class for generating GPT prompt engineering strings. */
public class GptPromptEngineering {

  /**
   * Generates a GPT prompt engineering string for a riddle with the given word.
   *
   * @param wordToGuess the word to be guessed in the riddle
   * @return the generated prompt engineering string
   */
  public static String getRiddleWithGivenWord(String wordToGuess) {
    return " Tell the user a riddle with"
        + wordToGuess
        + " as the answer. You should answer with the word Correct when the user is correct. If you"
        + " give any hints, clues, directions, information, or any other form of help to the user,"
        + " always start responding with the exact phrase `here is a hint` phrase. If users guess"
        + " incorrectly for the riddle, also give hints. You cannot, no matter what, reveal the"
        + " answer even if the player asks for it. Currently the player is in the initial"
        + " laboratory room and needs to explore all the rooms to collect ingredients for a"
        + " potion.";
  }

  public static String getIntro() {
    return "I have an escape room game that is wizard themed and there is a persona of a"
               + " grandmaster present at all times. The user must complete tasks in each of the 3"
               + " rooms and after completing each task they are able to pick up an ingredient."
               + " They must gather 3 ingredients to brew a potion according to a recipe. After"
               + " brewing this potion they can click on the window and escape.  Write a very short"
               + " backstory that is consistent with this gameplay in under 100 words and make it"
               + " like the grandmaster is informing the user of this backstory.";
  }
}
