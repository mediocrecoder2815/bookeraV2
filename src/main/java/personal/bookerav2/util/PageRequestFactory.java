package personal.bookerav2.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Set;

public final class PageRequestFactory {

    private static final int MAX_SIZE = 100;

    private PageRequestFactory() {}

    public static Pageable from(int page, int size, String sortBy,
                                boolean ascending, Set<String> allowedFields,
                                String defaultField)
    {
        if (page < 0) throw new IllegalArgumentException("page must be >= 0, got " + page);
        if (size < 1 || size > MAX_SIZE)
            throw new IllegalArgumentException("size must be between 1 and " + MAX_SIZE + ", got " + size);
        String field = defaultField;
        if (sortBy != null && !sortBy.isBlank()) {
            if (!allowedFields.contains(sortBy))
                throw new IllegalArgumentException("sortBy must be one of " + allowedFields + ", got " + sortBy);
            field = sortBy;
        }
        Sort sort = ascending ? Sort.by(field).ascending() : Sort.by(field).descending();
        return PageRequest.of(page, size, sort);
    }
}
