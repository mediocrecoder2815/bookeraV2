package personal.bookerav2.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PageRequestFactory unit tests")
class PageRequestFactoryTest {

    private static final Set<String> FIELDS = Set.of("bookId", "name");

    @Test
    void buildsPageableWithDefaults() {
        Pageable pageable = PageRequestFactory.from(0, 10, null, true, FIELDS, "bookId");
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertEquals("bookId", pageable.getSort().iterator().next().getProperty());
        assertTrue(pageable.getSort().iterator().next().isAscending());
    }

    @Test
    void acceptsWhitelistedSortBy() {
        Pageable pageable = PageRequestFactory.from(2, 25, "name", false, FIELDS, "bookId");
        assertEquals(2, pageable.getPageNumber());
        assertEquals(25, pageable.getPageSize());
        assertEquals("name", pageable.getSort().iterator().next().getProperty());
        assertTrue(pageable.getSort().iterator().next().isDescending());
    }

    @Test
    void rejectsNegativePage() {
        assertThrows(IllegalArgumentException.class,
                () -> PageRequestFactory.from(-1, 10, null, true, FIELDS, "bookId"));
    }

    @Test
    void rejectsSizeBelowOne() {
        assertThrows(IllegalArgumentException.class,
                () -> PageRequestFactory.from(0, 0, null, true, FIELDS, "bookId"));
    }

    @Test
    void rejectsOversizedSize() {
        assertThrows(IllegalArgumentException.class,
                () -> PageRequestFactory.from(0, 1000, null, true, FIELDS, "bookId"));
    }

    @Test
    void rejectsUnknownSortBy() {
        assertThrows(IllegalArgumentException.class,
                () -> PageRequestFactory.from(0, 10, "DROP_TABLE", true, FIELDS, "bookId"));
    }
}
