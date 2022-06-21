package com.fuyajo.GPXAnalayzer.gpx.speedColor;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.util.Pair;

import com.fuyajo.GPXAnalayzer.gpx.WayPointEntity;

public class SpeedColorHandler {

  public List<Pair<Double, Double> > generateSpeedColorList(List<WayPointEntity> trackPoint) {
    int pointGap = 10;
    // TODO: decide proper point gap

    return generateSpeedColorList(trackPoint, pointGap);
  }

  public List<Pair<Double, Double> > generateSpeedColorList(List<WayPointEntity> wayPointEntities, int pointGap) {
    List<Pair<Double, Double> > speedColorList = new ArrayList<>();
    speedColorList.add(Pair.of(0.0, 0.0));
    // TODO: implement
    return speedColorList;
  }
  
}
