package guru.springframework.springrestclientexamples.services;

import guru.springframework.api.domain.User;
import guru.springframework.api.domain.UserData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ApiServiceImpl implements ApiService {

    private RestTemplate restTemplate;

    private final String api_url;

    public ApiServiceImpl(RestTemplate restTemplate, @Value("${api.url}") String api_url) {
        this.restTemplate = restTemplate;
        this.api_url = api_url;
    }

    @Override
    public List<User> getUsers(Integer limit) {
//        UserData userData = restTemplate.getForObject("http://apifaketory.com/api/user?limit=" + limit, UserData.class);
//
//        return userData.getData();
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromUriString(api_url)
                .queryParam("limit", limit);

        UserData userData = restTemplate.getForObject(uriComponentsBuilder.toUriString(), UserData.class);
        return userData.getData();
    }

    @Override
    public Flux<User> getUsersReactive(Mono<Integer> limit) {

        // WRONG way to do this by calling block
        //        return WebClient
//                .create(api_url)
//                .get()
//                .uri(uriBuilder -> uriBuilder.queryParam("limit", limit.block()).build())
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .flatMap(resp -> resp.bodyToMono(UserData.class))
//                .flatMapIterable(UserData::getData);

//        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
//                .fromUriString(api_url)
//                .queryParam("limit", limit);
//
//        UserData userData = restTemplate.getForObject(uriComponentsBuilder.toUriString(), UserData.class);
//        return Flux.empty();

        // RIGHT way to do this without using block
        return limit
                .flatMapMany(integer ->
                        WebClient.create("http://apifaketory.com/api/user")
                            .get()
                            .uri(uriBuilder -> uriBuilder.queryParam("limit", integer).build())
                            .accept(MediaType.APPLICATION_JSON)
                            .exchange()
                            .flatMap(clientResponse -> clientResponse.bodyToMono(UserData.class))
                            .flatMapIterable(UserData::getData)
                );
    }
}
