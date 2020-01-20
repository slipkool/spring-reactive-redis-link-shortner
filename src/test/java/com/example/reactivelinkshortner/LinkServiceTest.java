package com.example.reactivelinkshortner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class LinkServiceTest {

	private LinkRepository linkRepository = mock(LinkRepository.class);
    private LinkService linkService = new LinkService("http://some-domain.com/", linkRepository);
    
    @Before
    public void setup() {
    	when(linkRepository.save(Mockito.any())).thenAnswer((Answer<Mono<Link>>) invocationOnMock -> Mono.just((Link) invocationOnMock.getArgument(0)));
    }

    @Test
    public void shortensLink(){
        StepVerifier.create(linkService.shortenLink("https://spring.io/"))
                .expectNextMatches(result -> result != null && result.length() > 0
                        && result.startsWith("http://some-domain.com/"))
                .expectComplete()
                .verify();
    }

}