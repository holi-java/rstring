package com.holi;

/**
 * Created by selonj on 16-9-5.
 */
public interface Replaceable {

  Replaceable replace(Context<String, Object> context) throws MissingValueException;

  Replaceable replace(String regex, Context<Context<Integer, String>, String> context);
}
