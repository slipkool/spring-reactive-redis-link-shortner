package com.example.reactivelinkshortner;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = LinkController.class)
public class LinkControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    LinkService linkService;

    @Test
    public void shortensLink(){
        Mockito.when(linkService.shortenLink("https://spring.io/")).thenReturn(Mono.just("http://localhost:8080/aass2211"));
        webTestClient.post()
                .uri("/link")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"link\":\"https://spring.io/\"}")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .jsonPath("$.shortenedLink")
                .value(val -> Assertions.assertThat(val).isEqualTo("http://localhost:8080/aass2211"));
    }

    @Test
    public void redirectoToOriginalLink(){
        Mockito.when(linkService.getOriginalLink("aaa21123")).thenReturn(Mono.just(new Link("https://spring.io/", "aaa21123")));
        webTestClient.get()
                .uri("/aaa21123")
                .exchange()
                .expectStatus()
                .isPermanentRedirect()
                .expectHeader()
                .value("Location", location -> Assertions.assertThat(location).isEqualTo("https://spring.io/"));
    }
}
