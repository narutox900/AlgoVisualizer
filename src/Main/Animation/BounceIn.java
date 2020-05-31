package Main.Animation;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class BounceIn extends CustomAnimation{

    public BounceIn(Node node, int circleCount, boolean reverse) {
        super(node, circleCount, reverse);

    }

    @Override
    CustomAnimation resetNode() {
        return this;
    }

    @Override
    void initTimeline() {
        setTimeline(new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(getNode().opacityProperty(), 0, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                        new KeyValue(getNode().scaleXProperty(), 0.3, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                        new KeyValue(getNode().scaleYProperty(), 0.3, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000))
                ),
                new KeyFrame(Duration.millis(100),
                        new KeyValue(getNode().scaleXProperty(), 1.1, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                        new KeyValue(getNode().scaleYProperty(), 1.1, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000))
                ),
                new KeyFrame(Duration.millis(200),
                        new KeyValue(getNode().scaleXProperty(), 0.9, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                        new KeyValue(getNode().scaleYProperty(), 0.9, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000))
                ),
                new KeyFrame(Duration.millis(300),
                        new KeyValue(getNode().opacityProperty(), 1, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                        new KeyValue(getNode().scaleXProperty(), 1.03, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                        new KeyValue(getNode().scaleYProperty(), 1.03, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000))
                ),
                new KeyFrame(Duration.millis(400),
                        new KeyValue(getNode().scaleXProperty(), 0.97, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                        new KeyValue(getNode().scaleYProperty(), 0.97, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000))
                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(getNode().opacityProperty(), 1, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                        new KeyValue(getNode().scaleXProperty(), 1, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000)),
                        new KeyValue(getNode().scaleYProperty(), 1, Interpolator.SPLINE(0.215, 0.610, 0.355, 1.000))
                )

        ));
    }
}
