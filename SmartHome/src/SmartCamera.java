import java.time.Duration;
import java.time.LocalDateTime;

public class SmartCamera extends SmartDevices{
    private double mb_cons_per_min;
    private double usedStorage;
    private LocalDateTime startedRunningAt;  // if the camera is on , it started running at this tim

    public SmartCamera(String name, double mb_cons_per_min) {
        super(name);
        this.mb_cons_per_min = mb_cons_per_min;
    }

    public SmartCamera(String name, double mb_cons_per_min, String initial_status) {
        super(name, initial_status);
        this.mb_cons_per_min = mb_cons_per_min;
    }

    @Override
    public String toString() {
        return "SmartCamera{" +
                "megabytes_consumed_per_second=" + mb_cons_per_min +
                "} " + super.toString();
    }


    public LocalDateTime getStartedRunningAt() {
        return startedRunningAt;
    }

    public void setStartedRunningAt(LocalDateTime startedRunningAt) {
        this.startedRunningAt = startedRunningAt;
    }


    /**
     * This method updates the variable usedStorage variable in this class
     * @param currentTime is the time when camere is switched off from on.
     */
    public void updateCons(LocalDateTime currentTime){
        Duration duration = Duration.between(startedRunningAt, currentTime);
        double minutes = duration.toMinutes();
        usedStorage += minutes * mb_cons_per_min;
    }

    public double getMb_cons_per_min() {
        return mb_cons_per_min;
    }

    public double getUsedStorage() {
        return usedStorage;
    }
}
