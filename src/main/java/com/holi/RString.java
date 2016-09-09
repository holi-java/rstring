package com.holi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.holi.Context.failsWhenMissingValue;
import static com.holi.Context.valueToString;
import static java.util.regex.Pattern.compile;

/**
 * Created by selonj on 16-9-5.
 */
public class RString implements Replaceable {
  private static final int VARIABLE_GROUP = 2;
  private static final int ESCAPING_GROUP = 1;
  private static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\\\([{}])|\\{([^{}]+)\\}");

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
    return replace(EXPRESSION_PATTERN, expandVariables(valueToString(failsWhenMissingValue(context))));
  }

  private static Context<Context<Integer, String>, String> expandVariables(Context<String, String> context) {
    return (groups) -> {
      String escaped = groups.get(ESCAPING_GROUP);
      if (escaped != null) return escaped;

      String name = groups.get(VARIABLE_GROUP).trim();
      return context.get(name);
    };
  }

  @Override public RString replace(String regex, Context<Context<Integer, String>, String> context) throws MissingValueException {
    return replace(compile(regex), failsWhenMissingValue(context));
  }

  public RString replace(Pattern pattern, Context<Context<Integer, String>, String> context) {
    Matcher matcher = pattern.matcher(sequence);

    if (matcher.find()) {
      int pos = 0;
      StringBuilder result = new StringBuilder();
      Context<Integer, String> groups = Context.groups(matcher);

      do {
        result.append(slice(pos, matcher.start()));//adds mismatched heading or surrounding
        result.append(context.get(groups));
        pos = matcher.end();
      } while (matcher.find());

      return valueOf(result.append(slice(pos, sequence.length())));//adds mismatched tailing
    }

    return this;
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
