# Seat #

The state at every table position at a particular time is stored in the Seat object.
  * The Seat state object encapsulates the following components:
    * The Seat belongs to a particular Avatar.
    * It has some ChipStack commitment to the pot.
    * Is might be all-in or quit/folded.
    * The round in which the last voluntary action was taken is tracked.
    * These can all be accessed with:<br> player, commitment, isAllIn, isFolded, volultarityActedDuring(Round), isActive.<br>
</li></ul><ul><li>There are a number of ways to advance the state of a Seat object:<br>
<ul><li>Seat.advance takes an Action with a ChickStack of stakes, a betSize, and a Round.<br>
</li><li>There is a special case <code>Seat.advanceBlind(Action, ChipStack)</code>.<br>
</li><li>You can also advance with Seat.fold(Round).</li></ul></li></ul>

<h1>State #

  * The State object encapsulates the following components:<br> The Round, all Seats, next Seat to act, remaining bets in round (max 4 per betting round, big blind counts as a bet), the last Avatar in the round that bet or called, the ChipStack stakes (i.e. how much each player must have to stay in), and the state at the start of the current round..<br>
<ul><li>To create a State, all you need is a list of Avatar objects in clockwise dealer-last order.<br>
</li><li>You can advance the State using State.advance(Action).<br> A special case of an Action advancement is advanceQuitter(quitter: Avatar).<br>
</li><li>To get a list of non-dominated actions, use <code>State.viableActions() :  Map&lt;AbstractAction, State&gt;</code> –these will not include folding when checking is possible–  or use <code>State.actions(nonDominated = false) : Map&lt;AbstractAction, State&gt;</code> to get a raw list of all legal actions.<br>
</li><li>You can query the State object with atEndOfHand(): boolean.<br>
</li><li>You can check the betSize(), if you canCheck() or canRaise(), and isSmallBet().<br>
</li><li>You can <code>reify(easeAction: FallbackAction): Action</code>
</li><li>You can also check the:<br> <code>round(), seats(), numActivePlayers(), unfolded(): List&lt;Seat&gt;, pot(): ChipStack, stakes(): ChipState</code> –this is the amount required to have in the pot to stay in the hand–, remainingBetsInRound(), toCall(): ChipStack.<br>
</li><li>You can check who is going to act next with:<br> nextToActIndex(): int, and nextToAct(): Seat, dealerIsNext(): boolean.<br>
</li><li>You can also ask for the HeadsUpStatus (will return null if number of players is not 2):<br> In progress, showdown, dealer wins, dealee wins.</li></ul>


<h1>Player</h1>

The Player interface represents a Limit Texas Hold'em player.<br>
<ul><li>A Player can observe the state before the action of every opponent with the <code>Player.observe(nextToActState: State)</code> callback.<br>
</li><li><code>Player.act(State, CardSequence): Action</code> is where the decision about which action to take happens.<br>
</li><li>There has to be a special case for QUIT actions because they are the only ones that can happen out-of-turn.  The <code>Player.hasQuit(): boolean</code> method indicates whether or not the given player has quit.<br>
</li><li>Finally, the <code>Player.handEnded(deltas: Map&lt;Avatar, ChipStack&gt;)</code> callback is invoked at the end of a hand.  After that, the player may start playing the next hand.</li></ul>


<h1>Dealer</h1>

The Dealer object facilitates the playing of a poker hands.<br>
<ul><li>To create a Dealer, you need to supply a Map from Avatar to Player objects.  Additionally, you need to specify whether or not the dealer should automatically post the blinds (otherwise the Player objects will be expected to return SMALL_BLIND and BIG_BLIND actions).<br>
</li><li>To play out a hand of poker, use:<br> <code>Dealer.play(clockwiseDealerLast: List&lt;Avatar&gt;, ChanceCards): StackedReplay</code>.<br>
</li><li>The Dealer will ask each Player in turn to act, and advance the game State.<br>
</li><li>The Dealer also handles Players quitting and going all-in.<br>
</li><li>Finally, the Dealer is responsible for publishing the handEnded event.</li></ul>


<h1>StateFlow</h1>

The Dealer does its job with the help of StateFlow.<br>
<ul><li>StateFlow starts off with the initial state, the most recent state is the head().<br>
</li><li>StateFlow.advance(Action) and advanceQuitter(Avatar) are used to move the head forward.<br>
</li><li><code>StateFlow.asReplay(ChanceCards): Replay</code> provides a replay upto the current point.<br>
</li><li><code>StateFlow.deltas(ChanceCards): Map&lt;Avatar, ChipStack&gt;</code> how much money each Avatar has won.  Note that before the end of the game these will all be negative, and at the end of the game one Avatar will have everybody's money, or there could also be pot splits.</li></ul>


<h1>StateTree</h1>

StateTree can be used to pre-compute every possible state ahead of time (or up to a specified ply), and then allows you to navigate the game tree.<br>
<ul><li>StateTree.Node objects represent the individual possible states.<br>
</li><li>Each StateTree.Node has an intent(): char –here char being used in the sense of an unsigned 16 bit value, not an actual character– which is a unique index.  It is possible that some Nodes will have the same intent(), because one is dominated by the other (e.g. folding when you can check).  This helps not waste resources on considering dominated actions.<br>
</li><li>The intent numbers within each round range from [0, # of unique intents in that round).<br> Also, intents in later Rounds are continuous between two earlier Round intents.<br>
</li><li>This indexing can be used to efficiently represent all possible states within a Round as an array.<br>
</li><li><code>StateTree.Node.acts(): Map&lt;AbstractAction, Node&gt;</code> can be used to retrieve the next nodes down the tree of possible States.