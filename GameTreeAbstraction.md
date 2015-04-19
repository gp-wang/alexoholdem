# Introduction #

Glossary:
  * bucket: set of card sequences (within a betting round) that the computer will consider identical.
  * hand strength: probability of winning + half probability of a split pot (assuming all players remain until the showdown).
  * millibet: in a limit hold'em game, the size of the small bet, divided by 1,000.

The abstraction process consists of the following stages:
  1. Pick number of buckets per betting round.
  1. Given a set of betting sequences (within a betting round), and a desired number of buckets: place each card sequence into one of the buckets.

(1) can be solved optimally for any fixed (2), given enough time (because your total number of buckets is limited finite available memory).  However, since each trial takes so long to compute, I have only performed basic trial & error optimization for (1).

The approaches implemented for (2) are:
  1. Sort card sequences by their hand strength, and split at equal percentiles.
  1. Use cluster analysis (K means ++) to group card sequences by hand strength.  This is the version that was used in AAAI 2009 heads-up limit hold'em competition averaging -2 millibets per hand.  Seems to make the most sense for card sequences at the river.
  1. For pre-river rounds: cluster over frequencies of clusters in subsequent river betting sequence strengths.  Has as a parameter the number of river buckets to use.
  1. For pre-river rounds: cluster over frequency with which subsequent betting hands fall into clusters their respective clusters.  This for examples allows for the hole buckets to take into account information from card sequences in all future betting rounds.  As of 2010-01-25 this method is not working very well.

Note that each set of card sequences is bucketed for a wide range of bucket counts, and the optimal way of spreading the bucket counts within a round is guaranteed.  This is done by formulating an integer linear programming problem, who's solution corresponds to the optimal arrangement.


# Details #

Details with citations... filler.