package com.holi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by selonj on 16-9-5.
 */
public class RString implements Replaceable {
  private static final int VARIABLE_GROUP = 2;
  private static final int LITERAL_GROUP = 1;
  private static final String PATTERN_REGEX = "\\\\([{}])|\\{([^{}]+)\\}";

  private String string;

  public RString(String string) {
    this.string = string;
  }

  public String replace(Context<String, Object> context) {
    return replace(PATTERN_REGEX, (groups) -> {
      String literal = groups.get(LITERAL_GROUP);
      if (literal != null) return literal;

      String name = groups.get(VARIABLE_GROUP).trim();
      return value(name, context);
    });
  }

  @Override public String replace(String regex, Context<Context<Integer, String>, String> context) {
    Matcher matcher = Pattern.compile(regex).matcher(string);

    StringBuilder result = new StringBuilder();
    int pos = 0;
    while (matcher.find()) {
      result.append(string.substring(pos, matcher.start()));//heading or surrounding
      result.append(context.get(matcher::group));
      pos = matcher.end();
    }

    result.append(string.substring(pos));//tailing
    return result.toString();
  }

  private String value(String name, Context<String, Object> context) {
    Object value = context.get(name);
    if (value == null) throw new MissingValueException("missing variable `" + name + "`!");
    return value.toString();
  }
}
