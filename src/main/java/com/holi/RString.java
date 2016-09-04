package com.holi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by selonj on 16-9-5.
 */
public class RString implements Replaceable {
  private String string;
  private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\{([^{}]+)\\}");

  public RString(String string) {
    this.string = string;
  }

  public String replace(Context context) {
    Matcher matcher = VARIABLE_PATTERN.matcher(string);
    StringBuilder result = new StringBuilder();

    int pos = 0;
    while (matcher.find()) {
      int start = matcher.start();
      if (pos < start) {
        result.append(string.substring(pos, start));
      }

      String name = matcher.group(1).trim();
      result.append(value(name, context));

      pos = matcher.end();
    }

    result.append(string.substring(pos));

    return result.toString();
  }

  private Object value(String name, Context context) {
    Object value = context.get(name);
    if (value == null) throw new MissingValueException("missing variable `" + name + "`!");
    return value;
  }
}
