# Action #

  * There are three types of action models, each is a generalization of the previous one.
  * The most detailed action model is called Action:
    * Quit, Fold
    * Check, Call, Call all-in
    * Bet, Bet all-in, Raise, Raise all-in
    * Small blind, Small blind all-in, Big blind, Big blind all-in

# AbstractAction #

The next more general action model is AbstractAction
  * Quit/Fold, Check/Call, Bet/Raise


# FallbackAction #

Finally FallbackAction moves are available at all times in the game:
  * Check/Fold, Check/Call, Raise/Call
  * In general, the choices that a player has fall into three categories:
    1. fold, check, raise: if there was no prior raise
    1. fold, call, raise: if there was a prior raise
    1. fold, call: if there were four prior bets, so another bet is not an option.
  * Note that at any point, a player may choose to fold.  However, in case (1) it makes no sense to fold because a player can just check instead.
  * Also note that the combination of fold, check, but without the possibility of raising can never happen.