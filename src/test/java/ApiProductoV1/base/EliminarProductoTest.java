package ApiProductoV1.base;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import ApiProductoV1.base.config.ApiConfig;
import ApiProductoV1.dto.ProductRequest;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.actors.Cast;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Delete;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.questions.ResponseConsequence;

@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Requisito 3. Eliminar un producto previamente creado usando la api /api/v1/product/")
public class EliminarProductoTest extends ApiConfig{
	@BeforeEach
	public void abrirEscenario() {
		OnStage.setTheStage(Cast.ofStandardActors());
	}
	
	@Test
	@DisplayName("Eliminar un producto previamente creado de manera exitosa")
	public void eliminarProducto() {
		ProductRequest nuevoProducto = ProductRequest.builder()
				.name("Iphone 3000")
				.description("Teelfono de alta gama")
				.price(3500.0f)
				.build();
		
		OnStage.theActorCalled("Tester").whoCan(CallAnApi.at("http://localhost:8081"));
		
		OnStage.theActorInTheSpotlight().attemptsTo(
				Post.to("/api/v1/product/")
					.with(
							request -> request
								.body(nuevoProducto).log().all()
					)
				);
		
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El codigo de la respuesta es 201",response -> response.statusCode(201))
		);				
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El valor del atributo status debe ser verdadero"
						,response -> response.body("status", equalTo(true)))
				);		
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El mensaje retornado debe ser El producto fue creado con éxito!"
						,response -> response.body("message", containsString("El producto fue creado con éxito!")))
				);
		
		String skuCreado = SerenityRest.lastResponse().jsonPath().getString("sku");
		
		OnStage.theActorInTheSpotlight().attemptsTo(
				Delete.from("/api/v1/product/{sku}/")
					.with(request -> request
							.pathParam("sku", skuCreado)
							.log()
							.all()
				)
		);
		
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El codigo de la respuesta es 200",response -> response.statusCode(200))
		);
		
		OnStage.theActorInTheSpotlight().attemptsTo(
				Get.resource("/api/v1/product/{sku}/")
					.with(request -> request
							.pathParam("sku", skuCreado)
							.log()
							.all()
				)
		);
		
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El mensaje retornado debe ser El producto no fue encontrado"
						,response -> response.body("message", containsString("El producto no fue encontrado")))
				);
	}
}
