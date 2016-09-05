package com.holi;

import java.util.Iterator;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * Created by selonj on 16-9-5.
 */
public interface Replaceable {

  Replaceable replace(Context<String, Object> context);

  Replaceable replace(String regex, Context<Context<Integer, String>, String> context);

  default Replaceable replace(Map<String, Object> variables) {
    return replace(variables::get);
  }

  default Replaceable replace(Object... values) {
    return replace(asList(values));
  }

  default Replaceable replace(Iterable<Object> values) {
    return replace(values.iterator());
  }

  default Replaceable replace(Iterator<Object> values) {
    return replace(name -> values.hasNext() ? values.next() : null);
  }

  @FunctionalInterface interface Context<K, V> {
    V get(K name);
  }
}
