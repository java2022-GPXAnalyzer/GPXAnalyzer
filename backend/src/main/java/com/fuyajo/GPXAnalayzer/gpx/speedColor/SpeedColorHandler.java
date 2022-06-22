package com.fuyajo.GPXAnalayzer.gpx.speedColor;

import com.fuyajo.GPXAnalayzer.gpx.WayPointEntity;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
// import java.util.Random;
import org.springframework.data.util.Pair;

public class SpeedColorHandler {

  public List<Pair<Double, Double>>
  generateSpeedColorList(List<WayPointEntity> trackPoint) {
    int pointGap = 1;
    // TODO: decide proper point gap
    int len = trackPoint.size();
    pointGap = Math.max(pointGap, len * 3 / 10);

    return generateSpeedColorList(trackPoint, pointGap);
  }

  private Double getDistanceBetweenWayPoint(WayPointEntity wayPoint1,
                                            WayPointEntity wayPoint2) {
    double lat1 = wayPoint1.getLatitude();
    double lon1 = wayPoint1.getLongitude();
    double lat2 = wayPoint2.getLatitude();
    double lon2 = wayPoint2.getLongitude();
    double a, b, R;
    R = 6378137; // radius of earth in meter
    lat1 = lat1 * Math.PI / 180.0;
    lat2 = lat2 * Math.PI / 180.0;
    a = lat1 - lat2;
    b = (lon1 - lon2) * Math.PI / 180.0;
    double d;
    double sa2, sb2;
    sa2 = Math.sin(a / 2.0);
    sb2 = Math.sin(b / 2.0);
    d = 2 * R *
        Math.asin(
            Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
    return d;
  }

  // get speed in M/s
  private Double getSpeed(WayPointEntity wayPoint1, WayPointEntity wayPoint2,
                          Double distance) {
    Double time =
        (double)Duration.between(wayPoint1.getTime(), wayPoint2.getTime())
            .toMillis();
    if (time == 0.0) {
      return 0.0;
    }
    return distance / time;
  }

  public List<Pair<Double, Double>>
  generateSpeedColorList(List<WayPointEntity> wayPointEntities, int pointGap) {
    List<Pair<Double, Double>> speedColorList = new ArrayList<>();
    double speedTotal = 0;
    double distanceTotal = 0;
    // Random random = new Random();
    // TODO: implement
    int len = (int)Math.ceil(wayPointEntities.size() / pointGap);
    // 0 1 2 3 4 5 6 7 8 9
    // 0 ~ 3 3 ~ 6 6 ~ 9
    // 0 1 2
    // 0 ~ 1 1 ~ 2
    for (int i = 0; i < len; i++) {
      Double speed = 0.0, distance = 0.0;
      boolean isBreak = false;
      for (int j = 0; j < pointGap; j++) {
        int index = i * pointGap + j;
        // 0 1, 1 2, 2 3
        if (index > wayPointEntities.size() - 1 ||
            index + 1 > wayPointEntities.size() - 1) {
          isBreak = true;
          break;
        }
        WayPointEntity wayPoint1 = wayPointEntities.get(index);
        WayPointEntity wayPoint2 = wayPointEntities.get(index + 1);
        distance += getDistanceBetweenWayPoint(wayPoint1, wayPoint2);
        speed += getSpeed(wayPoint1, wayPoint2, distance);
      }
      if (isBreak)
        break;
      speed /= (pointGap + 1);
      distance /= (pointGap + 1);
      speedTotal += speed;
      distanceTotal += distance;
      speedColorList.add(Pair.of(speed, distance));
    }
    double preDistance = 0;
    for (int i = 0; i < speedColorList.size(); i++) {
      Pair<Double, Double> speedColor = speedColorList.get(i);
      double speed = speedColor.getFirst() / speedTotal;
      double distance = speedColor.getSecond() / distanceTotal + preDistance;
      preDistance = distance;
      speedColor = Pair.of(speed, distance);
      speedColorList.set(i, speedColor);
    }
    return speedColorList;
  }
}
