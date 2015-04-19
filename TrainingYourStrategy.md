# First Steps #

First, we'll need to build or (much easier) [download](http://code.google.com/p/alexoholdem/downloads/list) all of the indexes.  The LpSolve platform-specific libraries are required for `Optimizer` (Ubuntu comes with them, ideally this requirement should be removed - it is used for [mixed integer programming](http://en.wikipedia.org/wiki/Linear_programming#Integer_unknowns)).

# Generating Abstraction #

We start by generating an abstraction using (for example) `AbstractionOptimizer.main()`,  you can pass in four arguments to set the desired abstraction size.  By default, you get:
```
    int  nHoleBuckets  =   5;
    char nFlopBuckets  =  25;
    char nTurnBuckets  = 125;
    char nRiverBuckets = 625;
```
`char` is used to represent an `unsigned short` value, not anything character related. Note that `65535` (Character.MAX\_VALUE) is the largest possible number of river buckets with the current implementation.

Note that the above defaults are a very small strategy, we have experimented with strategies as big as:
```
    int  nHoleBuckets  =    48;
    char nFlopBuckets  =  1024;
    char nTurnBuckets  =  8192;
    char nRiverBuckets = 65535;
```

The place that invokes the (cached) abstraction calculation is:
```
    HoldemAbstraction abs = abstractHoldem(
//        new KMeansBucketizer(),
//        new PotentialBucketizer(),
//        new HandStrengthAbs(),
//        new PercentileAbs(),
//        new HistBucketizer((byte) 4),
//        new HistBucketizer((byte) 2),
//        new HistBucketizer((byte) 3),
        nHoleBuckets, nFlopBuckets,
        nTurnBuckets, nRiverBuckets);
```
Uncomment any of the `Bucketizer` implementations to use different heuristics (or better yet, write your own =D).

You will see an output such as: "Error is: <root mean squared error>".  The lower the better.  If we run `AbstractionOptimizer.main()` with the same parameters again, the abstraction will not be calculated again, but rather retrieved from disk cache.  The error will be slightly different from run to run because it is calculated probabilistically.


# Training the Strategy #
Use `BucketizerTest` ((( TODO: Finish this section )))