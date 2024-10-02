package ApiProductoV1.base.config;
import org.junit.jupiter.api.BeforeAll;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public abstract class ApiConfig {
public static boolean inicializado=false;	
	
	@BeforeAll
	public static void configuracionInicial() {
		if (!inicializado) {
			RequestSpecification requestSpec = new RequestSpecBuilder()
					.setContentType(ContentType.JSON)
					.setBaseUri("http://localhost:8081")
					.setAccept(ContentType.JSON)
					.addHeader("User-Agent", "JBEnteprise")
					.build();
			
			requestSpec.header("Authorization","Bearer aGFzaGRzZnNkZnNkZnNkZnNk");
			RestAssured.requestSpecification = requestSpec;
			inicializado=true;
		}
	}

}
