package personal.bookerav2.entities.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CountryCode enum tests")
class CountryCodeTest {

    @Test
    @DisplayName("should return full country name")
    void shouldReturnFullName() {
        assertEquals("Czechia", CountryCode.CZ.getCountyName());
        assertEquals("United States", CountryCode.US.getCountyName());
        assertEquals("Great Britannia", CountryCode.GB.getCountyName());
    }

    @Test
    @DisplayName("should convert valid code")
    void shouldConvertValidCode() {
        assertEquals(CountryCode.CZ, CountryCode.convert("CZ"));
        assertEquals(CountryCode.US, CountryCode.convert("US"));
        assertEquals(CountryCode.KZ, CountryCode.convert("KZ"));
        assertEquals(CountryCode.RU, CountryCode.convert("RU"));
        assertEquals(CountryCode.IT, CountryCode.convert("IT"));
        assertEquals(CountryCode.GB, CountryCode.convert("GB"));
        assertEquals(CountryCode.CN, CountryCode.convert("CN"));
    }

    @Test
    @DisplayName("should be case-insensitive")
    void shouldBeCaseInsensitive() {
        assertEquals(CountryCode.CZ, CountryCode.convert("cz"));
        assertEquals(CountryCode.US, CountryCode.convert("us"));
        assertEquals(CountryCode.GB, CountryCode.convert("Gb"));
    }

    @Test
    @DisplayName("should throw for invalid code")
    void shouldThrowForInvalidCode() {
        assertThrows(IllegalArgumentException.class, () -> CountryCode.convert("XX"));
        assertThrows(IllegalArgumentException.class, () -> CountryCode.convert("FR"));
    }

    @Test
    @DisplayName("should have all expected values")
    void shouldHaveAllValues() {
        assertEquals(7, CountryCode.values().length);
    }
}
