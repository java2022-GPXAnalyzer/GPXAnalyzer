package com.fuyajo.GPXAnalayzer.gpx;

import java.util.UUID;

// 一個 GPX 實體, 包含基本的名子、UUID資訊
public abstract class AbstractEntity {
  private final UUID uuid;

  public AbstractEntity() {
    this.uuid = UUID.randomUUID();
  }

  public AbstractEntity(UUID uuid) {
    if (uuid == null) {
      this.uuid = UUID.randomUUID();
    } else {
      this.uuid = uuid;
    }
  }

  public UUID getUuid() {
    return uuid;
  }
  
}
