package az.example.countriesdemo.services;

import az.example.countriesdemo.exceptions.BadRequestException;
import az.example.countriesdemo.models.Country;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final CountryService countryService;

    public Set<String> findRoute(String fromCountryCode, String toCountryCode) {
        Map<String, Country> countryCodeToCountry = countryService.getAllCountries()
                .stream()
                .collect(Collectors.toMap(Country::getCountryCode, Function.identity()));

        validateBorders(fromCountryCode, toCountryCode, countryCodeToCountry);

        Set<String> route = new LinkedHashSet<>();
        route.add(fromCountryCode);

        findRoute(countryCodeToCountry, new HashSet<>(), route, fromCountryCode, toCountryCode);

        if (route.size() < 2){
            throw new BadRequestException();
        }
        return route;
    }

    //INFO: as we don't have requirement to find optimal path any possible land crossing can be the answer
    private void findRoute(Map<String, Country> countryCodeToCountry, Set<String> alreadyChecked, Set<String> route, String fromCountryCode, String toCountryCode) {
        Set<String> borders = countryCodeToCountry.get(fromCountryCode).getBorders();
        if (borders.contains(toCountryCode)){
            route.add(toCountryCode);
            return;
        }

        for(String border:borders){
            if (alreadyChecked.contains(border)){
                continue;
            }
            Set<String> routes = new LinkedHashSet<>();
            routes.add(border);
            alreadyChecked.add(border);
            findRoute(countryCodeToCountry, alreadyChecked, routes, border, toCountryCode);
            if (routes.size()>1){
                route.addAll(routes);
                return;
            }
        }
    }

    private void validateBorders(String fromCountryCode, String toCountryCode, Map<String, Country> countryCodeToCountry) {
        if (countryCodeToCountry.get(fromCountryCode).getBorders().isEmpty() ||
                countryCodeToCountry.get(toCountryCode).getBorders().isEmpty()) {
            throw new BadRequestException();
        }
    }
}
