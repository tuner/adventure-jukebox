package o1.adventure

/**
 * `Game` describes an interface to Adventure game. It also has some metadata fields that could be shown
 * in a game selection menu.
 *
 * Adventure game may either directly extends this trait or an adapter class could be used.
 *
 * TODO: Consider defining metadata in a companion object or in an annotation. Then it would be
 * TODO: accessible without first creating an instance of the game.
 *
 * @author Kari Lavikka
 */
trait Game {
  /**
   * Name of the game
   */
  def title: String

  /**
   * Comma separated list of authors
   */
  def authors: String

  /**
   * Year the game was written
   */
  def year: Int

  /**
   * Welcome message that is shown in the initial view
   */
  def welcomeMessage: String

  /**
   * Brief name of player's current location
   */
  def currentLocationName: String

  /**
   * A longer description of player's current location. It should also list possible directions of movement,
   * items that can be picked up and show all other necessary stateful information.
   */
  def currentLocationDescription: String

  /**
   * Determines whether the game is over.
   *
   * @return `true` if the player has won, lost or quit; `false` otherwise
   */
  def isOver: Boolean

  /**
   * Determines if the adventure is complete, that is, if the player has won.
   */
  def isComplete: Boolean

  /**
   * The number of turns that have passed since the start of the game.
   * */
  var turnCount: Int

  /**
   * Returns a message that is to be displayed to the player at the end of the game.
   * The message will be different depending on whether or not the player has completed the quest.
   */
  def goodbyeMessage: String

  /**
   * Requests a command from the player, plays a game turn accordingly,
   * and returns a report of what happened.
   */
  def playTurn(command: String): String

}
