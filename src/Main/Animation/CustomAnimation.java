package Main.Animation;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;


/**
 * @author Loïc Sculier aka typhon0
 */
public abstract class CustomAnimation {

    /**
     * Used to specify an animation that repeats indefinitely, until the
     * {@code stop()} method is called.
     */
    public static final int INDEFINITE = -1;
    private Timeline timeline;
    private boolean reset;
    private Node node;
    private CustomAnimation nextAnimation;
    private boolean hasNextAnimation;


    public CustomAnimation(Node node) {
        super();
        setNode(node);

    }

    public CustomAnimation() {
        hasNextAnimation = false;
        this.reset = false;
    }

    private CustomAnimation onFinished() {
        if (reset) {
            resetNode();
        }
        if (this.nextAnimation != null) {
            this.nextAnimation.play();
        }
        return this;
    }

    public CustomAnimation playOnFinished(CustomAnimation animation) {
        setNextAnimation(animation);
        return this;

    }

    public CustomAnimation setResetOnFinished(boolean reset) {
        this.reset = reset;
        return this;
    }

    public void play() {
        timeline.play();
    }

    public CustomAnimation stop() {
        timeline.stop();
        return this;
    }

    abstract CustomAnimation resetNode();

    abstract void initTimeline();


    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public boolean isResetOnFinished() {
        return reset;
    }

    protected void setReset(boolean reset) {
        this.reset = reset;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
        initTimeline();
        timeline.statusProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(Animation.Status.STOPPED)) {
                onFinished();
            }

        });
    }

    public CustomAnimation getNextAnimation() {
        return nextAnimation;
    }

    protected void setNextAnimation(CustomAnimation nextAnimation) {
        hasNextAnimation = true;
        this.nextAnimation = nextAnimation;
    }

    public boolean hasNextAnimation() {
        return hasNextAnimation;
    }

    protected void setHasNextAnimation(boolean hasNextAnimation) {
        this.hasNextAnimation = hasNextAnimation;
    }

    public CustomAnimation setCycleCount(int value) {
        this.timeline.setCycleCount(value);
        return this;
    }

    public CustomAnimation setSpeed(double value) {
        this.timeline.setRate(value);
        return this;
    }

    public CustomAnimation setDelay(Duration value) {
        this.timeline.setDelay(value);
        return this;
    }

    public final void setOnFinished(EventHandler<ActionEvent> value) {
        this.timeline.setOnFinished(value);
    }

}