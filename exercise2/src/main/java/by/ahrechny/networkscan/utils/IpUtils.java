package by.ahrechny.networkscan.utils;

import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddressString;
import inet.ipaddr.ipv4.IPv4Address;
import inet.ipaddr.ipv4.IPv4AddressSeqRange;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.SneakyThrows;

@SuppressWarnings("WeakerAccess")
public class IpUtils {

  /**
   * The method converts input string with ip-addresses to list of ip-addresses
   *
   * @param ipString string of IPs. Supported values:
   *  - single ip-address (e.g. 8.8.8.8),
   *  - multiple ip-addresses (e.g. 8.8.8.8,10.10.10.10)
   *  - range of ip-addresses (e.g. 10.10.10.1-10.10.10.10)
   * @return list of ip-addresses
   */
  public static List<String> getListIpAddresses (String ipString) {

    if (isValidIpAddress(ipString)) {
      return Collections.singletonList(ipString);
    } else if (ipString.contains(",")) {
      List<String> ipRange = new ArrayList<>();
      String[] splittedIPs = ipString.split(",");
      for (String ipAddress : splittedIPs) {
        if (isValidIpAddress(ipAddress.trim())) {
          ipRange.add(ipAddress.trim());
        }
      }
      return ipRange;
    } else if (isValidIpRange(ipString)) {
      IPv4AddressSeqRange iPv4Range = getIPv4Range(ipString);
      List<String> ipRange = new ArrayList<>();
      iPv4Range.getIterable().forEach(ip -> ipRange.add(ip.toString()));
      return ipRange;
    } else {
      return new ArrayList<>();
    }
  }

  /**
   * Check if string is actually valid ip-address. Support IPv4 and IPv6 formats
   *
   * @param ipAddress input string to check
   * @return true if string is valid ip-address, otherwise false
   */
  public static boolean isValidIpAddress (String ipAddress) {

    IPAddressString addrString = new IPAddressString(ipAddress);
    try {
      addrString.toAddress();
      return true;
    } catch(AddressStringException e) {
      return false;
    }
  }

  /**
   * Check if string is actually valid ip-address. Support only IPv4 format
   *
   * @param ipRange input string to check. Supported range format is x.x.x.x-y.y.y.y
   * @return true if string is valid ip range, otherwise false
   */
  public static boolean isValidIpRange (String ipRange) {
    try {
      getIPv4Range(ipRange);
      return true;
    } catch(Exception e) {
      return false;
    }
  }

  @SneakyThrows
  private static IPv4AddressSeqRange getIPv4Range (String ipRange){
    IPAddressString firstAddrString;
    IPAddressString secondAddrString;

    int dashIndex = ipRange.indexOf("-");
    firstAddrString = new IPAddressString(ipRange.substring(0, dashIndex).trim());
    secondAddrString = new IPAddressString(ipRange.substring(dashIndex+1).trim());

    return new IPv4AddressSeqRange(
          (IPv4Address) firstAddrString.toAddress(),
          (IPv4Address) secondAddrString.toAddress());
  }
}
