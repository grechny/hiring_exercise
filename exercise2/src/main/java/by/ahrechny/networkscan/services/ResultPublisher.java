package by.ahrechny.networkscan.services;

import by.ahrechny.networkscan.dto.NetworkPort;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class ResultPublisher {

  public void printNetworkPorts(List<NetworkPort> networkPortList) {

    Map<String, List<NetworkPort>> networkPortMap = new HashMap<>();
    for (NetworkPort networkPort : networkPortList) {
      List<NetworkPort> mapValue = networkPortMap.get(networkPort.getIpAddress());
      if (mapValue == null) {
        mapValue = new ArrayList<>();
        mapValue.add(networkPort);
        networkPortMap.put(networkPort.getIpAddress(), mapValue);
      } else {
        mapValue.add(networkPort);
      }
    }

    updateNetworkScanResults(networkPortMap);
  }

  public void printEmptyResult(String ipAddress) {
    Map<String, List<NetworkPort>> networkPortMap = new HashMap<>();
    networkPortMap.put(ipAddress, new ArrayList<>());
    updateNetworkScanResults(networkPortMap);
  }

  private void updateNetworkScanResults(Map<String, List<NetworkPort>> networkPortMap) {
    for (Map.Entry<String, List<NetworkPort>> networkPortMapEntry : networkPortMap.entrySet()) {
      String ipAddress = networkPortMapEntry.getKey();
      List<NetworkPort> actualNetworkPorts = networkPortMapEntry.getValue();
      List<NetworkPort> savedNetworkPorts = getNetworkPortsFromFile(ipAddress);
      if (actualNetworkPorts.equals(savedNetworkPorts)) {
        System.out.println("*Target - " + ipAddress + ": No new records found in the last scan.*");
      } else if (CollectionUtils.isEmpty(actualNetworkPorts)) {
        System.out.println("*Target - " + ipAddress + ": All ports are closed.*");
        saveNetworkPortsToFile(ipAddress, actualNetworkPorts);
      } else {
        System.out.println("*Target - " + ipAddress + ": Full scan results:*");
        for (NetworkPort networkPort : actualNetworkPorts) {
          System.out.println("Host: " + networkPort.getIpAddress()
              + ", open port: " + networkPort.getPort() + "/" + networkPort.getProtocol());
        }
        saveNetworkPortsToFile(ipAddress, actualNetworkPorts);
      }
    }
  }

  @SneakyThrows
  private List<NetworkPort> getNetworkPortsFromFile (String fileName) {
    List<NetworkPort> networkPorts = null;
    File f = new File(fileName);
    if (f.isFile() && f.canRead()) {
      networkPorts = new ArrayList<>();
      FileInputStream in = new FileInputStream(f);
      ObjectInputStream oi = new ObjectInputStream(in);
      while (in.available() > 0) {
        NetworkPort networkPort = (NetworkPort) oi.readObject();
        if (networkPort != null) {
          networkPorts.add(networkPort);
        }
      }
      in.close();
      oi.close();
    }
    return networkPorts;
  }

  @SneakyThrows
  private void saveNetworkPortsToFile(String fileName, List<NetworkPort> networkPorts) {
    FileOutputStream f = new FileOutputStream(new File(fileName));
    ObjectOutputStream o = new ObjectOutputStream(f);

    for (NetworkPort networkPort : networkPorts) {
      o.writeObject(networkPort);
    }

    o.close();
    f.close();
  }

}
