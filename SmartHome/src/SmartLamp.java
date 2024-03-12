public class SmartLamp extends SmartDevices{
    protected int kelvin;
    protected int brightness;

    public SmartLamp(String name) {
        super(name);
        this.kelvin = 4000;
        this.brightness = 100;
    }

    public SmartLamp(String name, String initial_status) {
        this(name, initial_status, 4000, 100);
    }

    public SmartLamp(String name, String initial_status, int kelvin, int brightness) {
        super(name, initial_status);
        this.kelvin = kelvin;
        this.brightness = brightness;
    }

    @Override
    public String toString() {
        return "SmartLamp{" +
                "kelvin=" + kelvin +
                ", brightness=" + brightness +
                "} " + super.toString();
    }

    public int getKelvin() {
        return kelvin;
    }

    public void setKelvin(int kelvin) {
        this.kelvin = kelvin;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }
}
