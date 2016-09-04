package com.holi;

import java.util.Iterator;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * Created by selonj on 16-9-5.
 */
public interface Replaceable {

  String replace(Context context);

  default String replace(Map<String, Object> variables) {
    return replace(variables::get);
  }

  default String replace(Object... values) {
    return replace(asList(values));
  }

  default String replace(Iterable<Object> values) {
    return replace(values.iterator());
  }

  default String replace(Iterator<Object> values) {
    return replace(name -> values.hasNext() ? values.next() : null);
  }

  @FunctionalInterface interface Context {
    Object get(String name);
  }
}
