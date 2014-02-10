package org.myweb.utils.rest;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface FilterParserService {
    @NotNull
    List<ImmutableTriple<String, String, String>> parse(String filter) throws FilterParserServiceException;
}
