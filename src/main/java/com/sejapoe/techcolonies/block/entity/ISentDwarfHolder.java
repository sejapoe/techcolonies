package com.sejapoe.techcolonies.block.entity;

import com.sejapoe.techcolonies.entity.DwarfEntity;
import com.sejapoe.techcolonies.entity.SentDwarf;

import java.util.List;

public interface ISentDwarfHolder {
  void insertEntity(DwarfEntity entity, int time);

  void extractEntity(SentDwarf entity);

  List<SentDwarf> getEntities();
}
