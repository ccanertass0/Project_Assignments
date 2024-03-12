import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Objects;

public class SmartHome {
    private SmartDevices[] smartDevices = new SmartDevices[0]; // where i store my devices
    private String inputFile;
    private String outputFile;
    private String[] inputArray;
    private LocalDateTime currentTime;
    private LocalDateTime initialTime;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
    private String[] sortedArray = new String[0];
    WriteOutput outTxt;


    /** This constructor has two parameters
     * @param inputFile  is the name of the input file
     * @param outputFile  is the name of the output file we will create
     * @throws FileNotFoundException
     *
     */



    public SmartHome(String inputFile, String outputFile) throws FileNotFoundException {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        ReadFile readFile = new ReadFile(inputFile);
        readFile.readAndAppend();
        this.inputArray = readFile.getInputArray();
        outTxt = new WriteOutput(outputFile);
    }


    /** This method where all the necessary actions are taken
     * every line of inputFile is read, understood here.
     * @throws IOException
     */
    public void process() throws IOException {


        outTxt.createWriter();


        inputArray = getArrayOfActualCommands(inputArray);

        
        boolean isInitialTimeSet = false;
        int i = 0;
        for(String command : inputArray){
            try {
                if (!command.equals("")) {
                    System.out.println("COMMAND: " + command);
                    outTxt.write("COMMAND: " + command + "\n");
                }
                String[] commandArray = command.split("\t");
                String keyCommand = commandArray[0];
                if (i++ == 0) {
                    if ((!keyCommand.equals("SetInitialTime") && !keyCommand.equals("")) && commandArray.length != 2) {
                        System.out.println("ERROR: First command must be set initial time! Program is going to terminate!");
                        outTxt.write("ERROR: First command must be set initial time! Program is going to terminate!\n");
                        break;
                    } else {
                        try {
                            commandArray[1] = setTimeToNormal(commandArray[1]);
                            LocalDateTime time = LocalDateTime.parse(commandArray[1], formatter);
                        } catch (DateTimeParseException e) {
                            System.out.println("ERROR: Format of the initial date is wrong! Program is going to terminate!");
                            outTxt.write("ERROR: Format of the initial date is wrong! Program is going to terminate!\n");
                            break;
                        } catch (Exception e2) {

                        }
                    }
                }

                int words = commandArray.length;
                switch (keyCommand) {
                    case "Add":
                        if (words < 3 || words > 6) {   //   guarenteed  6 >= words >= 3
                            System.out.println("ERROR: Erroneous command!");
                            outTxt.write("ERROR: Erroneous command!\n");
                            break;
                        } else {
                            String device = commandArray[1];
                            String nameOfTheDevice = commandArray[2];
                            switch (device) {
                                case "SmartPlug":
                                    switch (words) {
                                        case 3:
                                            if (isExisting(nameOfTheDevice)) {
                                                System.out.println("ERROR: There is already a smart device with same name!");
                                                outTxt.write("ERROR: There is already a smart device with same name!\n");
                                                break;
                                            } else {
                                                smartDevices = Arrays.copyOf(smartDevices, smartDevices.length + 1);
                                                smartDevices[smartDevices.length - 1] = new SmartPlug(nameOfTheDevice);
                                            }
                                            break;
                                        case 4:
                                            String initialStatus = commandArray[3];
                                            if (isExisting(nameOfTheDevice)) {
                                                System.out.println("ERROR: There is already a smart device with same name!");
                                                outTxt.write("ERROR: There is already a smart device with same name!\n");
                                                break;
                                            } else if (!(Objects.equals(initialStatus, "On") || Objects.equals(initialStatus, "Off"))) {
                                                System.out.println("ERROR: Erroneous command!");
                                                outTxt.write("ERROR: Erroneous command!\n");
                                                break;
                                            } else {
                                                smartDevices = Arrays.copyOf(smartDevices, smartDevices.length + 1);
                                                smartDevices[smartDevices.length - 1] = new SmartPlug(nameOfTheDevice, initialStatus);
                                            }
                                            break;
                                        case 5:
                                            String initialStatus2 = commandArray[3];

                                            if (isExisting(nameOfTheDevice)) {
                                                System.out.println("ERROR: There is already a smart device with same name!");
                                                outTxt.write("ERROR: There is already a smart device with same name!\n");
                                                break;
                                            } else if (!(Objects.equals(initialStatus2, "On") || Objects.equals(initialStatus2, "Off"))) {
                                                System.out.println("ERROR: Erroneous command!");
                                                outTxt.write("ERROR: Erroneous command!\n");
                                                break;
                                            }
                                            double ampere = Double.parseDouble(commandArray[4]);
                                            if (ampere <= 0) {
                                                System.out.println("ERROR: Ampere value must be a positive number!");
                                                outTxt.write("ERROR: Ampere value must be a positive number!\n");
                                                break;
                                            } else {
                                                smartDevices = Arrays.copyOf(smartDevices, smartDevices.length + 1);
                                                smartDevices[smartDevices.length - 1] = new SmartPlug(nameOfTheDevice, initialStatus2, ampere);
                                                if (initialStatus2.equals("On")) {
                                                    ((SmartPlug) getDeviceObject(nameOfTheDevice)).setStartedConsumingAt(currentTime);
                                                }
                                            }
                                            break;
                                        default:
                                            System.out.println("ERROR: Erroneous command!");
                                            outTxt.write("ERROR: Erroneous command!\n");
                                            break;
                                    }
                                    break;
                                case "SmartCamera":
                                    switch (words) {
                                        case 4:

                                            if (isExisting(nameOfTheDevice)) {
                                                System.out.println("ERROR: There is already a smart device with same name!");
                                                outTxt.write("ERROR: There is already a smart device with same name!\n");
                                                break;
                                            }
                                            int mbPerSecond = Integer.parseInt(commandArray[3]);
                                            if (mbPerSecond <= 0) {
                                                System.out.println("ERROR: Megabyte value must be a positive number!");
                                                outTxt.write("ERROR: Megabyte value must be a positive number!\n");
                                                break;
                                            } else {
                                                smartDevices = Arrays.copyOf(smartDevices, smartDevices.length + 1);
                                                smartDevices[smartDevices.length - 1] = new SmartCamera(nameOfTheDevice, mbPerSecond);
                                            }
                                            break;
                                        case 5:

                                            String initialStatus3 = commandArray[4];
                                            if (isExisting(nameOfTheDevice)) {
                                                System.out.println("ERROR: There is already a smart device with same name!");
                                                outTxt.write("ERROR: There is already a smart device with same name!\n");
                                                break;
                                            }
                                            double mbPerSecond2 = Double.parseDouble(commandArray[3]);
                                            if (mbPerSecond2 <= 0) {
                                                System.out.println("ERROR: Megabyte value must be a positive number!");
                                                outTxt.write("ERROR: Megabyte value must be a positive number!\n");
                                                break;
                                            } else if (!(Objects.equals(initialStatus3, "On") || Objects.equals(initialStatus3, "Off"))) {
                                                System.out.println("ERROR: Erroneous command!");
                                                outTxt.write("ERROR: Erroneous command!\n");
                                                break;
                                            } else {
                                                smartDevices = Arrays.copyOf(smartDevices, smartDevices.length + 1);
                                                smartDevices[smartDevices.length - 1] = new SmartCamera(nameOfTheDevice, mbPerSecond2, initialStatus3);
                                                if (initialStatus3.equals("On")) {
                                                    ((SmartCamera) getDeviceObject(nameOfTheDevice)).setStartedRunningAt(currentTime);
                                                }
                                            }
                                            break;
                                        default:
                                            System.out.println("ERROR: Erroneous command!");
                                            outTxt.write("ERROR: Erroneous command!\n");
                                            break;
                                    }
                                    break;
                                case "SmartLamp":
                                    switch (words) {
                                        case 3:
                                            if (isExisting(nameOfTheDevice)) {
                                                System.out.println("ERROR: There is already a smart device with same name!");
                                                outTxt.write("ERROR: There is already a smart device with same name!\n");
                                                break;
                                            } else {
                                                smartDevices = Arrays.copyOf(smartDevices, smartDevices.length + 1);
                                                smartDevices[smartDevices.length - 1] = new SmartLamp(nameOfTheDevice);
                                            }
                                            break;

                                        case 4:
                                            String initialStatus4 = commandArray[3];
                                            if (isExisting(nameOfTheDevice)) {
                                                System.out.println("ERROR: There is already a smart device with same name!");
                                                outTxt.write("ERROR: There is already a smart device with same name!\n");
                                                break;
                                            }else if (!(Objects.equals(initialStatus4, "On") || Objects.equals(initialStatus4, "Off"))) {
                                                System.out.println("ERROR: Erroneous command!");
                                                outTxt.write("ERROR: Erroneous command!\n");
                                                break;
                                            } else {
                                                smartDevices = Arrays.copyOf(smartDevices, smartDevices.length + 1);
                                                smartDevices[smartDevices.length - 1] = new SmartLamp(nameOfTheDevice, initialStatus4);
                                            }
                                            break;
                                        case 6:
                                            String initialStatus5 = commandArray[3];
                                            if (isExisting(nameOfTheDevice)) {
                                                System.out.println("ERROR: There is already a smart device with same name!");
                                                outTxt.write("ERROR: There is already a smart device with same name!\n");
                                                break;
                                            }else if (!(Objects.equals(initialStatus5, "On") || Objects.equals(initialStatus5, "Off"))) {
                                                System.out.println("ERROR: Erroneous command!");
                                                outTxt.write("ERROR: Erroneous command!\n");
                                                break;
                                            }
                                            int kelvin = Integer.parseInt(commandArray[4]);
                                            if (kelvin < 2000 || kelvin > 6500) {
                                                System.out.println("ERROR: Kelvin value must be in range of 2000K-6500K!\n");
                                                outTxt.write("ERROR: Kelvin value must be in range of 2000K-6500K!\n");
                                                break;
                                            }

                                            int brightness = Integer.parseInt(commandArray[5]);
                                            if (brightness < 0 || brightness > 100) {
                                                System.out.println("ERROR: Brightness must be in range of 0%-100%!");
                                                outTxt.write("ERROR: Brightness must be in range of 0%-100%!\n");
                                                break;
                                            } else {
                                                smartDevices = Arrays.copyOf(smartDevices, smartDevices.length + 1);
                                                smartDevices[smartDevices.length - 1] = new SmartLamp(nameOfTheDevice, initialStatus5, kelvin, brightness);
                                            }
                                            break;

                                        default:
                                            System.out.println("ERROR: Erroneous command!");
                                            outTxt.write("ERROR: Erroneous command!\n");
                                            break;
                                    }
                                    break;

                                case "SmartColorLamp":
                                    switch (words) {
                                        case 3:
                                            if (isExisting(nameOfTheDevice)) {
                                                System.out.println("ERROR: There is already a smart device with same name!");
                                                outTxt.write("ERROR: There is already a smart device with same name!\n");
                                                break;
                                            } else {
                                                smartDevices = Arrays.copyOf(smartDevices, smartDevices.length + 1);
                                                smartDevices[smartDevices.length - 1] = new SmartColorLamp(nameOfTheDevice);
                                            }
                                            break;
                                        case 4:
                                            String initialStatus6 = commandArray[3];
                                            if (isExisting(nameOfTheDevice)) {
                                                System.out.println("ERROR: There is already a smart device with same name!");
                                                outTxt.write("ERROR: There is already a smart device with same name!\n");
                                                break;
                                            } else if (!(Objects.equals(initialStatus6, "On") || Objects.equals(initialStatus6, "Off"))) {
                                                System.out.println("ERROR: Erroneous command!");
                                                outTxt.write("ERROR: Erroneous command!\n");
                                                break;
                                            } else {
                                                smartDevices = Arrays.copyOf(smartDevices, smartDevices.length + 1);
                                                smartDevices[smartDevices.length - 1] = new SmartColorLamp(nameOfTheDevice, initialStatus6);
                                            }
                                            break;

                                        case 6:
                                            String initialStatus7 = commandArray[3];
                                            String colorOrInt = commandArray[4];
                                            boolean isPossiblyHex = colorOrInt.contains("0x");
                                            int brightness;
                                            if (isExisting(nameOfTheDevice)) {
                                                System.out.println("ERROR: There is already a smart device with same name!");
                                                outTxt.write("ERROR: There is already a smart device with same name!\n");
                                                break;
                                            } else if (!(Objects.equals(initialStatus7, "On") || Objects.equals(initialStatus7, "Off"))) {
                                                System.out.println("ERROR: Erroneous command!");
                                                outTxt.write("ERROR: Erroneous command!\n");
                                                break;
                                            } else if (isPossiblyHex) {
                                                if (colorOrInt.substring(2).length() != 6) {
                                                    System.out.println("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
                                                    outTxt.write("ERROR: Color code value must be in range of 0x0-0xFFFFFF!\n");
                                                    break;
                                                } else if (!isHexadecimal(colorOrInt.substring(2))) {
                                                    System.out.println("ERROR: Erroneous command!");
                                                    outTxt.write("ERROR: Erroneous command!\n");
                                                    break;
                                                }else {  // it is hexadecimal
                                                    brightness = Integer.parseInt(commandArray[5]);
                                                    if (brightness < 0 || brightness > 100) {
                                                        System.out.println("ERROR: Brightness must be in range of 0%-100%!");
                                                        outTxt.write("ERROR: Brightness must be in range of 0%-100%!\n");
                                                        break;
                                                    }
                                                    smartDevices = Arrays.copyOf(smartDevices, smartDevices.length + 1);
                                                    smartDevices[smartDevices.length - 1] = new SmartColorLamp(nameOfTheDevice, initialStatus7, colorOrInt, brightness);
                                                }
                                            }else{
                                                int kelvin = Integer.parseInt(colorOrInt);
                                                if (kelvin < 2000 || kelvin > 6500) {
                                                    System.out.println("ERROR: Kelvin value must be in range of 2000K-6500K!");
                                                    outTxt.write("ERROR: Kelvin value must be in range of 2000K-6500K!\n");
                                                    break;
                                                }
                                                brightness = Integer.parseInt(commandArray[5]);
                                                if (brightness < 0 || brightness > 100) {
                                                    System.out.println("ERROR: Brightness must be in range of 0%-100%!");
                                                    outTxt.write("ERROR: Brightness must be in range of 0%-100%!\n");
                                                    break;
                                                }
                                                smartDevices = Arrays.copyOf(smartDevices, smartDevices.length + 1);
                                                smartDevices[smartDevices.length - 1] = new SmartColorLamp(nameOfTheDevice, initialStatus7, kelvin, brightness);
                                            }

                                            break;
                                        default:
                                            System.out.println("ERROR: Erroneous command!");
                                            outTxt.write("ERROR: Erroneous command!\n");
                                            break;
                                    }
                                    break;
                            }
                        }
                        break;

                    case "Remove":
                        if (words != 2) {
                            System.out.println("ERROR: Erroneous command!");
                            outTxt.write("ERROR: Erroneous command!\n");
                            break;
                        } else {
                            String nameOfTheDevice = commandArray[1];
                            if (!isExisting(nameOfTheDevice)) {
                                System.out.println("ERROR: There is not such a device!");
                                outTxt.write("ERROR: There is not such a device!\n");
                                break;
                            } else {
                                System.out.println("SUCCESS: Information about removed smart device is as follows:");
                                outTxt.write("SUCCESS: Information about removed smart device is as follows:\n");

                                if (getDeviceObject(nameOfTheDevice).getCurrentStatus().equals("On")) {
                                    if (getDeviceObject(nameOfTheDevice) instanceof SmartCamera) {

                                        ((SmartCamera) getDeviceObject(nameOfTheDevice)).updateCons(currentTime);
                                        ((SmartCamera) getDeviceObject(nameOfTheDevice)).setStartedRunningAt(null);

                                    } else if (getDeviceObject(nameOfTheDevice) instanceof SmartPlug) {

                                        if (((SmartPlug) getDeviceObject(nameOfTheDevice)).isSmtPluggedIn()) {
                                            ((SmartPlug) getDeviceObject(nameOfTheDevice)).updateWatt(currentTime);
                                            ((SmartPlug) getDeviceObject(nameOfTheDevice)).setStartedConsumingAt(null);
                                        }

                                    }
                                    getDeviceObject(nameOfTheDevice).setCurrentStatus("Off");
                                }
                                if (getDeviceObject(nameOfTheDevice) instanceof SmartColorLamp) {


                                    System.out.println("Smart Color Lamp " + nameOfTheDevice + " is " +
                                            getDeviceObject(nameOfTheDevice).getCurrentStatus().toLowerCase() + " and its color value is " +
                                            ((SmartColorLamp) getDeviceObject(nameOfTheDevice)).getKelvin() + "K with " +
                                            ((SmartColorLamp) getDeviceObject(nameOfTheDevice)).getBrightness() + "% brightness, and its time " +
                                            "to switch its status is " + getDeviceObject(nameOfTheDevice).getSwitchTime() + ".");      //////////// ALLLLLLLLLLLEREEEEEEEEEEERRRRRRRRTTTTTTT switchTimeeee


                                    outTxt.write("Smart Color Lamp " + nameOfTheDevice + " is " +
                                            getDeviceObject(nameOfTheDevice).getCurrentStatus().toLowerCase() + " and its color value is " +
                                            ((SmartColorLamp) getDeviceObject(nameOfTheDevice)).getKelvin() + "K with " +
                                            ((SmartColorLamp) getDeviceObject(nameOfTheDevice)).getBrightness() + "% brightness, and its time " +
                                            "to switch its status is " + getDeviceObject(nameOfTheDevice).getSwitchTime() + ".\n");


                                } else if (getDeviceObject(nameOfTheDevice) instanceof SmartLamp) {


                                    System.out.println("Smart Lamp " + nameOfTheDevice + " is " +
                                            getDeviceObject(nameOfTheDevice).getCurrentStatus().toLowerCase() + " and its kelvin value is " +
                                            ((SmartLamp) getDeviceObject(nameOfTheDevice)).getKelvin() + "K with " +
                                            ((SmartLamp) getDeviceObject(nameOfTheDevice)).getBrightness() + "% brightness, and its time " +
                                            "to switch its status is " + getDeviceObject(nameOfTheDevice).getSwitchTime() + ".");
                                    outTxt.write("Smart Lamp " + nameOfTheDevice + " is " +
                                            getDeviceObject(nameOfTheDevice).getCurrentStatus().toLowerCase() + " and its kelvin value is " +
                                            ((SmartLamp) getDeviceObject(nameOfTheDevice)).getKelvin() + "K with " +
                                            ((SmartLamp) getDeviceObject(nameOfTheDevice)).getBrightness() + "% brightness, and its time " +
                                            "to switch its status is " + getDeviceObject(nameOfTheDevice).getSwitchTime() + ".\n");


                                } else if (getDeviceObject(nameOfTheDevice) instanceof SmartPlug) {


                                    System.out.printf("Smart Plug " + nameOfTheDevice + " is " +
                                                    getDeviceObject(nameOfTheDevice).getCurrentStatus().toLowerCase() + " and consumed " +
                                                    "%.2f" + "W so far (excluding current device), " +
                                                    "and its time to switch its status is " + getDeviceObject(nameOfTheDevice).getSwitchTime() + ".\n",
                                            ((SmartPlug) getDeviceObject(nameOfTheDevice)).getEnergyConsumed());

                                    outTxt.writef("Smart Plug " + nameOfTheDevice + " is " +
                                                    getDeviceObject(nameOfTheDevice).getCurrentStatus().toLowerCase() + " and consumed " +
                                                    "%.2f" + "W so far (excluding current device), " +
                                                    "and its time to switch its status is " + getDeviceObject(nameOfTheDevice).getSwitchTime() + ".\n",
                                            ((SmartPlug) getDeviceObject(nameOfTheDevice)).getEnergyConsumed());


                                } else if (getDeviceObject(nameOfTheDevice) instanceof SmartCamera) {

                                    System.out.printf("Smart Camera " + nameOfTheDevice + " is " +
                                                    getDeviceObject(nameOfTheDevice).getCurrentStatus().toLowerCase() + " and used " +
                                                    "%.2f" + " MB of storage so far (excluding current status), and its time to switch its status is " +
                                                    getDeviceObject(nameOfTheDevice).getSwitchTime() + ".\n",
                                            ((SmartCamera) getDeviceObject(nameOfTheDevice)).getUsedStorage());

                                    outTxt.writef("Smart Camera " + nameOfTheDevice + " is " +
                                                    getDeviceObject(nameOfTheDevice).getCurrentStatus().toLowerCase() + " and used " +
                                                    "%.2f" + " MB of storage so far (excluding current status), and its time to switch its status is " +
                                                    getDeviceObject(nameOfTheDevice).getSwitchTime() + ".\n",
                                            ((SmartCamera) getDeviceObject(nameOfTheDevice)).getUsedStorage());
                                }
                                removeDevice(nameOfTheDevice);
                                arrangeSortedArray();

                            }


                        }
                        break;
                    case "Switch":
                        if (commandArray.length != 3) {
                            System.out.println("ERROR: Erroneous command!");
                            outTxt.write("ERROR: Erroneous command!\n");
                            break;
                        } else {
                            String nameOfTheDevice = commandArray[1];
                            String targetSwitch = commandArray[2];
                            if (!(targetSwitch.equals("Off") || targetSwitch.equals("On"))) {
                                System.out.println("ERROR: Erroneous command!");
                                outTxt.write("ERROR: Erroneous command!\n");
                                break;
                            } else if (!isExisting(nameOfTheDevice)) {
                                System.out.println("ERROR: There is not such a device!");
                                outTxt.write("ERROR: There is not such a device!\n");
                                break;
                            } else if (targetSwitch.equals(getDeviceObject(nameOfTheDevice).getCurrentStatus())) {
                                System.out.println("ERROR: This device is already switched " + targetSwitch.toLowerCase() + "!");
                                outTxt.write("ERROR: This device is already switched " + targetSwitch.toLowerCase() + "!\n");
                                break;
                            } else {

                                if (getDeviceObject(nameOfTheDevice) instanceof SmartCamera) {
                                    if (targetSwitch.equals("On")) {
                                        ((SmartCamera) getDeviceObject(nameOfTheDevice)).setStartedRunningAt(currentTime);
                                    } else {
                                        ((SmartCamera) getDeviceObject(nameOfTheDevice)).updateCons(currentTime);
                                        ((SmartCamera) getDeviceObject(nameOfTheDevice)).setStartedRunningAt(null);
                                    }
                                } else if (getDeviceObject(nameOfTheDevice) instanceof SmartPlug) {
                                    if (targetSwitch.equals("On")) {
                                        if (((SmartPlug) getDeviceObject(nameOfTheDevice)).isSmtPluggedIn()) {
                                            ((SmartPlug) getDeviceObject(nameOfTheDevice)).setStartedConsumingAt(currentTime);
                                        }
                                    } else {       //is on, and will be off
                                        if (((SmartPlug) getDeviceObject(nameOfTheDevice)).isSmtPluggedIn()) {
                                            ((SmartPlug) getDeviceObject(nameOfTheDevice)).updateWatt(currentTime);
                                            ((SmartPlug) getDeviceObject(nameOfTheDevice)).setStartedConsumingAt(null);
                                        }
                                    }
                                }

                                getDeviceObject(nameOfTheDevice).setCurrentStatus(targetSwitch);
                                if (getDeviceObject(nameOfTheDevice).getSwitchTime() != null) {  //switched and switchTime gets deleted.
                                    getDeviceObject(nameOfTheDevice).setSwitchTime(null);
                                }
                            }
                        }
                        break;
                    case "ChangeName":
                        if (commandArray.length != 3) {
                            System.out.println("ERROR: Erroneous command!");
                            outTxt.write("ERROR: Erroneous command!\n");
                            break;
                        } else {
                            String toBeSwitched = commandArray[1];
                            String targetName = commandArray[2];
                            if (targetName.equals(toBeSwitched)) {
                                System.out.println("ERROR: Both of the names are the same, nothing changed!");
                                outTxt.write("ERROR: Both of the names are the same, nothing changed!\n");
                                break;
                            } else if (isExisting(targetName)) {
                                System.out.println("ERROR: There is already a smart device with same name!");
                                outTxt.write("ERROR: There is already a smart device with same name!\n");
                                break;
                            } else if (!isExisting(toBeSwitched)) {
                                System.out.println("ERROR: There is not such a device!");
                                outTxt.write("ERROR: There is not such a device!\n");
                                break;
                            } else {
                                getDeviceObject(toBeSwitched).setName(targetName);
                            }
                        }
                        break;
                    case "SetInitialTime":
                        if (commandArray.length == 1) {
                            System.out.println("EERROR: First command must be set initial time! Program is going to terminate!");
                            outTxt.write("ERROR: First command must be set initial time! Program is going to terminate!\n");
                            break;
                        } else if (commandArray.length > 2) {
                            System.out.println("ERROR: Erroneous command!");
                            outTxt.write("ERROR: Erroneous command!\n");
                            break;
                        } else {
                            String initialTimeString = commandArray[1];

                            if (isInitialTimeSet) {
                                System.out.println("ERROR: Erroneous command!");
                                outTxt.write("ERROR: Erroneous command!\n");
                                break;
                            } else {
                                try {
                                    initialTimeString = setTimeToNormal(initialTimeString);
                                    initialTime = LocalDateTime.parse(initialTimeString, formatter);
                                    currentTime = initialTime;
                                } catch (DateTimeParseException e) {
                                    System.out.println("ERROR: Time format is not correct!");
                                    break;
                                }
                                System.out.println("SUCCESS: Time has been set to " + initialTime.format(formatter) + "!");
                                outTxt.write("SUCCESS: Time has been set to " + initialTime.format(formatter) + "!\n");
                                isInitialTimeSet = true;
                            }
                        }
                        break;
                    case "SetTime":
                        if (commandArray.length != 2) {
                            System.out.println("ERROR: Erroneous command!");
                            outTxt.write("ERROR: Erroneous command!\n");
                            break;
                        } else {
                            String targetTime = setTimeToNormal(commandArray[1]);

                            LocalDateTime tempTime;
                            try {
                                tempTime = LocalDateTime.parse(targetTime, formatter);

                            } catch (DateTimeParseException e) {
                                System.out.println("ERROR: Time format is not correct!");
                                outTxt.write("ERROR: Time format is not correct!\n");
                                break;
                            }

                            // at this point we know our time is legal, now it's time to check it

                            if (tempTime.isBefore(currentTime)) {
                                System.out.println("ERROR: Time cannot be reversed!");
                                outTxt.write("ERROR: Time cannot be reversed!\n");
                                break;
                            } else if (tempTime.isEqual(currentTime)) {
                                System.out.println("ERROR: There is nothing to change!");
                                outTxt.write("ERROR: There is nothing to change!\n");
                                break;
                            } else {
                                currentTime = tempTime;
                            }
                            arrangeSwitchTimes();
                        }
                        break;

                    case "SkipMinutes":
                        if (commandArray.length != 2) {
                            System.out.println("ERROR: Erroneous command!");
                            outTxt.write("ERROR: Erroneous command!\n");
                            break;
                        } else {

                            try {
                                int minutesToBeSkipped = Integer.parseInt(commandArray[1]);
                                if (minutesToBeSkipped < 0) {
                                    System.out.println("ERROR: Time cannot be reversed!");
                                    outTxt.write("ERROR: Time cannot be reversed!\n");
                                    break;
                                } else if (minutesToBeSkipped == 0) {
                                    System.out.println("ERROR: There is nothing to skip!");
                                    outTxt.write("ERROR: There is nothing to skip!\n");
                                    break;
                                }
                                currentTime = currentTime.plusMinutes(minutesToBeSkipped);
                            } catch (Exception e) {
                                System.out.println("ERROR: Erroneous command!");
                                outTxt.write("ERROR: Erroneous command!\n");
                            }
                        }

                        arrangeSwitchTimes();
                        break;

                    case "SetSwitchTime":
                        if (commandArray.length != 3) {
                            System.out.println("ERROR: Erroneous command!");
                            outTxt.write("ERROR: Erroneous command!\n");
                            break;
                        } else {
                            String nameOfTheDevice = commandArray[1];
                            LocalDateTime switchTime = null;
                            if (!isExisting(nameOfTheDevice)) {
                                System.out.println("ERROR: There is not such a device!");
                                outTxt.write("ERROR: There is not such a device!\n");
                            } else {
                                String switchTimeStr = setTimeToNormal(commandArray[2]);

                                try {
                                    switchTime = LocalDateTime.parse(switchTimeStr, formatter);
                                    if (switchTime.isBefore(currentTime)) {
                                        System.out.println("ERROR: Switch time cannot be in the past!");
                                        outTxt.write("ERROR: Switch time cannot be in the past!\n");
                                        break;
                                    }
                                } catch (DateTimeParseException e) {
                                    System.out.println("ERROR: Time format is not correct!");
                                    outTxt.write("ERROR: Time format is not correct!\n");
                                    break;
                                }

                                getDeviceObject(nameOfTheDevice).setSwitchTime(switchTime);
                            }
                        }
                        break;

                    case "Nop":
                        LocalDateTime closestTime = LocalDateTime.parse("9999-12-28_23:59:59", formatter);
                        for (SmartDevices smartDevice : smartDevices) {
                            if (smartDevice.getSwitchTime() != null) {
                                if (smartDevice.getSwitchTime().isBefore(closestTime)) {
                                    closestTime = smartDevice.getSwitchTime();
                                }
                            }
                        }

                        if (closestTime.equals(LocalDateTime.parse("9999-12-28_23:59:59", formatter))) {
                            System.out.println("ERROR: There is nothing to switch!");
                            outTxt.write("ERROR: There is nothing to switch!\n");
                            break;
                        } else {
                            currentTime = closestTime;
                            arrangeSwitchTimes();

                        }
                        break;
                    case "SetKelvin":
                        if (commandArray.length != 3) {
                            System.out.println("ERROR: Erroneous command!");
                            outTxt.write("ERROR: Erroneous command!\n");
                            break;
                        } else {
                            String nameOfTheDevice = commandArray[1];

                            if (!isExisting(nameOfTheDevice)) {
                                System.out.println("ERROR: There is not such a device!");
                                outTxt.write("ERROR: There is not such a device!\n");
                                break;
                            } else if (!(getDeviceObject(nameOfTheDevice) instanceof SmartLamp)) {
                                System.out.println("ERROR: This device is not a smart lamp!");
                                outTxt.write("ERROR: This device is not a smart lamp!\n");
                                break;
                            }
                            int kelvin = Integer.parseInt(commandArray[2]);
                            if (kelvin < 2000 || kelvin > 6500) {
                                System.out.println("ERROR: Kelvin value must be in range of 2000K-6500K!");
                                outTxt.write("ERROR: Kelvin value must be in range of 2000K-6500K!\n");
                                break;
                            } else {
                                ((SmartLamp) getDeviceObject(nameOfTheDevice)).setKelvin(kelvin);
                            }
                        }
                        break;

                    case "SetBrightness":
                        if (commandArray.length != 3) {
                            System.out.println("ERROR: Erroneous command!");
                            outTxt.write("ERROR: Erroneous command!\n");
                            break;
                        } else {
                            String nameOfTheDevice = commandArray[1];

                            if (!isExisting(nameOfTheDevice)) {
                                System.out.println("ERROR: There is not such a device!");
                                outTxt.write("ERROR: There is not such a device!\n");
                                break;
                            } else if (!(getDeviceObject(nameOfTheDevice) instanceof SmartLamp)) {
                                System.out.println("ERROR: This device is not a smart lamp!");
                                outTxt.write("ERROR: This device is not a smart lamp!\n");
                                break;
                            }
                            int brightness = Integer.parseInt(commandArray[2]);
                            if (brightness < 0 || brightness > 100) {
                                System.out.println("ERROR: Brightness must be in range of 0%-100%!");
                                outTxt.write("ERROR: Brightness must be in range of 0%-100%!\n");
                                break;
                            } else {
                                ((SmartLamp) getDeviceObject(nameOfTheDevice)).setBrightness(brightness);
                            }
                        }
                        break;
                    case "SetColorCode":
                        if (commandArray.length != 3) {
                            System.out.println("ERROR: Erroneous command!");
                            outTxt.write("ERROR: Erroneous command!\n");
                            break;
                        } else {
                            String nameOfTheDevice = commandArray[1];
                            String colorCode = commandArray[2];
                            boolean isPossiblyHex = colorCode.contains("0x");

                            if (!isExisting(nameOfTheDevice)) {
                                System.out.println("ERROR: There is not such a device!");
                                outTxt.write("ERROR: There is not such a device!\n");
                                break;
                            } else if (!(getDeviceObject(nameOfTheDevice) instanceof SmartColorLamp)) {
                                System.out.println("ERROR: This device is not a smart color lamp!");
                                outTxt.write("ERROR: This device is not a smart color lamp!\n");
                                break;
                            } else if (!isPossiblyHex) {
                                System.out.println("ERROR: Erroneous command!");
                                outTxt.write("ERROR: Erroneous command!\n");
                                break;
                            } else if (colorCode.substring(2).length() != 6) {
                                System.out.println("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
                                outTxt.write("ERROR: Color code value must be in range of 0x0-0xFFFFFF!\n");
                                break;
                            } else if (!isHexadecimal(colorCode.substring(2))) {
                                System.out.println("ERROR: Erroneous command!");
                                outTxt.write("ERROR: Erroneous command!\n");
                                break;
                            } else {
                                ((SmartColorLamp) getDeviceObject(nameOfTheDevice)).setColorCode(colorCode);
                            }
                        }
                        break;

                    case "SetWhite":
                        if (commandArray.length != 4) {
                            System.out.println("ERROR: Erroneous command!");
                            outTxt.write("ERROR: Erroneous command!\n");
                            break;
                        } else {
                            String nameOfTheDevice = commandArray[1];
                            if (!isExisting(nameOfTheDevice)) {
                                System.out.println("ERROR: There is not such a device!");
                                outTxt.write("ERROR: There is not such a device!\n");
                                break;
                            } else if (!(getDeviceObject(nameOfTheDevice) instanceof SmartLamp)) {
                                System.out.println("ERROR: This device is not a smart lamp!");
                                outTxt.write("ERROR: This device is not a smart lamp!\n");
                                break;
                            }
                            int kelvin = Integer.parseInt(commandArray[2]);
                            if (kelvin < 2000 || kelvin > 6500) {
                                System.out.println("ERROR: Kelvin value must be in range of 2000K-6500K!");
                                outTxt.write("ERROR: Kelvin value must be in range of 2000K-6500K!\n");
                                break;
                            }
                            int brightness = Integer.parseInt(commandArray[3]);
                            if (brightness < 0 || brightness > 100) {
                                System.out.println("ERROR: Brightness must be in range of 0%-100%!");
                                outTxt.write("ERROR: Brightness must be in range of 0%-100%!\n");
                                break;
                            } else {
                                ((SmartColorLamp) getDeviceObject(nameOfTheDevice)).setKelvin(kelvin);
                                ((SmartColorLamp) getDeviceObject(nameOfTheDevice)).setBrightness(brightness);
                                //(SmartColorLamp) getDeviceObject(nameOfTheDevice)).setColorCode("0xFFFFFF");
                            }
                        }
                        break;
                    case "SetColor":
                        if (commandArray.length != 4) {
                            System.out.println("ERROR: Erroneous command!");
                            outTxt.write("ERROR: Brightness must be in range of 0%-100%!\n");
                            break;
                        } else {
                            String nameOfTheDevice = commandArray[1];
                            if (!(isExisting(nameOfTheDevice))) {
                                System.out.println("ERROR: There is not such a device!");
                                outTxt.write("ERROR: There is not such a device!\n");
                                break;
                            } else if (!(getDeviceObject(nameOfTheDevice) instanceof SmartColorLamp)) {
                                System.out.println("ERROR: This device is not a smart color lamp!");
                                outTxt.write("ERROR: This device is not a smart color lamp!\n");
                                break;
                            }

                            String colorCode = commandArray[2];
                            boolean isPossiblyHex = colorCode.startsWith("0x");

                            if (!isPossiblyHex) {
                                System.out.println("ERROR: Erroneous command!");
                                outTxt.write("ERROR: Erroneous command!\n");
                                break;
                            } else if (colorCode.substring(2).length() != 6) {
                                System.out.println("ERROR: Color code value must be in range of 0x0-0xFFFFFF!");
                                outTxt.write("ERROR: Color code value must be in range of 0x0-0xFFFFFF!\n");
                                break;
                            } else if (!isHexadecimal(colorCode.substring(2))) {
                                System.out.println("ERROR: Erroneous command!");
                                outTxt.write("ERROR: Erroneous command!\n");
                                break;
                            }
                            int brightness = Integer.parseInt(commandArray[3]);
                            if (brightness < 0 || brightness > 100) {
                                System.out.println("ERROR: Brightness must be in range of 0%-100%!");
                                outTxt.write("ERROR: Brightness must be in range of 0%-100%!\n");
                                break;
                            } else {
                                ((SmartColorLamp) getDeviceObject(nameOfTheDevice)).setColorCode(colorCode);
                                ((SmartColorLamp) getDeviceObject(nameOfTheDevice)).setBrightness(brightness);
                            }
                        }
                        break;

                    case "PlugIn":
                        if (commandArray.length != 3) {
                            System.out.println("ERROR: Erroneous command!");
                            outTxt.write("ERROR: Erroneous command!\n");
                            break;
                        } else {
                            String nameOfTheDevice = commandArray[1];
                            if (!(isExisting(nameOfTheDevice))) {
                                System.out.println("ERROR: There is not such a device!");
                                outTxt.write("ERROR: There is not such a device!\n");
                                break;
                            } else if (!(getDeviceObject(nameOfTheDevice) instanceof SmartPlug)) {
                                System.out.println("ERROR: This device is not a smart plug!");
                                outTxt.write("ERROR: This device is not a smart plug!\n");
                                break;
                            } else if (((SmartPlug) getDeviceObject(nameOfTheDevice)).isSmtPluggedIn()) {
                                System.out.println("ERROR: There is already an item plugged in to that plug!");
                                outTxt.write("ERROR: There is already an item plugged in to that plug!\n");
                                break;
                            }

                            double ampere = Double.parseDouble(commandArray[2]);
                            if (ampere <= 0) {
                                System.out.println("ERROR: Ampere value must be a positive number!");
                                outTxt.write("ERROR: Ampere value must be a positive number!\n");
                                break;
                            } else {
                                ((SmartPlug) getDeviceObject(nameOfTheDevice)).setSmtPluggedIn(true);
                                ((SmartPlug) getDeviceObject(nameOfTheDevice)).setAmpere(ampere);
                                if (getDeviceObject(nameOfTheDevice).getCurrentStatus().equals("On")) {
                                    ((SmartPlug) getDeviceObject(nameOfTheDevice)).setStartedConsumingAt(currentTime);
                                    //    System.out.println("Started consuming at = " + currentTime.format(formatter));
                                }

                            }
                        }
                        break;
                    case "PlugOut":
                        if (commandArray.length != 2) {
                            System.out.println("ERROR: Erroneous command!");
                            outTxt.write("ERROR: Erroneous command!\n");
                            break;
                        } else {
                            String nameOfTheDevice = commandArray[1];
                            if (!isExisting(nameOfTheDevice)) {
                                System.out.println("ERROR: There is not such a device!");
                                outTxt.write("ERROR: There is not such a device!\n");
                                break;
                            } else if (!(getDeviceObject(nameOfTheDevice) instanceof SmartPlug)) {
                                System.out.println("ERROR: This device is not a smart plug!");
                                outTxt.write("ERROR: This device is not a smart plug!\n");
                                break;
                            } else if (!((SmartPlug) getDeviceObject(nameOfTheDevice)).isSmtPluggedIn()) {
                                System.out.println("ERROR: This plug has no item to plug out from that plug!");
                                outTxt.write("ERROR: This plug has no item to plug out from that plug!\n");
                                break;
                            } else {
                                if (getDeviceObject(nameOfTheDevice).getCurrentStatus().equals("On")) {
                                    System.out.println("Stopped consuming at = " + currentTime.format(formatter));
                                    outTxt.write("Stopped consuming at = " + currentTime.format(formatter) + "\n");
                                    ((SmartPlug) getDeviceObject(nameOfTheDevice)).updateWatt(currentTime);
                                }
                                ((SmartPlug) getDeviceObject(nameOfTheDevice)).setSmtPluggedIn(false);
                                ((SmartPlug) getDeviceObject(nameOfTheDevice)).setAmpere(0);
                                ((SmartPlug) getDeviceObject(nameOfTheDevice)).setStartedConsumingAt(null);
                            }
                        }

                        break;

                    case "ZReport":
                        System.out.println("Time is:\t" + currentTime.format(formatter));
                        outTxt.write("Time is:\t" + currentTime.format(formatter) + "\n");
                        arrangeSwitchTimes();
                        arrangeSortedArray();
                        printTheZReport();
                        break;
                    default:
                        System.out.println("ERROR: Erroneous command!");
                        outTxt.write("ERROR: Erroneous command!\n");
                        break;


                }
            }catch(Exception e){
                System.out.println("ERROR: Erroneous command!");
                outTxt.write("ERROR: Erroneous command!\n");
            }
            i++;
        }

        if(!inputArray[inputArray.length - 1].equals("ZReport") && isInitialTimeSet){
            System.out.println("ZReport");
            outTxt.write("ZReport:\n");
            System.out.println("Time is:\t" + currentTime.format(formatter));
            outTxt.write("Time is:\t" + currentTime.format(formatter) + "\n");
            printTheZReport();
        }
        outTxt.closeFile();

    }


    /**
     * checks if a device with the given exists in smartDevices array
     * @param name is the name of the device
     * @return true if it exists, false otherwise
     */
    private boolean isExisting(String name){
        for(SmartDevices smartDevice : smartDevices){
            if(smartDevice.getName().equals(name)){
                return true;
            }
        }
        return false;
    }  // isexisting in smartDevices ?

    public SmartDevices[] getSmartDevices(){
        return smartDevices;
    }


    /**
     * Checks if a given sequence of characters is correct
     * @param hexa the sequence of characters
     * @return true if it is hexadecimal, false otherwise
     */
    public boolean isHexadecimal(String hexa){
        char[] hexaBase = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        int matched = 0;
        for(int i = 0;  i < hexa.length(); i++){
            for(int j = 0; j < 16; j++){
                if(hexa.charAt(i) == hexaBase[j]){
                    matched++;
                }
            }
        }
        return (matched == hexa.length());
    }


    /**
     * we are geting the object of the class SmartDevices here,
     * for example we are going to be able to use
     * getDeviceObject(smartDeviceName).getCurrentStatus()
     * with the return value of this method
     * @param smartDeviceName is the name of whose object this method will return
     * @return object of the SmartDevices that has the name smartDeviceName
     */
    private SmartDevices getDeviceObject(String smartDeviceName){
        for(SmartDevices smartDevice : smartDevices){
            if(smartDevice.getName().equals(smartDeviceName)){
                return smartDevice;
            }
        }
        return null;
    }


    /**
     * removes the device from smartDevices array
     * @param smartDeviceName is the name of the device that will be removed from smartDevices array
     */
    private void removeDevice(String smartDeviceName){  //RMOVES DEVICE from smartDevices arra
        int index = 0;

        for(index = 0; index < smartDevices.length; index++){
            if(smartDevices[index].getName().equals(smartDeviceName)){
                smartDevices[index] = null;
                break;
            }
        }

        while (index < smartDevices.length - 1){
            smartDevices[index] = smartDevices[index +1];
            index++;
        }

        smartDevices[smartDevices.length - 1] = null;
        smartDevices = Arrays.copyOf(smartDevices, smartDevices.length - 1);
    }


    /** This is the method where we store names of devices in the order
     * of devices in the ZReport
     * which we will use in the method {@link #printTheZReport()} .
     *
     * This method sorts devices that has non-null switchTime  which is
     * one of the variables that every smart device has see: {@link  SmartDevices}.
     *
     * Then sorts devices that has null switchTimes but has non-null
     * lastSwitchTime in the class {@link  SmartDevices}.
     *
     * Then sorts devices that has both null switchTime and null
     *  lastSwitchTime in the order they are created.
     */
    private void arrangeSortedArray(){

        sortedArray = new String[0];
        int switchDeviceNum = 0;
        for(SmartDevices smartDevices1 : smartDevices){
            if(smartDevices1.getSwitchTime() != null){
                switchDeviceNum++;
            }
        }

        // we know how many device has switcTime now.

        while (sortedArray.length != switchDeviceNum){
            LocalDateTime closestTime = LocalDateTime.parse("9999-12-28_23:59:59", formatter);
            String name = null;
            for(SmartDevices smartDevice : smartDevices){
                if(smartDevice.getSwitchTime() != null && !isExistingInSortedArray(smartDevice.getName())){
                    if(smartDevice.getSwitchTime().isBefore(closestTime)){
                        closestTime = smartDevice.getSwitchTime();
                        name = smartDevice.getName();
                    }
                }
            }
            sortedArray = Arrays.copyOf(sortedArray, sortedArray.length + 1);
            sortedArray[sortedArray.length - 1] = name;
        }



        int c = 0;          // has lastSwitchTime but doesn't have normalSwitchTime
        for(SmartDevices smartDevice1 : smartDevices){
            if(smartDevice1.getSwitchTime() == null && smartDevice1.getLastSwitchTime() != null){
                c++;
            }
        }

        // we know how many device has no switchTime but has lastSwitchTime now.
        // these devices are sorted from closest to currentTime, to farthest

        while (sortedArray.length != switchDeviceNum + c){
            LocalDateTime closestNullTime = LocalDateTime.parse("1000-01-01_00:00:01", formatter);
            String name = null;
            for(SmartDevices smartDevice2 : smartDevices){
                if(smartDevice2.getSwitchTime() == null && smartDevice2.getLastSwitchTime() != null){
                    if(!isExistingInSortedArray(smartDevice2.getName()) && smartDevice2.getLastSwitchTime().isAfter(closestNullTime)){
                        closestNullTime = smartDevice2.getLastSwitchTime();
                        name = smartDevice2.getName();
                    }
                }
            }

            sortedArray = Arrays.copyOf(sortedArray, sortedArray.length + 1);
            sortedArray[sortedArray.length - 1] = name;
        }

        // below, devices that has null switchTime and null lastSwitchTime are sorted
        // they will in the order they were created.
        for(SmartDevices smartDevice3 : smartDevices){
            if(smartDevice3.getLastSwitchTime() == null && smartDevice3.getSwitchTime() == null){
                sortedArray = Arrays.copyOf(sortedArray, sortedArray.length + 1);
                sortedArray[sortedArray.length - 1] = smartDevice3.getName();
            }
        }

    }


    /**
     * {@link #arrangeSortedArray()} will use this to see if a device with the name in the parameter exists
     * in the sortedArray
     * @param name is the name of the device of whose presence is we are investigating in the sortedArray
     * @return
     */
    private boolean isExistingInSortedArray(String name){
        for(String deviceName : sortedArray){
            if(deviceName.equals(name)){
                return true;
            }
        }
        return false;
    }  //useful

    /** uses {@link #sortedArray}
     * to print the z report
     */
    public void printTheZReport(){  // executed when sortedArray is güncel
        for(String deviceName : sortedArray){
            String className = String.valueOf(getDeviceObject(deviceName).getClass());
            String status = getDeviceObject(deviceName).getCurrentStatus().toLowerCase();
            String switchTime = getDeviceObject(deviceName).getSwitchTime() == null ? "null" : getDeviceObject(deviceName).getSwitchTime().format(formatter);
            double watt;
            double mb;
            switch (className){
                case "class SmartPlug":
                    watt = ((SmartPlug) getDeviceObject(deviceName)).getEnergyConsumed();
                    System.out.printf("Smart Plug " + deviceName + " is " + status + " and consumed %.2fW so far " +
                            "(excluding current device), and its time to switch its status is " + switchTime + ".\n", watt);
                    outTxt.writef("Smart Plug " + deviceName + " is " + status + " and consumed %.2fW so far " +
                            "(excluding current device), and its time to switch its status is " + switchTime + ".\n", watt);
                    break;
                case "class SmartCamera":
                    mb = ((SmartCamera) getDeviceObject(deviceName)).getUsedStorage();
                    System.out.printf("Smart Camera " + deviceName + " is " + status + " and used %.2f MB of storage so far " +
                            "(excluding current status), and its time to switch its status is " + switchTime + ".\n", mb);
                    outTxt.writef("Smart Camera " + deviceName + " is " + status + " and used %.2f MB of storage so far " +
                            "(excluding current status), and its time to switch its status is " + switchTime + ".\n", mb);
                    break;

                case "class SmartLamp":
                    int kelvin = ((SmartLamp) getDeviceObject(deviceName)).getKelvin();
                    int brightness = ((SmartLamp) getDeviceObject(deviceName)).getBrightness();
                    System.out.println("Smart Lamp " + deviceName + " is " + status + " and its kelvin value is " + kelvin +
                            "K with " + brightness + "% brightness, and its time to switch its status is " + switchTime + ".");

                    outTxt.write("Smart Lamp " + deviceName + " is " + status + " and its kelvin value is " + kelvin +
                            "K with " + brightness + "% brightness, and its time to switch its status is " + switchTime + ".\n");

                    break;

                case "class SmartColorLamp":
                    boolean isColorMode = ((SmartColorLamp) getDeviceObject(deviceName)).getColorCode() != null;
                    int kelvin2 = ((SmartLamp) getDeviceObject(deviceName)).getKelvin();
                    int brightness2 = ((SmartLamp) getDeviceObject(deviceName)).getBrightness();
                    if(isColorMode){
                        String colorValue = ((SmartColorLamp) getDeviceObject(deviceName)).getColorCode();
                        System.out.println("Smart Color Lamp " + deviceName + " is " + status + " and its color value is " +
                                colorValue + " with " + brightness2 + "% brightness, and its time to switch its status is " + switchTime + ".");
                        outTxt.write("Smart Color Lamp " + deviceName + " is " + status + " and its color value is " +
                                colorValue + " with " + brightness2 + "% brightness, and its time to switch its status is " + switchTime + ".\n");
                    }else{
                        System.out.println("Smart Color Lamp " + deviceName + " is " + status + " and its color value is " +
                                kelvin2 + "K with " + brightness2 + "% brightness, and its time to switch its status is " + switchTime + ".");
                        outTxt.write("Smart Color Lamp " + deviceName + " is " + status + " and its color value is " +
                                kelvin2 + "K with " + brightness2 + "% brightness, and its time to switch its status is " + switchTime + ".\n");

                    }
                    break;
            }
        }

    }


    /**
     * this simple removes a device with the given name from {{@link #sortedArray}}
     * @param deviceName
     */
    private void removeDeviceFromSortedArray(String deviceName){
        int index = 0;
        for(String name : sortedArray){
            if(name.equals(deviceName)){
                break;
            }
            index++;   // at this index lies our device which will be removed
        }
        int i = index;
        while(i < sortedArray.length - 1){
            sortedArray[i] = sortedArray[i+1];
            i++;
        }
        sortedArray[sortedArray.length - 1] = null;

        sortedArray = Arrays.copyOf(sortedArray, sortedArray.length - 1);
    }

    /**
     * commands like Nop, setTime, SkipMinutes change the time
     * and this change in time may result in switches of devices
     * this method handles switches when time comes.
     * used also in SetSwitchTime command.
     *
     * Because every time movement also is in relation with
     * watt consumption and mb usage in SmartPlug, SmartCamera Classes
     * these watt and mb fields are updated inside the method
     */
    private void arrangeSwitchTimes(){
        for(SmartDevices smartDevice : smartDevices){
            if(smartDevice.getSwitchTime() != null){
                if(smartDevice.getSwitchTime().isBefore(currentTime) || smartDevice.getSwitchTime().isEqual(currentTime)){
                    smartDevice.setLastSwitchTime(smartDevice.switchTime);

                    if(smartDevice.getCurrentStatus().equals("On")){ // will be off

                        if(smartDevice instanceof  SmartCamera){
                            ((SmartCamera) smartDevice).updateCons(smartDevice.getSwitchTime());
                            ((SmartCamera) smartDevice).setStartedRunningAt(null);
                        } else if (smartDevice instanceof SmartPlug) {
                            if(((SmartPlug) smartDevice).isSmtPluggedIn()){
                                ((SmartPlug) smartDevice).updateWatt(smartDevice.getSwitchTime());
                                ((SmartPlug) smartDevice).setStartedConsumingAt(null);
                            }
                        }
                        smartDevice.setCurrentStatus("Off");

                    }else {
                        if(smartDevice instanceof SmartCamera){
                            ((SmartCamera) smartDevice).setStartedRunningAt(smartDevice.getSwitchTime());
                        } else if (smartDevice instanceof  SmartPlug) {
                            if(((SmartPlug) smartDevice).isSmtPluggedIn()){
                                ((SmartPlug) smartDevice).setStartedConsumingAt(smartDevice.getSwitchTime());
                            }
                        }
                        smartDevice.setCurrentStatus("On");
                    }

                    smartDevice.setSwitchTime(null);
                }

            }
        }
    }

    /** We remove empty lines of input.txt here
     * @param inputArray is the array of input.txt lines
     * @return inputArray is the same array without empty strings
     */
    private String[] getArrayOfActualCommands(String[] inputArray){
        String[] copyArr = inputArray;
        inputArray = new String[0];
        for(String line : copyArr){
            if(!line.equals("")){
                inputArray = Arrays.copyOf(inputArray, inputArray.length + 1);
                inputArray[inputArray.length - 1] = line;
            }
        }
        return inputArray;
    }


    /**
     * for day, month, hour, minute, second
     * 3 for example is changed as 03
     * @param s is should be a date
     * @return adds 0 if necessary to correct the date format for LocalDateTime class
     */
    public String setTimeToNormal(String s){

        String[] date = s.split("_");
        String[] dayPart = date[0].split("-");
        String[] hourPart = date[1].split(":");
        String time = dayPart[0];


        for(int i = 1; i < 3; i++){
            if(dayPart[i].length() == 1){
                dayPart[i] = "0" + dayPart[i];
            }
            time += "-" + dayPart[i];
        }

        time += "_";


        for(int i = 0; i < 3; i++){
            if(hourPart[i].length() == 1) {
                hourPart[i] = "0" + hourPart[i];
            }
            if(i == 0){
                time += hourPart[i];
            }else {
                time += ":" + hourPart[i];
            }
        }

        return time;

    }
}
