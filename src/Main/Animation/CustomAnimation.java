package Main.Animation;

import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;


public abstract class CustomAnimation {

    private Timeline timeline;
    private Node node;


    public CustomAnimation(Node node, int circleCount,boolean reverse) {
        super();
        setNode(node);
    }

    private CustomAnimation onFinished() {
//        if (reset) {
//            resetNode();
//        }
//        if (this.nextAnimation != null) {
//            this.nextAnimation.play();
//        }
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

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
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

    public final void setOnFinished(EventHandler<ActionEvent> value) {
        this.timeline.setOnFinished(value);
    }

}