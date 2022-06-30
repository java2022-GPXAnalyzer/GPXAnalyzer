package com.fuyajo.GPXAnalayzer.gpx.trackProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fuyajo.GPXAnalayzer.gpx.WayPointEntity;

public class HotSpotHandler {
  /* reference:
   * https://jimmy-huang.medium.com/kmeans%E5%88%86%E7%BE%A4%E6%BC%94%E7%AE%97%E6%B3%95-%E8%88%87-silhouette-%E8%BC%AA%E5%BB%93%E5%88%86%E6%9E%90-8be17e634589
   * https://zh.wikipedia.org/wiki/%E8%BD%AE%E5%BB%93_(%E8%81%9A%E7%B1%BB)
   */

  private static final Logger LOGGER = LoggerFactory.getLogger(HotSpotHandler.class);
  
  private double curSC = -1; // current silhouette coefficient

  public List<WayPointEntity> generateHotSpot(List<WayPointEntity> points) {
    double bestSC = curSC;
    List<WayPointEntity> bestHotSpots = new ArrayList<WayPointEntity>();
    LOGGER.info("Executing HotSpot algorithm...");
    for(int i = 2; i <= Math.min(10, points.size() - 1); i++) {
      List<WayPointEntity> hotSpots = kmean(points, i);
      LOGGER.info("k: " + i + ", sc = " + curSC);
      if(bestSC < curSC) {
        bestSC = curSC;
        bestHotSpots = hotSpots;
      }
    }
    return bestHotSpots;
  }

  private List<WayPointEntity> kmean(List<WayPointEntity> points, int k) {
    Random rand = new Random();
    List<WayPointEntity> centroids = new ArrayList<WayPointEntity>();
    List<WayPointEntity> pointsCopy = new ArrayList<WayPointEntity>(points);
    for(int i = 0; i < k; i++) {
      int randIdx = rand.nextInt(pointsCopy.size());
      centroids.add(new WayPointEntity(pointsCopy.get(randIdx)));
      pointsCopy.remove(randIdx);
    }

    List<Integer> pointGroup = new ArrayList<Integer>();
    for(int i = 0; i < points.size(); i++) {
      pointGroup.add(0);
    }
    boolean isContinue = true;
    while(isContinue) {
      isContinue = false;
      List<Double> latMean = new ArrayList<Double>();
      List<Double> lonMean = new ArrayList<Double>();
      List<Integer> selPointCnt = new ArrayList<Integer>();
      for(int i = 0; i < centroids.size(); i++) {
        latMean.add(0.0);
        lonMean.add(0.0);
        selPointCnt.add(0);
      }
      for(int i = 0; i < points.size(); i++) {
        double minDist = Double.POSITIVE_INFINITY;
        int selCentroidIdx = 0;
        for(int j = 0; j < centroids.size(); j++) {
          double dist = WayPointEntity.getDistanceWithMeter(points.get(i), centroids.get(j));
          if(minDist > dist) {
            minDist = dist;
            selCentroidIdx = j;
          }
        }
        latMean.set(
          selCentroidIdx,
          latMean.get(selCentroidIdx) + points.get(i).getLatitude().doubleValue());
        lonMean.set(
          selCentroidIdx,
          lonMean.get(selCentroidIdx) + points.get(i).getLongitude().doubleValue());
        selPointCnt.set(
          selCentroidIdx,
          selPointCnt.get(selCentroidIdx) + 1);
        pointGroup.set(i, selCentroidIdx);
      }
      for(int i = 0; i < centroids.size(); i++) {
        if(selPointCnt.get(i) == 0) {
          continue;
        }
        latMean.set(i, latMean.get(i) / selPointCnt.get(i));
        lonMean.set(i, lonMean.get(i) / selPointCnt.get(i));
        WayPointEntity newCentroid = new WayPointEntity(centroids.get(i));
        newCentroid.setLatitude(latMean.get(i));
        newCentroid.setLongitude(lonMean.get(i));
        if(WayPointEntity.getDistanceWithMeter(centroids.get(i), newCentroid) > 10) {
          isContinue = true;
        }
        centroids.set(i, newCentroid);
      }
    }

    // determine silhouette coefficient
    this.curSC = 0.0;
    List<Integer> groupCnt = new ArrayList<Integer>();
    for(int i = 0; i < centroids.size(); i++) {
      groupCnt.add(0);
    }
    for(int i = 0; i < points.size(); i++) {
      int groupIdx = pointGroup.get(i);
      groupCnt.set(groupIdx, groupCnt.get(groupIdx) + 1);
    }
    for(int i = 0; i < points.size(); i++) {
      if(groupCnt.get(pointGroup.get(i)) == 1) {
        continue;
      }

      List<Double> groupDist = new ArrayList<Double>();
      for(int j = 0; j < centroids.size(); j++) {
        groupDist.add(0.0);
      }
      double a = 0, b = Double.POSITIVE_INFINITY; // a: diff cluster, b: same cluster
      int aCnt = 0;
      for(int j = 0; j < points.size(); j++) {
        if(i == j) continue;
        double dist = WayPointEntity.getDistanceWithMeter(points.get(i), points.get(j));
        if(pointGroup.get(i) == pointGroup.get(j)) {
          a += dist;
          aCnt++;
        } else {
          int groupIdx = pointGroup.get(j);
          groupDist.set(groupIdx, groupDist.get(groupIdx) + dist);
        }
      }

      if(aCnt > 0) a /= aCnt;
      for(int j = 0; j < centroids.size(); j++) {
        if(pointGroup.get(i) == j) continue;
        if(groupCnt.get(j) == 0) continue;
        b = Math.min(b, groupDist.get(j) / groupCnt.get(j));
      }
      this.curSC += (b - a) / Math.max(a, b);
      // LOGGER.info(b + " " + a + " " + curSC);
    }
    curSC /= points.size();

    return centroids;
  }
}
