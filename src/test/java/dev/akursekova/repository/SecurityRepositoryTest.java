package dev.akursekova.repository;

import dev.akursekova.entities.Security;
import dev.akursekova.entities.User;
import dev.akursekova.exception.SecurityCreationException;
import dev.akursekova.exception.SecurityNotExistException;
import dev.akursekova.exception.UserNotExistException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SecurityRepositoryTest {

    private Map<Long, Security> securities;
    private SecurityRepository securityRepository;

    @BeforeEach
    void setup() {
        securities = new HashMap<>();
        this.securityRepository = new SecurityRepository(securities);

        Security security = new Security();
        security.setName("security1");
        securities.put(2L, security);
    }


    @ParameterizedTest
    @ValueSource(strings = {"", "     "})
    void test_addSecurity_UserNameIsBlank(String argument){
        Security security = new Security();
        security.setName(argument);
        assertThrows(SecurityCreationException.class, () -> securityRepository.addSecurity(security));
    }

    @Test
    void test_addSecurity_SecurityWithGivenSecurityNameAlreadyExists() {
        Security security = new Security();
        security.setName("security1");
        assertThrows(SecurityCreationException.class, () -> securityRepository.addSecurity(security));
    }

    @SneakyThrows
    @Test
    void test_addSecurity_NewSecurity() {
        Security security = new Security();
        security.setName("security2");
        securityRepository.addSecurity(security);
        assertEquals(2, securityRepository.securities.size());
    }

    @SneakyThrows
    @Test
    void test_getSecurity_WhenIdIsPresent(){
        Security expectedSecurity = securities.get(2L);
        assertEquals(expectedSecurity, securityRepository.getSecurity(2L));
    }

    @Test
    void test_getSecurity_WhenIdIsNotPresent_ShouldThrowSecurityNotExistException() {
        assertThrows(SecurityNotExistException.class,
                () -> securityRepository.getSecurity(1L));
    }

    @Test
    void test_getAllSecurities_ShouldReturnWholeCollection() {
        Collection<Security> expectedSecurities = securityRepository.securities.values();
        assertEquals(expectedSecurities, securityRepository.getAllSecurities());
    }

}