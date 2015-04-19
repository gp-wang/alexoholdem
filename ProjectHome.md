A version of AoBot trained for two days competed in AAAI 2009 and broke even.

currently featuring:
  * limit Texas Hold'em game engine, with hand history storage
  * fast poker hand evaluation
  * parsing and analysis of IRC poker histories
  * decision tree based opponent modeling (works better than neural networks), predicting actions and showdown outcomes
  * suit isomorphic (canonical) betting sequence encoding, which is a bi-directional map from 0..n to all betting sequences within a round
  * game tree reduction by grouping similar situations together and treating them as one (bucketing), percentile and hand strength based abstractions are implemented
  * heads-up limit near-equilibrium strategy using counterfactual regret minimization
  * counterfactual regret minimization for Kuhn Poker