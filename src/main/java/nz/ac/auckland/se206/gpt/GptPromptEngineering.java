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
    return " Introduce the yourself as a wizard and say that the user must solve a riddle to get"
        + " the gem ingredient. Then write a riddle with"
        + wordToGuess
        + " as the answer. You should answer with the word Correct when the user is correct,dont"
        + " give a hint at all if not asked for.  if the user asks for hints or any other form of"
        + " help, ALWAYS start responding with this exact phrase `here is a hint`. If users guess"
        + " incorrectly do not give hints. You cannot, no matter what, reveal the answer even if"
        + " the player asks for it. Even if player gives up, do not give the answer or any hints.";
  }

  /**
   * Generates a GPT prompt engineering string for a hint about the forest.
   *
   * @return the generated prompt engineering string
   */
  public static String getHintForest() {
    return "You are the grand wizard of an escape room. This is a magical escape room for which"
        + " user has to make a potion to escape. The next task the user has to do is fishing"
        + " up the correct ingredient in the forest room pond. Tell the user in a short"
        + " message that they have to fish up the ingredient and collect it. Begin the"
        + " sentence with 'here is a hint'";
  }

  /**
   * Generates a GPT prompt engineering string for a hint about the dragon room.
   *
   * @return the generated prompt engineering string
   */
  public static String getHintDragon() {
    return "You are the grand wizard of an escape room. This is a magical escape room for which"
        + " user has to make a potion to escape. The next task the user has to do is"
        + " matching the correct pairs of cards in the dragon room. Tell the user in a short"
        + " message that they have to match the pairs of cards and collect the magical"
        + " ingredient for their potion and begin the sentence with 'here is a hint'";
  }

  /**
   * Generates a GPT prompt engineering string for a hint about the activities in general.
   *
   * @return the generated prompt engineering string
   */
  public static String getHintActivity() {
    return "You are the grand wizard of an escape room. This is a magical escape room for which"
        + " user has to make a potion to escape. The next task the user has to do is to"
        + " collect the ingredients in the enchanted forest and dragon rooms. Tell the user"
        + " in a short message to go to these rooms to complete the activities and collect"
        + " the ingredients for their potion that lets them escape and begin the sentence"
        + " with 'here is a hint'";
  }

  /**
   * Generates a GPT prompt engineering string for a hint about the potion.
   *
   * @return the generated prompt engineering string
   */
  public static String getHintPotion() {
    return "You are the grand wizard of an escape room. This is a magical escape room for which"
        + " user has to make a potion to escape. The user has found all the ingredients"
        + " hidden in the rooms. The next task is to make the potion by combining them in"
        + " the cauldron. Tell the user in a short message that they have to mix their"
        + " ingredients in the cauldron to make the potion that lets them escape and begin"
        + " the sentence with 'here is a hint' ";
  }

  /**
   * Generates a GPT prompt engineering string for a hint about the gem.
   *
   * @return the generated prompt engineering string
   */
  public static String getHintGem() {
    // Tell GPT the context that user has solved a riddle and now must find the gem object
    return "You are the grand wizard of an escape room. This is a magical escape room for which"
        + " user has to make a potion to escape. The user has solved the riddle and the gem"
        + " ingredient is now unlocked. Tell the user in a short message that they have to"
        + " go to the lab room to find the gem ingredient begin the sentence with 'here is a"
        + " hint' ";
  }

  /**
   * Generates a GPT prompt engineering string for a hint about the final escape.
   *
   * @return the generated prompt engineering string
   */
  public static String getHintEscape() {
    return "You are the grand wizard of an escape room. This is a magical escape room for which"
        + " user has to make a potion to escape. The user has made the potion. ingredient."
        + " Tell the user in a short message that since they have made the potion the must"
        + " now find a way opening out of the room to escape and begin the sentence with"
        + " 'here is a hint' ";
  }

  /**
   * Generates a GPT prompt engineering string for the context/backstory of the game.
   *
   * @return the generated prompt engineering string
   */
  public static String getIntro() {
    // Introducing GPT to the game and story
    return "I have an escape room game that is wizard themed and there is a persona of a"
        + " grandmaster present at all times. The user must complete tasks in each of the 3"
        + " rooms and after completing each task they are able to pick up an ingredient."
        + " They must gather 3 ingredients to brew a potion according to a recipe. After"
        + " brewing this potion they can click on the window and escape.  Write a very short"
        + " backstory that is consistent with this gameplay in under 100 words and make it"
        + " like the grandmaster is informing the user of this backstory.";
  }

  /**
   * Generates a GPT prompt engineering string about the context of the game.
   *
   * @return the generated prompt engineering string
   */
  public static String getContext() {
    return "You are the grand wizard of an escape room. This is a magical escape room for which"
        + " user has to make a potion to escape. You may engage in casual conversation. Only"
        + " in the case the user asks for hints, any other form of help or expresses they"
        + " are unsure of what to do next in the escape room, always start responding with"
        + " the exact phrase `here is a hint`";
  }
}
