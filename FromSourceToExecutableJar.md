# Introduction #

You will need:
  * IDE with Maven and Subversion integration: I've successfully used IntelliJ Community Edition and Eclipse + Subclipse + AIM.
  * Check out the source code from SVN, see "Source" tab above.
  * Run the "install" goal, this will download all dependencies (except one, see next bullet), package the project and all of its dependencies into a self-contained executable jar (Java 6).
  * One system-specific binary dependency is also required: lp\_solve.  The Java bindings are already included, but their system-dependent component has been left out (need to fix this).  You can download it from their site, or if you're using Linux, just download the package and it will just work (tested with Ubuntu).
  * Most likely, your computer will run out of memory a number of times and eventually get stuck when you try to run it.  It requires a bunch of RAM, it took me about 12 GB to properly get it to run.  A pre-loading setting for staggered index building is in the works.


# Details #

Screen-shots, etc.

---

should package a self-contained executable for different platforms, as well as provide (compressed) index (lookup) files