# Introduction #

Plan to:
  * Refactor packages to be cleaner
  * Find FOSS pure-Java integer linear programming kit to eliminate system-specific dependency.
  * Make a pretty interface for playing games, possibly web-based, probably as part of a separate project.
  * Parallelize the CFRM algorithm, previous attempts at this have failed.
  * Make this project a lot more developer-friendly, please let me know if you are interested in participating in developing this project.


# Modularize #

  * General card game stuff:<br> Suit, Rank, Card<br>
<ul><li>Poker specific stuff:<br> Round, Hole, Community<br>
</li><li>Canonical card-sequence stuff:<br> CanonHole, Flop, Turn, River<br> Lookups, etc.</li></ul>


Remove all global state, make testable, test.