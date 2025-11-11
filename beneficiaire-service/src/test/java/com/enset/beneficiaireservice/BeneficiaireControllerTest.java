package com.enset.beneficiaireservice;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class BeneficiaireControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void testCreerBeneficiairePhysique() {
        String beneficiaireBody = """
            {
                "nom": "Sara",
                "prenom": "Jean",
                "rib": "FR7630001007941234567890185",
                "type": "PHYSIQUE"
            }
            """;

        given()
                .contentType(ContentType.JSON)
                .body(beneficiaireBody)
                .when()
                .post("/beneficiaires")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("nom", equalTo("Dupont"))
                .body("prenom", equalTo("Jean"))
                .body("rib", equalTo("FR7630001007941234567890185"))
                .body("type", equalTo("PHYSIQUE"));
    }


    @Test
    public void testRecupererBeneficiaireParId() {
        // D'abord créer un bénéficiaire
        String beneficiaireBody = """
            {
                "nom": "Martin",
                "prenom": "Sophie",
                "rib": "FR7630001007941234567890186",
                "type": "PHYSIQUE"
            }
            """;

        int beneficiaireId = given()
                .contentType(ContentType.JSON)
                .body(beneficiaireBody)
                .when()
                .post("/beneficiaires")
                .then()
                .extract()
                .path("id");

        // Ensuite le récupérer
        when()
                .get("/beneficiaires/{id}", beneficiaireId)
                .then()
                .statusCode(200)
                .body("id", equalTo(beneficiaireId))
                .body("nom", equalTo("Martin"))
                .body("prenom", equalTo("Sophie"));
    }

    @Test
    public void testBeneficiaireInexistant() {
        when()
                .get("/beneficiaires/9999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testListerTousLesBeneficiaires() {
        when()
                .get("/beneficiaires")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(0))); // Au moins une liste vide
    }

    @Test
    public void testVerifierExistenceBeneficiaire() {
        // Créer un bénéficiaire
        String beneficiaireBody = """
            {
                "nom": "Durand",
                "prenom": "Pierre",
                "rib": "FR7630001007941234567890187",
                "type": "PHYSIQUE"
            }
            """;

        int beneficiaireId = given()
                .contentType(ContentType.JSON)
                .body(beneficiaireBody)
                .when()
                .post("/beneficiaires")
                .then()
                .extract()
                .path("id");

        // Vérifier son existence
        when()
                .get("/beneficiaires/{id}/exists", beneficiaireId)
                .then()
                .statusCode(200)
                .body(equalTo("true"));
    }

    @Test
    public void testSupprimerBeneficiaire() {
        // Créer un bénéficiaire à supprimer
        String beneficiaireBody = """
            {
                "nom": "ASupprimer",
                "prenom": "Test",
                "rib": "FR7630001007941234567890188",
                "type": "PHYSIQUE"
            }
            """;

        int beneficiaireId = given()
                .contentType(ContentType.JSON)
                .body(beneficiaireBody)
                .when()
                .post("/beneficiaires")
                .then()
                .extract()
                .path("id");

        // Le supprimer
        when()
                .delete("/beneficiaires/{id}", beneficiaireId)
                .then()
                .statusCode(200);

        // Vérifier qu'il n'existe plus
        when()
                .get("/beneficiaires/{id}", beneficiaireId)
                .then()
                .statusCode(404);
    }
}