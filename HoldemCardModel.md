# Hole #

Hole cards are the two cards that a player hold in their hand.
  * Since there are 52 unique cards, there are 52 choose 2 = 52 x 51 = 2,652 unique Hole cards.
  * To create a Hole, use Hole.valueOf(Card a, Card b). Note that if a == b, then null is returned.
  * You can check if a Hole contains a pair of the same Rank with Hole.isPair(): boolean.<br> If a Hole is not a pair, then you can use Hole.lo() and Hole.hi() to extract the cards with a lower and higher Rank (respectively).</li></ul>


<h1>Community #

Community cards are the 0 to 5 cards that are shared among all players.
  * In the pre-flop round (Round.PREFLOP), the there are no Community cards.
  * In the flop round (Round.FLOP), there are three Community cards (3 additional).
  * In the turn round (Round.TURN), there are found Community cards (1 additional).
  * In the river round (Round.RIVER), there are five Community cards (1 additional).
  * You can create Community cards either directly by listing them in the constructor, or by using addTurn(Card) and addRiver(Card) from the flop and turn rounds respectively.
  * You can access the cards in a Community via: flopA(), flopB(), flopC(), turn(), river().
  * You can check if a Community.contains(Card) : boolean
  * Community.round() returns the Round that a Community card set belongs to. You can also use isPreflop(), hasFlop(), hasTurn(), hasRiver().
  * You can look back at previous stages in the Community cards with: asPreflop, asFlop, asTurn(), asRiver(), and asOf(Round).
  * Community.knownCount() returns the number of visible community cards (0, 3, 4, or 5).<br> Community.known() : Card<a href='.md'>.md</a> returns the individual cards in order as an array.</li></ul>


<h1>CardSequence #

A CardSequence is a combination of Hole and Community cards.<br>
Use new LiteralCardSequence(Hole[, Community]) to create a CardSequence.<br>
<br>
<br>
<h1>Deck</h1>

A Deck represents 52 cards in some sequence.<br>
<ul><li>You can use Deck.nextCard() : Card to retrieve the next card from the deck.<br>
</li><li>Use Deck.nextHole() : Hole, and Deck.nextFlop() : Community to draw multiple cards out of the deck at a time.<br>
</li><li>Use Deck.cardsDelt() to know how many cards were already dealt, and Deck.reset() to bring the deck back to how it was when cardsDealt() was zero (i.e. before any nextCard() calls).<br>
</li><li>For the purpose of this model, burn cards are never used.  Assuming that cards are coming from a truly random sequence, this never makes a difference.</li></ul>


<h1>ChanceCards</h1>

ChanceCards encapsulates all chance cards within a hand.<br>
<ul><li>ChanceCards.community(asOf : Round) : Community returns the Community cards as of a certain round.<br>
</li><li>ChanceCards.hole(forPlayer: Avatar): Hole returns the Hole cards for a particular Avatar (player ID).<br>
</li><li>DeckCards is an implementation of ChanceCards that uses a Deck.<br>
</li><li>LiteralCards allows you to directly specify ChanceCards in a constructor.<br>
</li><li>SwapCards allows you to have common Community cards with two specific sets of Hole cards.  You can swop between which of two Avatars get which of the two Hole card sets.  This is useful for testing bots by placing them in symmetric ChanceCard situations.