package az.example.countriesdemo.services;

import az.example.countriesdemo.exceptions.InternalServerException;
import az.example.countriesdemo.models.Country;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryService {
    private final RestTemplate restTemplate;

    public List<Country> getAllCountries() {
        ResponseEntity<List<Country>> res = restTemplate.exchange("https://raw.githubusercontent.com/mledoze/countries/master/countries.json",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Country>>() {
                });
        if (res.getBody() == null || res.getBody().isEmpty()) {
            throw new InternalServerException();
        }

        log.debug("{} countries returned", res.getBody().size());

        return res.getBody();
    }
}
