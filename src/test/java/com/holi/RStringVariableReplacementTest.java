package com.holi;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static java.util.Collections.emptyMap;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;

/**
 * Created by selonj on 16-9-5.
 */
public class RStringVariableReplacementTest {
  @Test public void returnDirectlyIfReplaceAStringLiteral() throws Exception {
    String result = replace("string", emptyMap());

    assertThat(result, equalTo("string"));
  }

  @Test public void replacesAllVariablesWithValue() throws Exception {
    String result = replace("{foo}{key}", with("foo=bar", "key=value"));

    assertThat(result, equalTo("barvalue"));
  }

  @Test public void doesNotReplaceThatValueWithVariableExpressionOfTheVariable() throws Exception {
    String result = replace("{foo}{key}", with("foo={key}", "key=value"));

    assertThat(result, equalTo("{key}value"));
  }

  @Test public void replacesVariableWithValueAndAppendTheTailingStringLiteral() throws Exception {
    String result = replace("{foo}!", with("foo=bar"));

    assertThat(result, equalTo("bar!"));
  }

  @Test public void replacesVariableWithValueAndAppendTheHeadingStringLiteral() throws Exception {
    String result = replace("foo:{foo}", with("foo=bar"));

    assertThat(result, equalTo("foo:bar"));
  }

  @Test public void replacesVariablesWithValueAndAppendTheSurroundingStringLiteral() throws Exception {
    String result = replace("{foo}-{key}", with("foo=bar", "key=value"));

    assertThat(result, equalTo("bar-value"));
  }

  @Test public void replacesVariablesWithArrayValues() throws Exception {
    String result = replace("{foo}-{key}", "bar", "value");

    assertThat(result, equalTo("bar-value"));
  }

  @Test public void throwsMissingValueExceptionIfReplacesVariableWithMapThatVariableDoesNotExists() throws Exception {
    try {
      replace("{foo}", emptyMap());
      fail("should failed");
    } catch (MissingValueException expected) {
      assertThat(expected, hasMessage(containsString("foo")));
    }
  }

  @Test public void throwsMissingValueExceptionIfReplacesVariableWithArrayHasNoEnoughValues() throws Exception {
    try {
      replace("{foo}", new Object[0]);
      fail("should failed");
    } catch (MissingValueException expected) {
      assertThat(expected, hasMessage(containsString("foo")));
    }
  }

  @Test public void variableNameCanContainingSpecialChars() throws Exception {
    String result = replace("{@foo}", with("@foo=bar"));

    assertThat(result, equalTo("bar"));
  }

  @Test public void replacesVariableWithTrimmedName() throws Exception {
    String result = replace("{\nfoo\n}", with("foo=bar"));

    assertThat(result, equalTo("bar"));
  }

  @Test public void ignoringReplaceVariableExpressionStartsWithEscapeChar() throws Exception {
    String result = replace("\\{foo}", emptyMap());

    assertThat(result, equalTo("{foo}"));
  }

  private String replace(String string, Object... values) {
    return new RString(string).replace(values);
  }

  private String replace(String string, Map<String, Object> variables) {
    return new RString(string).replace(variables);
  }

  private Map<String, Object> with(String... variables) {
    HashMap<String, Object> result = new HashMap<>();
    for (String variable : variables) {
      String[] pair = variable.split("=");
      result.put(pair[0], pair[1]);
    }
    return result;
  }
}