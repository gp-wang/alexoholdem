# Introduction #

The strongest bot I got is one based on University of Alberta papers about Counterfactual Regret Minimization, which is a game theoretic algorithm that approximates an optimal strategy.<br>

See:<br>
<ul><li><a href='http://poker.cs.ualberta.ca/publications/NIPS07-cfr.pdf'>http://poker.cs.ualberta.ca/publications/NIPS07-cfr.pdf</a>
</li><li><a href='http://poker.cs.ualberta.ca/publications/abourisk.msc.pdf'>http://poker.cs.ualberta.ca/publications/abourisk.msc.pdf</a></li></ul>

An optimal strategy is a Nash equilibrium, in the long term it will not lose (in the limit). However, it is not guaranteed to generate the most return, just that it will not lost a lot.  It is a very safe and very balanced strategy (although it can be surprisingly aggressive).<br>
<br />
You can look at a poker game as 4 grids, one per Round.<br>
Within a round we have betting sequence (State) index <b>s</b>, and a card sequence index <b>c</b>. <br> We can use <b>s</b> and <b>c</b> as matrix indexes, this provides a very dense representation of poker.<br>
<br />
Looking at poker as a grid has a problem: it requires way to much memory to store fully.<br>
One way of improving this is by eliminating dominated game states (StateTree.Node.intent() does this, but probably there is more that could be done), thereby making s smaller.<br>
Another is by grouping together indistinguishable card sequences (canonical indexing does this to some extent, but more could probably be done), thereby making c smaller.  We can also use a heuristic to group together similar card sequences into buckets (see Bucketing).<br>
These techniques combined (especially Bucketing) can be used fit the entire state-spade of Heads-up Limit Texas Hold'em in RAM.