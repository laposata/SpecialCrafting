package com.dreamtea.api.utils;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.stream.Collectors;

public class Utils {
  public static <T> String listEnum(T[] vals) {
    return Arrays.stream(vals).map(Object::toString).collect(Collectors.joining(","));
  }

}
