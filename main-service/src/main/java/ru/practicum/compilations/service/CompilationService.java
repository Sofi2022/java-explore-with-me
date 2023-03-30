package ru.practicum.compilations.service;

import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;

public interface CompilationService {

    CompilationDto addCompilation(NewCompilationDto compilation);
}
