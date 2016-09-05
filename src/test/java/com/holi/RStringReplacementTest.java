package com.holi;

import com.holi.Replaceable.Context;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by selonj on 16-9-5.
 */
public class RStringReplacementTest {
  @Test public void replaceAllIfAllMatched() throws Exception {
    RString foo = RString.valueOf("foo");

    RString result = foo.replace("[a-z]", toUpperCase());

    assertThat(result.toString(), equalTo("FOO"));
  }

  @Test public void returnDirectlyIfMismatched() throws Exception {
    RString foo = RString.valueOf("0");

    RString result = foo.replace("^[a-z]", toUpperCase());

    assertThat(result.toString(), equalTo("0"));
  }

  @Test public void preserveTheHeadingMismatched() throws Exception {
    RString foo = RString.valueOf("1f");

    RString result = foo.replace("[a-z]", toUpperCase());

    assertThat(result.toString(), equalTo("1F"));
  }

  @Test public void preserveTheSurroundingMismatched() throws Exception {
    RString foo = RString.valueOf("f1f");

    RString result = foo.replace("[a-z]", toUpperCase());

    assertThat(result.toString(), equalTo("F1F"));
  }

  @Test public void preserveTheTailingMismatched() throws Exception {
    RString foo = RString.valueOf("f1");

    RString result = foo.replace("[a-z]", toUpperCase());

    assertThat(result.toString(), equalTo("F1"));
  }

  private Context<Context<Integer, String>, String> toUpperCase() {
    return (groups) -> groups.get(0).toUpperCase();
  }
}
