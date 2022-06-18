package com.fuyajo.GPXAnalayzer.gpx;

import java.util.Map;
import java.util.HashMap;

public class ObjectPool {
  
  private Map<String, Object> entityMap = new HashMap<>();

  public ObjectPool() {}

  public void add(AbstractEntity entity) {
    entityMap.put(entity.getUuid().toString(), entity);
  }

  public void remove(String uuid) {
    entityMap.remove(uuid);
  }

  public void remove(AbstractEntity entity) {
    entityMap.remove(entity.getUuid().toString());
  }

  public Object get(String uuid) {
    return entityMap.get(uuid);
  }

}
