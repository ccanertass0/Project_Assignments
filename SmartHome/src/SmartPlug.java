import java.time.Duration;
import java.time.LocalDateTime;

public class SmartPlug extends SmartDevices{
    private double ampere;

    private boolean isSmtPluggedIn;

    private LocalDateTime startedConsumingAt;
    private double energyConsumed;




    public SmartPlug(String name) {
        super(name);
        this.ampere = 0;
    }

    public SmartPlug(String name, String initial_status) {
        super(name, initial_status);
        this.ampere = 0;
    }

    public SmartPlug(String name, String initial_status, double ampere) {
        super(name, initial_status);
        this.ampere = ampere;
        this.isSmtPluggedIn = true;
    }

    @Override
    public String toString() {
        return "SmartPlug{" +
                "ampere=" + ampere +
                "} " + super.toString();
    }



    public void setAmpere(double ampere) {
        this.ampere = ampere;
    }



    public boolean isSmtPluggedIn() {
        return isSmtPluggedIn;
    }

    public void setSmtPluggedIn(boolean smtPluggedIn) {
        isSmtPluggedIn = smtPluggedIn;
    }

    /**

     * @param startedConsumingAt is the time when this condition starts being true : switchStatus = "On" and isPLuggedIn = true
     */
    public void setStartedConsumingAt(LocalDateTime startedConsumingAt) {
        this.startedConsumingAt = startedConsumingAt;
    }


    /**
     * the amount of time switch status is on and smth. is plugged in
     * is the variable called hour.
     * @param stopTime when the status these both are on (swithStatus = "On" , isPluggedin = true) stops
     *      * being true ,at that time the method below is executed and watt value gets updated.
     */
    public void updateWatt(LocalDateTime stopTime){
        Duration duration = Duration.between(startedConsumingAt, stopTime);
        double hour = (double) duration.toMinutes()/60;
        energyConsumed += ampere * 220 * hour;


    }

    public double getEnergyConsumed() {
        return energyConsumed;
    }
}
