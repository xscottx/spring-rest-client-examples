package guru.springframework.springrestclientexamples.services;

import guru.springframework.api.domain.User;
import guru.springframework.api.domain.UserData;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ApiServiceImpl implements ApiService {

    private RestTemplate restTemplate;

    public ApiServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<User> getUsers(Integer limit) {
        UserData userData = restTemplate.getForObject("http://apifaketory.com/api/user?limit=" + limit, UserData.class);

        return userData.getData();
    }

//    @Override
//    public Flux<User> getUsersReactive(Mono<Integer> limit) {
//        return limit
//                .flatMapMany(integer ->
//                        WebClient.create("http://apifaketory.com/api/user")
//                            .get()
//                            .uri(uriBuilder -> uriBuilder.queryParam("limit", integer).build())
//                            .accept(MediaType.APPLICATION_JSON)
//                            .exchange()
//                            .flatMap(clientResponse -> clientResponse.bodyToMono(UserData.class))
//                            .flatMapIterable(UserData::getData)
//                );
//    }
}
