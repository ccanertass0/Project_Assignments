import java.time.LocalDateTime;

public class SmartDevices {
    protected String name;
    protected String initial_status;
    protected String currentStatus;
    protected LocalDateTime switchTime;
    protected LocalDateTime lastSwitchTime;

    public SmartDevices(String name, String initial_status) {
        this.name = name;
        this.initial_status = initial_status;
        this.currentStatus = initial_status;
    }

    public SmartDevices(String name) {
        this(name, "Off");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SmartDevices{" +
                "name='" + name + '\'' +
                ", initial_status='" + initial_status + '\'' +
                ", currentStatus='" + currentStatus + '\'' +
                ", switchTime=" + switchTime +
                '}';
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public LocalDateTime getSwitchTime() {
        return switchTime;
    }

    public void setSwitchTime(LocalDateTime switchTime) {
        this.switchTime = switchTime;
    }

    public LocalDateTime getLastSwitchTime() {
        return lastSwitchTime;
    }


    /**
     * Devices may have switchTime's and these switches are executed
     * when the time comes. So, to provide an extremely useful tool while sorting
     * when switchTime becomes null lastSwitchTime is set as the time last switch operation
     * executed on the device
     * @param lastSwitchTime
     */
    public void setLastSwitchTime(LocalDateTime lastSwitchTime) {
        this.lastSwitchTime = lastSwitchTime;
    }
}
