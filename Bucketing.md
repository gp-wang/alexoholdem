# Details #

  * The total state space of heads up limit poker is huge: <br> <code>&lt;&lt;NEED EXACT CALCULATION&gt;&gt;</code><br> More than 10 ^ 13 states.<br>
<ul><li>For a full analysis of the game, this many states can't fit in RAM (yet).<br> One way around this is to group similar card sequences together, and to treat them as if they were identical.  These groups are called “buckets”.<br>
</li><li>The Bucketizer class captures this idea.