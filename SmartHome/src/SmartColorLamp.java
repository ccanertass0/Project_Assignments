public class SmartColorLamp extends SmartLamp{
    private String colorCode;

    public SmartColorLamp(String name) {
        super(name);
    }

    public SmartColorLamp(String name, String initial_status) {
        super(name, initial_status);
    }

    public SmartColorLamp(String name, String initial_status, int kelvin, int brightness) {
        super(name, initial_status, kelvin, brightness);
    }

    public SmartColorLamp(String name, String initial_status, String colorCode, int brightness) {
        super(name, initial_status, 4000, brightness);
        this.colorCode = colorCode;
    }

    @Override
    public String toString() {
        return "SmartColorLamp{" +
                "colorCode='" + colorCode + '\'' +
                "} " + super.toString();
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}
