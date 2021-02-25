package microservices.user.util;

import microservices.user.services.exceptions.ServiceException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Configuration
public class PasswordHasher {

    @Autowired
    private Logger logger;

    @Bean
    @Scope("prototype")
    public String hash(String password, String uuid){
        try {
            /*SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            secureRandom.nextBytes(salt);
*/
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(uuid.getBytes());
            byte[] bytes = messageDigest.digest(password.getBytes());

            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                stringBuilder.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            return stringBuilder.toString();

        } catch (NoSuchAlgorithmException e) {
            logger.error("Error while hashing password", e);
            throw new ServiceException("Something went wrong");
        }
    }
}
