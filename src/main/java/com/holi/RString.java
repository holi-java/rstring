package com.holi;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by selonj on 16-9-5.
 */
public class RString implements Replaceable {
  private static final int VARIABLE_GROUP = 2;
  private static final int LITERAL_GROUP = 1;
  private static final String PATTERN_REGEX = "\\\\([{}])|\\{([^{}]+)\\}";

  private CharSequence sequence;

  public RString(CharSequence sequence) {
    if (sequence == null) throw new NullPointerException();
    this.sequence = sequence;
  }

  public static RString valueOf(Object obj) {
    return valueOf(String.valueOf(obj));
  }

  private static RString valueOf(CharSequence sequence) {
    return new RString(sequence);
  }

  @Override public RString replace(Object... values) {
    return (RString) Replaceable.super.replace(values);
  }

  @Override public RString replace(Iterable<Object> values) {
    return (RString) Replaceable.super.replace(values);
  }

  @Override public RString replace(Iterator<Object> values) {
    return (RString) Replaceable.super.replace(values);
  }

  @Override public RString replace(Map<String, Object> variables) {
    return (RString) Replaceable.super.replace(variables);
  }

  public RString replace(Context<String, Object> context) {
    return replace(PATTERN_REGEX, (groups) -> {
      String literal = groups.get(LITERAL_GROUP);
      if (literal != null) return literal;

      String name = groups.get(VARIABLE_GROUP).trim();
      return value(name, context);
    });
  }

  @Override public RString replace(String regex, Context<Context<Integer, String>, String> context) {
    Matcher matcher = Pattern.compile(regex).matcher(sequence);

    StringBuilder result = new StringBuilder();
    int pos = 0;
    while (matcher.find()) {
      result.append(sequence.subSequence(pos, matcher.start()));//heading or surrounding
      result.append(context.get(matcher::group));
      pos = matcher.end();
    }

    result.append(sequence.subSequence(pos, sequence.length()));//tailing
    return valueOf(result);
  }

  private String value(String name, Context<String, Object> context) {
    Object value = context.get(name);
    if (value == null) throw new MissingValueException("missing variable `" + name + "`!");
    return value.toString();
  }

  @Override public boolean equals(Object obj) {
    if (!RString.class.isInstance(obj)) return false;
    RString that = (RString) obj;
    return sequence.equals(that.sequence);
  }

  @Override public int hashCode() {
    return sequence.hashCode();
  }

  @Override public String toString() {
    return sequence.toString();
  }
}
