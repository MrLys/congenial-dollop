package com.budzilla

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Encoders
import io.jsonwebtoken.security.Keys
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.crypto.SecretKey

@SpringBootTest
class BudzillaApplicationTests {
	@Test
	fun contextLoads() {
		val key : SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512)
		val base64Key : String = Encoders.BASE64.encode(key.encoded)
		println(key.encoded)
		println(base64Key)
	}

}
