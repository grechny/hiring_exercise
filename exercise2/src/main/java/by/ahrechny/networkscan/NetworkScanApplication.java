package by.ahrechny.networkscan;

import by.ahrechny.networkscan.dto.NetworkPort;
import by.ahrechny.networkscan.services.PortScanner;
import by.ahrechny.networkscan.services.ResultPublisher;
import by.ahrechny.networkscan.utils.IpUtils;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.CollectionUtils;

@SpringBootApplication
public class NetworkScanApplication implements CommandLineRunner {

  private final PortScanner portScanner;
  private final ResultPublisher resultPublisher;

  public NetworkScanApplication(PortScanner portScanner, ResultPublisher resultPublisher) {
    this.portScanner = portScanner;
    this.resultPublisher = resultPublisher;
  }

  public static void main(String[] args) {
    SpringApplication.run(NetworkScanApplication.class, args);
  }

  @Override
  public void run(String... strings) {
    if (strings.length > 0) {
      List<String> listIpAddresses = IpUtils.getListIpAddresses(strings[0]);
      for (String ipAddress : listIpAddresses) {
        List<NetworkPort> networkPorts = portScanner.scanOpenPorts(ipAddress);
        if (CollectionUtils.isEmpty(networkPorts)) {
          resultPublisher.printEmptyResult(ipAddress);
        } else {
          resultPublisher.printNetworkPorts(networkPorts);
        }
      }
    }
    System.exit(0);
  }
}
