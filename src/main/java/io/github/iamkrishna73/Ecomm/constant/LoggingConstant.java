package io.github.iamkrishna73.Ecomm.constant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoggingConstant {
    public static final String START_METHOD_LOG = "START:{},{}";
    public static final String END_METHOD_LOG = "END:{}";
    public static final String ERROR_METHOD_LOG = "ERROR:{},{}";

    private LoggingConstant() {

    }
}
