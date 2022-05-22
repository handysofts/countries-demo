package az.example.countriesdemo.controllers;

import az.example.countriesdemo.services.RouteService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/routing")
@RequiredArgsConstructor
@Slf4j
public class RouteController {
    private final RouteService routeService;

    @GetMapping("/{fromCountryCode}/{toCountryCode}")
    public Res findRoute(@PathVariable String fromCountryCode, @PathVariable String toCountryCode) {
        log.debug("Finding route from {} to {}", fromCountryCode, toCountryCode);
        Set<String> route = routeService.findRoute(fromCountryCode, toCountryCode);
        return new Res(route);
    }

    @Data
    @AllArgsConstructor
    private static class Res {
        private Set<String> route;
    }
}
