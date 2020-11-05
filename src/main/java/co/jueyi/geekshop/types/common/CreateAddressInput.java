package co.jueyi.geekshop.types.common;

import lombok.Data;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class CreateAddressInput {
    private String fullName;
    private String company;
    private String streetLine1;
    private String streetLine2;
    private String city;
    private String province;
    private String postalCode;
    private String phoneNumber;
    private Boolean defaultShippingAddress;
    private Boolean defaultBillingAddress;
}
