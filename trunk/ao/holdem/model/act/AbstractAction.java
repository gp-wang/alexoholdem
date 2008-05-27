package ao.holdem.model.act;

import ao.holdem.model.Chips;


/**
 * 
 */
public enum AbstractAction
{
    //--------------------------------------------------------------------
    QUIT_FOLD(FallbackAction.CHECK_OR_FOLD){
        public Action toAction(
                Chips toCall, boolean betMadeThisRound) {
            return Action.FOLD;
        }},
    CHECK_CALL(FallbackAction.CHECK_OR_CALL){
        public Action toAction(
                Chips toCall, boolean betMadeThisRound) {
            return toCall.equals( Chips.ZERO )
                   ? Action.CHECK
                   : Action.CALL;
        }},
    BET_RAISE(FallbackAction.RAISE_OR_CALL){
        public Action toAction(
                Chips toCall, boolean betMadeThisRound) {
            return betMadeThisRound
                   ? Action.RAISE
                   : Action.BET;
        }};


    //--------------------------------------------------------------------
    private final FallbackAction FALLACK_ACTION;


    //--------------------------------------------------------------------
    private AbstractAction(FallbackAction fallbackAction)
    {
        FALLACK_ACTION = fallbackAction;
    }


    //--------------------------------------------------------------------
//    public Action fallback(HandState state)
//    {
//        return fallback(state.toCall(),
//                            state.remainingBetsInRound() < 4);
//    }

    public abstract Action toAction(
            Chips toCall,
            boolean betMadeThisRound);
//    {
//        switch (this)
//        {
//            case FOLD:
//                return RealAction.FOLD;
//
//            case CHECK_CALL:
//                return toCall.equals( Money.ZERO )
//                        ? RealAction.CHECK
//                        : RealAction.CHECK_CALL;
//
//            case BET_RAISE:
//                return betMadeThisRound
//                        ? RealAction.BET_RAISE
//                        : RealAction.BET;
//        }
//        throw new Error("should never be here");
//    }


    //--------------------------------------------------------------------
    public FallbackAction toFallbackAction()
    {
        return FALLACK_ACTION;
    }
}
