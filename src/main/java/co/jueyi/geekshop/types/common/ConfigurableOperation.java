package co.jueyi.geekshop.types.common;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on Nov, 2020 by @author bobo
 */
@Data
public class ConfigurableOperation {
    private String code;
    private List<ConfigArg> args = new ArrayList<>();
}
