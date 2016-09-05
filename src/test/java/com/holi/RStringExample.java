package com.holi;

import java.util.Map;
import org.junit.Test;

import static com.holi.utils.MappedVariables.with;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by selonj on 16-9-5.
 */
public class RStringExample {

  @Test public void capitalize() throws Exception {
    RString string = RString.valueOf("java string");

    RString result = string.replace("(?<=^|\\s+)(\\w)", (groups) -> groups.get(1).toUpperCase());

    assertThat(result.toString(), equalTo("Java String"));
  }

  @Test public void toCamelCase() throws Exception {
    RString string = RString.valueOf("java string");

    RString result = string.replace("(?:\\s+)(\\w)", (groups) -> groups.get(1).toUpperCase());

    assertThat(result.toString(), equalTo("javaString"));
  }

  @Test public void toSnakeCase() throws Exception {
    RString string = RString.valueOf("java string");

    RString result = string.replace("(?:\\s+)(\\w)", (groups) -> "_" + groups.get(1));

    assertThat(result.toString(), equalTo("java_string"));
  }

  @Test public void replaceVariableWithMappedValue() throws Exception {
    RString string = RString.valueOf("{user}@example.com");
    Map<String, Object> variables = with("user=foo");

    assertThat(string.replace(variables).toString(), equalTo("foo@example.com"));
  }

  @Test public void replaceVariableWithArrayValues() throws Exception {
    RString string = RString.valueOf("{user}@example.com");
    Object[] variables = {"foo"};

    assertThat(string.replace(variables).toString(), equalTo("foo@example.com"));
  }

  @Test public void dropsEscapeCharAndIgnoringReplaceIfVariableExpressionStartsWithEscapeChar() throws Exception {
    RString string = RString.valueOf("\\{user\\}");

    assertThat(string.replace("anything").toString(), equalTo("{user}"));
  }
}
