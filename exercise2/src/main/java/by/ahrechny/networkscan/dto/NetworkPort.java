package by.ahrechny.networkscan.dto;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NetworkPort implements Serializable {

  private String ipAddress;
  private Protocol protocol;
  private int port;
  private boolean open;

  public enum Protocol {TCP, UDP}
}
