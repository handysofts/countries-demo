package az.example.countriesdemo;

import az.example.countriesdemo.controllers.RouteController;
import az.example.countriesdemo.models.Country;
import az.example.countriesdemo.services.CountryService;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CountriesDemoApplicationIT {

    @Autowired
    private RouteController controller;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CountryService countryService;

    @Test
    public void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @BeforeEach
    public void init() {
        List<Country> countries = new ArrayList<>();
        countries.add(new Country("CZE", Sets.set("AUT", "DEU", "POL", "SVK")));
        countries.add(new Country("CAN", Sets.set("USA")));
        countries.add(new Country("CYP", new HashSet<>()));
        countries.add(new Country("ITA", Sets.set("AUT")));
        countries.add(new Country("AUT", Sets.set("ITA", "CZE")));

        Mockito.when(countryService.getAllCountries()).thenReturn(countries);
    }

    @Test
    public void shouldReturn400WhenZeroBorderCountryRequested() throws Exception {
        this.mockMvc.perform(get("/routing/CZE/CYP"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn400WhenThereIsNoLandCrossing() throws Exception {
        this.mockMvc.perform(get("/routing/CZE/CAN"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    public void shouldReturnRoute() throws Exception {
        this.mockMvc.perform(get("/routing/CZE/ITA"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.route[0]").value("CZE"))
                .andExpect(jsonPath("$.route[2]").value("ITA"));
    }
}
