package ao.learn.mst.gen

import org.specs2.mutable.Specification
import ao.learn.mst.gen2.game.{ExtensiveGameDecision, ExtensiveGame}
import ao.learn.mst.example.perfect.complete.PerfectCompleteGame
import ao.learn.mst.gen2.info.{InformationSetIndex, TraversingInformationSetIndexer}
import ao.learn.mst.cfr.{StrategyProfile, CfrMinimizer}
import ao.learn.mst.example.slot.specific.bin.DeterministicBinaryBanditGame
import ao.learn.mst.example.slot.specific.k.MarkovBanditGame
import ao.learn.mst.example.rps.RockPaperScissorsGame
import ao.learn.mst.example.perfect.complete.decision.{PerfectCompleteDecisionAfterDownInfo, PerfectCompleteDecisionAfterUpInfo, PerfectCompleteDecisionFirstInfo}
import ao.learn.mst.example.imperfect.complete.ImperfectCompleteGame
import ao.learn.mst.example.imperfect.complete.decision.{ImperfectCompleteDecisionSecondInfo, ImperfectCompleteDecisionFirstInfo}
import ao.learn.mst.example.incomplete.{IncompleteActionUp, IncompleteActionDown, IncompleteGame}
import ao.learn.mst.example.incomplete.node.{IncompleteInfoPlayerTwoAfterDown, IncompleteInfoPlayerTwoAfterUp, IncompleteInfoPlayerOneTypeTwo, IncompleteInfoPlayerOneTypeOne}
import ao.learn.mst.example.zerosum.ZeroSumGame
import ao.learn.mst.example.zerosum.info.{ZeroSumInfoBlue, ZeroSumInfoRed}
import ao.learn.mst.example.zerosum.node.ZeroSumDecisionRed


/**
 *
 */
class CfrMinimizerSpec
    extends Specification
{
  //--------------------------------------------------------------------------------------------------------------------
  "Counterfactual Regret Minimization algorithm" should {
    val minimizer = new CfrMinimizer()

    def approximateOptimalStrategy(
        game: ExtensiveGame,
        iterations: Int
        ): (InformationSetIndex, StrategyProfile) =
    {
      val informationSetIndex =
        TraversingInformationSetIndexer.index( game )

      val strategyProfile = trainStrategyProfile(
        game, informationSetIndex, iterations)

      (informationSetIndex, strategyProfile)
    }

    def trainStrategyProfile(
        game: ExtensiveGame,
        informationSetIndex: InformationSetIndex,
        iterations: Int
        ): StrategyProfile =
    {
      val strategyProfile =
        new StrategyProfile( informationSetIndex )

      for (i <- 1 to iterations) {
        minimizer.reduceRegret(
          game, informationSetIndex, strategyProfile)
      }

      strategyProfile
    }


    "Solve singleton information set problems" in {
      def approximateOptimalSingletonInformationSetStrategy(
           game: ExtensiveGame,
           iterations: Int): Seq[Double] =
      {
        approximateOptimalStrategy(game, iterations)._2.averageStrategy(
          game.treeRoot.asInstanceOf[ExtensiveGameDecision].informationSet,
          game.treeRoot.actions.size)
      }

      "Classical bandit setting" in {
        "Deterministic Binary Bandit" in {
          val optimalStrategy = approximateOptimalSingletonInformationSetStrategy(
            DeterministicBinaryBanditGame, 64)

          optimalStrategy.last must be greaterThan(0.99)
        }

        "Markovian K-armed Bandit" in {
          val optimalStrategy = approximateOptimalSingletonInformationSetStrategy(
            new MarkovBanditGame(8), 16 * 1024)

          optimalStrategy.last must be greaterThan(0.99)
        }
      }

      "Rock Packer Scissors" in {
        val optimalStrategy = approximateOptimalSingletonInformationSetStrategy(
          RockPaperScissorsGame, 1024)

        // (roughly) equal distribution
        optimalStrategy.min must be greaterThan(0.32)
      }
    }

    "Solve sample problems from Wikipedia" in {
      "Perfect and complete information" in {
        val optimalStrategyProfile = approximateOptimalStrategy(
          PerfectCompleteGame, 256)._2

        val firstStrategy = optimalStrategyProfile.averageStrategy(
          PerfectCompleteDecisionFirstInfo, 2)

        firstStrategy(0) must be greaterThan(0.99)

        val afterUpStrategy = optimalStrategyProfile.averageStrategy(
          PerfectCompleteDecisionAfterUpInfo, 2)

        afterUpStrategy(1) must be greaterThan(0.99)

        val afterDownStrategy = optimalStrategyProfile.averageStrategy(
          PerfectCompleteDecisionAfterDownInfo, 2)

        afterDownStrategy(0) must be greaterThan(0.66)
      }

      "Imperfect information" in {
        val optimalStrategyProfile = approximateOptimalStrategy(
          ImperfectCompleteGame, 128)._2

        val firstStrategy = optimalStrategyProfile.averageStrategy(
          ImperfectCompleteDecisionFirstInfo, 2)

        firstStrategy(1) must be greaterThan(0.99)

        val secondStrategy = optimalStrategyProfile.averageStrategy(
          ImperfectCompleteDecisionSecondInfo, 2)

        secondStrategy(0) must be greaterThan(0.99)
      }

      "Incomplete information (non-zero-sum adjusted)" in {
        // see adjustment in: IncompleteTerminal -> IncompleteTypeOne -> IncompleteActionDown

        val optimalStrategyProfile = approximateOptimalStrategy(
          IncompleteGame, 256)._2

        val playerOneTypeOneStrategy = optimalStrategyProfile.averageStrategy(
          IncompleteInfoPlayerOneTypeOne, 2)

        playerOneTypeOneStrategy(IncompleteActionDown.index) must be greaterThan(0.99)

        val playerOneTypeTwoStrategy = optimalStrategyProfile.averageStrategy(
          IncompleteInfoPlayerOneTypeTwo, 2)

        playerOneTypeTwoStrategy(IncompleteActionUp.index) must be greaterThan(0.99)

        // playerTwoAfterUpStrategy is irrelevant(i.e. all actions produce same outcome)

        val playerTwoAfterDownStrategy = optimalStrategyProfile.averageStrategy(
          IncompleteInfoPlayerTwoAfterDown, 2)

        playerTwoAfterDownStrategy(IncompleteActionUp.index) must be greaterThan(0.99)
      }

      "Zero sum" in {
        val optimalStrategyProfile = approximateOptimalStrategy(
          ZeroSumGame, 64)._2

        val redStrategy = optimalStrategyProfile.averageStrategy(
          ZeroSumInfoRed, 2)

        redStrategy(0) must be greaterThan(4.0/7 - 0.01)

        val blueStrategy = optimalStrategyProfile.averageStrategy(
          ZeroSumInfoBlue, 3)

        blueStrategy(0) must be lessThan(0.01)
        blueStrategy(1) must be greaterThan(4.0/7 - 0.01)
      }
    }
  }
}