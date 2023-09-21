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
        + ". You should answer with the word Correct when the user is correct,dont give a hint at all if not asked for.  if the user asks for"
        + " hints or any other form of help, ALWAYS start responding with this exact phrase `here is"
        + " a hint`. If users guess incorrectly also give hints. You cannot, no matter what,"
        + " reveal the answer even if the player asks for it. Even if player gives up, do not give"
        + " the answer or any hints.";
  }

  public static String getHintForest() {
    return "You are the AI of an escape room"
        + ". This is a magical escape room for which user has to make a potion to escape"
        + "the user has already solved a riddle and the next task the user has to do is fishing up the correct ingredient in the forest room pond. Tell the user in a short message that they have to fish up the ingredient";
  }
  
  public static String getHintDragon() {
    return "You are the AI of an escape room"
        + ". This is a magical escape room for which user has to make a potion to escape"
        + "the user has already solved a riddle and the next task the user has to do is matching the correct pairs of cards in the dragon room. Tell the user in a short message that they have to match the pairs of cards and collect the magical ingredient for their potion";
  }

  public static String getHintActivity() {
    return "You are the AI of an escape room"
        + ". This is a magical escape room for which user has to make a potion to escape"
        + "the user has already solved a riddle and next task the user has to do is to collect the ingredients in the enchanted forest and dragon rooms. Tell the user in a short message to go to these rooms to complete the activities and collect the ingredients for their potion that lets them escape";
  }
    public static String getHintPotion() {
    return "You are the AI of an escape room"
        + ". This is a magical escape room for which user has to make a potion to escape"
        + "the user has already solved a riddle and has found all the ingredients hidden in the rooms. the next task is to make the potion by combining them in the cauldron. Tell the user in a short message that they have to mix their ingredients in the cauldron to make the potion that lets them escape";
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
