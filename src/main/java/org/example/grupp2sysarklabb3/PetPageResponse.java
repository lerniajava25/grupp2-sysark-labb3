package org.example.grupp2sysarklabb3;

import java.util.List;

public record PetPageResponse (
    List<PetDTO> pets,
    int page,
    int size,
    long totalElements,
    int totalPages
){}
