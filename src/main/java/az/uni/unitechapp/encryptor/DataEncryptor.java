package az.uni.unitechapp.encryptor;

import lombok.experimental.UtilityClass;
import org.apache.commons.codec.digest.DigestUtils;

@UtilityClass
public class DataEncryptor {

    public String md5Hex(String data) {
        return DigestUtils.md5Hex(data);
    }

    public String sha256Hex(String data) {
        return DigestUtils.sha256Hex(data);
    }

}
