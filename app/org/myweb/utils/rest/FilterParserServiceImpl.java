package org.myweb.utils.rest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FilterParserServiceImpl implements FilterParserService {

    protected static final String FILTER_PARAM_SEPARATOR = "\\|";
    protected static final String FILTER_LOGICAL_OPERATOR_LEFT_SEPARATOR = "[";
    protected static final String FILTER_LOGICAL_OPERATOR_RIGHT_SEPARATOR = "]";

    protected static final String[] FILTER_REST_LOGICAL_OPERATOR_LIST = {"eq", "ne", "gt", "ge", "lt", "le", "like"};

    @NotNull
    protected String[] splitParameters(@NotNull String filter) {
        String[] res = new String[0];

        if(filter.isEmpty()) {
            return res;
        }

        res = filter.split(FILTER_PARAM_SEPARATOR);

        return res;
    }

    protected boolean isLogicalOperatorValid(@NotNull String logicalOperator) {

        boolean res = false;
        for(String s : FILTER_REST_LOGICAL_OPERATOR_LIST) {
            if(logicalOperator.equals(s)){
                return true;
            }
        }

        return res;
    }

    @NotNull
    protected String extractLogicalOperator(@NotNull String parameter) throws FilterParserServiceException {
        String res = "";

        if(parameter.isEmpty()) {
            return res;
        }

        int leftSepIndex = parameter.lastIndexOf(FILTER_LOGICAL_OPERATOR_LEFT_SEPARATOR);
        if(leftSepIndex == -1) {
            throw new FilterParserServiceException(
                    "left separator " + FILTER_LOGICAL_OPERATOR_LEFT_SEPARATOR + " is missing in the parameter "
                            + parameter
            );
        }

        leftSepIndex++;

        int rightSepIndex = parameter.lastIndexOf(FILTER_LOGICAL_OPERATOR_RIGHT_SEPARATOR);
        if(rightSepIndex == -1) {
            throw new FilterParserServiceException(
                    "right separator " + FILTER_LOGICAL_OPERATOR_RIGHT_SEPARATOR + " is missing in the parameter "
                            + parameter
            );
        }

        try {
            res = parameter.substring(leftSepIndex, rightSepIndex);
        } catch(IndexOutOfBoundsException ioobe) {
            throw new FilterParserServiceException(
                    "IndexOutOfBoundsException when extracting logical operator : "
                            + ExceptionUtils.getStackTrace(ioobe)
            );
        }

        if(!isLogicalOperatorValid(res)) {
            throw new FilterParserServiceException("Invalid logical operator : " + res + " is not a valid value.");
        }

        return res;
    }

    @NotNull
    protected String extractEntityPropertyName(@NotNull String parameter) throws FilterParserServiceException {
        String res = "";

        if(parameter.isEmpty()) {
            return res;
        }

        int leftSepIndex = parameter.lastIndexOf(FILTER_LOGICAL_OPERATOR_LEFT_SEPARATOR);
        if(leftSepIndex == -1) {
            throw new FilterParserServiceException(
                    "left separator " + FILTER_LOGICAL_OPERATOR_LEFT_SEPARATOR + " is missing in the parameter "
                            + parameter
            );
        }

        try {
            res = parameter.substring(0, leftSepIndex);
        } catch(IndexOutOfBoundsException ioobe) {
            throw new FilterParserServiceException(
                    "IndexOutOfBoundsException when extracting entity property name : "
                            + ExceptionUtils.getStackTrace(ioobe)
            );
        }

        return res;
    }

    @NotNull
    protected String extractEntityPropertyValue(@NotNull String parameter) throws FilterParserServiceException {
        String res = "";

        if(parameter.isEmpty()) {
            return res;
        }

        int rightSepIndex = parameter.lastIndexOf(FILTER_LOGICAL_OPERATOR_RIGHT_SEPARATOR);
        if(rightSepIndex == -1) {
            throw new FilterParserServiceException(
                    "right separator " + FILTER_LOGICAL_OPERATOR_RIGHT_SEPARATOR + " is missing in the parameter "
                            + parameter
            );
        }

        rightSepIndex++;

        try {
            res = parameter.substring(rightSepIndex, parameter.length());
        } catch(IndexOutOfBoundsException ioobe) {
            throw new FilterParserServiceException(
                    "IndexOutOfBoundsException when extracting entity property value : "
                            + ExceptionUtils.getStackTrace(ioobe)
            );
        }

        return res;
    }

    @NotNull
    protected ImmutableTriple<String, String, String> extractParameter(String parameter)
            throws FilterParserServiceException {

        return new ImmutableTriple<>(
                extractEntityPropertyName(parameter),
                extractLogicalOperator(parameter),
                extractEntityPropertyValue(parameter)
        );
    }

    /**
     * <strong>INPUT</strong>
     *
     * <p><u>filter structure</u></p>
     * <p>{@code <propertyName> [<logical_operator>] ::}</p>
     * <p>:: is filter separator. No logical operator between filters supported</p>
     * <p>ex : filter=name[like]lot|code[eq]4</p>
     *
     * <a href="http://www.odata.org/documentation/uri-conventions/#FilterSystemQueryOption">inspiration</a>
     * <a href="http://docs.oracle.com/javaee/6/api/javax/persistence/criteria/CriteriaBuilder.html">Criteria javadoc</a>
     *
     * <p>Supported logical operators :</p>
     * <table>
     *     <col width="34%"/><col width="33%"/><col width="33%"/>
     *     <thead><th><td>name</td><td>rest repr.</td><td>jpa repr.</td></th></thead>
     *     <tbody>
     *     <tr><td>==</td><td>eq</td><td>equal</td></tr>
     *     <tr><td>!=</td><td>ne</td><td>equal().not()</td></tr>
     *     <tr><td>&gt;</td><td>gt</td><td>gt</td></tr>
     *     <tr><td>&gt;=</td><td>ge</td><td>ge</td></tr>
     *     <tr><td>&lt;</td><td>lt</td><td>lt</td></tr>
     *     <tr><td>&lt;=</td><td>le</td><td>le</td></tr>
     *     <tr><td>LIKE</td><td>like</td><td>like</td></tr>
     *     </tbody>
     * </table>
     *
     * <strong>OUTPUT</strong>
     * <p>An ArrayList of parameters structured in a ImmutableTriple<String, String, Object>.</p>
     * <ul>
     *     <li>The first String is the entity property name.</li>
     *     <li>The second String is the logical operator between param name and param value.</li>
     *     <li>The third Object is the entity property value.</li>
     * </ul>
     * <p>Because logical operators between parameters are not supported in this impl,
     * the relationship between params is always AND.</p>
     *
     * @param filter the filter to parse
     * @return an ImmutableTriple with name, operator, value
     * @throws FilterParserServiceException if an error occurs when parsing
     */
    @NotNull
    @Override
    public List<ImmutableTriple<String, String, String>> parse(String filter) throws FilterParserServiceException {
//https://developer.stackmob.com/rest-api/api-docs
        List<ImmutableTriple<String,String,String>> res = new ArrayList<>();

        String[] parameterList = splitParameters(filter);
        for(String s : parameterList) {
            res.add(extractParameter(s));
        }

        return res;
    }
}
