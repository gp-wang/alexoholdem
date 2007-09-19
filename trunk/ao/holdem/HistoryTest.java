package ao.holdem;

import ao.holdem.bots.hist.BlindBot;
import ao.holdem.bots.hist.PredictorBot;
import ao.holdem.def.history_bot.BotHandle;
import ao.holdem.history.persist.PlayerHandleLookup;
import ao.holdem.history_game.Dealer;
import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.Arrays;

/**
 *
 */
public class HistoryTest
{
    //--------------------------------------------------------------------
    @Inject Provider<Dealer>   dealerProvider;
    @Inject PlayerHandleLookup players;


    //--------------------------------------------------------------------
    public void historyTest()
    {        
        Dealer dealer = dealerProvider.get();
        dealer.configure(Arrays.<BotHandle>asList(
                new BotHandle(players.lookup("blind"), new BlindBot()),
                new BotHandle(players.lookup("predictor"), new PredictorBot())));

        for (int i = 0; i < 2000; i++)
        {
            dealer.play();
        }
    }
}