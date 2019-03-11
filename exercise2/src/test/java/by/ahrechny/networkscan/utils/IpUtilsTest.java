package by.ahrechny.networkscan.utils;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class IpUtilsTest {

  @Test
  public void getListIpAddressesTest() {
    String ipAddressString;
    List<String> actualResult, expectedResult;

    ipAddressString = "1.1.1.1";
    actualResult = IpUtils.getListIpAddresses(ipAddressString);
    assertEquals("Lists have a different count of addresses", 1, actualResult.size());
    assertEquals("Lists have a different values", "1.1.1.1", actualResult.get(0));

    ipAddressString = "1.1.1.1-1.1.1.3";
    actualResult = IpUtils.getListIpAddresses(ipAddressString);
    expectedResult = Arrays.asList("1.1.1.1", "1.1.1.2", "1.1.1.3");
    assertEquals("Lists have a different count of addresses", expectedResult.size(), actualResult.size());
    assertEquals("Lists have a different values", actualResult, expectedResult);

    ipAddressString = "1.1.1.1,1.1.1.3";
    actualResult = IpUtils.getListIpAddresses(ipAddressString);
    expectedResult = Arrays.asList("1.1.1.1", "1.1.1.3");
    assertEquals("Lists have a different count of addresses", expectedResult.size(), actualResult.size());
    assertEquals("Lists have a different values", actualResult, expectedResult);
  }

  @Test
  public void isValidIpAddressTest() {
    String ipAddress = "1.1.1.1";
    assertTrue(IpUtils.isValidIpAddress(ipAddress));

    ipAddress = "10.0.0.0";
    assertTrue(IpUtils.isValidIpAddress(ipAddress));

    ipAddress = "fde2:838:7843:4991:ab34:89ea:12c5:9001";
    assertTrue(IpUtils.isValidIpAddress(ipAddress));

    ipAddress = "1::1";
    assertTrue(IpUtils.isValidIpAddress(ipAddress));

    ipAddress = "1.2.345.6";
    assertFalse(IpUtils.isValidIpAddress(ipAddress));

    ipAddress = "ipAddress";
    assertFalse(IpUtils.isValidIpAddress(ipAddress));
  }

  @Test
  public void isValidIpRangeTest() {
    String ipRange = "1.1.1.1-2.2.2.2";
    assertTrue(IpUtils.isValidIpRange(ipRange));

    ipRange = "2.2.2.2-1.1.1.1";
    assertTrue(IpUtils.isValidIpRange(ipRange));

    ipRange = "1.1.1.1-1.1.1.1";
    assertTrue(IpUtils.isValidIpRange(ipRange));

    ipRange = "1.1.1.1,2.2.2.2";
    assertFalse(IpUtils.isValidIpRange(ipRange));

    ipRange = "1.1.1.1";
    assertFalse(IpUtils.isValidIpRange(ipRange));

    ipRange = "ipRange";
    assertFalse(IpUtils.isValidIpRange(ipRange));
  }
}