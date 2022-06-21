package com.fuyajo.GPXAnalayzer.gpx.trackProcess;

import java.util.ArrayList;
import java.util.List;

import com.fuyajo.GPXAnalayzer.gpx.WayPointEntity;

import io.jenetics.jpx.WayPoint;

public class TrackSmoother {

  public List<WayPointEntity> smoothTrack(List<WayPointEntity> oriTrackPoints) {
    List<WayPointEntity> trackPoints = new ArrayList<WayPointEntity>();
    oriTrackPoints.forEach(trackPoint -> trackPoints.add(new WayPointEntity(trackPoint)));
    double distanceMean = 0;
    double milliMean = 0;
    for (int i = 0; i < trackPoints.size() - 1; i++) {
      WayPointEntity wpe1 = trackPoints.get(i);
      WayPointEntity wpe2 = trackPoints.get(i + 1);
      distanceMean += WayPointEntity.getDistanceWithMeter(
        trackPoints.get(i),
        trackPoints.get(i + 1)
      );
      // time different no more than 1 min
      milliMean += Math.min(
        60 * 1000,
        WayPointEntity.getMillisecondDifferent(wpe1, wpe2)
      );
    }
    distanceMean /= trackPoints.size() - 1;
    milliMean /= trackPoints.size() - 1;
    
    KalmanLatLong kalmanLatLong = new KalmanLatLong(distanceMean * 1000 / milliMean);
    for (int i = 0; i < trackPoints.size() - 1; i++) {
      WayPointEntity wpe = trackPoints.get(i);
      // one degree approximately equal 100 km
      kalmanLatLong.Process(
        wpe.getLatitude() * 1000,
        wpe.getLongitude() * 1000,
        5.0,
        wpe.getTime().toEpochMilli());
      WayPoint wp = wpe.getWayPoint().toBuilder()
        .lat(kalmanLatLong.get_lat() / 1000)
        .lon(kalmanLatLong.get_lng() / 1000)
        .build();
      wpe.setWayPoint(wp);
    }

    return trackPoints;
  }

  private class KalmanLatLong {
    // code from https://stackoverflow.com/a/15657798/14576600
    private final double MinAccuracy = 1;

    private double Q_metres_per_second;
    private long TimeStamp_milliseconds;
    private double lat;
    private double lng;
    private double variance; // P matrix. Negative means object uninitialised. NB: units irrelevant, as long
                            // as same units used throughout

    public KalmanLatLong(double Q_metres_per_second) {
      this.Q_metres_per_second = Q_metres_per_second;
      variance = -1;
    }

    public long get_TimeStamp() {
      return TimeStamp_milliseconds;
    }

    public double get_lat() {
      return lat;
    }

    public double get_lng() {
      return lng;
    }

    public double get_accuracy() {
      return Math.sqrt(variance);
    }

    public void SetState(double lat, double lng, double accuracy, long TimeStamp_milliseconds) {
      this.lat = lat;
      this.lng = lng;
      variance = accuracy * accuracy;
      this.TimeStamp_milliseconds = TimeStamp_milliseconds;
    }

    /// <summary>
    /// Kalman filter processing for lattitude and longitude
    /// </summary>
    /// <param name="lat_measurement_degrees">new measurement of lattidude</param>
    /// <param name="lng_measurement">new measurement of longitude</param>
    /// <param name="accuracy">measurement of 1 standard deviation error in
    /// metres</param>
    /// <param name="TimeStamp_milliseconds">time of measurement</param>
    /// <returns>new state</returns>
    public void Process(double lat_measurement, double lng_measurement, double accuracy, long TimeStamp_milliseconds) {
      if (accuracy < MinAccuracy)
        accuracy = MinAccuracy;
      if (variance < 0) {
        // if variance < 0, object is unitialised, so initialise with current values
        this.TimeStamp_milliseconds = TimeStamp_milliseconds;
        lat = lat_measurement;
        lng = lng_measurement;
        variance = accuracy * accuracy;
      } else {
        // else apply Kalman filter methodology

        long TimeInc_milliseconds = TimeStamp_milliseconds - this.TimeStamp_milliseconds;
        if (TimeInc_milliseconds > 0) {
          // time has moved on, so the uncertainty in the current position increases
          variance += TimeInc_milliseconds * Q_metres_per_second * Q_metres_per_second / 1000;
          this.TimeStamp_milliseconds = TimeStamp_milliseconds;
          // TO DO: USE VELOCITY INFORMATION HERE TO GET A BETTER ESTIMATE OF CURRENT
          // POSITION
        }

        // Kalman gain matrix K = Covarariance * Inverse(Covariance +
        // MeasurementVariance)
        // NB: because K is dimensionless, it doesn't matter that variance has different
        // units to lat and lng
        double K = variance / (variance + accuracy * accuracy);
        // apply K
        lat += K * (lat_measurement - lat);
        lng += K * (lng_measurement - lng);
        // new Covarariance matrix is (IdentityMatrix - K) * Covarariance
        variance = (1 - K) * variance;
      }
    }
  }
}