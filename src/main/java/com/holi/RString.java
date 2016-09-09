package com.holi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.holi.Context.failsWhenMissingValue;
import static com.holi.Context.valueToString;

/**
 * Created by selonj on 16-9-5.
 */
public class RString implements Replaceable {
  private static final int VARIABLE_GROUP = 2;
  private static final int ESCAPING_GROUP = 1;
  private static final String EXPRESSION_REGEX = "\\\\([{}])|\\{([^{}]+)\\}";

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

  public RString replace(Context<String, Object> context) throws MissingValueException {
    return replace(EXPRESSION_REGEX, expandVariables(valueToString(failsWhenMissingValue(context))));
  }

  private static Context<Context<Integer, String>, String> expandVariables(Context<String, String> context) {
    return (groups) -> {
      String escaped = groups.get(ESCAPING_GROUP);
      if (escaped != null) return escaped;

      String name = groups.get(VARIABLE_GROUP).trim();
      return context.get(name);
    };
  }

  @Override public RString replace(String regex, Context<Context<Integer, String>, String> context) {
    Context<Context<Integer, String>, String> replacement = failsWhenMissingValue(context);
    Matcher matcher = Pattern.compile(regex).matcher(sequence);

    StringBuilder result = new StringBuilder();
    int pos = 0;
    while (matcher.find()) {
      result.append(slice(pos, matcher.start()));//adds mismatched heading or surrounding
      result.append(replacement.get(matcher::group));
      pos = matcher.end();
    }

    result.append(slice(pos, sequence.length()));//adds mismatched tailing
    return valueOf(result);
  }

  private CharSequence slice(int start, int end) {
    return sequence.subSequence(start, end);
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
