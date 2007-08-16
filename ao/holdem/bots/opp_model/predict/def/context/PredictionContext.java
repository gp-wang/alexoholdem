package ao.holdem.bots.opp_model.predict.def.context;

/**
 *
 */
public interface PredictionContext
{
    public Class<? extends PredictionContext> predictionType();

    public double[] neuralInput();
    public int      neuralInputSize();
}
