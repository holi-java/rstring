package com.holi;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;

import static java.util.Arrays.asList;

/**
 * Created by selonj on 16-9-9.
 */
@FunctionalInterface
public interface Context<T, R> {
  R get(T name);

  static <T, R> Context<T, R> failsWhenMissingValue(Context<T, R> context) {
    return (name) -> {
      R value = context.get(name);
      if (value == null) throw new MissingValueException("missing variable `" + name + "`!");
      return value;
    };
  }

  static <T, R> Context<T, String> valueToString(Context<T, R> context) {
    return (name) -> context.get(name).toString();
  }

  static <T, R> Context<T, R> from(Map<T, R> variables) {
    return variables::get;
  }

  static <T, R> Context<T, R> from(Iterable<R> values) {
    return from(values.iterator());
  }

  @SafeVarargs
  static <T, R> Context<T, R> from(R... values) {
    return from(asList(values));
  }

  static <T, R> Context<T, R> from(Iterator<R> values) {
    return name -> values.hasNext() ? values.next() : null;
  }

  static Context<Integer, String> groups(final Matcher matcher) {
    return new Context<Integer, String>() {
      @Override public String get(Integer group) {
        return matcher.group(group);
      }

      @Override public String toString() {
        return "groups";
      }
    };
  }
}
