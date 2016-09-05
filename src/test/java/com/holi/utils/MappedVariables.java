package com.holi.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by selonj on 16-9-5.
 */
public class MappedVariables {
  public static Map<String, Object> with(String... variables) {
    HashMap<String, Object> result = new HashMap<>();
    for (String variable : variables) {
      String[] pair = variable.split("=");
      result.put(pair[0], pair[1]);
    }
    return result;
  }
}
