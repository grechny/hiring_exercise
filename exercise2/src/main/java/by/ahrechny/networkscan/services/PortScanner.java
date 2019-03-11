package by.ahrechny.networkscan.services;

import by.ahrechny.networkscan.dto.NetworkPort;
import by.ahrechny.networkscan.dto.NetworkPort.NetworkPortBuilder;
import by.ahrechny.networkscan.dto.NetworkPort.Protocol;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PortScanner {

  private static int NUMBER_OF_THREADS;
  private static int PORT_MIN;
  private static int PORT_MAX;
  private static int CONNECTION_TIMEOUT;
  private ExecutorService executorService;

  @Value("${scanner.number_of_threads}")
  public void setNumberOfThreads (int numberOfThreads) {
    NUMBER_OF_THREADS = numberOfThreads;
  }

  @Value("${scanner.port_min}")
  public void setPortMin (int portMin) {
    PORT_MIN = portMin;
  }

  @Value("${scanner.port_max}")
  public void setPortMax (int portMax) {
    PORT_MAX = portMax;
  }

  @Value("${scanner.connection_timeout}")
  public void setConnectionTimeout (int connectionTimeout) {
    CONNECTION_TIMEOUT = connectionTimeout;
  }

  @PostConstruct
  public void init () {
    executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
  }

  public List<NetworkPort> scanOpenPorts (String ipAddress) {

    List<Future<NetworkPort>> futures = new ArrayList<>();
    for (int port = PORT_MIN; port <= PORT_MAX; port++) {
      futures.add(isTCPPortOpen(ipAddress, port));
      futures.add(isUDPPortOpen(ipAddress, port));
    }

    return futures.stream().map(future -> {
      try {
        return future.get();
      } catch (Exception e) {
        return null;
      }
    }).filter(Objects::nonNull).filter(NetworkPort::isOpen).collect(Collectors.toList());
  }

  private Future<NetworkPort> isTCPPortOpen (String ipAddress, int port) {

    NetworkPortBuilder portBuilder = NetworkPort.builder()
        .ipAddress(ipAddress)
        .protocol(Protocol.TCP)
        .port(port);

    return executorService.submit(() -> {
      try (Socket socket = new Socket()) {
        socket.connect(new InetSocketAddress(ipAddress, port), CONNECTION_TIMEOUT);
        return portBuilder.open(true).build();
      } catch (Exception ex) {
        return portBuilder.open(false).build();
      }
    });
  }

  private Future<NetworkPort> isUDPPortOpen (String ipAddress, int port) {

    NetworkPortBuilder portBuilder = NetworkPort.builder()
        .ipAddress(ipAddress)
        .protocol(Protocol.UDP)
        .port(port);

    return executorService.submit(() -> {
      boolean isOpen = false;
      try (DatagramSocket datagramSocket = new DatagramSocket()) {
        datagramSocket.setSoTimeout(CONNECTION_TIMEOUT);
        datagramSocket.connect(new InetSocketAddress(ipAddress, port));
        datagramSocket.send(new DatagramPacket(new byte[64], 64));
        datagramSocket.receive(new DatagramPacket(new byte[64], 64));
        isOpen = true;
      } catch (Exception ignored) {
      }
      return portBuilder.open(isOpen).build();
    });
  }
}
