import java.util.*;

class Device {
    public String name;
    public String deviceType;
    public int strength;
    public List<Device> connections;
    public Device(String name, String deviceType, int strength) {
        this.name = name;
        this.deviceType = deviceType;
        this.strength = strength;
        this.connections = new ArrayList<>();
    }

    public Device(String name, String deviceType) {
        this(name, deviceType, 5);
    }

    public String toString() {
        return this.deviceType + " (" + this.name + ")";
    }
}

class Network {
    public Map<String, Device> devices= new HashMap<>();


    public void addDevice(Device device) {
        if (this.devices.containsKey(device.name)) {
            System.out.println("Error: That name already exists.");
        } else {
            this.devices.put(device.name, device);
            System.out.println("Successfully added " + device.name + ".");
        }
    }

    public void connectDevices(Device device1, Device device2) {
        if(!devices.containsKey(device1.name) || !devices.containsKey(device2.name)){
            System.out.println("Error: Node not found.");
        }
        else if (device1.connections.contains(device2)) {
            System.out.println("Error: Devices are already connected.");
        }

        else if (device1.name.equals(device2.name)) {
            System.out.println("Error: Cannot connect device to itself.");
        }
else {
            device1.connections.add(device2);
            device2.connections.add(device1);
            System.out.println("Successfully connected.");
        }
    }

    public void setDeviceStrength(Device device, int strength) {
        if (device.deviceType.equals("REPEATER")) {
            System.out.println("Error: A strength cannot be defined for a repeater.");
        } else if (strength < 0) {
            System.out.println("Error: Invalid command syntax.");
        } else {
            device.strength = strength;
            System.out.println("Successfully defined strength.");
        }
    }

    public void getRoute(Device srcDevice, Device destDevice) {
        if (srcDevice.deviceType.equals("REPEATER") || destDevice.deviceType.equals("REPEATER")) {
            System.out.println("Error: Route cannot be calculated with a repeater.");
            return;
        }

        if (!srcDevice.connections.contains(destDevice)) {
            Queue<Device> queue = new LinkedList<>();
            Map<Device, Device> parent = new HashMap<>();
            queue.add(srcDevice);
            Set<Device> visited = new HashSet<>();
            while (!queue.isEmpty()) {
                Device node = queue.poll();
                if (visited.contains(node)) {
                    continue;
                }
                visited.add(node);
                for (Device connection : node.connections) {
                    if (!visited.contains(connection)) {
                        queue.add(connection);
                        parent.put(connection, node);
                        if (connection == destDevice) {
                            String route = "";
                            Device current = destDevice;
                            while (current != srcDevice) {
                                route = current.name + " -> " + route;
                                current = parent.get(current);
                            }
                            route = srcDevice.name + " -> " + route;
                            System.out.println("Route: " + route.substring(0,route.length()-3));
                            return;
                        }
                    }
                }
            }
            System.out.println("Error: Route not found!");
        } else {
            System.out.println("Route: " + srcDevice.name + " -> " + destDevice.name);
        }
    }
}

public class Primitive {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Network network = new Network();

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            String[] tokens = input.split(" ");
            String command = tokens[0];

            if (command.equals("ADD")) {
                if(tokens.length!=3) {
                    System.out.println("Error: Invalid Command Syntax");
                    continue;
                }
                String name = tokens[2];
                String deviceType = tokens[1];
                int strength = 5;
                if(!deviceType.equals("COMPUTER") && !deviceType.equals("REPEATER")){
                    System.out.println("Error: Invalid Command Syntax");
                    continue;
                }
//                if (deviceType.equals("REPEATER")) {
//                    strength = Integer.parseInt(tokens[3]);
//                }
                Device device = new Device(name, deviceType, strength);
                network.addDevice(device);
            } else if (command.equals("CONNECT")) {
                if(tokens.length!=3){
                    System.out.println("Error: Invalid Command Syntax");
                    continue;
                }
                String device1Name = tokens[1];
                String device2Name = tokens[2];

                Device device1 = network.devices.get(device1Name);
                Device device2 = network.devices.get(device2Name);
                if(device1==null || device2==null){
                    System.out.println("Error: Node not found.");
                    continue;
                }
                network.connectDevices(device1, device2);
            } else if (command.equals("SET_DEVICE_STRENGTH")) {
                if(tokens.length!=3){
                    System.out.println("Error: Invalid Command Syntax");
                    continue;
                }
                String deviceName = tokens[1];
                int strength;
                try {
                    strength = Integer.parseInt(tokens[2]);
                }
                catch(Exception e){
                    System.out.println("Error: Invalid Command Syntax");
                    continue;
                }

                Device device = network.devices.get(deviceName);
                network.setDeviceStrength(device, strength);
            } else if (command.equals("INFO_ROUTE")) {
                if(tokens.length!=3){
                    System.out.println("Error: Invalid Command Syntax");
                    continue;
                }
                String srcDeviceName = tokens[1];
                String destDeviceName = tokens[2];
                Device srcDevice = network.devices.get(srcDeviceName);
                Device destDevice = network.devices.get(destDeviceName);
                if(srcDevice==null || destDevice==null){
                    System.out.println("Error: Node not found.");
                    continue;
                }
                if(srcDevice==destDevice) {
                    System.out.println("Route: " + srcDevice.name + " -> " + destDevice.name);
                    continue;
                }
                network.getRoute(srcDevice, destDevice);
            } else if (command.equals("QUIT")) {
                break;
            } else {
                System.out.println("Error: Invalid command.");
            }
        }
    }
}