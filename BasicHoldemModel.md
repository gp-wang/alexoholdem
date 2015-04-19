# Round #

Add your content here.
  * There are four betting rounds in a game of Texas Hold'em poker represented by the Round enum: PREFLOP, FLOP, TURN, RIVER.
  * For each round, you can use Round.previous() and Round.next() to get the previous/next rounds.  Round.PREFLOP.previous() and Round.RIVER.next() both return null.


# ChipStack #

There is a finite ChipStack that represents a player's stack of chips within a hand.
  * There are a finite number of possible ChipStacks because this model represents limit Texas Hold'em.
  * The model could be extended to the no-limit setting, however that is not done yet.
  * The ChipStack is measured in small blinds, because all bets have to be multiples of the small blind.
    * Note that this could potentially not be the case in limit Hold'em if going all-in on a bet.
In this case, the model will not precisely capture the game, and will essentially round up the last non-full all-in bet.
  * You can add and subtract ChipStacks with ChipStack.{plus, minus}. Other arithmetic operations are: negate, times, split (divide), and remainder.
  * You can access the number of chips in a ChipStack using:
smallBlinds(), bigBlinds(), smallBets(), bigBets(), and bets(boolean smallOrBig).
    * Small blinds are 1 chip, big blinds are 2 chips. When asking the the number of big blinds in a ChipStack with an odd number of chips, the returned number of big blinds is rounded up.
    * A small bet is equal to big blind and is played in the pre-flop and flop rounds.  Large bets are equal to 4 small blinds (2 big blinds) are are played in the turn and river rounds.
The smallBets() and bigBets() methods both round up in similar fashion to bigBlinds().
  * You can create a new ChipStack by using newInstance(int smallBlinds) or the following constants:  ZERO (0), SMALL\_BLIND (1), BIG\_BLIND (2), SMALL\_BET (2), BIG\_BET (4), MAX\_VALUE (32,767).
  * A ChipStack.toString() looks like: -3 sb, 10 sb, etc.

