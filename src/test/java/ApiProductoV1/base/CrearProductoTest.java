package ApiProductoV1.base;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import ApiProductoV1.base.config.ApiConfig;
import ApiProductoV1.base.pregunta.CodigoRespuesta;
import ApiProductoV1.dto.ProductRequest;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.serenitybdd.screenplay.actors.Cast;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.questions.ResponseConsequence;

@ExtendWith(SerenityJUnit5Extension.class)
@DisplayName("Requisito 1. Crear un nuevo producto usando la api /api/v1/product/")
public class CrearProductoTest extends ApiConfig {
	@BeforeEach
	public void abrirEscenario() {
		OnStage.setTheStage(Cast.ofStandardActors());
	}
	
	@Test
	@DisplayName("Crear un nuevo producto de manera exitosa")
	public void crearNuevoProducto() {
		ProductRequest nuevoProducto = ProductRequest.builder()
				.name("Iphone 4500")
				.description("Teléfono de alta gama")
				.price(4500.0f)
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
				ResponseConsequence.seeThatResponse("El codigo de respuesta es 201", response -> response.statusCode(201))
				);
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El valor del atributo status debe ser verdadero"
						,response -> response.body("status", equalTo(true)))
				);
		
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El valor del atributo status debe ser verdadero"
						,response -> response.body("message", containsString("El producto fue creado con éxito!")))
				);

	}


	@Test
	@DisplayName("Error de nuevo producto de manera exitosa")
	public void ErrorNuevoProducto() {
		ProductRequest nuevoProducto = ProductRequest.builder()
				.name("Iphone 4500")
				.description("Teléfono de alta gama")
				.price(4500.0f)
				.build();

		OnStage.theActorCalled("Tester").whoCan(CallAnApi.at("http://localhost:8081"));

		OnStage.theActorInTheSpotlight().attemptsTo(
				Post.to("/api/v1/product")
						.with(
								request -> request
										.body(nuevoProducto).log().all()
						)
		);

		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El codigo de respuesta es 404", response -> response.statusCode(404))
		);
		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El valor del atributo status debe ser verdadero"
						,response -> response.body("status", equalTo(404)))
		);

		OnStage.theActorInTheSpotlight().should(
				ResponseConsequence.seeThatResponse("El valor del atributo status debe ser verdadero"
						,response -> response.body("message", containsString("null")))
		);

	}










	@Test
	@DisplayName("Crear un nuevo producto aplicando pregunta para validar")
	public void crearNuevoProductoConPregunta() {
		ProductRequest nuevoProducto = ProductRequest.builder()
				.name("Iphone 2000")
				.description("Teelfono de alta gama")
				.price(3000.0f)
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
				seeThat("El codigo de respuesta es correcto o 201", CodigoRespuesta.was(), equalTo(201))
		);		
	}


}
