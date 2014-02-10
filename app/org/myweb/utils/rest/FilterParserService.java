package org.myweb.utils.rest;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface FilterParserService {
    @NotNull
    List<ImmutableTriple<String, String, String>> parse(String filter) throws FilterParserServiceException;
}
