package com.hiresense.common.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

class PageResponseTest {

    @Test
    void elementsAreMappedAndPagingDataIsKept() {
        // Page 1 (second page) of size 2, with 5 elements in total: 3 pages.
        PageImpl<Integer> page = new PageImpl<>(List.of(3, 4), PageRequest.of(1, 2), 5);

        PageResponse<String> response = PageResponse.from(page, n -> "item-" + n);

        assertThat(response.content()).containsExactly("item-3", "item-4");
        assertThat(response.page()).isEqualTo(1);
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.totalElements()).isEqualTo(5L);
        assertThat(response.totalPages()).isEqualTo(3);
    }

    @Test
    void emptyPageGivesEmptyContentAndZeroTotals() {
        PageImpl<Integer> page = new PageImpl<>(List.of(), PageRequest.of(0, 20), 0);

        PageResponse<String> response = PageResponse.from(page, String::valueOf);

        assertThat(response.content()).isEmpty();
        assertThat(response.totalElements()).isZero();
        assertThat(response.totalPages()).isZero();
    }
}
