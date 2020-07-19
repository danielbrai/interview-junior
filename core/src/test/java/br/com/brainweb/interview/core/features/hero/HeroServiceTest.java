package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.core.exception.HeroNotFoudException;
import br.com.brainweb.interview.model.Hero;
import br.com.brainweb.interview.model.enums.Race;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HeroServiceTest {

    @InjectMocks
    private HeroService service;

    @Mock
    private HeroRepository repository;

    @Test
    public void shouldBeReturnAnOptionalContainingTheWantedHeroIfItExists() {
        //given
        String wantedHeroId = "1b9f3125-b59d-49c0-90df-2cf6be63e39f";
        when(repository.getById(UUID.fromString(wantedHeroId))).thenReturn(this.createHero());

        //when
        Hero hero = this.service.getById(wantedHeroId);

        //then
        Assert.assertNotNull(hero);
    }

    @Test
    public void shouldThrowAnExceptionIfWantedHeroDoesNotExists() {
        //given
        String inexistentHeroId = "93918f6f-861e-4cf9-b6f9-c2b78a55e7a3";
        when(repository.getById(UUID.fromString(inexistentHeroId))).thenThrow(new EmptyResultDataAccessException(0));

        // when
        Exception exception = assertThrows(HeroNotFoudException.class, () -> {
            this.service.getById(inexistentHeroId);
        });

        // then
        Assert.assertTrue(exception.getMessage().contains("O herói pesquisado não foi encontrado"));
    }

    @Test
    public void shouldReturnAListWithHeroesWhoseNamesMatchWithTheGivenParam() {
        //given
        String filterParam = "man";
        List<Hero> searchedHeroes = new ArrayList<>();

        searchedHeroes.add(Hero.builder()
                .id(UUID.randomUUID())
                .name("Superman")
                .powerStatsId(UUID.randomUUID())
                .race(Race.ALIEN)
                .build());

        searchedHeroes.add(Hero.builder()
                .id(UUID.randomUUID())
                .name("Batman")
                .powerStatsId(UUID.randomUUID())
                .race(Race.HUMAN)
                .build());

        searchedHeroes.add(Hero.builder()
                .id(UUID.randomUUID())
                .name("Wonder Woman")
                .powerStatsId(UUID.randomUUID())
                .race(Race.DIVINE)
                .build());

        when(this.repository.getByName(filterParam)).thenReturn(searchedHeroes);

        // when
        Collection<Hero> byParam = this.service.getByParam(filterParam);

        // then
        int expectedNumberOfHeroesReturned = 3;
        Assert.assertFalse(byParam.isEmpty());
        Assert.assertEquals(expectedNumberOfHeroesReturned, byParam.size());
    }

    private Hero createHero() {
        return Hero.builder()
                .id(UUID.fromString("1b9f3125-b59d-49c0-90df-2cf6be63e39f"))
                .name("Martian Manhunter")
                .powerStatsId(UUID.fromString("d1f59cb0-a999-41a9-b5bd-7f0d821a819f"))
                .race(Race.ALIEN)
                .build();
    }
}